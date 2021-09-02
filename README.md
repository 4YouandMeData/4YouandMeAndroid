# 4YouAndMe

## Version Deploy

1.  Increase `version_code` and `version_name` in the [ProjectConfig.kt](https://github.com/4youandme/4YouandmeAndroid/blob/master/buildSrc/src/main/java/ProjectConfig.kt) file

```
object ProjectConfig {

    ...
    
    const val version_code = your_version_code_here
    const val version_name = "your_version_name_here"

    ...

}
	
	
```
2.  Run the following command to clean up the project

`./gradlew clean`
		
3.  Run the following command to build the project in **release** mode

`./gradlew assembleRelease`

4.  Run the following command to upload, close and release the **release** on the nexus repository

`./gradlew foryouandme:publishReleasePublicationToSonatypeRepository closeAndReleaseSonatypeStagingRepository`