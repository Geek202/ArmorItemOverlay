package me.geek.tom.armoritemhud.overlay.armor;

import com.mojang.blaze3d.systems.RenderSystem;
import me.geek.tom.armoritemhud.overlay.ArmorItemOverlay;
import me.geek.tom.armoritemhud.overlay.IOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;

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

        this.renderItemStack(item, 0, 0);

        int nameWidth = this.renderName(item);

        fill(-1, -1, 17 + nameWidth + 3, 17, 0x88000000);

        this.renderDurBar(item, nameWidth);

        RenderSystem.popMatrix();
    }

    @Override
    public void update() {
        float screenHeight = Minecraft.getInstance().getMainWindow().getScaledHeight();
        startPosY = screenHeight - (ITEM_GUI_HEIGHT * 4);
        startPosY -= 8;
    }

    private void renderItemStack(ItemStack itm, int x, int y) {
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
                itemRender.renderItemAndEffectIntoGUI(itm, x, y);
            } catch (Exception ignored) {
            }
            RenderSystem.popMatrix();
            RenderSystem.disableRescaleNormal();
            RenderSystem.disableLighting();
        }
    }

    private int renderName(ItemStack item) {
        FontRenderer font = Minecraft.getInstance().fontRenderer;

        RenderSystem.pushMatrix();

        String name = item.getDisplayName().getString();

        RenderSystem.translatef(17, 0, 0);
        font.drawStringWithShadow(name, 0, 2, 0xAAAAAA);

        RenderSystem.popMatrix();

        return font.getStringWidth(name);
    }

    private void renderDurBar(ItemStack item, int barWidth) {
        RenderSystem.pushMatrix();

        int maxDurability = item.getMaxDamage();
        int currentDurability = item.getDamage();

        RenderSystem.translatef(17, 10, 0);

        int rescaledWidth = rescale(currentDurability, 0, maxDurability, 0, barWidth);

        fill(0, 0, barWidth, 5, 0xFF0000);
        fill(0, 0, rescaledWidth, 5, 0xFF00); // @TODO Fix bar not showing?

        RenderSystem.popMatrix();
    }

    private int rescale(int val, int currentMin, int currentMax, int newMin, int newMax) {
        float unscaledDif = val - currentMin;
        float currentWidth = currentMax - currentMin;
        float ratio = unscaledDif / currentWidth;

        float scaledWidth = newMax - newMin;
        float scaledDif = ratio * scaledWidth;
        float newVal = newMin + scaledDif;

        return (int) newVal;
    }
}
