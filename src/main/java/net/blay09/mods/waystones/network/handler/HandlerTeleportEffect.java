package net.blay09.mods.waystones.network.handler;

import net.blay09.mods.waystones.WaystoneConfig;
import net.blay09.mods.waystones.Waystones;
import net.blay09.mods.waystones.network.message.MessageTeleportEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class HandlerTeleportEffect implements IMessageHandler<MessageTeleportEffect, IMessage> {

    @Override
    public IMessage onMessage(final MessageTeleportEffect message, MessageContext ctx) {
        Waystones.proxy.addScheduledTask(new Runnable() {

            @Override
            public void run() {
                Minecraft mc = Minecraft.getMinecraft();
                if (message.shouldPlaySound() && !WaystoneConfig.disableTeleportSound) {
                    mc.getSoundHandler()
                        .playSound(
                            new PositionedSoundRecord(
                                new ResourceLocation("portal.travel"),
                                0.25f,
                                1f,
                                message.getPos()
                                    .getX() + 0.5f,
                                message.getPos()
                                    .getY() + 0.5f,
                                message.getPos()
                                    .getZ() + 0.5f));
                }
                for (int i = 0; i < 128; i++) {
                    mc.theWorld.spawnParticle(
                        "portal",
                        message.getPos()
                            .getX() + (mc.theWorld.rand.nextDouble() - 0.5) * 3,
                        message.getPos()
                            .getY() + mc.theWorld.rand.nextDouble() * 3,
                        message.getPos()
                            .getZ() + (mc.theWorld.rand.nextDouble() - 0.5) * 3,
                        (mc.theWorld.rand.nextDouble() - 0.5) * 2,
                        -mc.theWorld.rand.nextDouble(),
                        (mc.theWorld.rand.nextDouble() - 0.5) * 2);
                }
            }
        });
        return null;
    }
}
