package me.udnek.jeiu.item;

import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.customregistry.CustomRegistries;
import me.udnek.itemscoreu.utils.ComponentU;
import me.udnek.jeiu.JeiU;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;

import java.util.ArrayList;

public class Items {
    public static final CustomItem HELP;
    public static final CustomItem INFORMATION = register(new TechnicalItem(Material.GUNPOWDER, "information_button", "gui.jeiu.information", 1104, false));
    public static final CustomItem FIRE_ICON = register(new TechnicalItem(Material.GUNPOWDER, "fire_icon", "", 1100, true));
    public static final CustomItem NEXT_BUTTON = register(new TechnicalItem(Material.GUNPOWDER, "next_button", "gui.jeiu.next", 1101, true));
    public static final CustomItem PREVIOUS_BUTTON = register(new TechnicalItem(Material.GUNPOWDER, "previous_button", "gui.jeiu.previous", 1102, true));
    public static final CustomItem BACK_BUTTON = register(new TechnicalItem(Material.ARROW, "back_button", "gui.jeiu.back", 1102, true));
    public static final CustomItem BANNER = register(new TechnicalItem(Material.GUNPOWDER, "recipe_banner", "", null, true));

    static {
        ArrayList<Component> lore = new ArrayList<>();

        lore.add(ComponentU.translatableWithInsertion("gui.jeiu.help.description.1", Component.keybind("key.mouse.left")
                .decoration(TextDecoration.ITALIC, false).decorate(TextDecoration.BOLD).color(TextColor.color(85, 255, 85))));
        lore.add(ComponentU.translatableWithInsertion("gui.jeiu.help.description.2", Component.keybind("key.mouse.right")
                .decoration(TextDecoration.ITALIC, false).decorate(TextDecoration.BOLD).color(TextColor.color(255, 85, 85))));
        for (int i = 3; i < 15; i++) {
            lore.add(Component.translatable("gui.jeiu.help.description."+i));
        }

        HELP = register(new TechnicalItem(Material.GUNPOWDER, "help_button", "gui.jeiu.help", 1103, false, lore));
    }

    private static CustomItem register(CustomItem customItem) {
        return CustomRegistries.ITEM.register(JeiU.getInstance(), customItem);
    }

}
