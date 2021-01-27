object Squareup {

    object Moshi : DependencyGroup("com.squareup.moshi", "1.11.0") {

        object Kotlin : Dependency(Moshi.group, "moshi-kotlin", Moshi.version)

        object Adapters : Dependency(Moshi.group, "moshi-adapters", Moshi.version)

    }

    object Retrofit2 : DependencyGroup("com.squareup.retrofit2", "2.9.0") {

        object Retrofit : Dependency(Retrofit2.group, "retrofit", Retrofit2.version)

        object Converter {

            object Moshi : Dependency(Retrofit2.group, "converter-moshi", Retrofit2.version)

        }

    }

    object OkHttp3 : DependencyGroup("com.squareup.okhttp3", "4.9.0") {

        object LoggingInterceptor :
            Dependency(OkHttp3.group, "logging-interceptor", OkHttp3.version)

    }

}