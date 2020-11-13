import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    kotlin("android.extensions")
    id("androidx.navigation.safeargs")
    id("maven-publish")
    id("com.github.dcendents.android-maven")
    id("com.jfrog.bintray")
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
}

dependencies {

    /* --- kotlin --- */

    implementation(Kotlin.stdlib.jdk7)

    /* --- appcompact --- */

    implementation(AndroidX.appCompat)

    /* --- fragment --- */

    implementation(AndroidX.fragmentKtx)

    /* --- core ktx --- */

    implementation(AndroidX.core.ktx)

    /* --- layout --- */

    implementation(AndroidX.constraintLayout)
    implementation(AndroidX.viewPager2)
    implementation(AndroidX.swipeRefreshLayout)

    /* --- android arch --- */

    implementation(AndroidX.lifecycle.service)
    implementation(AndroidX.lifecycle.viewModelKtx)
    implementation(AndroidX.lifecycle.liveDataKtx)
    implementation(AndroidX.lifecycle.viewModelSavedState)

    /* --- arrow --- */

    implementation(Arrow.fx)
    implementation(Arrow.fxCoroutines)
    implementation(Arrow.optics)
    implementation(Arrow.syntax)
    kapt(Arrow.meta)

    /* --- coroutines --- */

    implementation(KotlinX.coroutines.core)
    implementation(KotlinX.coroutines.android)

    /* --- navigation --- */

    implementation(AndroidX.navigation.fragmentKtx)
    implementation(AndroidX.navigation.uiKtx)

    /* --- encrypted prefs --- */

    implementation(AndroidX.security.cryptoKtx)

    /* --- moshi --- */

    implementation(Moshi.kotlin)
    implementation(Moshi.adapters)

    /* --- json api --- */

    implementation(Moshi.JsonApi.jsonApi)
    implementation(Moshi.JsonApi.retrofitConverter)

    /* --- timber --- */

    implementation(Timber.timber)

    /* --- retrofit --- */

    implementation(Square.retrofit2.retrofit)
    implementation(Square.retrofit2.converter.moshi)
    implementation(Square.okHttp3.loggingInterceptor)

    /* --- country code picker --- */

    implementation(CPP.cpp)

    /* --- span droid --- */

    implementation(Droid.span)

    /* --- recycler view --- */

    implementation(Droid.recycler)

    /* --- signature --- */

    implementation(SignaturePad.signaturePad)

    /* --- threeten --- */

    implementation(ThreeTenAbp.threetenabp)

    /* --- camera x --- */

    api(AndroidX.camera.core)
    api(AndroidX.camera.camera2)
    api(AndroidX.camera.view)

    /* --- mp4 parser --- */

    implementation(IsoParser.isoParser)

    /* --- toasty --- */

    implementation(Toasty.toasty)

    /* --- dexter --- */

    implementation(Dexter.dexter)

    /* --- firebase --- */

    implementation(Firebase.crashlyticsKtx)
    implementation(Firebase.analyticsKtx)
    implementation(Firebase.messagingKtx)

    /* --- lottie ---*/

    implementation(Lottie.lottie)

    /* --- chart ---*/

    implementation(MpAndroidChart.mpandroidchart)

    /* --- exo player --- */

    implementation(ExoPlayer.core)
    implementation(ExoPlayer.ui)

    /* --- test --- */

    testImplementation(Kotlin.Test.junit)
    testImplementation(Testing.mockK)
    testImplementation(Test.truth)
    testImplementation(AndroidX.archCore.testing)
    testImplementation(KotlinX.coroutines.core)
    androidTestImplementation(AndroidX.Test.Ext.junitKtx)
    androidTestImplementation(AndroidX.Test.espresso.core)

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
