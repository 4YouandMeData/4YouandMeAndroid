object Android {

    object Tools : DependencyIndependentGroup("com.android.tools") {

        object Desugar : Dependency(Tools.group, "desugar_jdk_libs", "1.1.5")

    }

}