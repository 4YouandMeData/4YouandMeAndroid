package com.foryouandme.researchkit.recorder

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.*
import android.speech.tts.TextToSpeech
import androidx.lifecycle.lifecycleScope
import com.foryouandme.core.arch.android.BaseService
import com.foryouandme.core.arch.flow.ErrorFlow
import com.foryouandme.core.arch.flow.StateUpdateFlow
import com.foryouandme.core.arch.flow.observeIn
import com.foryouandme.core.ext.launchSafe
import com.foryouandme.core.ext.mapNotNull
import com.foryouandme.researchkit.recorder.sensor.pedometer.PedometerRecorder
import com.foryouandme.researchkit.recorder.sensor.pedometer.PedometerRecorderData
import com.foryouandme.researchkit.step.sensor.SensorRecorderTarget
import com.foryouandme.researchkit.step.sensor.SensorStep
import com.foryouandme.researchkit.task.Task
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import java.io.File
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
open class RecorderService : BaseService() {

    /* --- state --- */
    private var state: RecorderState? = null

    /* --- tts --- */
    private var tts: TextToSpeech? = null

    /* --- job --- */
    private var taskTimer: Job? = null
    private var middleInstruction: List<Job>? = null
    private var sensorJob: Job? = null
    private var recorderJob: List<Job>? = null

    /* --- flow --- */
    @Inject
    lateinit var stateUpdateFlow: StateUpdateFlow<RecorderStateUpdate>

    @Inject
    lateinit var sensorFlow: StateUpdateFlow<SensorData>

    @Inject
    lateinit var errorFlow: ErrorFlow<RecorderError>

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_NOT_STICKY
    }

    private fun bindTTS(step: SensorStep) {
        if (tts == null)
            tts =
                if (step.hasVoice())
                    TextToSpeech(this) { setupTTS(step, it) }
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
            recorder

        }

    @ExperimentalCoroutinesApi
    private fun setupTaskTimer(step: SensorStep) {

        when (step.target) {
            is SensorRecorderTarget.Time -> {
                taskTimer =
                    lifecycleScope.launchSafe {

                        delay(step.target.duration * 1000L)
                        onRecorderDurationFinished(step, false)

                    }
            }
            is SensorRecorderTarget.Steps -> {
                sensorJob =
                    state?.recorderList
                        ?.firstOrNull { it is PedometerRecorder }
                        ?.flow
                        ?.onEach { recorderData ->
                            (recorderData as? PedometerRecorderData)
                                ?.let {

                                    lifecycleScope.launchSafe {

                                        sensorFlow.update(SensorData.Steps(it.steps))

                                        if (it.steps >= step.target.steps)
                                            onRecorderDurationFinished(step, true)

                                    }

                                }
                        }
                        ?.observeIn(this)
                        ?.job

                // start also a timer for timeout = steps * 1.5
                taskTimer =
                    lifecycleScope.launchSafe {

                        delay((step.target.steps * 1.5f).toLong() * 1000L)
                        onRecorderDurationFinished(step, false)

                    }
            }
        }

        // start recording from sensor
        state?.recorderList?.map {
            errorFlow.launchCatch(lifecycleScope, RecorderError.Recording(getStepIdentifier())) {
                it.start(applicationContext)
            }
        }

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
    private suspend fun onRecorderDurationFinished(step: SensorStep, stopTimer: Boolean) {

        stopRecorder(stopTimer)

        if (step.shouldVibrateOnFinish) vibrate()
        if (step.shouldPlaySoundOnFinish) playSound()
        step.finishedSpokenInstruction?.let { speakText(it) }

        // await some time before sending the completed event
        delay(step.estimateTimeInMsToSpeakEndInstruction)
        stateUpdateFlow.update(RecorderStateUpdate.Completed(getStepIdentifier()))

    }

    private fun shutDownTts() {

        tts?.let {
            if (it.isSpeaking) it.stop()
            it.shutdown()
        }

        tts = null
    }

    private suspend fun shutDownRecorders() {

        state?.let { recorderState -> recorderState.recorderList.map { it.cancel() } }

    }

    private suspend fun stopRecorders() {

        val results = state?.recorderList?.mapNotNull { it.stop() }

        mapNotNull(state?.step?.identifier, results)
            ?.let {
                stateUpdateFlow.update(RecorderStateUpdate.ResultCollected(it.a, it.b))
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

    private suspend fun stopRecorder(stopTimer: Boolean) {

        // stop all sensor recorder
        stopRecorders()

        // stop the timer
        if (stopTimer) {
            taskTimer?.cancel()
            taskTimer = null
        }

        // stop step counter
        sensorJob?.cancel()
        sensorJob = null

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
        sensorJob?.cancel()
        sensorJob = null

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
            recorderJob?.forEach { it.cancel() }
            recorderJob = null

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
                stateUpdateFlow.update(RecorderStateUpdate.Recording(sensorStep.identifier))
            }

        }

        val stateUpdate = stateUpdateFlow.stateUpdates
        val error = errorFlow.error
        val sensor = sensorFlow.stateUpdates

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