package me.geek.tom.armoritemhud.overlay.armor;

import com.mojang.blaze3d.systems.RenderSystem;
import me.geek.tom.armoritemhud.overlay.ArmorItemOverlay;
import me.geek.tom.armoritemhud.overlay.IOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

public class ArmorSlotRenderer implements IOverlay {

    private int slot;
    private float startPosX;
    private float startPosY;
    private ArmorItemOverlay.ArmorItemSet armor;

    public ArmorSlotRenderer(int slot, float startPosX, float startPosY, ArmorItemOverlay.ArmorItemSet armor) {
        this.slot = slot;
        this.startPosX = startPosX;
        this.startPosY = startPosY;
        this.armor = armor;
    }

    @Override
    public void render() {
        RenderSystem.pushMatrix();

        ItemStack item = this.armor.get(this.slot);

        float ofset = this.slot * 10;

        RenderSystem.translatef(startPosX, startPosY + ofset, 0.0f);

        this.renderItemStack(item, 0, 0);

        RenderSystem.popMatrix();
    }

    @Override
    public void update() { }

    private void renderItemStack(ItemStack itm, int x, int y) {
        ItemRenderer itemRender = Minecraft.getInstance().getItemRenderer();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0f);

        if (!itm.isEmpty()) {
            RenderSystem.pushMatrix();
            RenderSystem.translatef(0.0F, 0.0F, 32.0F);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.enableRescaleNormal();
            RenderSystem.enableLighting();
            short short1 = 240;
            short short2 = 240;
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
}
