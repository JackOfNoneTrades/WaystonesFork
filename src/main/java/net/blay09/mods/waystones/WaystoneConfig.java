package net.blay09.mods.waystones;

import java.util.Collection;

import net.blay09.mods.waystones.util.BlockPos;
import net.blay09.mods.waystones.util.WaystoneEntry;
import net.minecraftforge.common.config.Configuration;

import io.netty.buffer.ByteBuf;

public class WaystoneConfig {

    private static Configuration config;

    public static int teleportButtonX;
    public static int teleportButtonY;
    public static boolean disableParticles;
    public static boolean disableTextGlow;

    public boolean teleportButton;
    public int teleportButtonCooldown;
    public boolean teleportButtonReturnOnly;

    public boolean allowReturnScrolls;
    public boolean allowWarpStone;

    public int warpStoneCooldown;

    public boolean interDimension;

    public boolean creativeModeOnly;
    public boolean setSpawnPoint;

    public boolean globalNoCooldown;
    public boolean globalInterDimension;

    public static boolean showNametag;
    public static boolean enableWorldgen;
    public static boolean villageNamesCompat;

    public static int xpBaseCost;
    public static int xpBlocksPerLevel;
    public static int xpCrossDimCost;

    public static class Categories {

        public static final String general = "general";
        public static final String client = "client";
        public static final String generated = "generated";
    }

    public void reloadLocal(Configuration config) {
        teleportButton = config.getBoolean(
            "Teleport Button in GUI",
            Categories.general,
            false,
            "Should there be a button in the inventory to access the waystone menu?");
        teleportButtonCooldown = config.getInt(
            "Teleport Button Cooldown",
            Categories.general,
            300,
            0,
            86400,
            "The cooldown between usages of the teleport button in seconds.");
        teleportButtonReturnOnly = config.getBoolean(
            "Teleport Button Return Only",
            Categories.general,
            false,
            "If true, the teleport button will only let you return to the last activated waystone, instead of allowing to choose.");

        allowReturnScrolls = config
            .getBoolean("Allow Return Scrolls", Categories.general, true, "If true, return scrolls will be craftable.");
        allowWarpStone = config
            .getBoolean("Allow Warp Stone", Categories.general, true, "If true, the warp stone will be craftable.");

        teleportButtonX = config.getInt(
            "Teleport Button GUI X",
            Categories.client,
            60,
            -100,
            250,
            "The x position of the warp button in the inventory.");
        teleportButtonY = config.getInt(
            "Teleport Button GUI Y",
            Categories.client,
            60,
            -100,
            250,
            "The y position of the warp button in the inventory.");
        disableTextGlow = config.getBoolean(
            "Disable Text Glow",
            Categories.client,
            false,
            "If true, the text overlay on waystones will no longer always render at full brightness.");
        disableParticles = config.getBoolean(
            "Disable Particles",
            Categories.client,
            false,
            "If true, activated waystones will not emit particles.");

        warpStoneCooldown = config.getInt(
            "Teleportation Cooldown",
            Categories.general,
            300,
            0,
            86400,
            "The cooldown between usages of the Warp Stone and Waystone in seconds.");

        setSpawnPoint = config.getBoolean(
            "Set Spawnpoint on Activation",
            Categories.general,
            false,
            "If true, the player's spawnpoint will be set to the last activated waystone.");
        interDimension = config.getBoolean(
            "Interdimensional Teleport",
            Categories.general,
            true,
            "If true, all waystones work inter-dimensionally.");

        creativeModeOnly = config.getBoolean(
            "Creative Mode Only",
            Categories.general,
            false,
            "If true, waystones can only be placed in creative mode.");

        globalNoCooldown = config.getBoolean(
            "No Cooldown on Global Waystones",
            Categories.general,
            true,
            "If true, waystones marked as global have no cooldown.");
        globalInterDimension = config.getBoolean(
            "Interdimensional Teleport on Global Waystones",
            Categories.general,
            true,
            "If true, waystones marked as global work inter-dimensionally.");

        String[] serverWaystoneData = config.getStringList(
            "Server Waystones",
            Categories.generated,
            new String[0],
            "This option is automatically populated by the server when using the Server Hub Mode. Do not change.");
        WaystoneEntry[] serverWaystones = new WaystoneEntry[serverWaystoneData.length];
        for (int i = 0; i < serverWaystones.length; i++) {
            String[] split = serverWaystoneData[i].split("\u00a7");
            if (split.length < 3) {
                serverWaystones[i] = new WaystoneEntry("Invalid Waystone", 0, new BlockPos(0, 0, 0));
                serverWaystones[i].setGlobal(true);
                continue;
            }
            try {
                int dimensionId = Integer.parseInt(split[1]);
                String[] pos = split[2].split(",");
                serverWaystones[i] = new WaystoneEntry(
                    split[0],
                    dimensionId,
                    new BlockPos(Integer.parseInt(pos[0]), Integer.parseInt(pos[1]), Integer.parseInt(pos[2])));
                serverWaystones[i].setGlobal(true);
            } catch (NumberFormatException e) {
                serverWaystones[i] = new WaystoneEntry("Invalid Waystone", 0, new BlockPos(0, 0, 0));
                serverWaystones[i].setGlobal(true);
            }
        }
        WaystoneManager.setServerWaystones(serverWaystones);

        showNametag = config.getBoolean(
            "Show Waystone nametag",
            Categories.client,
            false,
            "If true, show a floating nametag with the Waystone's name, above it.");

        enableWorldgen = config
            .getBoolean("Enable Worldgen", Categories.general, true, "If true, generate a Waystone in each village.");

        villageNamesCompat = config.getBoolean(
            "Enable Village Names Compat",
            Categories.general,
            true,
            "If true, village Waystones will take their name from Village Names.");

        xpBaseCost = config.getInt(
            "Teleport Base XP Cost",
            Categories.general,
            5,
            -1,
            Integer.MAX_VALUE,
            "The minimum amount of XP levels consumed when using a Waystone. Set to -1 to disable cost altogether.");

        xpBlocksPerLevel = config.getInt(
            "Teleport XP Cost per X Blocks",
            Categories.general,
            100,
            0,
            Integer.MAX_VALUE,
            "Each how many blocks consume one XP level.");

        xpCrossDimCost = config.getInt(
            "Cross-dim Teleport XP Cost",
            Categories.general,
            5,
            0,
            Integer.MAX_VALUE,
            "How many XP levels are consumed for teleporting to another dimension.");
    }

    public static void storeServerWaystones(Configuration config, Collection<WaystoneEntry> entries) {
        String[] serverWaystones = new String[entries.size()];
        int i = 0;
        for (WaystoneEntry entry : entries) {
            serverWaystones[i] = entry.getName() + "\u00a7"
                + entry.getDimensionId()
                + "\u00a7"
                + entry.getPos()
                    .getX()
                + ","
                + entry.getPos()
                    .getY()
                + ","
                + entry.getPos()
                    .getZ();
            i++;
        }
        config
            .get(
                Categories.generated,
                "Server Waystones",
                "This option is automatically populated by the server when using the Server Hub Mode. Do not change.")
            .set(serverWaystones);
        if (config.hasChanged()) {
            config.save();
        }
    }

    public static WaystoneConfig read(ByteBuf buf) {
        WaystoneConfig config = new WaystoneConfig();
        config.teleportButton = buf.readBoolean();
        config.teleportButtonCooldown = buf.readInt();
        config.teleportButtonReturnOnly = buf.readBoolean();
        config.warpStoneCooldown = buf.readInt();
        config.interDimension = buf.readBoolean();
        config.globalInterDimension = buf.readBoolean();
        config.creativeModeOnly = buf.readBoolean();
        config.setSpawnPoint = buf.readBoolean();
        config.showNametag = buf.readBoolean();
        config.enableWorldgen = buf.readBoolean();
        config.villageNamesCompat = buf.readBoolean();
        config.xpBaseCost = buf.readInt();
        config.xpBlocksPerLevel = buf.readInt();
        config.xpCrossDimCost = buf.readInt();
        return config;
    }

    public void write(ByteBuf buf) {
        buf.writeBoolean(teleportButton);
        buf.writeInt(teleportButtonCooldown);
        buf.writeBoolean(teleportButtonReturnOnly);
        buf.writeInt(warpStoneCooldown);
        buf.writeBoolean(interDimension);
        buf.writeBoolean(globalInterDimension);
        buf.writeBoolean(creativeModeOnly);
        buf.writeBoolean(setSpawnPoint);
        buf.writeBoolean(showNametag);
        buf.writeBoolean(enableWorldgen);
        buf.writeBoolean(villageNamesCompat);
        buf.writeInt(xpBaseCost);
        buf.writeInt(xpBlocksPerLevel);
        buf.writeInt(xpCrossDimCost);
    }

    public static Configuration getRawConfig() {
        return config;
    }

    public static void setConfig(Configuration config) {
        WaystoneConfig.config = config;
    }
}
