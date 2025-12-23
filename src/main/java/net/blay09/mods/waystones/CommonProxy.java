package net.blay09.mods.waystones;

import net.blay09.mods.waystones.block.TileWaystone;
import net.blay09.mods.waystones.network.NetworkHandler;
import net.blay09.mods.waystones.network.message.MessageConfig;
import net.blay09.mods.waystones.worldgen.VillageWaystone;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.gen.structure.MapGenStructureIO;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.registry.VillagerRegistry;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        FMLCommonHandler.instance()
            .bus()
            .register(this);

        MapGenStructureIO
            .func_143031_a(VillageWaystone.VillageWaystonePiece.class, Waystones.MODID + ":VillageWaystone");
    }

    public void init(FMLInitializationEvent event) {
        VillagerRegistry.instance()
            .registerVillageCreationHandler(new VillageWaystone.CreationHandler());
    }

    public void addScheduledTask(Runnable runnable) {
        runnable.run();
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        NetworkHandler.channel.sendTo(new MessageConfig(Waystones.getConfig()), (EntityPlayerMP) event.player);
        WaystoneManager.sendPlayerWaystones(event.player);
    }

    @SubscribeEvent
    public void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        WaystoneManager.sendPlayerWaystones(event.player);
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        WaystoneManager.sendPlayerWaystones(event.player);
    }

    public void openWaystoneNameEdit(TileWaystone tileEntity) {

    }

    public void openWaystoneSelection(TileWaystone currentWaystone, boolean isFree) {

    }

    public void printChatMessage(int i, IChatComponent chatComponent) {

    }

    public void playSound(String soundName, float pitch) {

    }
}
