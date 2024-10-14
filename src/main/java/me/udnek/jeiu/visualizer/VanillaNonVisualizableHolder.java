package me.udnek.jeiu.visualizer;

import com.google.common.base.Preconditions;
import me.udnek.jeiu.util.Utils;
import me.udnek.jeiu.visualizer.abstraction.Visualizable;
import me.udnek.jeiu.visualizer.abstraction.Visualizer;
import org.bukkit.inventory.Recipe;
import org.bukkit.loot.LootTable;
import org.jetbrains.annotations.NotNull;

public class VanillaNonVisualizableHolder implements Visualizable {

    private Recipe recipe;
    private LootTable lootTable;
    public VanillaNonVisualizableHolder(@NotNull Recipe recipe) {
        Preconditions.checkArgument(Utils.isVanillaRecipe(recipe),"Can not visualize non-vanilla recipe");
        this.recipe = recipe;
    }
    public VanillaNonVisualizableHolder(@NotNull LootTable lootTable) {
        this.lootTable = lootTable;
    }
    @Override
    public @NotNull Visualizer getVisualizer() {
        if (recipe != null) return new VanillaRecipeVisualizer(recipe);
        return new LootTableVisualizer(lootTable);
    }
}
