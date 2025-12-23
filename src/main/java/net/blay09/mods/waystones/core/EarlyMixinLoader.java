package net.blay09.mods.waystones.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.blay09.mods.waystones.Waystones;

import cpw.mods.fml.relauncher.FMLLaunchHandler;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

@SuppressWarnings("unused")
@IFMLLoadingPlugin.MCVersion("1.7.10")
public class EarlyMixinLoader extends FentEarlyMixinLoader {

    public enum Side {
        CLIENT,
        SERVER,
        BOTH;
    }

    public static boolean isServer() {
        return FMLLaunchHandler.side()
            .isServer();
    }

    public static class MixinBuilder {

        private final List<String> mixins = new ArrayList<>();

        public MixinBuilder addMixin(String name, Side side, String modid) {
            if ((side == Side.CLIENT && isServer()) || (side == Side.SERVER && !isServer())) {
                return this;
            }

            mixins.add(modid + "." + name);
            return this;
        }

        public MixinBuilder addMixin(String name, Side side) {
            return addMixin(name, side, "minecraft");
        }

        public List<String> build() {
            return mixins;
        }
    }

    @Override
    public String getMixinConfig() {
        return "mixins." + Waystones.MODID + ".early.json";
    }

    @Override
    public List<String> getMixins(Set<String> loadedCoreMods) {
        return new MixinBuilder()
            // Accessors
            .addMixin("AccessorGuiContainer", Side.CLIENT)
            .addMixin("AccessorGuiScreen", Side.BOTH)
            // Rest

            .build();
    }
}
