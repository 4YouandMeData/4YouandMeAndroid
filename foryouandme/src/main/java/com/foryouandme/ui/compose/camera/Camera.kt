package com.foryouandme.ui.compose.camera

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.SurfaceTexture
import android.opengl.GLSurfaceView
import android.util.Rational
import android.util.Size
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
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
import com.foryouandme.core.ext.findActivity
import com.google.mediapipe.components.CameraHelper
import com.google.mediapipe.components.CameraHelper.CameraFacing
import com.google.mediapipe.components.CameraXPreviewHelper
import com.google.mediapipe.components.ExternalTextureConverter
import com.google.mediapipe.components.FrameProcessor
import com.google.mediapipe.framework.AndroidAssetUtil
import com.google.mediapipe.glutil.EglManager
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.GPUImageView
import jp.co.cyberagent.android.gpuimage.filter.GPUImageColorInvertFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSobelEdgeDetectionFilter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import java.util.concurrent.Executor
import java.util.concurrent.Executors
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
    var converter : YuvToRgbConverter = YuvToRgbConverter(context)
    var bitmap : Bitmap? = null
    val executor = Executors.newSingleThreadExecutor()
    var gpuImageView : GPUImageView

    BoxWithConstraints(modifier = modifier) {

            AndroidView(
                factory = { ctx ->
                    gpuImageView = GPUImageView(ctx)
                    converter = YuvToRgbConverter(ctx)
                    gpuImageView.rotation = 270F
                    gpuImageView.rotationY = 180F
                    gpuImageView.setScaleType(GPUImage.ScaleType.CENTER_CROP)
                    gpuImageView.filter = GPUImageSobelEdgeDetectionFilter().apply { setLineSize(0.5F) .apply { addFilter(GPUImageColorInvertFilter()) } }

                    startCameraGPU(
                        gpuImageView,
                        executor,
                        converter,
                        cameraLens,
                        cameraProvider,
                        lifecycleOwner,
                        bitmap,
                        ctx
                    )
                    {
                        camera = it
                        currentLens = cameraLens
                    }
                    gpuImageView
                },
                modifier = Modifier.fillMaxSize(),
                update = { view ->
                    if (cameraLens != currentLens)
                        startCameraGPU(
                            view,
                            executor,
                            converter,
                            cameraLens,
                            cameraProvider,
                            lifecycleOwner,
                            bitmap,
                            context
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

fun allocateBitmapIfNecessary(width: Int, height: Int, bitmap: Bitmap?): Bitmap {
    var newBitmap = bitmap
    if (newBitmap == null || newBitmap!!.width != width || newBitmap!!.height != height) {
        newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    }
    return newBitmap!!
}

@SuppressLint("UnsafeOptInUsageError")
fun startCameraGPU(
    gpuImageView: GPUImageView,
    executor: Executor,
    converter: YuvToRgbConverter,
    cameraLens: CameraLens,
    cameraProvider : ProcessCameraProvider?,
    lifecycleOwner: LifecycleOwner,
    oldBitmap: Bitmap?,
    context: Context,
    onCameraReady: (Camera) -> Unit
) {

    var cameraProvider = ProcessCameraProvider.getInstance()
    val cameraProviderFuture = ProcessCameraProvider.getInstance(context);
    cameraProviderFuture.addListener(Runnable {
        cameraProvider = cameraProviderFuture.get()
    }, ContextCompat.getMainExecutor(context))

    val imageAnalysis = ImageAnalysis.Builder().setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build()
    imageAnalysis.setAnalyzer(executor, ImageAnalysis.Analyzer {
        var bitmap = allocateBitmapIfNecessary(it.width, it.height, oldBitmap)
        converter.yuvToRgb(it.image!!, bitmap)
        it.close()
        gpuImageView.post {
            gpuImageView.setImage(bitmap)
        }
    })
    val cameraSelector =
        CameraSelector.Builder()
            .requireLensFacing(
                when (cameraLens) {
                    CameraLens.Back -> CameraSelector.LENS_FACING_BACK
                    CameraLens.Front -> CameraSelector.LENS_FACING_FRONT
                }
            )
            .build()

    cameraProvider?.bindToLifecycle(lifecycleOwner, cameraSelector, imageAnalysis)
}

fun startCamera (
    context: Context,
    lifecycleOwner: LifecycleOwner,
    previewView: PreviewView,
    videoCapture: VideoCapture,
    lens: CameraLens,
    width: Float,
    height: Float,
    onCameraReady: (Camera) -> Unit,
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
