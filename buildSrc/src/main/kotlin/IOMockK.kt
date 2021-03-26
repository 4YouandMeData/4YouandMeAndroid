object IOMockK : DependencyGroup("io.mockk", "1.11.0") {

    object MockK : Dependency(IOMockK.group, "mockk", IOMockK.version)

}