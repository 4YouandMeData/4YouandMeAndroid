package org.fouryouandme.researchkit.recorder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.*
import android.speech.tts.TextToSpeech
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import arrow.fx.typeclasses.Disposable
import kotlinx.coroutines.delay
import org.fouryouandme.R
import org.fouryouandme.core.arch.android.BaseService
import org.fouryouandme.core.arch.livedata.Event
import org.fouryouandme.core.arch.livedata.toEvent
import org.fouryouandme.core.ext.*
import org.fouryouandme.researchkit.recorder.sensor.pedometer.PedometerRecorder
import org.fouryouandme.researchkit.recorder.sensor.pedometer.PedometerRecorderData
import org.fouryouandme.researchkit.step.sensor.SensorRecorderTarget
import org.fouryouandme.researchkit.step.sensor.SensorStep
import org.fouryouandme.researchkit.task.Task
import timber.log.Timber
import java.io.File
import java.util.*

open class RecorderService : BaseService(), RecorderListener {

    private var state: RecorderState? = null

    private var tts: TextToSpeech? = null

    private var taskTimer: Disposable? = null

    private var middleInstruction: List<Disposable>? = null

    private var stateLiveDate: MutableLiveData<Event<RecordingState>> = MutableLiveData()

    private var sensorLiveDate: MutableLiveData<Event<SensorData>> = MutableLiveData()

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_NOT_STICKY
    }

    private suspend fun bindTTS(step: SensorStep): Unit =
        evalOnMain {
            if (tts == null)
                tts =
                    if (step.hasVoice())
                        TextToSpeech(this) { startCoroutineAsync { setupTTS(step, it) } }
                    else null
            else if (step.hasVoice()) {
                setupTaskTimer(step)
                setupReadInstructions(step)
            }
        }

    private suspend fun setupTTS(step: SensorStep, status: Int): Unit =
        evalOnMain {

            if (status == TextToSpeech.SUCCESS) {

                tts?.let {

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
                        shutDownTts()
                }

            } else {
                Timber.e("Failed to initialize TTS with error code $status")
                shutDownTts()
            }

            setupTaskTimer(step)
            setupReadInstructions(step)

        }

    private suspend fun buildRecorderList(
        step: SensorStep,
        outputDirectory: File
    ): List<Recorder> =

        step.recorderConfigurations.map {

            val recorder = it.recorderForStep(step, outputDirectory)
            recorder.recorderListener = this
            recorder

        }

    private suspend fun setupTaskTimer(step: SensorStep): Unit {

        when (step.target) {
            is SensorRecorderTarget.Time -> {
                taskTimer =
                    startCoroutineCancellableAsync {

                        evalOnIO { delay(step.target.duration * 1000L) }
                        evalOnMain { onRecorderDurationFinished(step) }

                    }
            }
            is SensorRecorderTarget.Steps -> {
                state?.recorderList
                    ?.firstOrNull { it is PedometerRecorder }
                    ?.liveData()
                    ?.observeEvent { recorderData ->
                        (recorderData as? PedometerRecorderData)
                            ?.let {

                                sensorLiveDate.value = SensorData.Steps(it.steps).toEvent()

                                if (it.steps >= step.target.steps)
                                    startCoroutineAsync { onRecorderDurationFinished(step) }

                            }
                    }

                // start also a timer for timeout = steps * 1.5
                /*taskTimer =
                    startCoroutineCancellableAsync {

                        evalOnIO { delay((step.target.steps * 1.5f).toLong() * 1000L) }
                        evalOnMain { onRecorderDurationFinished(step) }

                    }*/
            }
        }

        // start recording from sensor
        state?.recorderList?.map { it.start(applicationContext) }

    }


    private suspend fun setupReadInstructions(step: SensorStep): Unit {

        // play intro instruction
        step.spokenInstruction?.let { speakText(it) }

        // play instruction during the step
        middleInstruction =
            step.spokenInstructionMap.map { (key, value) ->

                startCoroutineCancellableAsync {

                    evalOnIO { delay(key * 1000L) }
                    evalOnMain { speakText(value) }

                }

            }
    }

    private suspend fun speakText(text: String): Unit {

        tts?.speak(
            text,
            TextToSpeech.QUEUE_FLUSH,
            null,
            hashCode().toString()
        )
    }

    private suspend fun showForegroundNotification(task: Task): Unit {

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

        startCoroutineAsync { shutDownRecorder() }

        super.onDestroy()

    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return RecorderServiceBinder()
    }

    private suspend fun onRecorderDurationFinished(step: SensorStep) =
        evalOnMain {

            stopRecorder()

            if (step.shouldVibrateOnFinish) vibrate()
            if (step.shouldPlaySoundOnFinish) playSound()
            step.finishedSpokenInstruction?.let { speakText(it) }

            // await some time before sending the completed event
            startCoroutineAsync {

                evalOnIO { delay(step.estimateTimeInMsToSpeakEndInstruction) }
                evalOnMain {
                    stateLiveDate.value = RecordingState.Completed(getStepIdentifier()).toEvent()
                    // reset live data value to avoid to send again the
                    // last event to new subscribers
                    stateLiveDate = MutableLiveData()
                    sensorLiveDate = MutableLiveData()
                }

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
        startCoroutineAsync { shutDownRecorder() }

    }

    private suspend fun shutDownTts(): Unit {

        tts?.let {
            if (it.isSpeaking) it.stop()
            it.shutdown()
        }

        tts = null
    }

    private suspend fun shutDownRecorders(): Unit {

        state?.let { recorderState ->
            recorderState.recorderList.map {
                it.recorderListener = null
                it.cancel()
            }
        }

    }

    private suspend fun stopRecorders(): Unit {

        val results = state?.recorderList?.mapNotNull { it.stop() }

        mapNotNull(state?.step?.identifier, results)
            ?.let {
                stateLiveDate.postValue(RecordingState.ResultCollected(it.a, it.b).toEvent())
            }

    }

    private suspend fun vibrate(): Unit {

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

    private suspend fun playSound(): Unit {

        val toneG = ToneGenerator(AudioManager.STREAM_ALARM, 50) // 50 = half volume
        // Play a low and high tone for 500 ms at full volume
        toneG.startTone(ToneGenerator.TONE_CDMA_LOW_L, DEFAULT_VIBRATION_AND_SOUND_DURATION)
        toneG.startTone(ToneGenerator.TONE_CDMA_HIGH_L, DEFAULT_VIBRATION_AND_SOUND_DURATION)

    }

    private suspend fun stopRecorder(): Unit {

        // stop all sensor recorder
        stopRecorders()

        // stop the timer
        taskTimer?.invoke()
        taskTimer = null

        // stop step counter
        state?.recorderList
            ?.firstOrNull { it is PedometerRecorder }
            ?.liveData()
            ?.removeObservers(this)

        // stop instruction
        middleInstruction?.let { list -> list.forEach { it() } }
        middleInstruction = null

    }

    private suspend fun shutDownRecorder(): Unit {

        shutDownTts()
        shutDownRecorders()

        // stop the timer
        taskTimer?.invoke()
        taskTimer = null

        // stop step counter
        state?.recorderList
            ?.firstOrNull { it is PedometerRecorder }
            ?.liveData()
            ?.removeObservers(this)

        // stop instruction
        middleInstruction?.let { list -> list.forEach { it() } }
        middleInstruction = null

        stateLiveDate = MutableLiveData()
        sensorLiveDate = MutableLiveData()

        stopSelf()

    }

    private fun getStepIdentifier(): String =
        state?.step?.identifier ?: "unknown"

    /**
     * This class will be what is returned when an view binds to this service.
     * The view will also use this to know what it can get from our service to know
     * about recorder status.
     */
    inner class RecorderServiceBinder : Binder() {

        suspend fun bind(
            outputDirectory: File,
            sensorStep: SensorStep,
            task: Task
        ): Unit {

            val recorders = buildRecorderList(sensorStep, outputDirectory)

            // clear old listeners
            state?.let { recorderState ->
                recorderState.recorderList.map { it.recorderListener = null }
            }

            showForegroundNotification(task)

            state =
                RecorderState(
                    startTime = System.currentTimeMillis(),
                    step = sensorStep,
                    task = task,
                    output = outputDirectory,
                    recorderList = recorders

                )

            bindTTS(sensorStep)


            stateLiveDate.postValue(
                RecordingState.Recording(sensorStep.identifier).toEvent()
            )

        }

        fun stateLiveData(): LiveData<Event<RecordingState>> = stateLiveDate

        fun sensorLiveData(): LiveData<Event<SensorData>> = sensorLiveDate

        suspend fun stop(): Unit = shutDownRecorder()

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