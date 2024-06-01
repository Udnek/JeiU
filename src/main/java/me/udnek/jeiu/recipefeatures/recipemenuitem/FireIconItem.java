package me.udnek.jeiu.recipefeatures.recipemenuitem;

import me.udnek.itemscoreu.customitem.CustomModelDataItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

public class FireIconItem extends CustomModelDataItem {

    @Override
    public int getCustomModelData() {
        return 1100;
    }

    @Override
    public Material getMaterial() {
        return Material.GUNPOWDER;
    }

    @Override
    protected String getRawDisplayName() {
        return "";
    }

    @Override
    public Component getDisplayName() {
        return Component.empty();
    }

    @Override
    protected String getItemName() {
        return "fire_icon";
    }
}
