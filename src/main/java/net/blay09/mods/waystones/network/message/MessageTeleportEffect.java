package net.blay09.mods.waystones.network.message;

import net.blay09.mods.waystones.util.BlockPos;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class MessageTeleportEffect implements IMessage {

    private BlockPos pos;
    private boolean playSound;

    public MessageTeleportEffect() {}

    public MessageTeleportEffect(BlockPos pos) {
        this(pos, false);
    }

    public MessageTeleportEffect(BlockPos pos, boolean playSound) {
        this.pos = pos;
        this.playSound = playSound;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = BlockPos.fromLong(buf.readLong());
        playSound = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(pos.toLong());
        buf.writeBoolean(playSound);
    }

    public BlockPos getPos() {
        return pos;
    }

    public boolean shouldPlaySound() {
        return playSound;
    }

}
