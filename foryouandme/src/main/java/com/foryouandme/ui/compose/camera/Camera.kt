package com.foryouandme.ui.compose.camera

import android.annotation.SuppressLint
import android.content.Context
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.core.VideoCapture
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.foryouandme.core.ext.catchToNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@SuppressLint("UnsafeOptInUsageError", "MissingPermission", "RestrictedApi")
@Composable
fun Camera(
    cameraLens: CameraLens,
    cameraFlash: CameraFlash,
    cameraEvents: Flow<CameraEvent>,
    modifier: Modifier = Modifier,
    onRecordError: () -> Unit = {}
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val videoCapture = remember { VideoCapture.Builder().build() }
    var camera: Camera? by remember { mutableStateOf(null) }
    var currentLens by remember { mutableStateOf(cameraLens) }
    var currentFlash by remember { mutableStateOf(cameraFlash) }

    AndroidView(
        factory = { ctx ->
            val previewView = PreviewView(ctx)
            startCamera(context, lifecycleOwner, previewView, videoCapture, cameraLens)
            {
                camera = it
                currentLens = cameraLens
            }
            previewView
        },
        modifier = modifier,
        update = { view ->

            if (cameraLens != currentLens)
                startCamera(context, lifecycleOwner, view, videoCapture, cameraLens)
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
    onCameraReady: (Camera) -> Unit
) {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
    val executor = ContextCompat.getMainExecutor(context)
    cameraProviderFuture.addListener(
        {
            val cameraProvider = cameraProviderFuture.get()
            val preview =
                Preview.Builder()
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