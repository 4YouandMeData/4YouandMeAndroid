open class DependencyGroup(val group: String, val version: String)

open class DependencyIndependentGroup(val group: String)

open class Dependency(
    private val group: String,
    private val artifact: String,
    private val version: String
) {

    fun get(): String = "$group:$artifact:$version"

}