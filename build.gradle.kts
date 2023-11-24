plugins {
    id 'java'
    id 'org.jetbrains.kotlin.jvm' version '1.9.0'
    id "io.github.patrick.remapper" version "1.4.0"
    id 'com.github.johnrengelman.shadow' version '8.1.1'
    id "io.papermc.paperweight.userdev" version "1.5.5"
    id "xyz.jpenilla.run-paper" version "2.1.0" // Adds runServer and runMojangMappedServer tasks for testing
}

group = 'gg.flyte'
version = '1.0.0'

repositories {
    maven("https://jitpack.io")
    maven("https://repo.flyte.gg/releases")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://repo.codemc.io/repository/maven-snapshots/")
}

dependencies {
    paperweight.paperDevBundle("1.20.2-R0.1-SNAPSHOT")

    implementation(libs.twilight)
    implementation(libs.paperlib)

    implementation(libs.lamp.common)
    implementation(libs.lamp.bukkit)

    implementation 'net.wesjd:anvilgui:1.7.0-SNAPSHOT'
    implementation "org.mongodb:mongodb-driver-sync:4.9.0"
    implementation "gg.flyte:twilight:1.0.30"
    implementation "com.github.SuperGlueLib:LampWrapper:709c323c17"
    compileOnly "com.github.koca2000:NoteBlockAPI:1.6.2"
}

build.dependsOn(shadowJar)
remap.mustRunAfter(build)

tasks {
    remap {
        version.set("1.20.1")
        mustRunAfter build
    }
}

tasks {
    // Configure reobfJar to run when invoking the build task
    assemble.dependsOn(reobfJar)

    shadowJar {
        def pack = "com.learnspigot.event.shaded."
        relocate("gg.flyte.twilight", "${pack}twilight")
        relocate("com.github.supergluelib.lamp", "${pack}lamp.wrapper")
        relocate("revxrsal.commands", "${pack}lamp")
        relocate("com.google.gson", "${pack}gson")
        relocate("kotlin", "${pack}kotlin")
        relocate("com.mongodb", "${pack}mongodb")
        relocate("io.github.cdimascio.dotenv", "${pack}dotenv")
        relocate("net.wesjd.anvilgui", "${pack}anvilgui")
        relocate("org.bson", "${pack}bson")
        relocate("org.bson", "${pack}bson")
        relocate("org.intellij.lang.annotations", "${pack}jetbrains.annotations")
        relocate("org.jetbrains.annotations", "${pack}jetbrains.annotations")
    }
}

def targetJavaVersion = 17
java {
    def javaVersion = JavaVersion.toVersion(targetJavaVersion)
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
    if (JavaVersion.current() < javaVersion) {
        toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
    }
}

tasks.withType(JavaCompile).configureEach {
    if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible()) {
        options.release = targetJavaVersion
    }
}
