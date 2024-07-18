package me.udnek.jeiu.recipe_feature.item;

import me.udnek.itemscoreu.customitem.ConstructableCustomItem;
import org.bukkit.Material;

public class RecipeBannerItem extends ConstructableCustomItem {

    @Override
    public String getRawId() {
        return "recipe_banner";
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
