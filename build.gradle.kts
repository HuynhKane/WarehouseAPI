plugins {
	java
	id("org.springframework.boot") version "3.3.5"
	id("io.spring.dependency-management") version "1.1.6"
}

group = "com.WarehouseAPI"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}


dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	implementation("org.springframework.boot:spring-boot-starter-web")
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation ("org.springframework.boot:spring-boot-starter-security")
	implementation ("io.jsonwebtoken:jjwt-api:0.11.5")
	implementation ("io.jsonwebtoken:jjwt-impl:0.11.5")
	implementation ("io.jsonwebtoken:jjwt-jackson:0.11.5")
	// Spring Boot WebSocket starter
	implementation ("org.springframework.boot:spring-boot-starter-websocket")

	// https://mvnrepository.com/artifact/org.springframework/spring-messaging
	implementation("org.springframework:spring-messaging:6.1.12")

	// Spring Boot Test for testing
	testImplementation ("org.springframework.boot:spring-boot-starter-test")
	// https://mvnrepository.com/artifact/dev.langchain4j/langchain4j-open-ai
	implementation("dev.langchain4j:langchain4j-open-ai:1.0.0-beta2")
 	// https://mvnrepository.com/artifact/dev.langchain4j/langchain4j-embeddings
	implementation("dev.langchain4j:langchain4j-embeddings:0.36.2")
	// https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp
	implementation("com.squareup.okhttp3:okhttp:4.9.3")
	// https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind
	implementation("com.fasterxml.jackson.core:jackson-databind:2.15.0")
	// Bill of Materials (BOM) to manage Java library versions
	implementation ("dev.langchain4j:langchain4j-bom:0.36.2")
	// https://mvnrepository.com/artifact/dev.langchain4j/langchain4j-hugging-face
	implementation("dev.langchain4j:langchain4j-hugging-face:0.24.0")
	// Java library for Apache PDFBox Document Parser
	implementation("dev.langchain4j:langchain4j-document-parser-apache-pdfbox:0.32.0")
	// https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-starter-webmvc-ui
	// https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-starter-webmvc-ui
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
	// https://mvnrepository.com/artifact/io.swagger.core.v3/swagger-annotations
	implementation("io.swagger.core.v3:swagger-annotations:2.2.15")
	// https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-webflux
	implementation("org.springframework.boot:spring-boot-starter-webflux:3.3.3")

	// https://mvnrepository.com/artifact/dev.langchain4j/langchain4j
	implementation("dev.langchain4j:langchain4j:0.24.0")

	// https://mvnrepository.com/artifact/org.mapstruct/mapstruct
	implementation("org.mapstruct:mapstruct:1.6.3")



}

tasks.withType<Test> {
	useJUnitPlatform()
}
