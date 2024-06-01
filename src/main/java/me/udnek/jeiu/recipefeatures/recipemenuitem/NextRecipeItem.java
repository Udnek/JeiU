package me.udnek.jeiu.recipefeatures.recipemenuitem;

import me.udnek.itemscoreu.customitem.CustomModelDataItem;
import org.bukkit.Material;

public class NextRecipeItem extends CustomModelDataItem {

    @Override
    public Material getMaterial() {
        return Material.GUNPOWDER;
    }


    @Override
    protected String getRawDisplayName() {
        return "gui.jeiu.arrow_right_active";
    }

    @Override
    protected String getItemName() {
        return "next_recipe_button";
    }

    @Override
    public int getCustomModelData() {
        return 1101;
    }
}
