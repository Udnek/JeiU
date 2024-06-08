package me.udnek.jeiu.recipe_feature.visualizer;

import me.udnek.jeiu.recipe_feature.RecipesMenu;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

public interface AbstractRecipeVisualizer{

    void visualize(RecipesMenu recipesMenu, Recipe recipe, ItemStack targetItem);

}
