package me.udnek.jeiu.recipe_feature.item;

import me.udnek.itemscoreu.customitem.CustomItem;
import org.bukkit.Material;

public class FireIconItem extends CustomItem {

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
    public String getRawItemName() {return "";}

    @Override
    public boolean getHideTooltip() {return true;}
}
