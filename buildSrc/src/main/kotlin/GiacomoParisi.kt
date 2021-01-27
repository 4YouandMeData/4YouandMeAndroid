object GiacomoParisi {

    object SpanDroid : DependencyGroup("com.giacomoparisi.spandroid", "0.1") {

        object Span : Dependency(
            SpanDroid.group,
            "span-droid",
            SpanDroid.version
        )

    }

    object RecyclerDroid : DependencyGroup(
        "com.giacomoparisi.recyclerdroid",
        "1.8.0-beta09"
    ) {

        object Core : Dependency(
            RecyclerDroid.group,
            "recycler-droid-core",
            RecyclerDroid.version
        )

    }

}