object Vanpra: DependencyGroup("io.github.vanpra.compose-material-dialogs", "0.4.2") {

    object Core: Dependency(Vanpra.group, "core", Vanpra.version)

    object Datetime: Dependency(Vanpra.group, "datetime", Vanpra.version)

}