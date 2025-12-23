package me.udnek.jeiu.visualizer.implementation;

import me.udnek.jeiu.item.BannerItem;
import me.udnek.jeiu.item.LootTableIconItem;
import me.udnek.jeiu.item.StructureIconItem;
import me.udnek.jeiu.menu.RecipesMenu;
import me.udnek.jeiu.util.StructureCache;
import me.udnek.jeiu.visualizer.Visualizer;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.generator.structure.Structure;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class StructureSetVIsualizer implements Visualizer {

    protected List<NamespacedKey> ids;

    public StructureSetVIsualizer(@NotNull List<NamespacedKey> structureIds){
        this.ids = structureIds;
    }

    @Override
    public void visualize(@NotNull RecipesMenu recipesMenu) {
        recipesMenu.setThemedItem(RecipesMenu.getBannerPosition(), BannerItem.withModel(BannerItem.BIG_LOOT_TABLE));
        //recipesMenu.setItem(RecipesMenu.getRecipeStationPosition(), recipesMenu.getQuery().getItemStack());

        int collum = 0;
        int row = 0;
        final int xSize = 8;
        for (int i = 0; i < Math.min(6*xSize, ids.size()); i++) {
            NamespacedKey structureId = ids.get(i);
            ItemStack icon = StructureIconItem.withStructure(structureId);

            recipesMenu.setItem(row * 9 + collum + RecipesMenu.VISUALIZER_X_OFFSET, icon);
            collum++;
            if (collum % xSize == 0) {
                collum = 0;
                row++;
            }
        }
    }

    @Override
    public @Nullable List<Component> getInformation() {
        return null;
        //return List.of(Component.text("ID: " + id.asString()));
    }

    @Override
    public void tickAnimation() {}
}
