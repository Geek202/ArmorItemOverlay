package me.geek.tom.armoritemhud.overlay;

import com.mojang.blaze3d.matrix.MatrixStack;

public interface IOverlay {
    void render(MatrixStack stack);
    void update();
}
