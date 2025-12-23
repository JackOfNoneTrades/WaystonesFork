package net.blay09.mods.waystones.worldgen;

import java.util.List;
import java.util.Random;

import net.blay09.mods.waystones.Waystones;
import net.blay09.mods.waystones.block.TileWaystone;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraftforge.common.util.ForgeDirection;

import cpw.mods.fml.common.registry.VillagerRegistry;

public class VillageWaystone {

    public static class VillageWaystonePiece extends StructureVillagePieces.Village {

        public VillageWaystonePiece() {}

        public VillageWaystonePiece(StructureVillagePieces.Start start, int type, Random rand, StructureBoundingBox box,
            int coordBaseMode) {
            super(start, type);
            this.coordBaseMode = coordBaseMode;
            this.boundingBox = box;
        }

        public static VillageWaystonePiece buildComponent(StructureVillagePieces.Start start,
            List<StructureComponent> pieces, Random rand, int x, int y, int z, int coordBaseMode, int type) {
            StructureBoundingBox box = StructureBoundingBox
                .getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 5, 6, 5, coordBaseMode);

            if (!canVillageGoDeeper(box)) return null;
            if (StructureComponent.findIntersecting(pieces, box) != null) return null;

            return new VillageWaystonePiece(start, type, rand, box, coordBaseMode);
        }

        @Override
        public boolean addComponentParts(World world, Random rand, StructureBoundingBox box) {
            if (this.field_143015_k < 0) {
                this.field_143015_k = this.getAverageGroundLevel(world, box);
                if (this.field_143015_k < 0) return true;

                // Lower the entire structure by 1 so platform is on the floor
                this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.minY - 1, 0);
            }

            int xMin = this.boundingBox.minX;
            int yMin = this.boundingBox.minY;
            int zMin = this.boundingBox.minZ;

            // Clear area above platform
            this.fillWithAir(world, box, 0, 1, 0, 4, 5, 4);

            // Platform (1 block high)
            this.fillWithBlocks(world, box, 0, 0, 0, 4, 0, 4, Blocks.cobblestone, Blocks.cobblestone, false);

            // Waystone position (sit on top of platform)
            int waystoneX = xMin + 2;
            int waystoneY = yMin + 1;
            int waystoneZ = zMin + 2;

            // Lower block
            world.setBlock(waystoneX, waystoneY, waystoneZ, Waystones.blockWaystone, 2, 2);
            // Upper block
            world.setBlock(
                waystoneX,
                waystoneY + 1,
                waystoneZ,
                Waystones.blockWaystone,
                ForgeDirection.UNKNOWN.ordinal(),
                2);

            // Initialize TileEntity
            TileWaystone tile = (TileWaystone) world.getTileEntity(waystoneX, waystoneY, waystoneZ);

            /*
             * if (tile != null) {
             * tile.setWaystoneName("Village Waystone");
             * }
             */

            return true;
        }

    }

    public static class CreationHandler implements VillagerRegistry.IVillageCreationHandler {

        @Override
        public StructureVillagePieces.PieceWeight getVillagePieceWeight(Random random, int size) {
            return new StructureVillagePieces.PieceWeight(VillageWaystonePiece.class, 1, 1);
        }

        @Override
        public Class<?> getComponentClass() {
            return VillageWaystonePiece.class;
        }

        @Override
        public StructureComponent buildComponent(StructureVillagePieces.PieceWeight villagePiece,
            StructureVillagePieces.Start startPiece, List pieces, Random random, int p1, int p2, int p3,
            int coordBaseMode, int type) {
            return VillageWaystonePiece.buildComponent(startPiece, pieces, random, p1, p2, p3, coordBaseMode, type);
        }
    }
}
