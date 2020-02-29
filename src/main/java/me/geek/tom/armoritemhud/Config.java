package me.geek.tom.armoritemhud;

import me.geek.tom.armoritemhud.overlay.EnumOverlayPosition;
import net.minecraftforge.common.ForgeConfigSpec;

public class Config {

    public static String CATEGORY_GENERAL = "general";

    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec CLIENT_CONFIG;

    public static ForgeConfigSpec.EnumValue<EnumOverlayPosition> OVERLAY_POS;
    public static ForgeConfigSpec.BooleanValue SHOW_OVERLAY;

    static {

        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
        ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

        CLIENT_BUILDER.comment("General settings").push(CATEGORY_GENERAL);

        OVERLAY_POS = CLIENT_BUILDER.comment("The position of the armor overlay").defineEnum("overlayPosition", EnumOverlayPosition.BOTTOMLEFT);
        SHOW_OVERLAY = CLIENT_BUILDER.comment("Show the overlay?").define("showOverlay", true);

        CLIENT_BUILDER.pop();

        COMMON_CONFIG = COMMON_BUILDER.build();
        CLIENT_CONFIG = CLIENT_BUILDER.build();
    }

}
