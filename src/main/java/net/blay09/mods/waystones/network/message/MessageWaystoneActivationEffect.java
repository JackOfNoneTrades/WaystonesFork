package net.blay09.mods.waystones.network.message;

import net.blay09.mods.waystones.util.BlockPos;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class MessageWaystoneActivationEffect implements IMessage {

    private BlockPos pos;

    public MessageWaystoneActivationEffect() {}

    public MessageWaystoneActivationEffect(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = BlockPos.fromLong(buf.readLong());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(pos.toLong());
    }

    public BlockPos getPos() {
        return pos;
    }
}
