import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependencySpec

/**
 * Generated by https://github.com/jmfayard/buildSrcVersions
 *
 * Find which updates are available by running
 *     `$ ./gradlew buildSrcVersions`
 * This will only update the comments.
 *
 * YOU are responsible for updating manually the dependency version.
 */
object Versions {
    const val org_jetbrains_kotlinx_kotlinx_coroutines: String = "1.3.9"

    const val com_squareup_retrofit2: String = "2.9.0"

    const val org_jetbrains_kotlin: String = "1.4.0" // available: "1.4.10"

    const val androidx_navigation: String = "2.2.2" // available: "2.3.0"

    const val androidx_lifecycle: String = "2.2.0"

    const val moe_banana: String = "3.5.0"

    const val android_arch_lifecycle_extensions: String = "1.1.1"

    const val com_android_tools_build_gradle: String = "4.0.1"

    const val androidx_test_ext_junit: String = "1.1.2"

    const val junit_junit: String = "4.13"

    const val de_fayard_buildsrcversions_gradle_plugin: String = "0.7.0"

    const val arrow_integration_retrofit_adapter: String = "0.10.4"

    const val arrow_fx_coroutines: String = "0.11.0"

    const val logging_interceptor: String = "4.8.1" // available: "4.9.0"

    const val recycler_droid_core: String = "1.8.0-beta06"

    const val firebase_analytics: String = "17.5.0"

    const val constraintlayout: String = "2.0.1"

    const val google_services: String = "4.3.3"

    const val security_crypto: String = "1.0.0-rc01"

    const val camera_camera2: String = "1.0.0-beta08"

    const val moshi_adapters: String = "1.8.0" // available: "1.10.0"

    const val mpandroidchart: String = "v3.1.0" // available: "3.1.0"

    const val espresso_core: String = "3.3.0"

    const val signature_pad: String = "1.3.1"

    const val arrow_optics: String = "0.11.0"

    const val arrow_syntax: String = "0.11.0"

    const val fragment_ktx: String = "1.3.0-alpha06"

    const val moshi_kotlin: String = "1.10.0"

    const val camera_core: String = "1.0.0-beta08"

    const val camera_view: String = "1.0.0-alpha15"

    const val lint_gradle: String = "27.0.1"

    const val threetenabp: String = "1.2.4"

    const val arrow_meta: String = "0.11.0"

    const val span_droid: String = "0.1"

    const val viewpager2: String = "1.0.0"

    const val appcompat: String = "1.2.0"

    const val isoparser: String = "1.1.9" // available: "1.1.22"

    const val arrow_fx: String = "0.11.0"

    const val core_ktx: String = "1.3.1"

    const val dexter: String = "6.2.1"

    const val lottie: String = "3.4.2"

    const val timber: String = "4.7.1"

    const val toasty: String = "1.5.0"

    const val aapt2: String = "4.0.1-6197926"

    const val ccp: String = "2.4.1"

    /**
     * Current version: "6.5"
     * See issue 19: How to update Gradle itself?
     * https://github.com/jmfayard/buildSrcVersions/issues/19
     */
    const val gradleLatestVersion: String = "6.6.1"
}

/**
 * See issue #47: how to update buildSrcVersions itself
 * https://github.com/jmfayard/buildSrcVersions/issues/47
 */
val PluginDependenciesSpec.buildSrcVersions: PluginDependencySpec
    inline get() =
            id("de.fayard.buildSrcVersions").version(Versions.de_fayard_buildsrcversions_gradle_plugin)
