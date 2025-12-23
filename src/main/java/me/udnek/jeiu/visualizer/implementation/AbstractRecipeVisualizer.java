package me.udnek.jeiu.visualizer.implementation;

import me.udnek.jeiu.menu.RecipesMenu;
import me.udnek.jeiu.visualizer.Visualizer;
import me.udnek.jeiu.visualizer.animator.Animator;
import me.udnek.jeiu.visualizer.animator.RecipeChoiceAnimator;
import org.bukkit.inventory.RecipeChoice;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractRecipeVisualizer implements Visualizer {

    protected List<Animator> animators = new ArrayList<>();
    protected RecipesMenu menu;
    @Override
    @OverridingMethodsMustInvokeSuper
    public void visualize(@NotNull RecipesMenu recipesMenu) {
        this.menu = recipesMenu;
    }
    @Override
    public void tickAnimation() {
        for (Animator animator : animators) {
            menu.setItem(animator.getPosition(), animator.getNextFrame());
        }
    }
    public void setChoice(int index, @Nullable RecipeChoice recipeChoice){
        animators.add(new RecipeChoiceAnimator(index, recipeChoice));
        menu.setItem(index, recipeChoice);
    }
    public void addAnimator(@NotNull Animator animator){
        animators.add(animator);
    }
}
