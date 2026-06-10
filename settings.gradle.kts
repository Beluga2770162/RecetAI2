pluginManagement {
    repositories {
        // Permitimos que Google, Maven Central y el Portal de Plugins resuelvan de forma eficiente
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    // Actualizado a la versión estable para evitar advertencias de compatibilidad con Gradle moderno
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

dependencyResolutionManagement {
    // FAIL_ON_PROJECT_REPOS es una excelente práctica de seguridad: obliga a que todas las librerías
    // se declaren de forma centralizada aquí y prohíbe que los submódulos alteren los repositorios.
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    repositories {
        google()       // Repositorio principal para componentes de Android, Compose y Firebase
        mavenCentral() // Repositorio para Coil, JUnit y herramientas de terceros
    }
}

// Nombre oficial del proyecto en el sistema de compilación
rootProject.name = "RecetAI"

// Módulo principal de la aplicación que contiene todo tu código blindado
include(":app")

