package me.udnek.jeiu.recipefeatures.visualizer;

import me.udnek.jeiu.recipefeatures.RecipesMenu;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

public interface AbstractRecipeVisualizer{

    void visualize(RecipesMenu recipesMenu, Recipe recipe, ItemStack targetItem);

}
