package net.blay09.mods.waystones.worldgen;

import java.util.Random;

import net.blay09.mods.waystones.EndWaystoneData;
import net.blay09.mods.waystones.WaystoneManager;
import net.blay09.mods.waystones.Waystones;
import net.blay09.mods.waystones.block.TileWaystone;
import net.blay09.mods.waystones.util.WaystoneEntry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public final class WorldSpawnWaystone {

    private static final String STRUCTURE_ID = "world_spawn";

    private WorldSpawnWaystone() {}

    public static void tryGenerate(World world) {
        EndWaystoneData data = EndWaystoneData.get(world);
        if (data.isWorldSpawnWaystoneGenerated()) {
            return;
        }

        ChunkCoordinates spawn = world.getSpawnPoint();
        Random rand = new Random(world.getSeed() ^ 0x57A7B91DL);
        int variant = Waystones.varInstanceCommon.resolveStructureWaystoneVariant(
            STRUCTURE_ID,
            TileWaystone.VARIANT_STONE,
            world,
            world.getBiomeGenForCoords(spawn.posX, spawn.posZ).biomeID,
            rand);
        if (variant < 0) {
            return;
        }

        for (int attempt = 0; attempt < 48; attempt++) {
            int x = spawn.posX + rand.nextInt(97) - 48;
            int z = spawn.posZ + rand.nextInt(97) - 48;
            int y = world.getTopSolidOrLiquidBlock(x, z);

            Block floor = world.getBlock(x, y - 1, z);
            Material floorMaterial = floor.getMaterial();
            if (!floorMaterial.isSolid() || floorMaterial.isLiquid()) {
                continue;
            }

            if (!world.isAirBlock(x, y, z) || !world.isAirBlock(x, y + 1, z)) {
                continue;
            }

            Block waystoneBlock = Waystones.getWaystoneBlock(variant);
            world.setBlock(x, y, z, waystoneBlock, 2, 2);
            world.setBlock(x, y + 1, z, waystoneBlock, ForgeDirection.UNKNOWN.ordinal(), 2);

            TileWaystone tile = (TileWaystone) world.getTileEntity(x, y, z);
            if (tile != null) {
                String resolvedName = Waystones.varInstanceCommon.resolveStructureWaystoneName(STRUCTURE_ID, null);
                if (resolvedName != null && !resolvedName.isEmpty()) {
                    tile.setWaystoneName(resolvedName);
                }

                if (Waystones.varInstanceCommon.shouldForceGlobalStructureWaystone(STRUCTURE_ID)
                    && tile.getWaystoneName() != null
                    && !tile.getWaystoneName()
                        .isEmpty()) {
                    if (Waystones.varInstanceCommon.shouldAutoActivateGlobalStructureWaystone(STRUCTURE_ID)) {
                        WaystoneManager.addServerWaystone(new WaystoneEntry(tile));
                    } else {
                        tile.setForceGlobalOnActivation(true);
                    }
                }
            }

            data.setWorldSpawnWaystoneGenerated(true);
            Waystones.debug("Spawned world spawn waystone at " + x + " " + y + " " + z);
            return;
        }
    }
}
