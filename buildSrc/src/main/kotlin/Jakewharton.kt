object Jakewharton : DependencyIndependentGroup("com.jakewharton") {

    object Timber : Dependency(
        "${Jakewharton.group}.timber",
        "timber",
        "4.7.1"
    )

    object ThreeTenAbp : Dependency(
        "${Jakewharton.group}.threetenabp",
        "threetenabp",
        "1.3.0"
    )

}