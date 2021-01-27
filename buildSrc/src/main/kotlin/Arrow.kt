object Arrow : DependencyGroup("io.arrow-kt", "0.11.0") {

    object Fx : Dependency(Arrow.group, "arrow-fx", Arrow.version)

    object FxCoroutines : Dependency(Arrow.group, "arrow-fx-coroutines", Arrow.version)

    object Optics : Dependency(Arrow.group, "arrow-optics", Arrow.version)

    object Syntax : Dependency(Arrow.group, "arrow-syntax", Arrow.version)

    object Meta : Dependency(Arrow.group, "arrow-meta", Arrow.version)

}