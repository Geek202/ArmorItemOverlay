package me.geek.tom.armoritemhud.overlay;

import com.mojang.blaze3d.matrix.MatrixStack;
import me.geek.tom.armoritemhud.overlay.armor.ArmorSlotRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import static me.geek.tom.armoritemhud.overlay.armor.ArmorSlotRenderer.ITEM_GUI_HEIGHT;

public class ArmorItemOverlay implements IOverlay {

    private ArmorItemSet armor = new ArmorItemSet(
            ItemStack.EMPTY,
            ItemStack.EMPTY,
            ItemStack.EMPTY,
            ItemStack.EMPTY
    );

    public ArmorItemOverlay() {

        float screenHeight = Minecraft.getInstance().getMainWindow().getScaledHeight();
        float startPosY = screenHeight - (ITEM_GUI_HEIGHT * 4);

        OverlayManager.registerOverlays(
                new ArmorSlotRenderer(0, 10, startPosY - 10, this.armor),
                new ArmorSlotRenderer(1, 10, startPosY - 10, this.armor),
                new ArmorSlotRenderer(2, 10, startPosY - 10, this.armor),
                new ArmorSlotRenderer(3, 10, startPosY - 10, this.armor)
        );
    }

    @Override
    public void render(MatrixStack stack) {

    }

    @Override
    public void update() {
        this.updateArmor();
    }

    private void updateArmor() {
        ClientPlayerEntity player = Minecraft.getInstance().player;

        assert player != null;
        NonNullList<ItemStack> playerArmor = player.inventory.armorInventory;

        armor.setHead(playerArmor.get(3));
        armor.setChest(playerArmor.get(2));
        armor.setLegs(playerArmor.get(1));
        armor.setFeet(playerArmor.get(0));
    }

    public static class ArmorItemSet {
        private ItemStack[] armor = new ItemStack[4];

        public ArmorItemSet(ItemStack head, ItemStack chest, ItemStack legs, ItemStack feet) {
            armor[0] = head;
            armor[1] = chest;
            armor[2] = legs;
            armor[3] = feet;
        }

        public ItemStack getHead() {
            return armor[0];
        }

        public ItemStack getChest() {
            return armor[1];
        }

        public ItemStack getLegs() {
            return armor[2];
        }

        public ItemStack getFeet() {
            return armor[3];
        }

        public void setHead(ItemStack newItem) {
            armor[0] = newItem;
        }

        public void setChest(ItemStack newItem) {
            armor[1] = newItem;
        }

        public void setLegs(ItemStack newItem) {
            armor[2] = newItem;
        }

        public void setFeet(ItemStack newItem) {
            armor[3] = newItem;
        }

        public ItemStack get(int idx) {
            return armor[idx];
        }
    }
}
