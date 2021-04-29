package com.foryouandme.ui.compose.camera

import android.annotation.SuppressLint
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.core.VideoCapture
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
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
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val cameraController = remember { LifecycleCameraController(context) }
    val videoCapture = remember { VideoCapture.Builder().build() }

    AndroidView(
        factory = { ctx ->
            val previewView = PreviewView(ctx)
            previewView.controller = cameraController
            cameraController.bindToLifecycle(lifecycleOwner)
            val executor = ContextCompat.getMainExecutor(ctx)
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
                                when (cameraLens) {
                                    CameraLens.Back -> CameraSelector.LENS_FACING_BACK
                                    CameraLens.Front -> CameraSelector.LENS_FACING_FRONT
                                }
                            )
                            .build()

                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        videoCapture
                    )
                },
                executor
            )
            previewView
        },
        modifier = modifier,
    )

    LaunchedEffect(cameraLens) {
        cameraController.cameraSelector =
            when (cameraLens) {
                CameraLens.Back -> CameraSelector.DEFAULT_BACK_CAMERA
                CameraLens.Front -> CameraSelector.DEFAULT_FRONT_CAMERA
            }
    }

    LaunchedEffect(cameraFlash) {
        cameraController.enableTorch(cameraFlash is CameraFlash.On)
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

                            override fun onVideoSaved(outputFileResults: VideoCapture.OutputFileResults) {

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