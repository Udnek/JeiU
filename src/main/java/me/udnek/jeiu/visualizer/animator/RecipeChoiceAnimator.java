package me.udnek.jeiu.visualizer.animator;

import com.google.common.base.Preconditions;
import me.udnek.coreu.custom.recipe.choice.CustomRecipeChoice;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class RecipeChoiceAnimator implements Animator {

    protected int position;
    protected @Nullable RecipeChoice choice;
    protected int frameIndex = -1;
    protected final int size;
    public RecipeChoiceAnimator(int position, @Nullable RecipeChoice choice){
        Preconditions.checkArgument(isAnimatableChoice(choice), "Choice can not be animated!");
        this.position = position;
        this.choice = choice;
        switch (choice) {
            case null -> size = 0;
            case RecipeChoice.MaterialChoice materialChoice -> size = materialChoice.getChoices().size();
            case RecipeChoice.ExactChoice exactChoice -> size = exactChoice.getChoices().size();
            default -> size = ((CustomRecipeChoice) choice).getAllPossible().size();
        }
    }

    public static boolean isAnimatableChoice(@Nullable RecipeChoice recipeChoice){
        return switch (recipeChoice) {
            case null -> true;
            case RecipeChoice.MaterialChoice materialChoice -> true;
            case RecipeChoice.ExactChoice exactChoice -> true;
            case CustomRecipeChoice customRecipeChoice -> true;
            default -> false;
        };
    }

    protected int getNewIndex(){
        frameIndex ++;
        if (frameIndex > size-1) frameIndex = 0;
        return frameIndex;
    }

    @Override
    public @Nullable ItemStack getNextFrame(){
        return switch (choice) {
            case null -> null;
            case RecipeChoice.MaterialChoice materialChoice ->
                    new ItemStack(materialChoice.getChoices().get(getNewIndex()));
            case RecipeChoice.ExactChoice exactChoice -> exactChoice.getChoices().get(getNewIndex());
            default -> ((CustomRecipeChoice) choice).getAllPossible().get(getNewIndex());
        };
    }

    @Override
    public int getPosition() {
        return position;
    }
}
