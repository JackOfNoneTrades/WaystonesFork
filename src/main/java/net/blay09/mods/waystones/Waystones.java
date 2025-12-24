package net.blay09.mods.waystones;

import net.blay09.mods.waystones.block.BlockWaystone;
import net.blay09.mods.waystones.block.TileWaystone;
import net.blay09.mods.waystones.item.ItemReturnScroll;
import net.blay09.mods.waystones.item.ItemWarpStone;
import net.blay09.mods.waystones.network.NetworkHandler;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.ShapedOreRecipe;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = Waystones.MODID, name = "Waystones-X", guiFactory = "net.blay09.mods.waystones.client.gui.GuiFactory")
@SuppressWarnings("unused")
public class Waystones {

    public static final String MODID = "waystones";

    @Mod.Instance(MODID)
    public static Waystones instance;

    @SidedProxy(
        serverSide = "net.blay09.mods.waystones.CommonProxy",
        clientSide = "net.blay09.mods.waystones.client.ClientProxy")
    public static CommonProxy proxy;

    public static BlockWaystone blockWaystone;
    public static ItemReturnScroll itemReturnScroll;
    public static ItemWarpStone itemWarpStone;

    public static Configuration configuration;

    private WaystoneConfig config;

    public static final Logger LOG = LogManager.getLogger(MODID);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        blockWaystone = new BlockWaystone();
        GameRegistry.registerBlock(blockWaystone, "waystone");
        GameRegistry.registerTileEntity(TileWaystone.class, MODID + ":waystone");

        itemReturnScroll = new ItemReturnScroll();
        GameRegistry.registerItem(itemReturnScroll, "warpScroll");

        itemWarpStone = new ItemWarpStone();
        GameRegistry.registerItem(itemWarpStone, "warpStone");

        NetworkHandler.init();

        configuration = new Configuration(event.getSuggestedConfigurationFile());
        config = new WaystoneConfig();
        config.reloadLocal(configuration);
        WaystoneConfig.setConfig(configuration);
        if (configuration.hasChanged()) {
            configuration.save();
        }

        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        FMLInterModComms.sendMessage("Waila", "register", "net.blay09.mods.waystones.compat.WailaProvider.register");

        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        if (instance.config.allowReturnScrolls) {
            GameRegistry.addRecipe(
                new ShapedOreRecipe(
                    new ItemStack(itemReturnScroll, 3),
                    "GEG",
                    "PPP",
                    'G',
                    "nuggetGold",
                    'E',
                    Items.ender_pearl,
                    'P',
                    Items.paper));
        }

        if (instance.config.allowWarpStone) {
            GameRegistry.addRecipe(
                new ShapedOreRecipe(
                    new ItemStack(itemWarpStone),
                    "DED",
                    "EGE",
                    "DED",
                    'D',
                    "dyePurple",
                    'E',
                    Items.ender_pearl,
                    'G',
                    "gemEmerald"));
        }

        if (!config.creativeModeOnly) {
            GameRegistry.addRecipe(
                new ShapedOreRecipe(
                    new ItemStack(blockWaystone),
                    " S ",
                    "SWS",
                    "OOO",
                    'S',
                    Blocks.stonebrick,
                    'W',
                    itemWarpStone,
                    'O',
                    Blocks.obsidian));
        }
    }

    public static WaystoneConfig getConfig() {
        return instance.config;
    }

    public void setConfig(WaystoneConfig config) {
        this.config = config;
    }

}
