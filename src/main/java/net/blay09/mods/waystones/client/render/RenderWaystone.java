package net.blay09.mods.waystones.client.render;

import static org.lwjgl.opengl.GL11.*;

import net.blay09.mods.waystones.PlayerWaystoneData;
import net.blay09.mods.waystones.WaystoneConfig;
import net.blay09.mods.waystones.WaystoneManager;
import net.blay09.mods.waystones.Waystones;
import net.blay09.mods.waystones.block.TileWaystone;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

public class RenderWaystone extends TileEntitySpecialRenderer {

    private static final ResourceLocation texture = new ResourceLocation(
        Waystones.MODID,
        "textures/entity/waystone.png");
    private static final ResourceLocation textureActive = new ResourceLocation(
        Waystones.MODID,
        "textures/entity/waystone_active.png");

    private static final ResourceLocation[] activeTextures = {
        new ResourceLocation(Waystones.MODID, "textures/entity/waystone_active_1.png"),
        new ResourceLocation(Waystones.MODID, "textures/entity/waystone_active_2.png"),
        new ResourceLocation(Waystones.MODID, "textures/entity/waystone_active_3.png"),
        new ResourceLocation(Waystones.MODID, "textures/entity/waystone_active_4.png"),
        new ResourceLocation(Waystones.MODID, "textures/entity/waystone_active_5.png"),
        new ResourceLocation(Waystones.MODID, "textures/entity/waystone_active_6.png") };

    private static final ResourceLocation textureNonActive = new ResourceLocation(
        Waystones.MODID,
        "textures/entity/waystone_nonactive.png");

    private final ModelWaystone model = new ModelWaystone();

    float getCooldownProgress(TileWaystone tileWaystone) {
        if (!tileWaystone.hasWorldObj()) return 1f; // fully charged if not in world

        long lastUse = PlayerWaystoneData.getLastWarpStoneUse(Minecraft.getMinecraft().thePlayer);
        long cooldown = Waystones.getConfig().warpStoneCooldown * 1000L;
        long timeSince = System.currentTimeMillis() - lastUse;
        return Math.min(1f, Math.max(0f, (float) timeSince / cooldown));
    }

    public static int normalizeToFive(float x) {
        return Math.round(x * 5);
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float partialTicks) {
        TileWaystone tileWaystone = (TileWaystone) tileEntity;
        boolean stoneIsKnown = WaystoneManager.getKnownWaystone(tileWaystone.getWaystoneName()) != null
            || WaystoneManager.getServerWaystone(tileWaystone.getWaystoneName()) != null;
        bindTexture(texture);

        float angle = tileEntity.hasWorldObj()
            ? WaystoneManager.getRotationYaw(ForgeDirection.getOrientation(tileEntity.getBlockMetadata()))
            : 0f;
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glColor4f(1f, 1f, 1f, 1f);
        GL11.glTranslated(x + 0.5, y, z + 0.5);
        GL11.glRotatef(angle, 0f, 1f, 0f);
        GL11.glRotatef(-180f, 1f, 0f, 0f);
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        GL11.glEnable(GL_BLEND);
        GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        model.renderAll();
        if (tileWaystone.hasWorldObj() && stoneIsKnown) {
            GL11.glScalef(1.05f, 1.05f, 1.05f);

            GL11.glDisable(GL11.GL_CULL_FACE); // render all faces

            // Render nonactive pillar normally (with lighting)
            bindTexture(textureNonActive);
            GL11.glEnable(GL11.GL_LIGHTING); // ensure lighting is on
            Minecraft.getMinecraft().entityRenderer.enableLightmap(0);
            model.renderPillar();

            // Render active pillar with glow (lighting off)
            if (!WaystoneConfig.disableTextGlow) {
                GL11.glDisable(GL11.GL_LIGHTING);
                Minecraft.getMinecraft().entityRenderer.disableLightmap(0);
            }
            bindTexture(activeTextures[normalizeToFive(getCooldownProgress(tileWaystone))]);
            model.renderPillar();
            if (!WaystoneConfig.disableTextGlow) {
                GL11.glEnable(GL11.GL_LIGHTING);
                Minecraft.getMinecraft().entityRenderer.enableLightmap(0);
            }

            GL11.glEnable(GL11.GL_CULL_FACE); // restore culling

        }
        GL11.glDisable(GL_BLEND);
        GL11.glPopMatrix();

        if (WaystoneConfig.showNametag && tileWaystone.hasWorldObj() && stoneIsKnown) {
            renderWaystoneName(tileWaystone, x + 0.5, y + 2.5, z + 0.5);
        }
    }

    private void renderWaystoneName(TileWaystone tile, double x, double y, double z) {
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        String name = tile.getWaystoneName();

        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);

        // Face the player
        GL11.glRotatef(-RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(RenderManager.instance.playerViewX, 1.0F, 0.0F, 0.0F);

        float scale = 0.01666667F * 1.6F; // adjust size
        GL11.glScalef(-scale, -scale, scale);

        // Draw background
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        int width = fontRenderer.getStringWidth(name) / 2;
        Tessellator tess = Tessellator.instance;
        tess.startDrawingQuads();
        tess.setColorRGBA_F(0f, 0f, 0f, 0.5f); // semi-transparent black
        tess.addVertex(-width - 1, -1, 0);
        tess.addVertex(-width - 1, 8, 0);
        tess.addVertex(width + 1, 8, 0);
        tess.addVertex(width + 1, -1, 0);
        tess.draw();

        // Draw text
        fontRenderer.drawString(name, -width, 0, 0xFFFFFF); // white
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);

        GL11.glPopMatrix();
    }

}
