object IOMockK : DependencyGroup("io.mockk", "1.10.5") {

    object MockK : Dependency(IOMockK.group, "mockk", IOMockK.version)

}