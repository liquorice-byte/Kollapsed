plugins {
    java
    application
    id("org.jetbrains.kotlin.jvm") version "2.1.20"
    id("org.javamodularity.moduleplugin") version "1.8.15"
    id("org.openjfx.javafxplugin") version "0.1.0"
    id("org.beryx.jlink") version "4.0.2"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.4.0-RC"
    id("com.gradleup.shadow") version "9.4.2"
}

group = "it.liquorice"
version = "1.0.0"

repositories {
    mavenCentral()
}

val junitVersion = "5.12.1"


tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

application {
    mainModule.set("it.liquorice.kollapsed")
    mainClass.set("it.liquorice.kollapsed.Launcher")
}
kotlin {
    jvmToolchain(21)
}

javafx {
    version = "21.0.6"
    modules = listOf("javafx.controls", "javafx.fxml", "javafx.web")
}

dependencies {
    // implementation("org.controlsfx:controlsfx:11.2.1")
    implementation("ch.qos.logback:logback-classic:1.5.32")
    implementation("io.github.mkpaz:atlantafx-base:2.1.0")
    implementation("org.kordamp.ikonli:ikonli-javafx:12.4.0")
    implementation("org.kordamp.ikonli:ikonli-fluentui-pack:12.4.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.11.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-javafx:1.10.2")
    implementation("org.openani.jsystemthemedetector:jSystemThemeDetector:3.9")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

jlink {
    imageZip.set(layout.buildDirectory.file("/distributions/app-${javafx.platform.classifier}.zip"))
    options.set(listOf("--strip-debug", "--compress", "2", "--no-header-files", "--no-man-pages"))
    launcher {
        name = "Kollapsed"
    }
    jpackage {
        installerType = when {
            org.gradle.internal.os.OperatingSystem.current().isMacOsX -> "dmg"
            org.gradle.internal.os.OperatingSystem.current().isWindows -> "exe"
            else -> "app-image"
        }
        appVersion = "$version"
        icon = when {
            org.gradle.internal.os.OperatingSystem.current().isWindows -> "src/main/resources/it/liquorice/kollapsed/img/icon.ico"
            org.gradle.internal.os.OperatingSystem.current().isMacOsX -> "src/main/resources/it/liquorice/kollapsed/img/icon.icns"
            else -> "src/main/resources/it/liquorice/kollapsed/img/icon.png"
        }
        installerOptions = listOf(
            "--mac-package-name", "Kollapsed",
            "--mac-package-identifier", "it.liquorice.kollapsed"
        )
    }
}

tasks.compileKotlin {
    destinationDirectory.set(tasks.compileJava.get().destinationDirectory)
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "it.liquorice.kollapsed.Launcher"
    }
}