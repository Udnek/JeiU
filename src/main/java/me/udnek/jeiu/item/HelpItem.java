package me.udnek.jeiu.item;

import me.udnek.itemscoreu.customitem.ConstructableCustomItem;
import me.udnek.itemscoreu.utils.ComponentU;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class HelpItem extends ConstructableCustomItem {

    @Override
    public List<Component> getLore() {

        ArrayList<Component> lore = new ArrayList<Component>();

        lore.add(ComponentU.translatableWithInsertion("gui.jeiu.help.description.1", Component.keybind("key.mouse.left")
                .decoration(TextDecoration.ITALIC, false).decorate(TextDecoration.BOLD).color(TextColor.color(85, 255, 85))));
        lore.add(ComponentU.translatableWithInsertion("gui.jeiu.help.description.2", Component.keybind("key.mouse.right")
                .decoration(TextDecoration.ITALIC, false).decorate(TextDecoration.BOLD).color(TextColor.color(255, 85, 85))));
        lore.add(Component.translatable("gui.jeiu.help.description.3"));
        lore.add(Component.translatable("gui.jeiu.help.description.4"));
        lore.add(Component.translatable("gui.jeiu.help.description.5"));
        lore.add(Component.translatable("gui.jeiu.help.description.6"));
        lore.add(Component.translatable("gui.jeiu.help.description.7"));
        lore.add(Component.translatable("gui.jeiu.help.description.8"));
        lore.add(Component.translatable("gui.jeiu.help.description.9"));
        lore.add(Component.translatable("gui.jeiu.help.description.10"));
        lore.add(Component.translatable("gui.jeiu.help.description.11"));
        lore.add(Component.translatable("gui.jeiu.help.description.12"));
        lore.add(Component.translatable("gui.jeiu.help.description.13"));
        lore.add(Component.translatable("gui.jeiu.help.description.14"));

        return lore;
    }

    @Override
    public Material getMaterial() {
        return Material.GUNPOWDER;
    }

    @Override
    public String getRawId() {
        return "help_button";
    }

    @Override
    public String getRawItemName() {
        return "gui.jeiu.help";
    }

    @Override
    public Integer getCustomModelData() {
        return 1104;
    }
}
