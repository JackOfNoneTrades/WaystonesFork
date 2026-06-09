package net.blay09.mods.waystones.mixins.early.minecraft;

import java.util.Random;

import net.blay09.mods.waystones.EndWaystoneData;
import net.blay09.mods.waystones.WaystoneManager;
import net.blay09.mods.waystones.Waystones;
import net.blay09.mods.waystones.block.TileWaystone;
import net.blay09.mods.waystones.util.WaystoneEntry;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenSpikes;
import net.minecraftforge.common.util.ForgeDirection;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldGenSpikes.class)
public class MixinWorldGenSpikes {

    private static final String STRUCTURE_ID = "end_spike";

    @Inject(method = "generate", at = @At("RETURN"))
    private void addExtraEndWaystoneSpike(World world, Random rand, int x, int y, int z,
        CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue() || world.isRemote || world.provider.dimensionId != 1) {
            return;
        }

        EndWaystoneData data = EndWaystoneData.get(world);
        if (data.isEndSpikeWaystoneGenerated()) {
            return;
        }

        int biomeId = world.getBiomeGenForCoords(x, z).biomeID;
        int variant = Waystones.varInstanceCommon
            .resolveStructureWaystoneVariant(STRUCTURE_ID, TileWaystone.VARIANT_STONE, world, biomeId, rand);
        if (variant < 0) {
            return;
        }

        for (int attempt = 0; attempt < 32; attempt++) {
            int dx = x + rand.nextInt(41) - 20;
            int dz = z + rand.nextInt(41) - 20;
            int radius = 1 + rand.nextInt(3); // 1..3 -> diameter 3..7

            // Avoid loading unloaded chunks during population (causes cascading worldgen crash)
            int chunkX = dx >> 4;
            int chunkZ = dz >> 4;
            if (!world.getChunkProvider()
                .chunkExists(chunkX, chunkZ)) {
                continue;
            }

            // Keep the entire spike footprint inside a single already-loaded chunk.
            // Any cross-chunk getBlock/setBlock during decoration can cause recursive populate.
            if (((dx - radius) >> 4) != chunkX || ((dx + radius) >> 4) != chunkX
                || ((dz - radius) >> 4) != chunkZ
                || ((dz + radius) >> 4) != chunkZ) {
                continue;
            }

            int top = world.getTopSolidOrLiquidBlock(dx, dz);
            int height = 1 + rand.nextInt(3); // 1..3

            boolean valid = true;
            for (int px = dx - radius; px <= dx + radius && valid; px++) {
                for (int pz = dz - radius; pz <= dz + radius; pz++) {
                    int offX = px - dx;
                    int offZ = pz - dz;
                    if (offX * offX + offZ * offZ > radius * radius + 1) {
                        continue;
                    }

                    if (world.getBlock(px, top - 1, pz) != Blocks.end_stone) {
                        valid = false;
                        break;
                    }

                    for (int py = top; py <= top + height + 1; py++) {
                        if (!world.isAirBlock(px, py, pz)) {
                            valid = false;
                            break;
                        }
                    }

                    if (!valid) {
                        break;
                    }
                }
            }

            if (!valid) {
                continue;
            }

            for (int py = top; py < top + height; py++) {
                for (int px = dx - radius; px <= dx + radius; px++) {
                    for (int pz = dz - radius; pz <= dz + radius; pz++) {
                        int offX = px - dx;
                        int offZ = pz - dz;
                        if (offX * offX + offZ * offZ <= radius * radius + 1) {
                            world.setBlock(px, py, pz, Blocks.obsidian, 0, 2);
                        }
                    }
                }
            }

            int waystoneY = top + height;

            net.minecraft.block.Block waystoneBlock = Waystones.getWaystoneBlock(variant);
            world.setBlock(dx, waystoneY, dz, waystoneBlock, 2, 2);
            world.setBlock(dx, waystoneY + 1, dz, waystoneBlock, ForgeDirection.UNKNOWN.ordinal(), 2);

            TileWaystone tile = (TileWaystone) world.getTileEntity(dx, waystoneY, dz);
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

            data.setEndSpikeWaystoneGenerated(true);
            Waystones.debug(
                "Spawned end spike waystone at " + dx
                    + " "
                    + waystoneY
                    + " "
                    + dz
                    + " radius="
                    + radius
                    + " height="
                    + height);
            break;
        }
    }
}
