object MediaPipe {
    object Flogger : DependencyGroup ("com.google.flogger", "latest.release") {
        object FloggerCore : Dependency (
            Flogger.group,
            "flogger",
            Flogger.version
        )

        object FloggerSystemBackend : Dependency (
            Flogger.group,
            "flogger-system-backend",
            Flogger.version
        )

    }

    object Findbugs : DependencyGroup ("com.google.code.findbugs", "latest.release") {
        object FindbugsJsr : Dependency (
            Findbugs.group,
            "jsr305",
            Findbugs.version
        )
    }

    object Guava : DependencyGroup ("com.google.guava", "27.0.1-android") {
        object GuavaCore : Dependency (
            Guava.group,
            "guava",
            Guava.version
        )
    }

    object Protobuf : DependencyGroup ("com.google.protobuf", "3.19.1") {
        object ProtobufJavalite : Dependency (
            Protobuf.group,
            "protobuf-javalite",
            Protobuf.version
        )
    }

    object AutoValue : DependencyGroup ("com.google.auto.value", "1.8.1") {
        object AutoValueAnnotation : Dependency (
            AutoValue.group,
            "auto-value-annotations",
            AutoValue.version
        )

        object AutoValueCore : Dependency (
            AutoValue.group,
            "auto-value",
            AutoValue.version
        )
    }

    //@TODO remove in case of necessary

    object GPUImage : DependencyGroup ("jp.co.cyberagent.android", "2.1.0") {
        object GPUImageCore : Dependency (
            GPUImage.group,
            "gpuimage",
            GPUImage.version
        )
    }
}