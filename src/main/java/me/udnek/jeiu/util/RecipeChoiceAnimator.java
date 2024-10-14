package me.udnek.jeiu.util;

import com.google.common.base.Preconditions;
import me.udnek.itemscoreu.customrecipe.choice.CustomRecipeChoice;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.jetbrains.annotations.Nullable;

public class RecipeChoiceAnimator {

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

    public static boolean isAnimatableChoice(RecipeChoice recipeChoice){
        if (recipeChoice == null) return true;
        if (recipeChoice instanceof RecipeChoice.MaterialChoice) return true;
        if (recipeChoice instanceof RecipeChoice.ExactChoice) return true;
        if (recipeChoice instanceof CustomRecipeChoice) return true;
        return false;
    }

    protected int getNewIndex(){
        frameIndex ++;
        if (frameIndex > size-1) frameIndex = 0;
        return frameIndex;
    }

    public @Nullable ItemStack getFrame(){
        if (choice == null) return null;
        if (choice instanceof RecipeChoice.MaterialChoice materialChoice){
            return new ItemStack(materialChoice.getChoices().get(getNewIndex()));
        } else if (choice instanceof RecipeChoice.ExactChoice exactChoice){
            return exactChoice.getChoices().get(getNewIndex());
        } else {
            return  ((CustomRecipeChoice) choice).getAllPossible().get(getNewIndex());
        }
    }


    public int getPosition() {
        return position;
    }
}
