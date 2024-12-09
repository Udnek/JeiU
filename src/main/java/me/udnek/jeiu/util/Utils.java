package me.udnek.jeiu.util;

import me.udnek.jeiu.visualizer.VanillaNonVisualizableHolder;
import me.udnek.jeiu.visualizer.abstraction.Visualizable;
import org.bukkit.inventory.*;
import org.bukkit.loot.LootTable;

import java.util.List;
import java.util.function.Consumer;

public class Utils {

    public static boolean isVanillaRecipe(Recipe recipe) {

        if (recipe instanceof ShapedRecipe) return true;
        if (recipe instanceof ShapelessRecipe) return true;

        if (recipe instanceof FurnaceRecipe) return true;
        if (recipe instanceof BlastingRecipe) return true;
        if (recipe instanceof SmokingRecipe) return true;
        if (recipe instanceof CampfireRecipe) return true;

        if (recipe instanceof StonecuttingRecipe) return true;

        if (recipe instanceof SmithingTrimRecipe) return true;
        if (recipe instanceof SmithingTransformRecipe) return true;

        if (recipe instanceof TransmuteRecipe) return true;

        return false;
    }

    public static void toVisualizables(List<Recipe> recipes, List<LootTable> lootTables, Consumer<Visualizable> consumer){
        for (Recipe recipe : recipes) {
            if (recipe instanceof SmithingTrimRecipe) continue;
            if (recipe instanceof Visualizable visualizable) consumer.accept(visualizable);
            else if (Utils.isVanillaRecipe(recipe)) consumer.accept(new VanillaNonVisualizableHolder(recipe));
        }
        for (LootTable lootTable : lootTables) {
            if (lootTable instanceof Visualizable visualizable) consumer.accept(visualizable);
            else consumer.accept(new VanillaNonVisualizableHolder(lootTable));
        }
    }
}
