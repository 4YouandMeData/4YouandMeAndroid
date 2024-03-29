# MIGRATION

## 0.2.11

### 1) Configuration update

Removed keys:

STUDY\_INFO\_CONTACT\_INFO

STUDY\_INFO\_REWARDS

STUDY\_INFO\_FAQ


## 0.2.10

### 1) Recommended dependencies versions

Android Studio : ArticFox 2020.3.1 RC 1

com.android.tools.build:gradle: : **7.0.0**

com.google.dagger:hilt-android-gradle-plugin : **2.38.1**

com.google.dagger:hilt-android : **2.38.1**

com.google.dagger:hilt-android-gradle-compiler : **2.38.1**

com.google.firebase:firebase-crashlytics-ktx : **18.2.0**

**gradle-wrapper**:
	
1. aprire il file gradle-wrapper.properties
2.  modificare il distributionUrl con la versione **7.1.1**
3. `distributionUrl=https\://services.gradle.org/distributions/gradle-7.1.1-all.zip`

## 0.2.8

### 1) Recommended dependencies versions

Android Studio : ArticFox-RC1

com.android.tools.build:gradle: : **7.0.0-rc01**

### 2) Refactor

1) **Environment** was renamed in **StudySettings**

it is suggested to rename the name of the class that implements environment and the method in the module

**Before**

```
class SampleEnvironment() : Environment()
```

```
@Provides
@Singleton
fun provideEnvironment(): Environment =
        SampleEnvironment()
```

**After**

```
class SampleStudySettings() : StudySettings()
```
```
@Provides
@Singleton
fun provideStudySettings(): StudySettings =
        SampleStudySettings()
```

2) All functions of this class have been transformed to val

**Before**

```
override fun isDebuggable(): Boolean = true
```

**After**

```
override val isDebuggable: Boolean = true
```

3) Added **isLocationPermissionEnabled** in the Environment

## 0.2.4

### 1) Recommended dependencies versions

Android Studio : **ArticFox-Beta5**

androidx.core:core-ktx : **1.6.0**

## 0.2.2

### 1) Recommended dependencies versions

Android Studio : **ArticFox-Beta4**

com.android.tools.build:gradle: : **7.1.0-alpha02**

com.google.dagger:hilt-android-gradle-plugin : **2.37**

com.google.dagger:hilt-android : **2.37**

com.google.dagger:hilt-android-gradle-compiler : **2.37**

org.jetbrains.kotlin:kotlin-gradle-plugin : **1.5.10**

org.jetbrains.kotlin:kotlin-stdlib : **1.5.10**

com.google.gms:google-services : **4.3.8**

com.google.firebase:firebase-crashlytics-gradle : **2.7.1**

com.google.firebase:firebase-crashlytics-ktx : **18.1.0**

com.google.firebase:firebase-analytics-ktx : **19.0.0**

com.google.firebase:firebase-messaging-ktx : **22.0.0**

**gradle-wrapper**:
	
1. aprire il file gradle-wrapper.properties
2.  modificare il distributionUrl con la versione **7.1**
3. `distributionUrl=https\://services.gradle.org/distributions/gradle-7.1-all.zip`