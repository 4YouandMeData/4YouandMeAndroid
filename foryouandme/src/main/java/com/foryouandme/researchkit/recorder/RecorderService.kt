package com.foryouandme.researchkit.recorder

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
import androidx.lifecycle.lifecycleScope
import com.foryouandme.R
import com.foryouandme.core.arch.android.BaseService
import com.foryouandme.core.arch.flow.StateUpdateFlow
import com.foryouandme.core.ext.*
import com.foryouandme.researchkit.recorder.sensor.pedometer.PedometerRecorder
import com.foryouandme.researchkit.recorder.sensor.pedometer.PedometerRecorderData
import com.foryouandme.researchkit.step.sensor.SensorRecorderTarget
import com.foryouandme.researchkit.step.sensor.SensorStep
import com.foryouandme.researchkit.task.Task
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharedFlow
import timber.log.Timber
import java.io.File
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
open class RecorderService : BaseService(), RecorderListener {

    private var state: RecorderState? = null

    private var tts: TextToSpeech? = null

    private var taskTimer: Job? = null

    private var middleInstruction: List<Job>? = null

    @Inject
    lateinit var stateUpdateFlow: StateUpdateFlow<RecordingState>

    @Inject
    lateinit var sensorFlow: StateUpdateFlow<SensorData>

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_NOT_STICKY
    }

    private fun bindTTS(step: SensorStep) {
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

    private fun setupTTS(step: SensorStep, status: Int) {

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

    private fun buildRecorderList(
        step: SensorStep,
        outputDirectory: File
    ): List<Recorder> =

        step.recorderConfigurations.map {

            val recorder = it.recorderForStep(step, outputDirectory)
            recorder.recorderListener = this
            recorder

        }

    @ExperimentalCoroutinesApi
    private fun setupTaskTimer(step: SensorStep) {

        when (step.target) {
            is SensorRecorderTarget.Time -> {
                taskTimer =
                    lifecycleScope.launchSafe {

                        delay(step.target.duration * 1000L)
                        onRecorderDurationFinished(step)

                    }
            }
            is SensorRecorderTarget.Steps -> {
                state?.recorderList
                    ?.firstOrNull { it is PedometerRecorder }
                    ?.liveData()
                    ?.observeEvent { recorderData ->
                        (recorderData as? PedometerRecorderData)
                            ?.let {

                                lifecycleScope.launchSafe {

                                    sensorFlow.update(SensorData.Steps(it.steps))

                                    if (it.steps >= step.target.steps)
                                        onRecorderDurationFinished(step)

                                }

                            }
                    }

                // start also a timer for timeout = steps * 1.5
                taskTimer =
                    lifecycleScope.launchSafe {

                        delay((step.target.steps * 1.5f).toLong() * 1000L)
                        onRecorderDurationFinished(step)

                    }
            }
        }

        // start recording from sensor
        lifecycleScope.launchSafe { state?.recorderList?.map { it.start(applicationContext) } }

    }


    private fun setupReadInstructions(step: SensorStep) {

        // play intro instruction
        step.spokenInstruction?.let { speakText(it(applicationContext)) }

        // play instruction during the step
        middleInstruction =
            step.spokenInstructionMap.map { (key, value) ->

                lifecycleScope.launchSafe {

                    delay(key * 1000L)
                    speakText(value)

                }

            }
    }

    private fun speakText(text: String) {

        tts?.speak(
            text,
            TextToSpeech.QUEUE_FLUSH,
            null,
            hashCode().toString()
        )

    }

    private fun showForegroundNotification(task: Task) {

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
                .setContentTitle("${task.type} Recording")
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

        lifecycleScope.launchSafe { shutDownRecorder() }

        super.onDestroy()

    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return RecorderServiceBinder()
    }

    @ExperimentalCoroutinesApi
    private suspend fun onRecorderDurationFinished(step: SensorStep) {

        stopRecorder()

        if (step.shouldVibrateOnFinish) vibrate()
        if (step.shouldPlaySoundOnFinish) playSound()
        step.finishedSpokenInstruction?.let { speakText(it) }

        // await some time before sending the completed event
        delay(step.estimateTimeInMsToSpeakEndInstruction)
        stateUpdateFlow.update(RecordingState.Completed(getStepIdentifier()))
        // reset replay cache to avoid to send again the
        // last value to new subscribers
        stateUpdateFlow.resetReplayCache()
        sensorFlow.resetReplayCache()

    }

    /**
     * RecorderListener callback
     *
     * @param recorder The generating recorder object.
     * @param error    The error that occurred.
     */
    override fun onFail(recorder: Recorder, error: Throwable) {

        lifecycleScope.launchSafe {
            stateUpdateFlow.update(RecordingState.Failure(getStepIdentifier()))
            shutDownRecorder()
        }

    }

    private fun shutDownTts() {

        tts?.let {
            if (it.isSpeaking) it.stop()
            it.shutdown()
        }

        tts = null
    }

    private suspend fun shutDownRecorders() {

        state?.let { recorderState ->
            recorderState.recorderList.map {
                it.recorderListener = null
                it.cancel()
            }
        }

    }

    private suspend fun stopRecorders() {

        val results = state?.recorderList?.mapNotNull { it.stop() }

        mapNotNull(state?.step?.identifier, results)
            ?.let {
                stateUpdateFlow.update(RecordingState.ResultCollected(it.a, it.b))
            }

    }

    private fun vibrate() {

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

    private fun playSound() {

        val toneG = ToneGenerator(AudioManager.STREAM_ALARM, 50) // 50 = half volume
        // Play a low and high tone for 500 ms at full volume
        toneG.startTone(ToneGenerator.TONE_CDMA_LOW_L, DEFAULT_VIBRATION_AND_SOUND_DURATION)
        toneG.startTone(ToneGenerator.TONE_CDMA_HIGH_L, DEFAULT_VIBRATION_AND_SOUND_DURATION)

    }

    private suspend fun stopRecorder() {

        // stop all sensor recorder
        stopRecorders()

        // stop the timer
        taskTimer?.cancel()
        taskTimer = null

        // stop step counter
        state?.recorderList
            ?.firstOrNull { it is PedometerRecorder }
            ?.liveData()
            ?.removeObservers(this)

        // stop instruction
        middleInstruction?.forEach { it.cancel() }
        middleInstruction = null

    }

    @ExperimentalCoroutinesApi
    private suspend fun shutDownRecorder() {

        shutDownTts()
        shutDownRecorders()

        // stop the timer
        taskTimer?.cancel()
        taskTimer = null

        // stop step counter
        state?.recorderList
            ?.firstOrNull { it is PedometerRecorder }
            ?.liveData()
            ?.removeObservers(this)

        // stop instruction
        middleInstruction?.forEach { it.cancel() }
        middleInstruction = null

        stateUpdateFlow.resetReplayCache()
        sensorFlow.resetReplayCache()

        stopSelf()

    }

    private fun getStepIdentifier(): String = state?.step?.identifier ?: "unknown"

    /**
     * This class will be what is returned when an view binds to this service.
     * The view will also use this to know what it can get from our service to know
     * about recorder status.
     */
    inner class RecorderServiceBinder : Binder() {

        fun bind(
            outputDirectory: File,
            sensorStep: SensorStep,
            task: Task
        ) {

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


            lifecycleScope.launchSafe {
                stateUpdateFlow.update(RecordingState.Recording(sensorStep.identifier))
            }

        }

        val stateUpdate: SharedFlow<RecordingState> = stateUpdateFlow.stateUpdates
        val sensor: SharedFlow<SensorData> = sensorFlow.stateUpdates

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