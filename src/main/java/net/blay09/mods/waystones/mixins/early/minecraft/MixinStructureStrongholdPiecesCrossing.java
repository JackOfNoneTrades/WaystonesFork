package net.blay09.mods.waystones.mixins.early.minecraft;

import java.util.Random;

import net.blay09.mods.waystones.WaystoneManager;
import net.blay09.mods.waystones.Waystones;
import net.blay09.mods.waystones.block.BlockWaystone;
import net.blay09.mods.waystones.block.TileWaystone;
import net.blay09.mods.waystones.util.WaystoneEntry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureStrongholdPieces;
import net.minecraftforge.common.util.ForgeDirection;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StructureStrongholdPieces.Crossing.class)
public abstract class MixinStructureStrongholdPiecesCrossing {

    private static final String STRUCTURE_ID = "stronghold";
    private static final int DEDUPE_RADIUS = 96;

    @Inject(method = "addComponentParts", at = @At("TAIL"))
    private void addWaystoneOnCrossingRoof(World world, Random rand, StructureBoundingBox box,
        CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) {
            return;
        }

        AccessorStructureComponent structureAccessor = (AccessorStructureComponent) this;

        // Crossing has layered slab roof over stairs; top 3x3 center is local (6,5,8).
        int localX = 6;
        int localY = 6;
        int localZ = 8;

        int x = structureAccessor.callGetXWithOffset(localX, localZ);
        int y = structureAccessor.callGetYWithOffset(localY);
        int z = structureAccessor.callGetZWithOffset(localX, localZ);
        int biomeId = world.getBiomeGenForCoords(x, z).biomeID;

        int autoVariant = rand.nextBoolean() ? TileWaystone.VARIANT_MOSSY_STONEBRICK : TileWaystone.VARIANT_STONEBRICK;
        int variant = Waystones.varInstanceCommon
            .resolveStructureWaystoneVariant(STRUCTURE_ID, autoVariant, world, biomeId, rand);
        if (variant < 0) {
            return;
        }

        if (!box.isVecInside(x, y, z) || !box.isVecInside(x, y + 1, z)) {
            return;
        }

        if (hasNearbyLoadedWaystone(world, x, y, z, DEDUPE_RADIUS)) {
            return;
        }

        // Ensure we are really on top of the 3x3 top plate.
        Block below = world.getBlock(x, y - 1, z);
        if (below != Blocks.double_stone_slab && below != Blocks.stone_slab) {
            return;
        }

        Block existing = world.getBlock(x, y, z);
        if (isWaystoneBlock(existing)) {
            return;
        }

        raiseRoofOverWaystone(world, rand, x, y, z);

        Waystones.debug("Spawned stronghold waystone at " + " " + x + " " + y + " " + z);

        Block waystoneBlock = Waystones.getWaystoneBlock(variant);
        world.setBlock(x, y, z, waystoneBlock, 2, 2);
        world.setBlock(x, y + 1, z, waystoneBlock, ForgeDirection.UNKNOWN.ordinal(), 2);

        TileWaystone tile = (TileWaystone) world.getTileEntity(x, y, z);
        if (tile == null || world.isRemote) {
            return;
        }

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

    private static boolean isWaystoneBlock(Block block) {
        return block instanceof BlockWaystone;
    }

    private static boolean hasNearbyLoadedWaystone(World world, int x, int y, int z, int radius) {
        int radiusSq = radius * radius;
        for (Object obj : world.loadedTileEntityList) {
            if (!(obj instanceof TileWaystone)) {
                continue;
            }
            TileEntity tile = (TileEntity) obj;
            int dx = tile.xCoord - x;
            int dz = tile.zCoord - z;
            if (dx * dx + dz * dz > radiusSq) {
                continue;
            }
            if (Math.abs(tile.yCoord - y) > 24) {
                continue;
            }
            return true;
        }
        return false;
    }

    private static void raiseRoofOverWaystone(World world, Random rand, int x, int y, int z) {
        // The original roof cap above this spot is 3x3 one block above the waystone top.
        int oldRoofY = y + 2;
        int newRoofY = oldRoofY + 1;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                world.setBlock(x + dx, oldRoofY, z + dz, Blocks.air, 0, 2);
                world.setBlock(x + dx, newRoofY, z + dz, Blocks.stonebrick, randomStonebrickMeta(rand), 2);
            }
        }
    }

    private static int randomStonebrickMeta(Random rand) {
        int r = rand.nextInt(100);
        if (r < 45) {
            return 0; // normal stone bricks
        }
        if (r < 80) {
            return 1; // mossy stone bricks
        }
        return 2; // cracked stone bricks
    }
}
