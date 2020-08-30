import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

plugins {
    id("org.springframework.boot") version "2.3.3.RELEASE"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
    id("org.asciidoctor.convert") version "1.5.8"
    id("org.liquibase.gradle") version "2.0.4"
    id("com.google.cloud.tools.jib") version "2.5.0"
    kotlin("jvm") version "1.3.72"
    kotlin("plugin.spring") version "1.3.72"
    kotlin("plugin.jpa") version "1.3.72"
}

group = "com.exxbrain"
version = "0.1.9"

configurations {
    developmentOnly
    "runtimeClasspath" {
        extendsFrom(configurations["developmentOnly"])
    }
    compileOnly {
        extendsFrom(configurations["annotationProcessor"])
    }
}

defaultTasks("bootRun")

repositories {
    mavenCentral()
}

if (project.hasProperty("prod")) {
    apply(from = "gradle/profile_prod.gradle.kts")
} else {
    apply(from = "gradle/profile_dev.gradle.kts")
}

val liquibaseRunList get() = if (!project.hasProperty("runList")) "main" else project.properties["runList"]
val diffChangelogFile get() = "src/main/resources/db/changelog/db.changelog-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ".yaml"

liquibase {
    activities.register("main") {
        this.arguments = mapOf(
                "driver" to "org.postgresql.Driver",
                "url" to "jdbc:postgresql://localhost:5432/arch",
                "username" to "postgres",
                "password" to "12345",
                "changeLogFile" to "src/main/resources/db/changelog/db.changelog-master.yaml",
                "defaultSchemaName" to "",
                "logLevel" to "debug",
                "classpath" to "src/main/resources/"
        )
    }
    activities.register("diffLog") {
        this.arguments = mapOf(
                "driver" to "org.postgresql.Driver",
                "url" to "jdbc:postgresql://localhost:5432/arch",
                "username" to "postgres",
                "password" to "12345",
                "changeLogFile" to diffChangelogFile,
                "referenceUrl" to "hibernate:spring:com.exxbrain.arch.user.entity?dialect=org.hibernate.dialect.PostgreSQLDialect&hibernate.physical_naming_strategy=org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy&hibernate.implicit_naming_strategy=org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy",
                "defaultSchemaName" to "",
                "logLevel" to "debug",
                "classpath" to "$buildDir/classes/kotlin/main"
        )
    }
    runList = liquibaseRunList
}

val snippetsDir get() = file("build/generated-snippets")
val springCloudVersion by extra ("Hoxton.SR7")

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-rest")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.liquibase:liquibase-core")
    runtimeOnly("org.postgresql:postgresql")
    liquibaseRuntime ("org.liquibase:liquibase-core")
    liquibaseRuntime ("org.liquibase.ext:liquibase-hibernate5:3.6")
    liquibaseRuntime (sourceSets.main.get().compileClasspath)
    liquibaseRuntime ("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("org.springframework.cloud:spring-cloud-starter-contract-verifier")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.test {
    outputs.dir(snippetsDir)
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.asciidoctor {
    inputs.dir(snippetsDir)
    dependsOn(tasks.test)
}

tasks.compileJava {
    dependsOn(tasks.processResources)
}

jib {
    to {
        image = "exxbrain/architecture"
    }
}

tasks.build {
    dependsOn(tasks.jib)
}
