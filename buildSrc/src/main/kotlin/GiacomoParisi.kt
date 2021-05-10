object GiacomoParisi {

    object SpanDroid : DependencyGroup("com.github.giacomoParisi", "4271a0ec1b") {

        object Span : Dependency(
            SpanDroid.group,
            "span-droid",
            SpanDroid.version
        )

    }

    object RecyclerDroid : DependencyGroup(
        "com.github.giacomoParisi",
        "f0ba0306bc"
    ) {

        object Core : Dependency(
            RecyclerDroid.group,
            "recycler-droid",
            RecyclerDroid.version
        )

    }

}