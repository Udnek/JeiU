package me.udnek.jeiu.item;

import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.customregistry.CustomRegistries;
import me.udnek.itemscoreu.util.ComponentU;
import me.udnek.jeiu.JeiU;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;

import java.util.ArrayList;

public class Items {
    public static final CustomItem HELP;
    public static final CustomItem INFORMATION = register(new TechnicalItem(Material.LEATHER_HELMET, "information_button", "gui.jeiu.information", 5104, false));
    public static final CustomItem FIRE_ICON = register(new TechnicalItem(Material.LEATHER_HELMET, "fire_icon", "", 5100, true));
    public static final CustomItem NEXT_BUTTON = register(new TechnicalItem(Material.LEATHER_HELMET, "next_button", "gui.jeiu.next", 5101, true));
    public static final CustomItem PREVIOUS_BUTTON = register(new TechnicalItem(Material.LEATHER_HELMET, "previous_button", "gui.jeiu.previous", 5102, true));
    public static final CustomItem BACK_BUTTON = register(new TechnicalItem(Material.LEATHER_HELMET, "back_button", "gui.jeiu.back", 5102, true));
    public static final CustomItem BANNER = register(new TechnicalItem(Material.LEATHER_HELMET, "banner", "", null, true));

    static {
        ArrayList<Component> lore = new ArrayList<>();

        lore.add(ComponentU.translatableWithInsertion("gui.jeiu.help.description.1", Component.keybind("key.mouse.left")
                .decoration(TextDecoration.ITALIC, false).decorate(TextDecoration.BOLD).color(TextColor.color(85, 255, 85))));
        lore.add(ComponentU.translatableWithInsertion("gui.jeiu.help.description.2", Component.keybind("key.mouse.right")
                .decoration(TextDecoration.ITALIC, false).decorate(TextDecoration.BOLD).color(TextColor.color(255, 85, 85))));
        for (int i = 3; i < 15; i++) {
            lore.add(Component.translatable("gui.jeiu.help.description."+i));
        }

        HELP = register(new TechnicalItem(Material.LEATHER_HELMET, "help_button", "gui.jeiu.help", 5103, false, lore));
    }

    private static CustomItem register(CustomItem customItem) {
        return CustomRegistries.ITEM.register(JeiU.getInstance(), customItem);
    }

}
