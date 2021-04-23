package com.foryouandme.researchkit.step.video.compose

import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat

@Composable
fun Camera(
    isBackCameraToggled: Boolean,
    isFlashEnabled: Boolean,
    modifier: Modifier = Modifier
) {

    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val cameraController = remember { LifecycleCameraController(context) }

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
                                if (isBackCameraToggled) CameraSelector.LENS_FACING_BACK
                                else CameraSelector.LENS_FACING_FRONT
                            )
                            .build()

                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview
                    )
                },
                executor
            )
            previewView
        },
        modifier = modifier,
    )

    cameraController.cameraSelector =
        if (isBackCameraToggled) CameraSelector.DEFAULT_BACK_CAMERA
        else CameraSelector.DEFAULT_FRONT_CAMERA

    cameraController.enableTorch(isFlashEnabled)

}