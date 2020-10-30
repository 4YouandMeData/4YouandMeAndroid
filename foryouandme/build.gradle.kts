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

    implementation(Libs.kotlin_stdlib_jdk7)

    /* --- appcompact --- */

    implementation(Libs.appcompat)

    /* --- fragment --- */

    implementation(Libs.fragment_ktx)

    /* --- core ktx --- */

    implementation(Libs.core_ktx)

    /* --- layout --- */

    implementation(Libs.constraintlayout)
    implementation(Libs.viewpager2)

    /* --- android arch --- */

    implementation(Libs.lifecycle_viewmodel_ktx)
    implementation(Libs.lifecycle_livedata_ktx)
    implementation(Libs.android_arch_lifecycle_extensions)
    implementation(Libs.lifecycle_viewmodel_savedstate)

    /* --- arrow --- */

    implementation(Libs.arrow_fx)
    implementation(Libs.arrow_fx_coroutines)
    implementation(Libs.arrow_optics)
    implementation(Libs.arrow_syntax)
    kapt(Libs.arrow_meta)

    /* coroutines */

    implementation(Libs.kotlinx_coroutines_core)
    implementation(Libs.kotlinx_coroutines_android)

    /* --- navigation --- */

    implementation(Libs.navigation_fragment_ktx)
    implementation(Libs.navigation_ui_ktx)

    /* --- encrypted prefs --- */

    implementation(Libs.security_crypto)

    /* --- moshi --- */

    implementation(Libs.moshi_kotlin)
    implementation(Libs.moshi_adapters)

    /* --- json api --- */

    implementation(Libs.moshi_jsonapi)
    implementation(Libs.moshi_jsonapi_retrofit_converter)

    /* --- timber --- */

    implementation(Libs.timber)

    /* --- retrofit --- */

    implementation(Libs.retrofit)
    implementation(Libs.converter_moshi)
    implementation(Libs.logging_interceptor)

    /* --- country code picker --- */

    implementation(Libs.ccp)

    /* --- span droid --- */

    implementation(Libs.span_droid)

    /* --- recycler view --- */

    implementation(Libs.recycler_droid_core)

    /* --- signature --- */

    implementation(Libs.signature_pad)

    /* --- permissions --- */

    implementation(Libs.dexter)

    /* --- threeten --- */

    implementation(Libs.threetenabp)

    /* --- camera x --- */

    api(Libs.camera_core)
    api(Libs.camera_camera2)
    api(Libs.camera_view)

    /* --- mp4 parser --- */

    implementation(Libs.isoparser)

    /* --- toasty --- */

    implementation(Libs.toasty)

    /* --- dexter --- */

    implementation(Libs.dexter)

    /* --- firebase --- */

    implementation(Libs.firebase_analytics_ktx)
    implementation(Libs.firebase_crashlytics_ktx)
    implementation(Libs.firebase_messaging_ktx)

    /* --- lottie ---*/

    implementation(Libs.lottie)

    /* --- chart ---*/

    implementation(Libs.mpandroidchart)

    /* --- test --- */

    testImplementation(Libs.junit_junit)
    androidTestImplementation(Libs.androidx_test_ext_junit)
    androidTestImplementation(Libs.espresso_core)

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