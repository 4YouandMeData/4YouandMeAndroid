object IOMockK : DependencyGroup("io.mockk", "1.12.0") {

    object MockK : Dependency(IOMockK.group, "mockk", IOMockK.version)

}