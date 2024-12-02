import com.github.jengelman.gradle.plugins.shadow.transformers.Log4j2PluginsCacheFileTransformer
import net.ltgt.gradle.errorprone.CheckSeverity
import net.ltgt.gradle.errorprone.errorprone
import java.net.URI

plugins {
    id("java")
    id("net.ltgt.errorprone") version "4.1.0"
    id("com.diffplug.spotless") version "7.0.0.BETA4"
    id("com.gradleup.shadow") version "8.3.5"
    `maven-publish`
}

group = "org.builtonaws.secretsanta"
version = "1.0-SNAPSHOT+01"

repositories {
    mavenCentral()
}

dependencies {
    // null checking
    errorprone("com.uber.nullaway:nullaway:0.12.1")
    errorprone("com.google.errorprone:error_prone_core:2.36.0")
    implementation("org.jspecify:jspecify:1.0.0")
    // json
    implementation(platform("com.fasterxml.jackson:jackson-bom:2.18.2"))
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    // lambda
    implementation("com.amazonaws:aws-lambda-java-core:1.2.3")
    implementation("com.amazonaws:aws-lambda-java-events:3.14.0")
    // aws sdks
    implementation(platform("software.amazon.awssdk:bom:2.29.23"))
    implementation("software.amazon.awssdk:dynamodb-enhanced")
    // logging
    implementation(platform("org.apache.logging.log4j:log4j-bom:3.0.0-beta3"))
    implementation("org.apache.logging.log4j:log4j-layout-template-json")
    implementation("org.apache.logging.log4j:log4j-core")
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:3.0.0-beta2")
    implementation("org.slf4j:slf4j-api:2.1.0-alpha1")
    implementation("com.amazonaws:aws-lambda-java-log4j2:1.6.0")
    // dagger
    val daggerVersion = "2.52"
    implementation("com.google.dagger:dagger:$daggerVersion")
    annotationProcessor("com.google.dagger:dagger-compiler:$daggerVersion")
    // testing
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
    withJavadocJar()
    withSourcesJar()
}
tasks.withType<JavaCompile> {
    options.errorprone {
        check("NullAway", CheckSeverity.ERROR)
        option("NullAway:AnnotatedPackages", "org.builtonaws.secretsanta")
        option("NullAway:ExcludedClassAnnotations", "jakarta.annotation.Generated,javax.annotation.processing.Generated")
        disableWarningsInGeneratedCode = true
    }
}
tasks.compileTestJava {
    options.errorprone {
        disable("NullAway")
    }
}

tasks.test {
    useJUnitPlatform()
}

spotless {
    // optional: limit format enforcement to just the files changed by this feature branch
    // ratchetFrom("origin/main")

    format("misc") {
        target(".gitattributes", ".gitignore")
        trimTrailingWhitespace()
        indentWithTabs()
        endWithNewline()
    }

    java {
        palantirJavaFormat()
    }

    kotlinGradle {
        ktlint()
    }
}

tasks.jar {
    enabled = false
}
tasks.shadowJar {
    transform(Log4j2PluginsCacheFileTransformer::class.java)
}
tasks.build {
    dependsOn(tasks.spotlessApply)
    dependsOn(tasks.shadowJar)
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = URI.create("https://maven.pkg.github.com/secret-santa-app/secret-santa-api")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }

    publications {
        create<MavenPublication>("github") {
            from(components["shadow"])

            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()

            pom {
                name = "SecretSanta API"
                description = "SecretSanta Lambda HTTP API"
                inceptionYear = "2024"
                packaging = "jar"
                url = "https://github.com/secret-santa-app/secret-santa-api"

                licenses {
                    license {
                        name = "The Apache License, Version 2.0"
                        url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
                    }
                }

                developers {
                    developer {
                        name = "João N. Matos"
                        email = "me@joaonmatos.com"
                        timezone = "Europe/Berlin"
                        url = "https://www.joaonmatos.com/"
                    }
                }

                scm {
                    connection = "scm:git:https://github.com/secret-santa-app/secret-santa-api.git"
                    developerConnection = "scm:git:ssh://git@github.com:secret-santa-app/secret-santa-api.git"
                    url = "https://github.com/secret-santa-app/secret-santa-api"
                }
            }
        }
    }
}
