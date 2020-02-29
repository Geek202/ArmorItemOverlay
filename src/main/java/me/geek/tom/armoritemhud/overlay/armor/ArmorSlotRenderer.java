package me.geek.tom.armoritemhud.overlay.armor;

import com.mojang.blaze3d.systems.RenderSystem;
import me.geek.tom.armoritemhud.Config;
import me.geek.tom.armoritemhud.overlay.ArmorItemOverlay;
import me.geek.tom.armoritemhud.overlay.EnumOverlayPosition;
import me.geek.tom.armoritemhud.overlay.IOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;
import org.apache.commons.lang3.tuple.Pair;

@SuppressWarnings("SameParameterValue")
public class ArmorSlotRenderer extends Screen implements IOverlay {

    private int slot;
    private float startPosX;
    private float startPosY;
    private ArmorItemOverlay.ArmorItemSet armor;

    public static final int ITEM_GUI_HEIGHT = 20;

    public ArmorSlotRenderer(int slot, float startPosX, float startPosY, ArmorItemOverlay.ArmorItemSet armor) {
        super(new StringTextComponent(""));
        this.slot = slot;
        this.startPosX = startPosX;
        this.startPosY = startPosY;
        this.armor = armor;
    }

    @Override
    public void render() {
        if (this.armor.get(this.slot).isEmpty() || !Config.SHOW_OVERLAY.get())
            return;

        RenderSystem.pushMatrix();

        ItemStack item = this.armor.get(this.slot);

        // RenderSystem.translatef(startPosX, startPosY + ofset, 0.0f);

        int nameWidth = this.nameWidth(item);

        int boxWidth = 17 + nameWidth + 3;
        int boxHeight = 17;
        preconfigureRender(boxWidth, boxHeight);

        fill(-1, -1, boxWidth, boxHeight, 0x88000000);

        this.renderName(item);
        this.renderDurBar(item, nameWidth);
        this.renderItemStack(item);

        RenderSystem.popMatrix();
    }

    private void preconfigureRender(int boxWidth, int boxHeight) {
        EnumOverlayPosition overlayPos = Config.OVERLAY_POS.get();

        int ofset = ITEM_GUI_HEIGHT * 3;
        if (overlayPos.equals(EnumOverlayPosition.BOTTOMLEFT) ||
                overlayPos.equals(EnumOverlayPosition.BOTTOMRIGHT)) {
            ofset *= -1;
        }

        int posOfset = (3 - this.slot) * ITEM_GUI_HEIGHT;

        Pair<Integer, Integer> pos = overlayPos.toScreenCoords(boxWidth, boxHeight, 0, ofset - posOfset);
        int x = pos.getLeft();
        int y = pos.getRight();

        RenderSystem.translatef(x, y, 0);
    }

    @Override
    public void update() {
        // float screenHeight = Minecraft.getInstance().getMainWindow().getScaledHeight();
        // startPosY = screenHeight - (ITEM_GUI_HEIGHT * 4);
        // startPosY -= 8;
    }

    private void renderItemStack(ItemStack itm) {
        ItemRenderer itemRender = Minecraft.getInstance().getItemRenderer();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0f);

        if (!itm.isEmpty()) {
            RenderSystem.pushMatrix();
            RenderSystem.translatef(0.0F, 0.0F, 32.0F);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.enableRescaleNormal();
            RenderSystem.enableLighting();
            RenderHelper.setupGui3DDiffuseLighting();
            try {
                itemRender.renderItemAndEffectIntoGUI(itm, 0, 0);
            } catch (Exception ignored) {
            }
            RenderSystem.popMatrix();
            RenderSystem.disableRescaleNormal();
            RenderSystem.disableLighting();
        }
    }

    private int nameWidth(ItemStack item) {
        return Minecraft.getInstance().fontRenderer.getStringWidth(item.getDisplayName().getString());
    }

    private void renderName(ItemStack item) {
        FontRenderer font = Minecraft.getInstance().fontRenderer;

        RenderSystem.pushMatrix();

        String name = item.getDisplayName().getString();

        RenderSystem.translatef(18, 0, 0);
        font.drawStringWithShadow(name, 0, 2, 0xAAAAAA);

        RenderSystem.popMatrix();
    }

    private void renderDurBar(ItemStack item, int barWidth) {
        RenderSystem.pushMatrix();

        int maxDurability = item.getMaxDamage();
        int currentDurability = item.getDamage();

        RenderSystem.translatef(17, 12, 0);

        int rescaledWidth = rescale(currentDurability, maxDurability, barWidth);

        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 0.0f);
        drawBar(barWidth, 3, 0x88AA0000);
        drawBar(rescaledWidth, 3, 0xFF00AA00);

        RenderSystem.popMatrix();
    }

    private int rescale(int val, int currentMax, int newMax) {
        float ratio = (float) val / (float) currentMax;

        float scaledDif = (1 - ratio) * (float) newMax;

        return (int) scaledDif;
    }

    private void drawBar(int width, int height, int colour) {
        RenderSystem.disableDepthTest();
        RenderSystem.disableTexture();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        int red = colour >> 16 & 255;
        int green = colour >> 8 & 255;
        int blue = colour & 255;
        int alpha = colour >> 24 & 255;

        this.draw(bufferbuilder, 0, 0, width, height, red, green, blue, alpha);

        RenderSystem.enableTexture();
        RenderSystem.enableDepthTest();
    }

    private void draw(BufferBuilder renderer, int x, int y, int width, int height, int red, int green, int blue, int alpha) {
        renderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        renderer.pos(x, y, 0.0D).color(red, green, blue, alpha).endVertex();
        renderer.pos(x, y + height, 0.0D).color(red, green, blue, alpha).endVertex();
        renderer.pos(x + width, y + height, 0.0D).color(red, green, blue, alpha).endVertex();
        renderer.pos(x + width, y, 0.0D).color(red, green, blue, alpha).endVertex();
        Tessellator.getInstance().draw();
    }
}
