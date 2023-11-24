plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.shadow)
    alias(libs.plugins.paperweight)
    alias(libs.plugins.run.paper)

//    id "io.github.patrick.remapper" version "1.4.0"
}

group = "gg.flyte"
version = "1.0.0"

repositories {
    maven("https://jitpack.io")
    maven("https://repo.flyte.gg/releases")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://repo.codemc.io/repository/maven-snapshots/")
}

dependencies {
    paperweight.paperDevBundle("1.20.1-R0.1-SNAPSHOT")

    implementation(libs.twilight)
    implementation(libs.paperlib)

    implementation(libs.lamp.common)
    implementation(libs.lamp.bukkit)

    implementation(libs.anvil.gui)
    implementation(libs.mongodb)
    compileOnly(libs.noteblock.api)
}

tasks {
    build { dependsOn(shadowJar) }
    runServer { minecraftVersion("1.20.1") }
    compileKotlin { kotlinOptions.jvmTarget = "17" }

    shadowJar {
        val pack = "gg.flyte.event.shaded."
        relocate("gg.flyte.twilight", "${pack}twilight")
        relocate("revxrsal.commands", "${pack}lamp")
        relocate("com.google.gson", "${pack}gson")
        relocate("kotlin", "${pack}kotlin")
        relocate("com.mongodb", "${pack}mongodb")
        relocate("io.github.cdimascio.dotenv", "${pack}dotenv")
        relocate("net.wesjd.anvilgui", "${pack}anvilgui")
        relocate("org.bson", "${pack}bson")
        relocate("org.intellij.lang.annotations", "${pack}jetbrains.annotations")
        relocate("org.jetbrains.annotations", "${pack}jetbrains.annotations")
        relocate("io.papermc.lib", "${pack}paperlib")
    }
}

//remap.mustRunAfter(build)

//tasks {
//    remap {
//        version.set("1.20.1")
//        mustRunAfter build
//    }
//}


    // Configure reobfJar to run when invoking the build task
//    assemble.dependsOn(reobfJar)




//def targetJavaVersion = 17
//java {
//    def javaVersion = JavaVersion.toVersion(targetJavaVersion)
//    sourceCompatibility = javaVersion
//    targetCompatibility = javaVersion
//    if (JavaVersion.current() < javaVersion) {
//        toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
//    }
//}
//
//tasks.withType(JavaCompile).configureEach {
//    if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible()) {
//        options.release = targetJavaVersion
//    }
//}
