Description
-----------

The `ForYouAndMe` project contains an `SampleApp Project` to easily build and run an Android app that implements the `ForYouAndMe` framework. Follow the instructions under **SampleApp Project** paragraph to build and run it.

The `ForYouAndMe` framework is also available as an Android library in order to use it in a new project created from scratch. Follow the instructions under **Create a study app from scratch** paragraph to build and run it.

SampleApp Project
-----------------

To run the example project:

1.  clone the repo,
    
2.  download your Firebase project `google-services.json` from Firebase Console(follow instructions at [https://firebase.google.com/docs/cloud-messaging/android/client](https://firebase.google.com/docs/cloud-messaging/android/client) ) then move it under the `app/src` folder.
    
3.  Navigate - using _Finder_ on _Mac_, or _Windows Explorer_ on _Windows_ - to `/app/src/main/res/values` folder and rename the `secrets_sample.xml` to `secrets.xml`.
    
4.  in Android Studio open `secrets.xml` file under `app/res/values` and fill the property values with the following:
    
    1.  `STUDY_ID` identifier of your study as recorded on your server as the `alias` of your study.
        
    2.  `BASE_URL` base url of the server that provides your remote APIs(ex.: [https://api.example.com](https://api.example.com)).
        
    3.  `OAUTH_BASE_URL` base url of the server that handles your Oauth authentication against the supported integrations(ex.: [https://oauth.example.com](https://oauth.example.com)).
        
    4.  `PIN_CODE_SUFFIX` (only needed in studies that use the pin login) pin code suffix needed for your study, or to `none` if pin code is not supported for your study.

5.  Ensure that your app Application Id matches with the created Firebase app id by checking its value in the `build.gradle.kts(:app)`.

6.  select the connected device or a simulator and run the app.
    

**Create a study app from scratch**
-----------------------------------

The following instructions assume you want to install ForYouAndMe on a brand new project.

1.  Create a new Android Studio project using “No Activity“ template
    
2.  Enter app name and bundle id(**it’s highly recommended to use something like** _**com.foryouandme.<App\_name> as bundle id**_)
    
3.  Select “Use Kotlin script (.kts) for Gradle build files“ option.
    
4.  Click on “Finish“.
    
5.  Open “Android Studio->Preferences“, in the menu on the left select “Build, Execution, Deployment->Build Tools-> Gradle“, and ensure to have _Gradle JDK_ set on 11.0. If not change it and then click on “Apply“ and the on “OK“.
    
6.  Open _build.gradle(Project)_ and change it as follows
    
    ```java
    buildscript {
    
        repositories {
    
            google()
            jcenter()
            mavenCentral()
    
        }
    
        dependencies {
    
            classpath("com.android.tools.build:gradle:<version>")
            classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:<version>")
            classpath("com.google.gms:google-services:<version>")
            classpath("com.google.firebase:firebase-crashlytics-gradle:<version>")
            classpath("com.google.dagger:hilt-android-gradle-plugin:<version>")
    
            // NOTE: Do not place your application dependencies here; they belong
            // in the individual module build.gradle files
        }
    
    }
    
    allprojects {
    
        repositories {
            google()
            jcenter()
            mavenCentral()
            maven("https://jitpack.io")
        }
    
    }
    
    tasks.register("clean", Delete::class) {
        delete(rootProject.buildDir)
    }
    ```
    
7.  Open _build.gradle(_app) and change it as follows:
    
    ```java
    import java.io.FileInputStream
    import java.util.*
    
    plugins {
        id("com.android.application")
        kotlin("android")
        kotlin("kapt")
        kotlin("android.extensions")
        id("com.google.gms.google-services")
        id("com.google.firebase.crashlytics")
        id("dagger.hilt.android.plugin")
    }
    
    android {
    
        compileSdk = 31
        buildToolsVersion = "30.0.3"
    
        defaultConfig {
            applicationId = <App package name>
            minSdk = 23
            targetSdk = 30
            versionCode = 1
            versionName = "1.0"
    
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    
        signingConfigs {
    
            if (rootProject.file("app/signing.properties").exists()) {
                val signingRelease = Properties()
                signingRelease.load(FileInputStream(rootProject.file("app/signing.properties")))
                create("<app name>") {
                    storeFile = rootProject.file("app/keystore.jks")
                    storePassword = signingRelease.getProperty("storePassword")
                    keyAlias = signingRelease.getProperty("keyAlias")
                    keyPassword = signingRelease.getProperty("keyPassword")
                }
            }
    
        }
    
        buildTypes {
            create("production") {
                matchingFallbacks.add("release")
                isMinifyEnabled = false
                signingConfig = signingConfigs.maybeCreate("<app name>")
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
                isDebuggable = false
            }
            create("staging") {
                matchingFallbacks.add("release")
                isMinifyEnabled = false
                signingConfig = signingConfigs.maybeCreate("<app name>")
                applicationIdSuffix = ".staging"
                versionNameSuffix = "-staging"
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
                isDebuggable = false
            }
            create("staging-dev") {
                matchingFallbacks.add("debug")
                signingConfig = signingConfigs.maybeCreate("<app name>")
                applicationIdSuffix = ".staging"
                versionNameSuffix = "-staging"
                isDebuggable = true
            }
        }
    
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }
    
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
    
    dependencies {
    
       /* --- 4youAndMe --- */
    
        implementation("net.4youandme:foryouandme:<version>")
    
        /* --- firebase --- */
    
        implementation("com.google.firebase:firebase-analytics-ktx:<version>")
        implementation("com.google.firebase:firebase-crashlytics-ktx:<version>")
        implementation("com.google.firebase:firebase-messaging-ktx:<version>")
    
        /* --- kotlin --- */
    
        implementation("org.jetbrains.kotlin:kotlin-stdlib:<version>")
    
        /* --- android --- */
    
        implementation("androidx.core:core-ktx:<version>")
        implementation("androidx.appcompat:appcompat:<version>")
        implementation("com.google.android.material:material:<version>")
    
        /* --- dagger --- */
    
        implementation("com.google.dagger:hilt-android:<version>")
        kapt("com.google.dagger:hilt-android-compiler:<version>")
    
        /* --- test --- */
    
        testImplementation("junit:junit:<version>")
        androidTestImplementation("androidx.test.ext:junit:<version>")
        androidTestImplementation("androidx.test.espresso:espresso-core:<version>")
    
    }
    ```
    

*   For each dependency use the last version if a specific version is not explicitly indicated by developer. Ex.: `implementation("com.foryouandme.4youandme:4youandme:<last_version>")`  
    with <_last\_version_\> last available version of the specific library.
    
*   Replace `<App package name>` with package name of the app.
    
*   Replace `<app name>` with name of the app.
    

  

1.  Run Gradle Sync from Android Studio.
    
2.  Open
    

_settings.gradle.kts_ and change it as follows:

```java
rootProject.name = "<app name>"
include(":app", ":<app name>")
```

Replace `<app name>` with name of the app.

  

10\. Create a Firebase project on [https://console.firebase.google.com/](https://console.firebase.google.com/) . Add Firebase SDKs crashlitics and analytics following official guidelines:  
[https://firebase.google.com/docs/analytics/get-started?platform=android](https://firebase.google.com/docs/analytics/get-started?platform=android)  
[https://firebase.google.com/docs/crashlytics/get-started?platform=android](https://firebase.google.com/docs/crashlytics/get-started?platform=android)

11\. Inside the Firebase project, create a staging app and a production app. Note that the production app must have the package name you choose, but the staging app package name must be in this format:  
`<package name>.staging`

12\. Download `google-services.json` files from Firebase. Using finder(on Mac OS) navigate to your project and then in `app` folder, here you must put the production `google-services.json` . Navigate in `app/src` and create two folders named as follows: `staging` and `staging-dev`. Put in both folders the staging `google-services.json`.

13\. On Android Studio, under `app/java` module, create a new Kotlin class named `<App name>StudySettings`and extends it with `StudySettings` (from `com.foryouandme.data.datasource.StudySettings`).  
Implement class members manually or using IDE help. The new class should look as follows:

```java
import android.content.Context
import com.foryouandme.data.datasource.StudySettings

class SampleStudySettings(private val context: Context) : StudySettings() {

    override val getApiBaseUrl: String = <APIs full URL>
    override val getOAuthBaseUrl: String = <OAuth full URL>
    override val isDebuggable: Boolean = true
    override val isStaging: Boolean = true
    override val studyId: String = <ID of the study>
    override val useCustomData: Boolean = false
    override val pinCodeSuffix: String = <suffix used in PIN code login>
    override val isLocationPermissionEnabled: Boolean = true

}
```

*   _getApiBaseUrl_: APIs base URL, for example `https://api.example.exampleapp.com`
    
*   _getOAuthBaseUrl_: OAuth base URL, for example `https://oauth.example.exampleapp.com`
    
*   _isDebuggable_: must be `true`, it’s conditioned by build type.
    
*   _isStaging_: must be `true`, it’s conditioned by build type.
    
*   _studyId_: ID of the study as defined on configuration panel.
    
*   _useCustomData_: if set to `true` it enable the in app custom section(currently used for a specific case on _Bump_).
    
*   _pinCodeSuffix_: it’s optional and it must be set only in case of PIN code login. This suffix is study specific, and it’s part of the email address generated when a user is created in a PIN code login study. For example, if full address is `123123@exampleapp.com`, suffix is `@exampleapp.com`.
    
*   `isLocationPermissionEnabled`: if set to `true` it enable the location tracking for analytics
    

It’s recommended to create a `secrets.xml` to store sensible data as API url, Oauth url and pin code suffix.

14\. Create a file named `secrets.xml` under `app/src/main/res/values/` . The content will look as follow:

```java
<?xml version="1.0" encoding="utf-8"?>
<resources>

    <string name="STUDY_ID" translatable="false">example_study</string>
    <string name="BASE_URL" translatable="false">https://api.example.exampleapp.com</string>
    <string name="OAUTH_BASE_URL" translatable="false">https://auth.example.exampleapp.com</string>

</resources>
```

**This file must contain production values.**  

15\. Copy this file in the following paths:

*   `app/src/staging-dev/res/values/`
    
*   `app/src/staging/res/values/`
    

In this copies, replace production values with staging values.

16\. Now, you can change the content of the class created at point 13, replacing values as follow:  

```java
    override val isDebuggable: Boolean = true

    override val isStaging: Boolean = true

    override val studyId: String =
        context.getString(R.string.STUDY_ID)

    override val apiBaseUrl: String =
        context.getString(R.string.BASE_URL)

    override val oAuthBaseUrl: String =
        context.getString(R.string.OAUTH_BASE_URL)

    override val useCustomData: Boolean = false

    override val pinCodeSuffix: String =
        context.getString(R.string.PIN_CODE_SUFFIX)

    override val isLocationPermissionEnabled: Boolean = true
```

17\. In the App module, under app/java, create a class that extends _ImageConfiguration_(`com.foryouandme.core.arch.deps.ImageConfiguration`) and fill it as follows:

```java
class SampleImageConfiguration : ImageConfiguration {
    /* --- common ---*/
    override fun exampleImage(): Int = R.drawable.example_image
    ...
    /* --- auth --- */
    ...
    /* --- main --- */
    ...
    /* --- task --- */
    ...
    /* --- menu item --- */
    ...
}
```

Put here all the image resources names. Remember to copy images resources files under `res/drawable`.

18\. in app module, create a class that extends _VideoConfiguration_(`import com.foryouandme.core.arch.deps.VideoConfiguration`) and fill it as follows:

```java
class SampleAppVideoConfiguration : VideoConfiguration {

    override fun introVideo(): Int = R.raw.intro_video
    
}
```

Then place the intro video file under `app/src/main/res/raw` folder.

If your study app doesn’t have an intro video, then put the return value to 0, as follows:  
override fun introVideo(): Int = 0

19\. In app module, create a class that extends _ForYouAndMeApp_(`com.foryouandme.core.arch.app.ForYouAndMeApp`). Fill content as follows, replacing `Sample` with your app name.  

```java
@HiltAndroidApp
class SampleApp : ForYouAndMeApp()
```

20\. In app module, create a class named `"<app_name>Module”`. Fill it as the following example:

```java
@Module
@InstallIn(SingletonComponent::class)
object SampleAppModule {

    @Provides
    @Singleton
    fun provideStudySettings(@ApplicationContext context: Context): StudySettings =
        SampleStudySettings(context)

    @Provides
    @Singleton
    fun provideImageConfiguration(): ImageConfiguration =
        SampleImageConfiguration()

    @Provides
    @Singleton
    fun provideVideoConfiguration(): VideoConfiguration =
        SampleVideoConfiguration()

    @Provides
    @Singleton
    fun provideFirebaseAnalytics(@ApplicationContext context: Context): FirebaseAnalytics =
        FirebaseAnalytics.getInstance(context)
        
}
```

21\. Open `AndroidManifest.xml` and write as follows:

```java
<?xml version="1.0" encoding="utf-8"?>

...

    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.WAKE_LOCK" />

    <application
        android:name="<App_main_class_name>"
        android:allowBackup="false"
        android:fullBackupContent="false"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.<App_name>" >

        <meta-data
            android:name="com.google.firebase.messaging.default_notification_icon"
            android:resource="@drawable/logo_study_secondary" />

    </application>

</manifest>
```

  
22\. To build the app you need to have your `keystore.jks` and its credentials. To create a new one use the official guide [https://developer.android.com/studio/publish/app-signing](https://developer.android.com/studio/publish/app-signing) .

Once you got a `keystore.jks` file, copy it under `<project_root>/app/` folder.

23\. Create a file and name it `signing.properties` . Put it under `<project_root>/app/` folder.

Now your app is ready to be build.