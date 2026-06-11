package net.blay09.mods.waystones.network.handler;

import net.blay09.mods.waystones.network.message.MessageDimensionNames;
import net.blay09.mods.waystones.util.DimensionUtil;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class HandlerDimensionNames implements IMessageHandler<MessageDimensionNames, IMessage> {

    @Override
    public IMessage onMessage(MessageDimensionNames message, MessageContext ctx) {
        for (int i = 0; i < message.getAmount(); i++) {
            DimensionUtil.setEntry(message.getIds()[i], DimensionUtil.localizeDimName(message.getNames()[i]));
        }
        return null;
    }
}
