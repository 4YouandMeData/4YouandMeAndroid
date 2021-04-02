# 4YouAndMe

## Version Deploy

1.  Increase `version_code` and `version_name` in the [ProjectConfig.kt](https://github.com/4youandme/4YouandmeAndroid/blob/master/buildSrc/src/main/java/ProjectConfig.kt) file

```
object ProjectConfig {

    ...
    
    const val version_code = 39
    const val version_name = "0.1.39"

    ...

}
	
	
```
2.  Run the following command to clean up the project

`./gradlew clean`
		
3.  Run the following command to build the project in **release** mode

`./gradlew assembleRelease`

Run the following command to upload the **release** on the nexus repository

`./gradlew foryouandme:publishReleasePublicationToForYouAndMe`