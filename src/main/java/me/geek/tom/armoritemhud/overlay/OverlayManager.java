package me.geek.tom.armoritemhud.overlay;

import com.google.common.collect.Lists;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;
import java.util.List;

import static me.geek.tom.armoritemhud.ArmorItemHud.MODID;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class OverlayManager {

    private static List<IOverlay> overlays = Lists.newArrayList();

    public static void registerOverlays(IOverlay... newOverlays) {
        overlays.addAll(Arrays.asList(newOverlays));
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType().equals(RenderGameOverlayEvent.ElementType.HOTBAR)) {
            render();
        }
    }

    private static void render() {
        for (IOverlay overlay : overlays)
            overlay.update();

        for (IOverlay overlay : overlays)
            overlay.render();
    }
}
