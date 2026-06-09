package net.blay09.mods.waystones.network.handler;

import net.blay09.mods.waystones.Waystones;
import net.blay09.mods.waystones.block.BlockWaystone;
import net.blay09.mods.waystones.network.message.MessageWaystoneActivationEffect;
import net.blay09.mods.waystones.util.BlockPos;
import net.minecraft.client.Minecraft;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class HandlerWaystoneActivationEffect implements IMessageHandler<MessageWaystoneActivationEffect, IMessage> {

    @Override
    public IMessage onMessage(final MessageWaystoneActivationEffect message, MessageContext ctx) {
        Waystones.proxy.addScheduledTask(new Runnable() {

            @Override
            public void run() {
                BlockPos pos = message.getPos();
                BlockWaystone
                    .clientActivationEffects(Minecraft.getMinecraft().theWorld, pos.getX(), pos.getY(), pos.getZ());
            }
        });
        return null;
    }
}
