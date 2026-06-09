val clientOnlyModIds = listOf(
    "ModularUI2",
    "Baubles-Expanded",
    "angelica",
    "shouldersurfing",
    "Sound-Physics",
)

tasks.withType<JavaExec>().configureEach {
    if (name.startsWith("runServer")) {
        doFirst("stripClientOnlyMods") {
            classpath = classpath.filter { file ->
                clientOnlyModIds.none { modId -> file.name.contains(modId, ignoreCase = true) }
            }
        }
    }
}
