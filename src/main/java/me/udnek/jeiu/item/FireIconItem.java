package me.udnek.jeiu.item;

import me.udnek.itemscoreu.customitem.ConstructableCustomItem;
import org.bukkit.Material;

public class FireIconItem extends ConstructableCustomItem {

    @Override
    public Integer getCustomModelData() {
        return 1100;
    }

    @Override
    public String getRawId() {
        return "fire_icon";
    }

    @Override
    public Material getMaterial() {
        return Material.GUNPOWDER;
    }

    @Override
    public String getRawItemName() {
        return "";
    }

    @Override
    public boolean getHideTooltip() {
        return true;
    }
}
