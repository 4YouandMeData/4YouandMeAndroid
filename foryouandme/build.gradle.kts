import java.io.FileInputStream
import java.net.URI
import java.util.*

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    kotlin("android.extensions")
    id("androidx.navigation.safeargs")
    id("dagger.hilt.android.plugin")
    id("maven-publish")
    id("signing")
}

android {

    compileSdk = ProjectConfig.compile_sdk

    defaultConfig {
        minSdk = ProjectConfig.min_sdk
        targetSdk = ProjectConfig.target_sdk
        testInstrumentationRunner = ProjectConfig.test_instrumentation_runner
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
        useIR = true
    }

    buildFeatures {
        viewBinding = true
        // Enables Jetpack Compose for this module
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = AndroidX.Compose.version
    }

}

dependencies {

    /* --- kotlin --- */

    implementation(Kotlin.StdLib.get())

    /* --- appcompact --- */

    implementation(AndroidX.AppCompat.get())

    /* --- fragment --- */

    implementation(AndroidX.FragmentKtx.get())

    /* --- core ktx --- */

    implementation(AndroidX.Core.CoreKtx.get())

    /* --- layout --- */

    implementation(AndroidX.ConstraintLayout.get())
    implementation(AndroidX.ViewPager2.get())
    implementation(AndroidX.SwipeRefreshLayout.get())

    /* --- android arch --- */

    api(AndroidX.Lifecycle.Service.get())
    implementation(AndroidX.Lifecycle.ViewModelKtx.get())
    implementation(AndroidX.Lifecycle.LiveDataKtx.get())
    implementation(AndroidX.Lifecycle.ViewModelSavedState.get())
    implementation(AndroidX.Lifecycle.CommonJava8.get())

    /* --- coroutines --- */

    implementation(KotlinX.Coroutines.Core.get())
    implementation(KotlinX.Coroutines.Android.get())

    /* --- navigation --- */

    implementation(AndroidX.Navigation.FragmentKtx.get())
    implementation(AndroidX.Navigation.UIKtx.get())
    implementation(AndroidX.Navigation.Compose.get())

    /* --- encrypted prefs --- */

    implementation(AndroidX.Security.CryptoKtx.get())

    /* --- moshi --- */

    api(Squareup.Moshi.Kotlin.get())
    implementation(Squareup.Moshi.Adapters.get())

    /* --- json api --- */

    implementation(Banana.MoshiJsonApi.get())
    implementation(Banana.MoshiRetrofitConverter.get())

    /* --- timber --- */

    implementation(Jakewharton.Timber.get())

    /* --- retrofit --- */

    implementation(Squareup.Retrofit2.Retrofit.get())
    implementation(Squareup.Retrofit2.Converter.Moshi.get())
    implementation(Squareup.OkHttp3.LoggingInterceptor.get())

    /* --- country code picker --- */

    implementation(Hbb20.Ccp.get())

    /* --- span droid --- */

    implementation(GiacomoParisi.SpanDroid.Span.get())

    /* --- recycler view --- */

    implementation(GiacomoParisi.RecyclerDroid.Core.get())

    /* --- signature --- */

    implementation(GCacace.SignaturePad.get())

    /* --- threeten --- */

    implementation(Jakewharton.ThreeTenAbp.get())

    /* --- camera x --- */

    api(AndroidX.Camera.Core.get())
    api(AndroidX.Camera.Camera2.get())
    api(AndroidX.Camera.Lifecycle.get())
    api(AndroidX.Camera.View.get())

    /* --- mp4 parser --- */

    implementation(Mp4Parser.IsoParser.get())

    /* --- toasty --- */

    implementation(GrenderG.Toasty.get())

    /* --- dexter --- */

    implementation(Karumi.Dexter.get())

    /* --- firebase --- */

    implementation(Google.Firebase.CrashlyticsKtx.get())
    implementation(Google.Firebase.AnalyticsKtx.get())
    implementation(Google.Firebase.MessagingKtx.get())

    /* --- lottie ---*/

    implementation(AirBnb.Lottie.get())

    /* --- chart ---*/

    implementation(PhilJay.MpAndroidChart.get())

    /* --- exo player --- */

    implementation(Google.ExoPlayer.Core.get())
    implementation(Google.ExoPlayer.UI.get())

    /* --- location --- */

    api(Google.PlayServices.Location.get())

    /* --- hilt --- */

    implementation(Google.Dagger.Hilt.Android.get())
    implementation(AndroidX.Hilt.LifecycleViewModel.get())
    implementation(AndroidX.Hilt.NavigationCompose.get())
    kapt(AndroidX.Hilt.Compiler.get())
    kapt(Google.Dagger.Hilt.Compiler.get())

    /* --- room --- */
    api(AndroidX.Room.Runtime.get())
    api(AndroidX.Room.Ktx.get())
    kapt(AndroidX.Room.Compiler.get())

    /* --- video compressor --- */
    
    implementation(AbedElazizShe.get())

    /* --- compose --- */
    implementation(AndroidX.Compose.UI.get())
    implementation(AndroidX.Compose.UI.UITooling.get())
    implementation(AndroidX.Compose.Material.get())
    implementation(AndroidX.Compose.Foundation.get())
    implementation(AndroidX.Compose.Animation.get())
    implementation(AndroidX.Activity.Compose.get())
    implementation(AndroidX.Lifecycle.ViewModelCompose.get())

    /* --- accompanist --- */
    implementation(Google.Accompanist.Coil.get())
    implementation(Google.Accompanist.Insets.get())
    implementation(Google.Accompanist.SystemUIController.get())
    implementation(Google.Accompanist.Pager.get())

    /* --- test --- */

    testImplementation(Kotlin.Test.JUnit.get())
    testImplementation(IOMockK.MockK.get())
    testImplementation(Google.Test.Truth.get())
    testImplementation(AndroidX.ArchCore.Testing.get())
    testImplementation(KotlinX.Coroutines.Core.get())
    androidTestImplementation(AndroidX.Test.Ext.JunitKtx.get())
    androidTestImplementation(AndroidX.Test.Espresso.Core.get())

}

tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class).configureEach {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}


/* --- maven central --- */

tasks {

    register("androidJavadocJar", Jar::class) {
        archiveClassifier.set("javadoc")
        from("$buildDir/javadoc")
        //dependsOn(dokkaJavadoc)
    }

    register("androidSourcesJar", Jar::class) {
        archiveClassifier.set("sources")
        from(project.android.sourceSets.getByName("main").java.name)
    }

}

var ossrhUsername = ""
var ossrhPassword = ""
extra["signing.keyId"] = ""
extra["signing.password"] = ""
extra["signing.secretKeyRingFile"] = ""

if (rootProject.file("local.properties").exists()) {
    val properties = Properties()
    properties.load(FileInputStream(rootProject.file("local.properties")))

    ossrhUsername = properties.getProperty("ossrhUsername")
    ossrhPassword = properties.getProperty("ossrhPassword")
    extra["signing.keyId"] = properties.getProperty("signing.keyId")
    extra["signing.password"] = properties.getProperty("signing.password")
    extra["signing.secretKeyRingFile"] = properties.getProperty("signing.secretKeyRingFile")

}

val artifactName: String = "foryouandme"
val artifactGroup: String = "net.4youandme"
val artifactVersion: String = ProjectConfig.version_name

publishing {

    publications {

        create<MavenPublication>("release") {

            groupId = artifactGroup
            artifactId = artifactName
            version = artifactVersion

            // Two artifacts, the `aar` (or `jar`) and the sources
            if (project.plugins.findPlugin("com.android.library") != null) {
                artifact("$buildDir/outputs/aar/${project.name}-release.aar")
            } else {
                artifact("$buildDir/libs/${project.name}-${version}.jar")
            }
            artifact(tasks.getByName("androidSourcesJar"))

            pom {
                packaging = "aar"
                name.set(artifactName)
                description.set("ForYouAndMe Android SDK")
                url.set("https://github.com/4youandme/4YouandmeAndroid")
                licenses {
                    license {
                        name.set("ForYouAndMe Android SDK")
                        url.set("https://github.com/4youandme/4YouandmeAndroid")
                    }
                }
                developers {
                    developer {
                        id.set("giacomo.balzo")
                        name.set("Giacomo Parisi")
                        email.set("giacomo@balzo.eu")
                    }
                    developer {
                        id.set("iacopo.balzo")
                        name.set("Iacopo Checcacci")
                        email.set("iacopo@balzo.eu")
                    }
                }
                scm {
                    connection.set("scm:git:github.com/4youandme/4YouandmeAndroid.git")
                    developerConnection.set("scm:git:ssh://github.com/4youandme/4YouandmeAndroid.git")
                    url.set("https://github.com/4youandme/4YouandmeAndroid/tree/main")
                }
                withXml {

                    // Add dependencies to pom file
                    val dependenciesNode = asNode().appendNode("dependencies")
                    configurations.getByName("implementation")
                        .allDependencies
                        .forEach {
                            val groupId =
                                if (it.group == rootProject.name) artifactGroup else it.group
                            val artifactId = it.name
                            val version =
                                if (it.group == rootProject.name) ProjectConfig.version_name
                                else it.version
                            if (groupId != null && version != null) {
                                val dependencyNode =
                                    dependenciesNode.appendNode("dependency")
                                dependencyNode.appendNode("groupId", groupId)
                                dependencyNode.appendNode("artifactId", artifactId)
                                dependencyNode.appendNode("version", version)
                            }
                        }

                }
            }
        }
    }

    repositories {

        maven {

            // This is an arbitrary name, you may also use "mavencentral" or
            // any other name that's descriptive for you
            name = "ForYouAndMe"
            url = URI("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = ossrhUsername
                password = ossrhPassword
            }

        }

    }

}

signing {

    sign(publishing.publications)

}