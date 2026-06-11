package net.blay09.mods.waystones.network.message;

import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class MessageDimensionNames implements IMessage {

    public MessageDimensionNames() {}

    int amount;
    Integer[] ids;
    String[] names;

    public MessageDimensionNames(boolean isserver) {
        this.amount = 0;
        this.ids = new Integer[DimensionManager.getStaticDimensionIDs().length];
        this.names = new String[this.ids.length];
        for (int dimId : DimensionManager.getStaticDimensionIDs()) {
            this.ids[this.amount] = dimId;
            WorldServer world = DimensionManager.getWorld(dimId);
            WorldProvider provider = world != null ? world.provider : DimensionManager.createProviderFor(dimId);
            this.names[this.amount] = provider.getDimensionName();
            this.amount++;
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.amount = buf.readInt();
        this.ids = new Integer[this.amount];
        this.names = new String[this.amount];
        for (int i = 0; i < this.amount; i++) {
            this.ids[i] = buf.readInt();
            this.names[i] = ByteBufUtils.readUTF8String(buf);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.amount);
        for (int i = 0; i < this.amount; i++) {
            buf.writeInt(this.ids[i]);
            ByteBufUtils.writeUTF8String(buf, this.names[i]);
        }
    }

    public int getAmount() {
        return amount;
    }

    public Integer[] getIds() {
        return ids;
    }

    public String[] getNames() {
        return names;
    }
}
