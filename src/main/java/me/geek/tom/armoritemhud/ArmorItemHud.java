package me.geek.tom.armoritemhud;

import me.geek.tom.armoritemhud.overlay.ArmorItemOverlay;
import me.geek.tom.armoritemhud.overlay.OverlayManager;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.StartupMessageManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ArmorItemHud.MODID)
public class ArmorItemHud {

    public static final String MODID = "ait";

    public ArmorItemHud() {
        StartupMessageManager.addModMessage("ArmorItemHud::construct");

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::init);
    }

    private void init(FMLCommonSetupEvent event) {
        StartupMessageManager.addModMessage("ArmorItemHud::init");

        OverlayManager.registerOverlays(
                new ArmorItemOverlay()
        );
    }
}
