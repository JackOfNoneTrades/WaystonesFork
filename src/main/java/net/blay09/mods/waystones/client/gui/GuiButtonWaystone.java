package net.blay09.mods.waystones.client.gui;

import net.blay09.mods.waystones.WaystoneConfig;
import net.blay09.mods.waystones.Waystones;
import net.blay09.mods.waystones.util.WaystoneEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

public class GuiButtonWaystone extends GuiButton {

    public static ResourceLocation orbResourceLocation = new ResourceLocation(
        Waystones.MODID,
        "textures/gui/xporb.png");

    private final WaystoneEntry waystone;
    private final int xpCost;

    public GuiButtonWaystone(int id, int x, int y, WaystoneEntry waystone, int xpCost) {
        super(id, x, y, (waystone.isGlobal() ? EnumChatFormatting.YELLOW : "") + waystone.getName());
        this.waystone = waystone;
        this.xpCost = xpCost;
    }

    public WaystoneEntry getWaystone() {
        return waystone;
    }

    /**
     * Draws a texture scaled to a target size.
     *
     * @param texture      ResourceLocation of the texture
     * @param x            Screen X
     * @param y            Screen Y
     * @param baseWidth    Actual texture width (px)
     * @param baseHeight   Actual texture height (px)
     * @param targetWidth  Desired on-screen width
     * @param targetHeight Desired on-screen height
     */
    public static void drawScaledTexture(ResourceLocation texture, int x, int y, int baseWidth, int baseHeight,
        int targetWidth, int targetHeight) {

        Minecraft.getMinecraft()
            .getTextureManager()
            .bindTexture(texture);
        Gui.func_146110_a(x, y, 0.0F, 0.0F, targetWidth, targetHeight, (float) baseWidth, (float) baseHeight);
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        super.drawButton(mc, mouseX, mouseY);

        if (WaystoneConfig.xpBaseCost > -1 && !Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode) {
            // Cost
            int color = (Minecraft.getMinecraft().thePlayer.experienceLevel >= xpCost) ? 0x55FF55 : 0xFF5555;
            String s = String.valueOf(xpCost);

            mc.fontRenderer.drawString(s, xPosition + width + 3, yPosition + 7, color);

            // XP orb
            drawScaledTexture(
                orbResourceLocation,
                xPosition + width + mc.fontRenderer.getStringWidth(s) + 5,
                yPosition + 6,
                8,
                8,
                8,
                8);
        }
    }
}
