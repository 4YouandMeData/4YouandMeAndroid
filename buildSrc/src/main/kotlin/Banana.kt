object Banana : DependencyGroup("moe.banana", "3.5.0") {

    object MoshiJsonApi : Dependency(Banana.group, "moshi-jsonapi", Banana.version)

    object MoshiRetrofitConverter :
        Dependency(Banana.group, "moshi-jsonapi-retrofit-converter", Banana.version)

}