package net.blay09.mods.waystones.mixins.early.minecraft;

import java.util.List;
import java.util.Random;

import net.blay09.mods.waystones.Waystones;
import net.blay09.mods.waystones.worldgen.FortressWaystone;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureNetherBridgePieces;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.world.gen.structure.StructureNetherBridgePieces$Piece")
public abstract class MixinStructureNetherBridgePiecesPiece {

    @Inject(
        method = "getNextComponent(Lnet/minecraft/world/gen/structure/StructureNetherBridgePieces$Start;Ljava/util/List;Ljava/util/Random;IIIIIZ)Lnet/minecraft/world/gen/structure/StructureComponent;",
        at = @At("HEAD"),
        cancellable = true)
    private void prioritizeFortressWaystonePiece(StructureNetherBridgePieces.Start start, List components, Random rand,
        int x, int y, int z, int coordBaseMode, int type, boolean secondary,
        CallbackInfoReturnable<StructureComponent> cir) {
        if (!Waystones.getConfig().enableWorldgen) {
            return;
        }

        if (hasFortressWaystonePiece(components)) {
            return;
        }

        StructureComponent waystonePiece = FortressWaystone.FortressWaystonePiece
            .createValidComponent(components, rand, x, y, z, coordBaseMode, type + 1);
        if (waystonePiece != null) {
            FortressWaystone.FortressWaystonePiece fortressPiece = (FortressWaystone.FortressWaystonePiece) waystonePiece;
            fortressPiece.setFortressHash(computeFortressHash(start));
            components.add(waystonePiece);
            start.field_74967_d.add(waystonePiece);
            cir.setReturnValue(waystonePiece);
        }
    }

    @SuppressWarnings("rawtypes")
    private static boolean hasFortressWaystonePiece(List components) {
        for (Object component : components) {
            if (component instanceof FortressWaystone.FortressWaystonePiece) {
                return true;
            }
        }
        return false;
    }

    private static int computeFortressHash(StructureNetherBridgePieces.Start start) {
        int hash = 17;
        hash = 31 * hash + start.getBoundingBox().minX;
        hash = 31 * hash + start.getBoundingBox().minY;
        hash = 31 * hash + start.getBoundingBox().minZ;
        return hash;
    }
}
