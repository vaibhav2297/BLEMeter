pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()

        maven("https://oss.sonatype.org/content/repositories/snapshots/")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "BLEMeter"
include(":app")
include(":feature:dashboard")
include(":feature:authentication")
include(":core:network")
include(":core:payment")
include(":core:data:auth")
include(":core:designsystem")
include(":core:navigation")
include(":core:local")
include(":feature:wallet")
include(":feature:wallet")
include(":core:data:wallet")
include(":core:logger")
include(":core:file")
include(":core:data:meter")
