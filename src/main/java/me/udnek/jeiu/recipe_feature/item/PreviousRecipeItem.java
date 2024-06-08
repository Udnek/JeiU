package me.udnek.jeiu.recipe_feature.item;

import me.udnek.itemscoreu.customitem.CustomItem;
import org.bukkit.Material;

public class PreviousRecipeItem extends CustomItem {

    @Override
    public String getRawId() {
        return "previous_recipe_button";
    }

    @Override
    public Material getMaterial() {
        return Material.GUNPOWDER;
    }

    @Override
    public String getRawItemName() {
        return "gui.jeiu.arrow_left_active";
    }
    @Override
    public Integer getCustomModelData() {
        return 1102;
    }
}
