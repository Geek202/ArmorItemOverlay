package me.geek.tom.armoritemhud.overlay.armor;

import com.mojang.blaze3d.systems.RenderSystem;
import me.geek.tom.armoritemhud.overlay.ArmorItemOverlay;
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
        if (this.armor.get(this.slot).isEmpty())
            return;

        RenderSystem.pushMatrix();

        ItemStack item = this.armor.get(this.slot);

        float ofset = this.slot * ITEM_GUI_HEIGHT;

        RenderSystem.translatef(startPosX, startPosY + ofset, 0.0f);

        int nameWidth = this.nameWidth(item);

        fill(-1, -1, 17 + nameWidth + 3, 17, 0x88000000);

        this.renderName(item);
        this.renderDurBar(item, nameWidth);
        this.renderItemStack(item);

        RenderSystem.popMatrix();
    }

    @Override
    public void update() {
        float screenHeight = Minecraft.getInstance().getMainWindow().getScaledHeight();
        startPosY = screenHeight - (ITEM_GUI_HEIGHT * 4);
        startPosY -= 8;
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
        drawBar(barWidth, 3, 0xFF0000);
        drawBar(rescaledWidth, 3, 0xFF00);

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
        RenderSystem.disableAlphaTest();
        RenderSystem.disableBlend();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        int red = colour >> 16 & 255;
        int green = colour >> 8 & 255;
        int blue = colour & 255;

        this.draw(bufferbuilder, 0, 0, width, height, red, green, blue, 255);
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
