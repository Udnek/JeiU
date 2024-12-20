package me.udnek.jeiu.util;

import me.udnek.jeiu.visualizer.VanillaNonVisualizableHolder;
import me.udnek.jeiu.visualizer.abstraction.Visualizable;
import org.bukkit.inventory.*;
import org.bukkit.loot.LootTable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Utils {

    public static boolean isVanillaRecipe(@NotNull Recipe recipe) {

        return switch (recipe) {
            case ShapedRecipe ignored -> true;
            case ShapelessRecipe ignored -> true;
            case FurnaceRecipe ignored -> true;
            case BlastingRecipe ignored -> true;
            case SmokingRecipe ignored -> true;
            case CampfireRecipe ignored -> true;
            case StonecuttingRecipe ignored -> true;
            case SmithingTrimRecipe ignored -> true;
            case SmithingTransformRecipe ignored -> true;
            case TransmuteRecipe ignored -> true;
            default -> false;
        };

    }

    public static void toVisualizables(@NotNull List<Recipe> recipes, @NotNull List<LootTable> lootTables, @NotNull Consumer<Visualizable> consumer){
        List<Visualizable> result = new ArrayList<>();

        for (LootTable lootTable : lootTables) {
            if (lootTable instanceof Visualizable visualizable) result.add(visualizable);
            else result.add(new VanillaNonVisualizableHolder(lootTable));
        }

        for (Recipe recipe : recipes) {
            if (Utils.isVanillaRecipe(recipe) && !(recipe instanceof Visualizable)){
                if (recipe instanceof SmithingTrimRecipe) result.add(new VanillaNonVisualizableHolder(recipe));
                else result.addFirst(new VanillaNonVisualizableHolder(recipe));
            }
            else if (recipe instanceof Visualizable visualizable) result.addFirst(visualizable);
        }


        result.forEach(consumer);
    }
}
