package net.blay09.mods.waystones.worldgen;

import java.util.List;
import java.util.Random;

import net.blay09.mods.waystones.WaystoneManager;
import net.blay09.mods.waystones.Waystones;
import net.blay09.mods.waystones.block.TileWaystone;
import net.blay09.mods.waystones.util.WaystoneEntry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureNetherBridgePieces;
import net.minecraftforge.common.util.ForgeDirection;

public class FortressWaystone {

    private static final String STRUCTURE_ID = "fortress";
    private static final int UNRESOLVED_VARIANT = Integer.MIN_VALUE;

    public static class FortressWaystonePiece extends StructureNetherBridgePieces.Entrance {

        private int resolvedVariant = UNRESOLVED_VARIANT;
        private boolean waystonePlaced;
        private int fortressHash;

        public FortressWaystonePiece() {}

        public FortressWaystonePiece(int type, Random rand, StructureBoundingBox box, int coordBaseMode) {
            super(type, rand, box, coordBaseMode);
        }

        public static FortressWaystonePiece createValidComponent(List<StructureComponent> components, Random rand,
            int x, int y, int z, int coordBaseMode, int type) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox
                .getComponentToAddBoundingBox(x, y, z, -5, -3, 0, 13, 14, 13, coordBaseMode);
            return isAboveGround(structureboundingbox)
                && StructureComponent.findIntersecting(components, structureboundingbox) == null
                    ? new FortressWaystonePiece(type, rand, structureboundingbox, coordBaseMode)
                    : null;
        }

        @Override
        protected void func_143011_b(NBTTagCompound tag) {
            super.func_143011_b(tag);
            resolvedVariant = tag.getInteger("WsVariant");
            waystonePlaced = tag.getBoolean("WsPlaced");
            fortressHash = tag.getInteger("WsHash");
        }

        @Override
        protected void func_143012_a(NBTTagCompound tag) {
            super.func_143012_a(tag);
            tag.setInteger("WsVariant", resolvedVariant);
            tag.setBoolean("WsPlaced", waystonePlaced);
            tag.setInteger("WsHash", fortressHash);
        }

        public void setFortressHash(int fortressHash) {
            this.fortressHash = fortressHash;
        }

        @Override
        public boolean addComponentParts(World world, Random rand, StructureBoundingBox box) {
            this.fillWithBlocks(world, box, 0, 3, 0, 12, 4, 12, Blocks.nether_brick, Blocks.nether_brick, false);
            this.fillWithBlocks(world, box, 0, 5, 0, 12, 13, 12, Blocks.air, Blocks.air, false);
            this.fillWithBlocks(world, box, 0, 5, 0, 1, 12, 12, Blocks.nether_brick, Blocks.nether_brick, false);
            this.fillWithBlocks(world, box, 11, 5, 0, 12, 12, 12, Blocks.nether_brick, Blocks.nether_brick, false);
            this.fillWithBlocks(world, box, 2, 5, 11, 4, 12, 12, Blocks.nether_brick, Blocks.nether_brick, false);
            this.fillWithBlocks(world, box, 8, 5, 11, 10, 12, 12, Blocks.nether_brick, Blocks.nether_brick, false);
            this.fillWithBlocks(world, box, 5, 9, 11, 7, 12, 12, Blocks.nether_brick, Blocks.nether_brick, false);
            this.fillWithBlocks(world, box, 2, 5, 0, 4, 12, 1, Blocks.nether_brick, Blocks.nether_brick, false);
            this.fillWithBlocks(world, box, 8, 5, 0, 10, 12, 1, Blocks.nether_brick, Blocks.nether_brick, false);
            this.fillWithBlocks(world, box, 5, 9, 0, 7, 12, 1, Blocks.nether_brick, Blocks.nether_brick, false);
            this.fillWithBlocks(world, box, 2, 11, 2, 10, 12, 10, Blocks.nether_brick, Blocks.nether_brick, false);
            this.fillWithBlocks(
                world,
                box,
                5,
                8,
                0,
                7,
                8,
                0,
                Blocks.nether_brick_fence,
                Blocks.nether_brick_fence,
                false);

            int i;
            for (i = 1; i <= 11; i += 2) {
                this.fillWithBlocks(
                    world,
                    box,
                    i,
                    10,
                    0,
                    i,
                    11,
                    0,
                    Blocks.nether_brick_fence,
                    Blocks.nether_brick_fence,
                    false);
                this.fillWithBlocks(
                    world,
                    box,
                    i,
                    10,
                    12,
                    i,
                    11,
                    12,
                    Blocks.nether_brick_fence,
                    Blocks.nether_brick_fence,
                    false);
                this.fillWithBlocks(
                    world,
                    box,
                    0,
                    10,
                    i,
                    0,
                    11,
                    i,
                    Blocks.nether_brick_fence,
                    Blocks.nether_brick_fence,
                    false);
                this.fillWithBlocks(
                    world,
                    box,
                    12,
                    10,
                    i,
                    12,
                    11,
                    i,
                    Blocks.nether_brick_fence,
                    Blocks.nether_brick_fence,
                    false);
                this.placeBlockAtCurrentPosition(world, Blocks.nether_brick, 0, i, 13, 0, box);
                this.placeBlockAtCurrentPosition(world, Blocks.nether_brick, 0, i, 13, 12, box);
                this.placeBlockAtCurrentPosition(world, Blocks.nether_brick, 0, 0, 13, i, box);
                this.placeBlockAtCurrentPosition(world, Blocks.nether_brick, 0, 12, 13, i, box);
                this.placeBlockAtCurrentPosition(world, Blocks.nether_brick_fence, 0, i + 1, 13, 0, box);
                this.placeBlockAtCurrentPosition(world, Blocks.nether_brick_fence, 0, i + 1, 13, 12, box);
                this.placeBlockAtCurrentPosition(world, Blocks.nether_brick_fence, 0, 0, 13, i + 1, box);
                this.placeBlockAtCurrentPosition(world, Blocks.nether_brick_fence, 0, 12, 13, i + 1, box);
            }

            this.placeBlockAtCurrentPosition(world, Blocks.nether_brick_fence, 0, 0, 13, 0, box);
            this.placeBlockAtCurrentPosition(world, Blocks.nether_brick_fence, 0, 0, 13, 12, box);
            this.placeBlockAtCurrentPosition(world, Blocks.nether_brick_fence, 0, 0, 13, 0, box);
            this.placeBlockAtCurrentPosition(world, Blocks.nether_brick_fence, 0, 12, 13, 0, box);

            for (i = 3; i <= 9; i += 2) {
                this.fillWithBlocks(
                    world,
                    box,
                    1,
                    7,
                    i,
                    1,
                    8,
                    i,
                    Blocks.nether_brick_fence,
                    Blocks.nether_brick_fence,
                    false);
                this.fillWithBlocks(
                    world,
                    box,
                    11,
                    7,
                    i,
                    11,
                    8,
                    i,
                    Blocks.nether_brick_fence,
                    Blocks.nether_brick_fence,
                    false);
            }

            this.fillWithBlocks(world, box, 4, 2, 0, 8, 2, 12, Blocks.nether_brick, Blocks.nether_brick, false);
            this.fillWithBlocks(world, box, 0, 2, 4, 12, 2, 8, Blocks.nether_brick, Blocks.nether_brick, false);
            this.fillWithBlocks(world, box, 4, 0, 0, 8, 1, 3, Blocks.nether_brick, Blocks.nether_brick, false);
            this.fillWithBlocks(world, box, 4, 0, 9, 8, 1, 12, Blocks.nether_brick, Blocks.nether_brick, false);
            this.fillWithBlocks(world, box, 0, 0, 4, 3, 1, 8, Blocks.nether_brick, Blocks.nether_brick, false);
            this.fillWithBlocks(world, box, 9, 0, 4, 12, 1, 8, Blocks.nether_brick, Blocks.nether_brick, false);

            int j;
            for (i = 4; i <= 8; ++i) {
                for (j = 0; j <= 2; ++j) {
                    this.func_151554_b(world, Blocks.nether_brick, 0, i, -1, j, box);
                    this.func_151554_b(world, Blocks.nether_brick, 0, i, -1, 12 - j, box);
                }
            }

            for (i = 0; i <= 2; ++i) {
                for (j = 4; j <= 8; ++j) {
                    this.func_151554_b(world, Blocks.nether_brick, 0, i, -1, j, box);
                    this.func_151554_b(world, Blocks.nether_brick, 0, 12 - i, -1, j, box);
                }
            }

            // Replace vanilla lava-well center with open space for waystone.
            this.fillWithBlocks(world, box, 5, 5, 5, 7, 5, 7, Blocks.air, Blocks.air, false);
            // Ensure the old center base brick is never left behind.
            this.placeBlockAtCurrentPosition(world, Blocks.air, 0, 6, 0, 6, box);
            // Fill the two-block gap directly under the waystone.
            this.fillWithBlocks(world, box, 6, 3, 6, 6, 4, 6, Blocks.nether_brick, Blocks.nether_brick, false);

            int x = this.getXWithOffset(6, 6);
            int y = this.getYWithOffset(5);
            int z = this.getZWithOffset(6, 6);

            if (!box.isVecInside(x, y, z) || !box.isVecInside(x, y + 1, z)) {
                return true;
            }

            if (resolvedVariant == UNRESOLVED_VARIANT) {
                int biomeId = world.getBiomeGenForCoords(x, z).biomeID;
                resolvedVariant = Waystones.varInstanceCommon
                    .resolveStructureWaystoneVariant(STRUCTURE_ID, TileWaystone.VARIANT_STONE, world, biomeId, rand);
            }

            if (resolvedVariant < 0 || waystonePlaced) {
                return true;
            }

            Block waystoneBlock = Waystones.getWaystoneBlock(resolvedVariant);
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

            waystonePlaced = true;
            Waystones.debug(
                "Spawned fortress waystone room piece at " + x
                    + " "
                    + y
                    + " "
                    + z
                    + " fortressHash="
                    + Integer.toHexString(fortressHash));
            return true;
        }
    }
}
