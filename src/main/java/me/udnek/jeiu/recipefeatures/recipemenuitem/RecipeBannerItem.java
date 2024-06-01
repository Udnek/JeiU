package me.udnek.jeiu.recipefeatures.recipemenuitem;

import me.udnek.itemscoreu.customitem.CustomItem;
import org.bukkit.Material;

public class RecipeBannerItem extends CustomItem {

    @Override
    public Material getMaterial() {
        return Material.GUNPOWDER;
    }

    @Override
    protected String getRawDisplayName() {
        return "";
    }

    @Override
    protected String getItemName() {
        return "recipe_banner";
    }
}
