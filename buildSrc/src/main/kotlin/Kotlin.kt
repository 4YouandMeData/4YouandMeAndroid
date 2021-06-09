object Kotlin : DependencyGroup("org.jetbrains.kotlin", "1.5.10") {

    object StdLib : Dependency(Kotlin.group, "kotlin-stdlib", Kotlin.version)

    object Test {

        object JUnit : Dependency(Kotlin.group, "kotlin-test-junit", Kotlin.version)

    }

}