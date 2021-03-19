import java.io.FileInputStream
import java.net.URI
import java.util.*

plugins {
    id("maven-publish")
    id("signing")
}

val Project.android: com.android.build.gradle.LibraryExtension get() =
    (this as ExtensionAware).extensions.getByName("android") as com.android.build.gradle.LibraryExtension

tasks {

    register("androidJavadocJar", Jar::class) {
        archiveClassifier.set("javadoc")
        from("$buildDir/javadoc")
        //dependsOn(dokkaJavadoc)
    }

    register("androidSourcesJar", Jar::class) {
        archiveClassifier.set("sources")
        from(project.android.sourceSets.getByName("main").java.srcDirs)
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

configure<PublishingExtension> {

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

configure<SigningExtension> {

    sign(the<PublishingExtension>().publications)

}