# Waystones-X
Minecraft Mod. Teleport back to activated waystones. For Survival, Adventure or Servers.
(Waystones mod fork.)

![logo_small](src/main/resources/assets/waystones/logo_small.png)

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
* Configurable worldgen inside of Villages
* Automatic activation upon naming
* Configurable [Village Names](https://modrinth.com/mod/village-names) mod compat
* Configurable teleport cost. Flat cost, distance cost, flat cross-dim cost.
* Congigurable global cooldown. Cooldown status indicator.

## Downloads
<!--* [CurseForge ![curse](images/icons/curse.png)](https://www.curseforge.com/minecraft/mc-mods/fentlib)
* [Modrinth ![modrinth](images/icons/modrinth.png)](https://modrinth.com/mod/fentlib)-->
* [Git ![git](images/icons/git.png)](https://github.com/JackOfNoneTrades/WaystonesFork/releases)

## Dependencies
* [UniMixins](https://modrinth.com/mod/unimixins) ([![curse](images/icons/curse.png)](https://www.curseforge.com/minecraft/mc-mods/unimixins), [![modrinth](images/icons/modrinth.png)](https://modrinth.com/mod/unimixins/versions), [![git](images/icons/git.png)](https://github.com/LegacyModdingMC/UniMixins/releases)) is a required dependency.


![vn](images/screenshots/village_names.png)
![cp_cost](images/screenshots/xp_cost.png)

## Building

`./gradlew build`.

## Credits
* BlayTheNinth for the original mod
* kuzuanpa for some bugfixes
* [GT:NH buildscript](https://github.com/GTNewHorizons/ExampleMod1.7.10)

## License

`LgplV3 + SNEED`.

## Buy me a coffee

* [ko-fi.com](ko-fi.com/jackisasubtlejoke)
* Monero: `893tQ56jWt7czBsqAGPq8J5BDnYVCg2tvKpvwTcMY1LS79iDabopdxoUzNLEZtRTH4ewAcKLJ4DM4V41fvrJGHgeKArxwmJ`

<br>

![license](images/lgplsneed_small.png)
