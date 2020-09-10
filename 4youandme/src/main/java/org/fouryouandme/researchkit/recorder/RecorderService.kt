package org.fouryouandme.researchkit.recorder

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.*
import android.speech.tts.TextToSpeech
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import arrow.core.None
import arrow.core.Option
import arrow.core.getOrElse
import arrow.core.some
import arrow.fx.IO
import arrow.fx.extensions.fx
import arrow.fx.typeclasses.Disposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import org.fouryouandme.R
import org.fouryouandme.core.arch.livedata.Event
import org.fouryouandme.core.arch.livedata.toEvent
import org.fouryouandme.core.ext.unsafeRunAsync
import org.fouryouandme.core.ext.unsafeRunAsyncCancelable
import org.fouryouandme.researchkit.result.FileResult
import org.fouryouandme.researchkit.result.Result
import org.fouryouandme.researchkit.step.Step
import org.fouryouandme.researchkit.task.Task
import timber.log.Timber
import java.io.File
import java.util.*

open class RecorderService : Service(), RecorderListener {

    private var state: Option<RecorderState> = None

    private var tts: Option<TextToSpeech> = None

    private var taskTimer: Option<Disposable> = None

    private var middleInstruction: Option<List<Disposable>> = None

    private var stateLiveDate: MutableLiveData<Event<RecordingState>> = MutableLiveData()

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int = START_NOT_STICKY

    private fun bindTTS(step: Step.ActiveStep): Unit {
        tts =
            if (step.hasVoice())
                TextToSpeech(
                    this@RecorderService
                ) { status ->

                    if (status == TextToSpeech.SUCCESS) {

                        tts.map {

                            val languageAvailable =
                                it.isLanguageAvailable(Locale.getDefault())
                            // >= 0 means
                            // LANG_AVAILABLE,
                            // LANG_COUNTRY_AVAILABLE,
                            // or LANG_COUNTRY_VAR_AVAILABLE
                            // TODO: fix language
                            if (languageAvailable >= 0)
                                it.language = Locale.US
                            else
                                tts = None
                        }

                    } else {
                        Timber.e("Failed to initialize TTS with error code $status")
                        tts = None
                    }

                    setupTaskTimer(step)
                    setupReadInstructions(step)

                }.some()
            else None
    }

    private fun buildRecorderList(step: Step.ActiveStep, outputDirectory: File): List<Recorder> =

        step.recorderConfigurations.map {

            val recorder = it.recorderForStep(step, outputDirectory)
            recorder.recorderListener = this.some()
            recorder.start(applicationContext)
            recorder

        }

    private fun setupTaskTimer(step: Step.ActiveStep): Unit {

        // Now allow the recorder to record for as long as the active step requires
        // TODO: allow different mode to detect the finish (es. number of steps)
        taskTimer =
            IO.fx {

                continueOn(Dispatchers.IO)
                !effect { delay(step.duration * 1000L) }
                continueOn(Dispatchers.Main)
                onRecorderDurationFinished(step)

            }.unsafeRunAsyncCancelable().some()

    }


    private fun setupReadInstructions(step: Step.ActiveStep): Unit {

        // play intro instruction
        step.spokenInstruction.map { speakText(it) }

        // play instruction during the step
        middleInstruction =
            step.spokenInstructionMap.map { (key, value) ->

                IO.fx {

                    continueOn(Dispatchers.IO)
                    !effect { delay(key * 1000L) }
                    continueOn(Dispatchers.Main)
                    speakText(value)

                }.unsafeRunAsyncCancelable()

            }.some()
    }

    private fun speakText(text: String): Unit {

        tts.map {
            it.speak(
                text,
                TextToSpeech.QUEUE_FLUSH,
                null,
                hashCode().toString()
            )
        }

    }

    private fun showForegroundNotification(task: Task): Unit {

        //stopForeground(true)

        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // Starting with API 26, notifications must be contained in a channel
        if (Build.VERSION.SDK_INT >= 26) {

            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_TITLE,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            channel.description = NOTIFICATION_CHANNEL_DESC
            notificationManager.createNotificationChannel(channel)

        }

        // TODO: Resume step
        val contentIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            Intent(),
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationBuilder =
            NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setContentTitle("${task.identifier} Recording")
                .setContentText("Recording")
                .setContentIntent(contentIntent)

        // TODO: set notification icon
        notificationBuilder.setSmallIcon(R.drawable.error)

        //startForeground(1, notificationBuilder.build())

        notificationManager.notify(1, notificationBuilder.build())
    }

    /**
     * This would never be called under normal operation while the recorders are running
     * It will only be called when the user chooses to end the task and discard the results
     * or in special very rare cases like user shut their phone off, serious memory issues, etc
     * It should be treated like a recorder canceled scenario
     */
    override fun onDestroy() {

        shutDownRecorder()

        super.onDestroy()

    }

    override fun onBind(intent: Intent): IBinder? =
        RecorderServiceBinder()

    @RequiresPermission(value = Manifest.permission.VIBRATE, conditional = true)
    private fun onRecorderDurationFinished(step: Step.ActiveStep) {

        if (step.shouldVibrateOnFinish) vibrate()
        if (step.shouldPlaySoundOnFinish) playSound()
        step.finishedSpokenInstruction.map { speakText(it) }

        IO.fx {

            continueOn(Dispatchers.IO)
            !effect { delay(step.estimateTimeInMsToSpeakEndInstruction) }
            continueOn(Dispatchers.Main)
            stateLiveDate.value = RecordingState.Completed(getStepIdentifier()).toEvent()
            // reset live data value to avoid to send again the last event to new subscribers
            stateLiveDate = MutableLiveData()

        }.unsafeRunAsync()

        stopRecorder()

    }

    /**
     * RecorderListener callback
     *
     * @param recorder The generating recorder object.
     * @param result   The generated result.
     */
    override fun onComplete(recorder: Recorder, result: Result) {

        // Due to the RecorderService having to store results in a SharedPreferences file
        // We do not allow any Results other than FileResults
        check(result is FileResult) {
            "RecorderService only works with Recorders that return FileResults"
        }

        state = state.map { recorderState ->
            recorderState.copy(
                recorderList = recorderState.recorderList
                    .mapNotNull { if (it == recorder) null else it },
                resultList = recorderState.resultList.plus(result)
            )
        }

    }

    /**
     * RecorderListener callback
     *
     * @param recorder The generating recorder object.
     * @param error    The error that occurred.
     */
    override fun onFail(recorder: Recorder, error: Throwable) {

        stateLiveDate.value = RecordingState.Failure(getStepIdentifier()).toEvent()
        shutDownRecorder()

    }

    private fun shutDownTts(): Unit {

        tts.map {
            if (it.isSpeaking) it.stop()
            it.shutdown()
        }

        tts = None
    }

    private fun shutDownRecorders(): Unit {

        state.map { recorderState ->
            recorderState.recorderList.map {
                it.recorderListener = None
                it.cancel()
            }
        }

    }

    private fun stopRecorders(): Unit {

        state.map { recorderState ->
            recorderState.recorderList.map { it.stop() }
        }

    }

    override val broadcastContext: Context? get() = this

    @RequiresPermission(Manifest.permission.VIBRATE)
    private fun vibrate(): Unit {

        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    DEFAULT_VIBRATION_AND_SOUND_DURATION.toLong(),
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        else vibrator.vibrate(DEFAULT_VIBRATION_AND_SOUND_DURATION.toLong())

    }

    private fun playSound(): Unit {

        val toneG = ToneGenerator(AudioManager.STREAM_ALARM, 50) // 50 = half volume
        // Play a low and high tone for 500 ms at full volume
        toneG.startTone(ToneGenerator.TONE_CDMA_LOW_L, DEFAULT_VIBRATION_AND_SOUND_DURATION)
        toneG.startTone(ToneGenerator.TONE_CDMA_HIGH_L, DEFAULT_VIBRATION_AND_SOUND_DURATION)

    }

    private fun stopRecorder(): Unit {

        stopRecorders()

        taskTimer.map { it() }
        taskTimer = None

        middleInstruction.map { list -> list.forEach { it() } }
        middleInstruction = None

    }

    private fun shutDownRecorder(): Unit {

        shutDownTts()
        shutDownRecorders()

        taskTimer.map { it() }
        taskTimer = None

        middleInstruction.map { list -> list.forEach { it() } }
        middleInstruction = None

        stateLiveDate = MutableLiveData()

        stopSelf()

    }

    private fun getStepIdentifier(): String =
        state.map { it.step.identifier }.getOrElse { "unknown" }

    /**
     * This class will be what is returned when an view binds to this service.
     * The view will also use this to know what it can get from our service to know
     * about recorder status.
     */
    inner class RecorderServiceBinder : Binder() {

        fun bind(
            outputDirectory: File,
            activeStep: Step.ActiveStep,
            task: Task
        ): Unit {

            if (activeStep.duration > 0) {

                val recorders = buildRecorderList(activeStep, outputDirectory)

                // clear old listeners
                state.map { recorderState ->
                    recorderState.recorderList.map { it.recorderListener = None }
                }

                showForegroundNotification(task)

                state = RecorderState(
                    startTime = System.currentTimeMillis(),
                    step = activeStep,
                    task = task,
                    output = outputDirectory,
                    recorderList = recorders

                ).some()

                bindTTS(activeStep)

            }

        }

        fun stateLiveData(): LiveData<Event<RecordingState>> = stateLiveDate

        fun stop(): Unit = shutDownRecorder()

    }

    companion object {


        private const val NOTIFICATION_CHANNEL_ID =
            "RecorderService_NotificationChannel"
        private const val NOTIFICATION_CHANNEL_TITLE =
            "Study progress tracker"
        private const val NOTIFICATION_CHANNEL_DESC =
            "Records and shows your progress during a task."

        private const val DEFAULT_VIBRATION_AND_SOUND_DURATION: Int = 500

        fun start(
            context: Context,
            serviceConnection: RecorderServiceConnection
        ) {

            val intent = Intent(context.applicationContext, RecorderService::class.java)

            context.startService(intent)
            context.bindService(
                intent,
                serviceConnection,
                Context.BIND_AUTO_CREATE
            )

        }
    }
}