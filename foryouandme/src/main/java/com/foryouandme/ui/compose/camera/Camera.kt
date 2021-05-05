package com.foryouandme.ui.compose.camera

import android.annotation.SuppressLint
import android.content.Context
import android.util.Rational
import android.util.Size
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.core.VideoCapture
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.foryouandme.core.ext.catchToNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlin.math.roundToInt

@SuppressLint("UnsafeOptInUsageError", "MissingPermission", "RestrictedApi")
@Composable
fun Camera(
    cameraLens: CameraLens,
    cameraFlash: CameraFlash,
    cameraEvents: Flow<CameraEvent>,
    modifier: Modifier = Modifier,
    onRecordError: () -> Unit = {},
    videoSettings: VideoSettings = VideoSettings()
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val videoCapture = remember { builtVideoCapture(videoSettings) }
    var camera: Camera? by remember { mutableStateOf(null) }
    var currentLens by remember { mutableStateOf(cameraLens) }
    var currentFlash by remember { mutableStateOf(cameraFlash) }

    BoxWithConstraints(modifier = modifier) {

        val widthPixel = LocalDensity.current.run { maxWidth.toPx() }
        val heightPixel = LocalDensity.current.run { maxHeight.toPx() }

        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx)
                previewView.scaleType = PreviewView.ScaleType.FILL_CENTER
                previewView.implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                startCamera(
                    context,
                    lifecycleOwner,
                    previewView,
                    videoCapture,
                    cameraLens,
                    widthPixel,
                    heightPixel
                )
                {
                    camera = it
                    currentLens = cameraLens
                }
                previewView
            },
            modifier = Modifier.fillMaxSize(),
            update = { view ->

                if (cameraLens != currentLens)
                    startCamera(
                        context,
                        lifecycleOwner,
                        view,
                        videoCapture,
                        cameraLens,
                        widthPixel,
                        heightPixel
                    )
                    {
                        camera = it
                        currentLens = cameraLens
                    }

                if (cameraFlash != currentFlash) {
                    camera?.cameraControl?.enableTorch(cameraFlash is CameraFlash.On)
                    currentFlash = cameraFlash
                }

            }
        )
    }

    LaunchedEffect(key1 = cameraEvents) {
        cameraEvents.onEach {
            when (it) {
                CameraEvent.Pause ->
                    videoCapture.stopRecording()
                is CameraEvent.Record -> {

                    videoCapture.startRecording(
                        VideoCapture.OutputFileOptions.Builder(it.file).build(),
                        ContextCompat.getMainExecutor(context),
                        object : VideoCapture.OnVideoSavedCallback {

                            override fun onVideoSaved(
                                outputFileResults: VideoCapture.OutputFileResults
                            ) {

                            }

                            override fun onError(
                                videoCaptureError: Int,
                                message: String,
                                cause: Throwable?
                            ) {
                                onRecordError()
                            }

                        }
                    )
                }
            }
        }.collect()
    }

}

fun startCamera(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    previewView: PreviewView,
    videoCapture: VideoCapture,
    lens: CameraLens,
    width: Float,
    height: Float,
    onCameraReady: (Camera) -> Unit
) {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
    val executor = ContextCompat.getMainExecutor(context)
    val aspectRatio = Rational(width.roundToInt(), height.roundToInt())
    cameraProviderFuture.addListener(
        {
            val cameraProvider = cameraProviderFuture.get()
            val preview =
                Preview.Builder()
                    .setTargetAspectRatio(aspectRatio.toInt())
                    .build()
                    .also { it.setSurfaceProvider(previewView.surfaceProvider) }

            val cameraSelector =
                CameraSelector.Builder()
                    .requireLensFacing(
                        when (lens) {
                            CameraLens.Back -> CameraSelector.LENS_FACING_BACK
                            CameraLens.Front -> CameraSelector.LENS_FACING_FRONT
                        }
                    )
                    .build()

            catchToNull { cameraProvider.unbindAll() }
            val camera =
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    videoCapture
                )

            onCameraReady(camera)

        },
        executor
    )
}

@SuppressLint("RestrictedApi")
private fun builtVideoCapture(settings: VideoSettings): VideoCapture {

    val builder = VideoCapture.Builder()

    if (settings.frameRate != null) builder.setVideoFrameRate(settings.frameRate)
    if (settings.targetResolution != null) {

        val size = Size(settings.targetResolution.width, settings.targetResolution.height)
        builder.setTargetResolution(size)
        builder.setDefaultResolution(size)
        builder.setMaxResolution(size)

    }

    if (settings.bitrate != null) builder.setBitRate(settings.bitrate)

    return builder.build()

}