object GPUImage {

    object GPUImage : DependencyGroup ("jp.co.cyberagent.android", "2.1.0") {
        object GPUImageCore : Dependency (
            GPUImage.group,
            "gpuimage",
            GPUImage.version
        )
    }

}