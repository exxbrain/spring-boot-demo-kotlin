configurations {
    "developmentOnly"
    "runtimeClasspath" {
        extendsFrom(configurations["developmentOnly"])
    }
}

dependencies {
    "developmentOnly"("org.springframework.boot:spring-boot-devtools")
}

val profiles = "dev"

tasks.getByName<ProcessResources>("processResources") {
    inputs.property("version", project.version)
    inputs.property("springProfiles", profiles)
    filesMatching("**/application.properties") {
        filter { it.replace("#spring.profiles.active#", profiles) }
        filter { it.replace("#app.version#", project.version as String) }
    }
}

tasks.named("bootJar") {
    dependsOn(tasks["processResources"])
}