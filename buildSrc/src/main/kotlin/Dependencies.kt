open class DependencyGroup(val group: String, val version: String)

open class DependencyIndependentGroup(val group: String)

open class Dependency(
    val group: String,
    val artifact: String,
    val version: String
) {

    fun get(): String = "$group:$artifact:$version"

}