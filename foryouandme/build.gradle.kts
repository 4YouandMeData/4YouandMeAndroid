import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    kotlin("android.extensions")
    id("androidx.navigation.safeargs")
    id("maven-publish")
    id("com.jfrog.bintray")
    id("dagger.hilt.android.plugin")
}

androidExtensions { isExperimental = true }

android {

    compileSdkVersion(AndroidConfig.compile_sdk)

    defaultConfig {
        minSdkVersion(AndroidConfig.min_sdk)
        targetSdkVersion(AndroidConfig.target_sdk)
        versionCode = AndroidConfig.version_code
        versionName = AndroidConfig.version_name
        testInstrumentationRunner = AndroidConfig.test_instrumentation_runner
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
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    buildFeatures {
        viewBinding = true
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

    /* --- arrow --- */

    implementation(Arrow.Fx.get())
    implementation(Arrow.FxCoroutines.get())
    implementation(Arrow.Optics.get())
    implementation(Arrow.Syntax.get())
    kapt(Arrow.Meta.get())

    /* --- coroutines --- */

    implementation(KotlinX.Coroutines.Core.get())
    implementation(KotlinX.Coroutines.Android.get())

    /* --- navigation --- */

    implementation(AndroidX.Navigation.FragmentKtx.get())
    implementation(AndroidX.Navigation.UIKtx.get())

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
    kapt(AndroidX.Hilt.Compiler.get())
    kapt(Google.Dagger.Hilt.Compiler.get())

    /* --- room --- */
    api(AndroidX.Room.Runtime.get())
    api(AndroidX.Room.Ktx.get())
    kapt(AndroidX.Room.Compiler.get())

    /* --- test --- */

    testImplementation(Kotlin.Test.JUnit.get())
    testImplementation(IOMockK.MockK.get())
    testImplementation(Google.Test.Truth.get())
    testImplementation(AndroidX.ArchCore.Testing.get())
    testImplementation(KotlinX.Coroutines.Core.get())
    androidTestImplementation(AndroidX.Test.Ext.JunitKtx.get())
    androidTestImplementation(AndroidX.Test.Espresso.Core.get())

}

/* ======== BINTRAY ======== */
tasks {

    val sourcesJar by creating(Jar::class) {
        archiveClassifier.set("sources")
        from(android.sourceSets.getByName("main").java.srcDirs)
    }

    artifacts {
        archives(sourcesJar)
    }

}

val artifactName: String = project.name
val artifactGroup: String = Library.group
val artifactVersion: String = AndroidConfig.version_name

publishing {
    publications {
        create<MavenPublication>("4youandme") {

            groupId = artifactGroup
            artifactId = artifactName
            version = artifactVersion

            artifact("$buildDir/outputs/aar/${artifactId}-release.aar")
            artifact(tasks.getByName("sourcesJar"))

            pom {
                packaging = "aar"
                name.set(Library.name)
                description.set(Library.pomDescription)
                url.set(Library.pomUrl)
                licenses {
                    license {
                        name.set(Library.pomLicenseName)
                        url.set(Library.pomLicenseUrl)
                        distribution.set(Library.repo)
                    }
                }
                developers {
                    developer {
                        id.set(Library.pomDeveloperId)
                        name.set(Library.pomDeveloperName)
                        email.set(Library.pomDeveloperEmail)
                    }
                }
                scm {
                    url.set(Library.pomScmUrl)
                }
                withXml {

                    // Add dependencies to pom file
                    val dependenciesNode = asNode().appendNode("dependencies")
                    (configurations.releaseImplementation.get().allDependencies +
                            configurations.releaseCompile.get().allDependencies)
                        .forEach {
                            val groupId =
                                if (it.group == rootProject.name) Library.group else it.group
                            val artifactId = it.name
                            val version =
                                if (it.group == rootProject.name) AndroidConfig.version_name
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
}

bintray {

    user = gradleLocalProperties(rootDir).getProperty("bintray.user").toString()
    key = gradleLocalProperties(rootDir).getProperty("bintray.apikey").toString()

    publish = true

    setPublications("4youandme")

    pkg.apply {
        repo = Library.repo
        name = artifactName
        userOrg = Library.organization
        githubRepo = Library.githubRepo
        vcsUrl = Library.pomScmUrl
        description = Library.pomDescription
        setLabels("4youandme")
        setLicenses(Library.pomLicenseName)
        desc = Library.pomDescription
        websiteUrl = Library.pomUrl
        issueTrackerUrl = Library.pomIssueUrl
        githubReleaseNotesFile = Library.githubReadme
        version.apply {
            name = artifactVersion
            desc = Library.pomDescription
            vcsTag = artifactVersion
            gpg.sign = true
            gpg.passphrase = gradleLocalProperties(rootDir).getProperty("bintray.gpg.password")
        }
    }
}
