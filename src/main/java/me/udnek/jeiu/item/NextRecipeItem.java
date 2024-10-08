package me.udnek.jeiu.item;

import me.udnek.itemscoreu.customitem.ConstructableCustomItem;
import org.bukkit.Material;

public class NextRecipeItem extends ConstructableCustomItem {

    @Override
    public String getRawId() {
        return "next_recipe_button";
    }

    @Override
    public Material getMaterial() {
        return Material.GUNPOWDER;
    }

    @Override
    public String getRawItemName() {
        return "gui.jeiu.arrow_right_active";
    }

    @Override
    public Integer getCustomModelData() {
        return 1101;
    }
}
