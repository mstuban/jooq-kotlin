import dev.monosoul.jooq.migration.MigrationLocation
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.0.0"
	id("io.spring.dependency-management") version "1.1.4"
	id("dev.monosoul.jooq-docker") version "6.0.14"
	id("org.flywaydb.flyway") version "9.16.3"
	kotlin("jvm") version "1.9.22"
	kotlin("plugin.spring") version "1.9.22"
	kotlin("plugin.jpa") version "1.9.22"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/milestone") }
	maven { url = uri("https://repo.spring.io/snapshot") }
	maven { url = uri("https://plugins.gradle.org/m2/") }
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa") {
		//exclude(group = "jakarta.persistence", module = "jakarta.persistence.api")
	}
	implementation("org.springframework.boot:spring-boot-starter-jooq")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.postgresql:postgresql:42.7.2")
	implementation("org.jooq:jooq-kotlin")
	implementation("org.jooq:jooq")
	implementation("org.flywaydb:flyway-core")
	implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
//	implementation("org.hibernate:hibernate-core:6.4.4.Final")
//	implementation("org.hibernate:hibernate-entitymanager:5.6.15.Final")

	jooqCodegen(platform("org.springframework.boot:spring-boot-dependencies:3.2.2"))
	jooqCodegen("org.postgresql:postgresql")
	jooqCodegen("ch.qos.logback:logback-classic")
	jooqCodegen("org.jooq:jooq-codegen")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

fun Array<String>.filterAndTrimPrefix(prefix: String) =
	this.filter { it.startsWith(prefix) }
		.map { it.substring(prefix.length) }

tasks {
	generateJooqClasses {
		schemas.set(listOf("public"))
		basePackageName.set("com.example.jooq")
		migrationLocations.setFromFilesystem(
			project.files("$projectDir/src/main/resources/db/migration"),
		)
		migrationLocations.set(
			listOf(
				MigrationLocation.Filesystem(
					project.files("$projectDir/src/main/resources/db/migration"),
				),
			),
		)
		usingJavaConfig {
			database.isIncludeExcludeColumns = true
			database.withExcludes(
				listOf(
					"(pg_catalog|information_schema)\\..*",
					"flyway_schema_history"
				).joinToString("|"),
			)
		}
	}
}
