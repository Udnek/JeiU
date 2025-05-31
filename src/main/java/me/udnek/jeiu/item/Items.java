package me.udnek.jeiu.item;

import me.udnek.coreu.custom.item.CustomItem;
import me.udnek.coreu.custom.registry.CustomRegistries;
import me.udnek.coreu.util.ComponentU;
import me.udnek.jeiu.JeiU;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.ArrayList;
import java.util.List;

public class Items {
    public static final CustomItem HELP;
    public static final CustomItem FIRE_ICON = register(new TechnicalItem("fire_icon",  true));
    public static final CustomItem NEXT = register(new TechnicalItem("next", true));
    public static final CustomItem PREVIOUS = register(new TechnicalItem("previous", true));
    public static final CustomItem BACK = register(new TechnicalItem("back", true));
    public static final CustomItem BANNER = register(new BannerItem());
    public static final CustomItem SWITCH = register(new SwitchItem());

    static {
        List<Component> lore = new ArrayList<>();

        lore.add(ComponentU.translatableWithInsertion("gui.jeiu.help.description.1", Component.keybind("key.mouse.left")
                .decoration(TextDecoration.ITALIC, false).decorate(TextDecoration.BOLD).color(TextColor.color(85, 255, 85))));
        lore.add(ComponentU.translatableWithInsertion("gui.jeiu.help.description.2", Component.keybind("key.mouse.right")
                .decoration(TextDecoration.ITALIC, false).decorate(TextDecoration.BOLD).color(TextColor.color(255, 85, 85))));
        for (int i = 3; i < 15; i++) {
            lore.add(Component.translatable("gui.jeiu.help.description."+i));
        }

        HELP = register(new TechnicalItem("help", false, lore));
    }

    private static CustomItem register(CustomItem customItem) {
        return CustomRegistries.ITEM.register(JeiU.getInstance(), customItem);
    }

}
