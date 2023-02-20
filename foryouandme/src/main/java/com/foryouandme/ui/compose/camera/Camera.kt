package com.foryouandme.ui.compose.camera

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.util.Size
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.VideoCapture
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.GPUImageView
import jp.co.cyberagent.android.gpuimage.filter.GPUImageColorInvertFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSobelEdgeDetectionFilter
import jp.co.cyberagent.android.gpuimage.util.Rotation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@SuppressLint("UnsafeOptInUsageError", "MissingPermission", "RestrictedApi")
@Composable
fun Camera(
    cameraLens: CameraLens,
    cameraFlash: CameraFlash,
    filterCamera: FilterCamera,
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
    var currentFilterCamera by remember { mutableStateOf(filterCamera) }
    var converter = YuvToRgbConverter(context)
    var bitmap: Bitmap? = null
    val executor = Executors.newSingleThreadExecutor()
    var gpuImageView: GPUImageView? = null
    val filterNormal = GPUImageFilter()
    val filterInverted = GPUImageSobelEdgeDetectionFilter().apply {
        setLineSize(0.4F).apply {
            addFilter(GPUImageColorInvertFilter())
        }
    }

    BoxWithConstraints(modifier = modifier) {
        AndroidView(
            factory = { ctx ->
                gpuImageView = GPUImageView(ctx)
                gpuImageView?.setRotation(Rotation.ROTATION_90)
                gpuImageView?.setScaleType(GPUImage.ScaleType.CENTER_CROP)
                converter = YuvToRgbConverter(ctx)

                startCameraGPU(
                    gpuImageView!!,
                    executor,
                    converter,
                    cameraLens,
                    lifecycleOwner,
                    bitmap,
                    ctx,
                    videoCapture,
                    filterCamera,
                    filterNormal,
                    filterInverted,
                )
                {
                    camera = it
                    currentLens = cameraLens
                    currentFilterCamera = filterCamera
                }
                gpuImageView!!
            },
            modifier = Modifier.fillMaxSize(),
            update = { view ->
                if (cameraLens != currentLens || currentFilterCamera != filterCamera)
                    startCameraGPU(
                        view,
                        executor,
                        converter,
                        cameraLens,
                        lifecycleOwner,
                        bitmap,
                        context,
                        videoCapture,
                        filterCamera,
                        filterNormal,
                        filterInverted
                    )
                    {
                        camera = it
                        currentLens = cameraLens
                        currentFilterCamera = filterCamera
                    }

                if (cameraFlash != currentFlash) {
                    camera?.cameraControl?.enableTorch(cameraFlash is CameraFlash.On)
                    currentFlash = cameraFlash
                }

            }

        )

        // Oval for face positioning
        Canvas(modifier = Modifier.fillMaxSize(), onDraw = {
            val ovalPath = Path().apply {
                val centerX = size.width / 2
                val centerY = size.height / 2 - size.height / 12
                val radiusY = size.minDimension / 1.45f
                val radiusX = radiusY / 1.55f
                addOval(Rect(centerX - radiusX, centerY - radiusY, centerX + radiusX, centerY + radiusY))
            }
            clipPath(ovalPath, clipOp = ClipOp.Difference) {
                drawRect(SolidColor(Color.Black.copy(alpha = 0.35f)))
            }
        })

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

fun startCameraGPU(
    gpuImageView: GPUImageView,
    executor: Executor,
    converter: YuvToRgbConverter,
    cameraLens: CameraLens,
    lifecycleOwner: LifecycleOwner,
    oldBitmap: Bitmap?,
    context: Context,
    videoCapture: VideoCapture,
    filterCamera: FilterCamera,
    filterStandard: GPUImageFilter,
    filterInverted: GPUImageSobelEdgeDetectionFilter,
    onCameraReady: (Camera?) -> Unit,
) {
    var cameraProvider: ProcessCameraProvider? = null
    val cameraProviderFuture = ProcessCameraProvider.getInstance(context);
    cameraProviderFuture.addListener({
        cameraProvider = cameraProviderFuture.get()
        startCameraIfReady(
            gpuImageView,
            executor,
            converter,
            cameraLens,
            cameraProvider,
            lifecycleOwner,
            oldBitmap,
            context,
            videoCapture,
            filterCamera,
            filterStandard,
            filterInverted,
            onCameraReady,
        )
    }, ContextCompat.getMainExecutor(context))
}

@SuppressLint("UnsafeOptInUsageError")
fun startCameraIfReady(
    gpuImageView: GPUImageView,
    executor: Executor,
    converter: YuvToRgbConverter,
    cameraLens: CameraLens,
    cameraProvider: ProcessCameraProvider?,
    lifecycleOwner: LifecycleOwner,
    oldBitmap: Bitmap?,
    context: Context,
    videoCapture: VideoCapture,
    filterCamera: FilterCamera,
    filterStandard: GPUImageFilter,
    filterInverted: GPUImageSobelEdgeDetectionFilter,
    onCameraReady: (Camera?) -> Unit,
) {

    cameraProvider?.unbindAll()

    gpuImageView.filter =
        when (filterCamera) {
            FilterCamera.On -> filterInverted
            FilterCamera.Off -> filterStandard
        }

    val imageAnalysis =
        ImageAnalysis.Builder().setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
    imageAnalysis.setAnalyzer(executor) {
        var bitmap = allocateBitmapIfNecessary(it.width, it.height, oldBitmap)
        converter.yuvToRgb(it.image!!, bitmap)
        it.close()
        gpuImageView.post {
            gpuImageView.setImage(bitmap)
        }
    }

    val cameraSelector =
        CameraSelector.Builder()

            .requireLensFacing(
                when (cameraLens) {
                    CameraLens.Back -> CameraSelector.LENS_FACING_BACK
                    CameraLens.Front -> CameraSelector.LENS_FACING_FRONT
                }
            )
            .build()

    if (cameraLens is CameraLens.Front) {
        gpuImageView.setRotation(Rotation.ROTATION_270)
        gpuImageView.rotationY = 180f
    } else {
        gpuImageView.setRotation(Rotation.ROTATION_90)
    }

    val camera =
        cameraProvider?.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            videoCapture
        )

    onCameraReady(camera)

    cameraProvider?.bindToLifecycle(lifecycleOwner, cameraSelector, imageAnalysis, videoCapture)
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