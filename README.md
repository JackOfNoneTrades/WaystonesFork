# Waystones
Minecraft Mod. Teleport back to activated waystones. For Survival, Adventure or Servers.

### Building on Linux
You must use java 8, for example:
```
VERSION=debug BUILD_NUMBER="" JAVA_HOME=/usr/lib/jvm/java-8-openjdk/ ./gradlew build
```

### Fixes:
* Nether portals aren't generated when teleporting to the Nether (@kuzuanpa)
* Fixed some rendering bugs (@kuzuanpa)

### New features:
* Clicking an activated Waystone will open the teleport menu, instead of needing to shift-click it (much more intuitive)
* The teleport menu shows the Waystone name at the top
* If configured (false by default), Waystones show nametags with their names
* It is now impossible to have two global Waystones with the same name, or two non-global ones with the same name. Global and non global is allowed.
* If a player exits the Waystone creation menu without properly naming it, the creation/naming menu will be shown upon next interaction (instead of creating an empty-named Waystone)
* GUI Config