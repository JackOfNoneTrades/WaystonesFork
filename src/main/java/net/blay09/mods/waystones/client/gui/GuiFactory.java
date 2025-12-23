package net.blay09.mods.waystones.client.gui;

import com.google.common.collect.ImmutableList;

import cpw.mods.fml.client.IModGuiFactory;
import cpw.mods.fml.client.config.GuiConfig;
import net.blay09.mods.waystones.WaystoneConfig;
import net.blay09.mods.waystones.Waystones;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigElement;

import java.util.Set;

@SuppressWarnings("unused")
public class GuiFactory implements IModGuiFactory {

    @Override
    public void initialize(Minecraft minecraftInstance) {}

    @Override
    public Class<? extends GuiScreen> mainConfigGuiClass() {
        return ConfigGui.class;
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }

    @Override
    public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
        return null;
    }

    public static class ConfigGui extends GuiConfig {

        public ConfigGui(GuiScreen parentScreen) {
            super(
                    parentScreen,
                    ImmutableList.of(
                            new ConfigElement(
                                    WaystoneConfig.getRawConfig()
                                            .getCategory(WaystoneConfig.Categories.general)),
                            new ConfigElement(
                                    WaystoneConfig.getRawConfig()
                                            .getCategory(WaystoneConfig.Categories.client)),
                            new ConfigElement(
                                    WaystoneConfig.getRawConfig()
                                            .getCategory(WaystoneConfig.Categories.generated))),
                    Waystones.MOD_ID,
                    Waystones.MOD_ID,
                    false,
                    false,
                    I18n.format("gui.waystones:configTitle"));
        }

        @Override
        public void initGui() {
            super.initGui();
        }

        @Override
        public void drawScreen(int mouseX, int mouseY, float partialTicks) {
            // You can do things like create animations, draw additional elements, etc. here
            super.drawScreen(mouseX, mouseY, partialTicks);
        }

        @Override
        protected void actionPerformed(GuiButton b) {
            super.actionPerformed(b);
            /* "Done" button */
            if (b.id == 2000) {
                /* Syncing config */
                Waystones.getConfig().reloadLocal(Waystones.configuration);
                Waystones.configuration.save();
            }
        }
    }
}
