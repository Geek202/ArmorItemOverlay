package me.geek.tom.armoritemhud.overlay;

import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.tuple.Pair;

@SuppressWarnings("SuspiciousNameCombination")
public enum EnumOverlayPosition {
    TOPLEFT,
    TOPRIGHT,
    BOTTOMLEFT,
    BOTTOMRIGHT,
    TOPCENTER;

    public Pair<Integer, Integer> toScreenCoords(int width, int height, int xOfset, int yOfset) {
        int x, y;

        int screenWidth = Minecraft.getInstance().getMainWindow().getScaledWidth();
        int screenHeight = Minecraft.getInstance().getMainWindow().getScaledHeight();

        switch (this) {
            case TOPLEFT:
                x = 10;
                y = 10;
                break;
            case TOPRIGHT:
                x = (screenWidth - 10) - width;
                y = 10;
                break;
            case BOTTOMLEFT:
                x = 10;
                y = (screenHeight - 10) - height;
                break;
            case BOTTOMRIGHT:
                x = (screenWidth - 10) - width;
                y = (screenHeight - 10) - height;
                break;
            case TOPCENTER:
                int screenCenterPosition = screenWidth / 2;
                x = screenCenterPosition - (width / 2);
                y = 10;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + this);
        }

        x += xOfset;
        y += yOfset;

        return Pair.of(x, y);
    }
}
