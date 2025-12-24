package net.blay09.mods.waystones.util;

import net.blay09.mods.waystones.PlayerWaystoneData;
import net.blay09.mods.waystones.WaystoneManager;
import net.blay09.mods.waystones.block.TileWaystone;
import net.minecraft.nbt.NBTTagCompound;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;

public class WaystoneEntry {

    private final String name;
    private final int dimensionId;
    private final BlockPos pos;
    private boolean isGlobal;

    public WaystoneEntry(String name, int dimensionId, BlockPos pos) {
        this.name = name;
        this.dimensionId = dimensionId;
        this.pos = pos;
    }

    public WaystoneEntry(TileWaystone tileWaystone) {
        this.name = tileWaystone.getWaystoneName();
        this.dimensionId = tileWaystone.getWorldObj().provider.dimensionId;
        this.pos = new BlockPos(tileWaystone);
    }

    public String getName() {
        return name;
    }

    public int getDimensionId() {
        return dimensionId;
    }

    public BlockPos getPos() {
        return pos;
    }

    public boolean isGlobal() {
        return isGlobal;
    }

    public void setGlobal(boolean isGlobal) {
        this.isGlobal = isGlobal;
    }

    public static WaystoneEntry read(ByteBuf buf) {
        return new WaystoneEntry(ByteBufUtils.readUTF8String(buf), buf.readInt(), BlockPos.fromLong(buf.readLong()));
    }

    public static WaystoneEntry read(NBTTagCompound tagCompound) {
        return new WaystoneEntry(
            tagCompound.getString("Name"),
            tagCompound.getInteger("Dimension"),
            BlockPos.fromLong(tagCompound.getLong("Position")));
    }

    public void write(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, name);
        buf.writeInt(dimensionId);
        buf.writeLong(pos.toLong());
    }

    public NBTTagCompound writeToNBT() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        tagCompound.setString("Name", name);
        tagCompound.setInteger("Dimension", dimensionId);
        tagCompound.setLong("Position", pos.toLong());
        return tagCompound;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WaystoneEntry that = (WaystoneEntry) o;
        return dimensionId == that.dimensionId && pos.equals(that.pos);
    }

    @Override
    public int hashCode() {
        int result = dimensionId;
        result = 31 * result + pos.hashCode();
        return result;
    }

    public static WaystoneEntry[] getCombinedWaystones() {
        WaystoneEntry[] playerWaystones = PlayerWaystoneData.fromPlayer(
            FMLClientHandler.instance()
                .getClientPlayerEntity())
            .getWaystones();
        WaystoneEntry[] combinedWaystones = new WaystoneEntry[WaystoneManager.getServerWaystones()
            .size() + playerWaystones.length];
        int i = 0;
        for (WaystoneEntry entry : WaystoneManager.getServerWaystones()) {
            combinedWaystones[i] = entry;
            i++;
        }
        System.arraycopy(playerWaystones, 0, combinedWaystones, i, playerWaystones.length);
        return combinedWaystones;
    }

    public static boolean tileAndEntryShareCoords(WaystoneEntry entry, TileWaystone tile) {
        return entry.getPos()
            .getX() == tile.xCoord
            && entry.getPos()
                .getY() == tile.yCoord
            && entry.getPos()
                .getZ() == tile.zCoord;
    }
}
