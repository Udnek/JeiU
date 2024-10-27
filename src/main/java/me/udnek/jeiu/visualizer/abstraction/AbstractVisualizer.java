package me.udnek.jeiu.visualizer.abstraction;

import me.udnek.jeiu.menu.RecipesMenu;
import me.udnek.jeiu.util.RecipeChoiceAnimator;
import org.bukkit.inventory.RecipeChoice;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractVisualizer implements Visualizer{

    protected List<RecipeChoiceAnimator> animators = new ArrayList<>();
    protected RecipesMenu menu;
    @Override
    public void visualize(@NotNull RecipesMenu recipesMenu) {
        this.menu = recipesMenu;
    }
    @Override
    public void tickAnimation() {
        animators.forEach(animator -> menu.setItem(animator));
    }
    public void setChoice(int index, @Nullable RecipeChoice recipeChoice){
        animators.add(new RecipeChoiceAnimator(index, recipeChoice));
        menu.setItem(index, recipeChoice);
    }
}
