package me.udnek.jeiu;

import org.bukkit.inventory.*;

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

        return false;

    }

}
