package net.blay09.mods.waystones.mixins.early.minecraft;

import java.util.Random;

import net.blay09.mods.waystones.WaystoneManager;
import net.blay09.mods.waystones.Waystones;
import net.blay09.mods.waystones.block.TileWaystone;
import net.blay09.mods.waystones.util.WaystoneEntry;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.ComponentScatteredFeaturePieces;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraftforge.common.util.ForgeDirection;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ComponentScatteredFeaturePieces.DesertPyramid.class)
public abstract class MixinComponentScatteredFeaturePiecesDesertPyramid {

    private static final String STRUCTURE_ID = "temple_desert";

    @Inject(method = "addComponentParts", at = @At("TAIL"))
    private void addWaystoneAboveBlueWool(World world, Random rand, StructureBoundingBox box,
        CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) {
            return;
        }

        // blue wool is at local (10, 0, 10)
        AccessorStructureComponent structureAccessor = (AccessorStructureComponent) this;
        int x = structureAccessor.callGetXWithOffset(10, 10);
        int y = structureAccessor.callGetYWithOffset(1); // 1 above
        int z = structureAccessor.callGetZWithOffset(10, 10);
        int biomeId = world.getBiomeGenForCoords(x, z).biomeID;

        int variant = Waystones.varInstanceCommon
            .resolveStructureWaystoneVariant(STRUCTURE_ID, TileWaystone.VARIANT_SANDSTONE, world, biomeId, rand);
        if (variant < 0) {
            return;
        }

        if (!box.isVecInside(x, y, z) || !box.isVecInside(x, y + 1, z)) {
            return;
        }

        net.minecraft.block.Block waystoneBlock = Waystones.getWaystoneBlock(variant);
        world.setBlock(x, y, z, waystoneBlock, 2, 2);
        world.setBlock(x, y + 1, z, waystoneBlock, ForgeDirection.UNKNOWN.ordinal(), 2);

        TileWaystone tile = (TileWaystone) world.getTileEntity(x, y, z);
        if (tile != null && !world.isRemote) {
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
    }
}
