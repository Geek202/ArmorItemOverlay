package me.geek.tom.armoritemhud;


import me.geek.tom.armoritemhud.overlay.EnumOverlayPosition;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static me.geek.tom.armoritemhud.ArmorItemHud.MODID;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PositionEditScreen extends Screen {

    private static final int WIDTH = 179;
    private static final int HEIGHT = 151;

    private PositionEditScreen() {
        super(new StringTextComponent(""));
    }

    private static void open(Button button) {
        Minecraft.getInstance().displayGuiScreen(new PositionEditScreen());
    }

    @Override
    protected void init() {
        int relX = (this.width - WIDTH) / 2;
        int relY = (this.height - HEIGHT) / 2;

        addButton(new Button(relX + 10, relY + 20, 100, 20, new StringTextComponent("Top left"), button -> setPos(EnumOverlayPosition.TOPLEFT)));
        addButton(new Button(relX + 10, relY + 40, 100, 20, new StringTextComponent("Top Right"), button -> setPos(EnumOverlayPosition.TOPRIGHT)));
        addButton(new Button(relX + 10, relY + 60, 100, 20, new StringTextComponent("Top Center"), button -> setPos(EnumOverlayPosition.TOPCENTER)));
        addButton(new Button(relX + 10, relY + 80, 100, 20, new StringTextComponent("Bottom Left"), button -> setPos(EnumOverlayPosition.BOTTOMLEFT)));
        addButton(new Button(relX + 10, relY + 100, 100, 20, new StringTextComponent("Bottom Right"), button -> setPos(EnumOverlayPosition.BOTTOMRIGHT)));

        addButton(new Button(relX + 10, relY + 120, 100, 20, new StringTextComponent("Toggle Display"), this::toggle));
    }

    private void toggle(Button btn) {
        Config.SHOW_OVERLAY.set(!Config.SHOW_OVERLAY.get());
    }

    private void setPos(EnumOverlayPosition newPos) {
        Config.OVERLAY_POS.set(newPos);
    }

    @SubscribeEvent
    public static void onScreenOpen(GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.getGui() instanceof InventoryScreen)
        event.addWidget(new Button(5, 5, 25, 20, new StringTextComponent("AIO"), PositionEditScreen::open));
    }
}
