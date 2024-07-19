package me.udnek.jeiu.recipe.visualizer;

import me.udnek.jeiu.item.Items;
import me.udnek.jeiu.recipe.RecipesMenu;
import me.udnek.jeiu.recipe.Visualizable;
import org.bukkit.Material;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;

public class VanillaRecipeVisualizer implements Visualizable {

    private static final int craftingMatrixOffset = 9 * 2 + 1;
    private static final int craftingResultOffset = 9 * 3 + 5;
    ////
    private static final int cookingInputOffset = 9 * 2 + 2;
    private static final int cookingResultOffset = 9 * 3 + 5;
    ////
    private static final int smithingTemplateOffset = 9 * 3 + 1;
    private static final int smithingBaseOffset = 9 * 3 + 2;
    private static final int smithingAdditionOffset = 9 * 3 + 3;
    private static final int smithingResultOffset = 9 * 3 + 5;
    ////
    private static final int stonecuttingInputOffset = 9 * 3 + 2;
    private static final int stonecuttingResultOffset = 9 * 3 + 5;

    private static final int recipeBannerOffset = 9 + 2;
    private static final int cookingRecipeFireIconOffset = 9 * 3 + 2;

    public static final ItemStack RECIPE_BANNER = Items.BANNER.getItem();
    public static final ItemStack FIRE_ICON = Items.FIRE_ICON.getItem();


    private RecipesMenu recipesMenu;
    private final Recipe recipe;

    public VanillaRecipeVisualizer(Recipe recipe){
        this.recipe = recipe;
    }

    public void visualize(RecipesMenu recipesMenu) {

        this.recipesMenu = recipesMenu;

        this.setDecorItems();

        // SHAPE

        if (recipe instanceof ShapedRecipe) {

            String[] shape = ((ShapedRecipe) recipe).getShape();
            Map<Character, RecipeChoice> recipeChoiceMap = ((ShapedRecipe) recipe).getChoiceMap();
            RecipeChoice recipeChoice;

            int extraXOffset = 0;
            int extraYOffset = 0;

            if (shape.length != 3) extraYOffset = 1;
            if (shape[0].length() == 1) extraXOffset = 1;


            for (int y = 0; y < shape.length; y++) {
                for (int x = 0; x < shape[0].length(); x++) {

                    recipeChoice = recipeChoiceMap.get(shape[y].charAt(x));

                    this.setItemInCraftingMatrix(x + extraXOffset, y + extraYOffset, recipeChoice);

                }
            }
            this.recipesMenu.setItemAt(craftingResultOffset, recipe.getResult());
        }

        // SHAPELESS
        else if (recipe instanceof ShapelessRecipe) {
            List<RecipeChoice> recipeChoiceList = ((ShapelessRecipe) recipe).getChoiceList();

            int extraOffset = 0;
            if (recipeChoiceList.size() == 1) extraOffset = 1;

            int y;
            int x;
            for (int i = 0; i < recipeChoiceList.size(); i++) {
                y = (int) Math.ceil((float) (i + 1) / 3) - 1;
                x = i % 3;
                this.setItemInCraftingMatrix(x + extraOffset, y + extraOffset, recipeChoiceList.get(i));
            }
            this.recipesMenu.setItemAt(craftingResultOffset, recipe.getResult());
        }
        // COOKING
        else if (recipe instanceof CookingRecipe) {

            RecipeChoice inputChoice = ((CookingRecipe<?>) recipe).getInputChoice();
            this.recipesMenu.setItemAt(cookingInputOffset, inputChoice);
            this.recipesMenu.setItemAt(cookingResultOffset, recipe.getResult());
        }
        //SMITHING
        else if (recipe instanceof SmithingTransformRecipe) {

            RecipeChoice base = ((SmithingTransformRecipe) recipe).getBase();
            RecipeChoice addition = ((SmithingTransformRecipe) recipe).getAddition();
            RecipeChoice template = ((SmithingTransformRecipe) recipe).getTemplate();
            this.recipesMenu.setItemAt(smithingTemplateOffset, template);
            this.recipesMenu.setItemAt(smithingBaseOffset, base);
            this.recipesMenu.setItemAt(smithingAdditionOffset, addition);
            this.recipesMenu.setItemAt(smithingResultOffset, recipe.getResult());

        } else if (recipe instanceof SmithingTrimRecipe) {

            RecipeChoice base = ((SmithingTrimRecipe) recipe).getBase();
            RecipeChoice addition = ((SmithingTrimRecipe) recipe).getAddition();
            RecipeChoice template = ((SmithingTrimRecipe) recipe).getTemplate();
            this.recipesMenu.setItemAt(smithingTemplateOffset, template);
            this.recipesMenu.setItemAt(smithingBaseOffset, base);
            this.recipesMenu.setItemAt(smithingAdditionOffset, addition);
            this.recipesMenu.setItemAt(smithingResultOffset, recipe.getResult());
        }
        //STONECUTTING
        else if (recipe instanceof StonecuttingRecipe) {

            RecipeChoice inputChoice = ((StonecuttingRecipe) recipe).getInputChoice();
            this.recipesMenu.setItemAt(stonecuttingInputOffset, inputChoice);
            this.recipesMenu.setItemAt(stonecuttingResultOffset, recipe.getResult());
        }
    }

    private void setItemInCraftingMatrix(int x, int y, RecipeChoice recipeChoice) {
        this.recipesMenu.setItemAt(y * 9 + x + craftingMatrixOffset, recipeChoice);
    }

    private void setDecorItems() {

        int bannerCustomModelData;
        Material blockMaterial;
        if (recipe instanceof ShapelessRecipe || recipe instanceof ShapedRecipe) {
            bannerCustomModelData = 1000;
            blockMaterial = Material.CRAFTING_TABLE;
        } else if (recipe instanceof FurnaceRecipe) {
            this.recipesMenu.setItemAt(cookingRecipeFireIconOffset, FIRE_ICON);
            bannerCustomModelData = 1001;
            blockMaterial = Material.FURNACE;
        } else if (recipe instanceof BlastingRecipe) {
            this.recipesMenu.setItemAt(cookingRecipeFireIconOffset, FIRE_ICON);
            bannerCustomModelData = 1001;
            blockMaterial = Material.BLAST_FURNACE;
        } else if (recipe instanceof SmokingRecipe) {
            this.recipesMenu.setItemAt(cookingRecipeFireIconOffset, FIRE_ICON);
            bannerCustomModelData = 1001;
            blockMaterial = Material.SMOKER;
        } else if (recipe instanceof CampfireRecipe) {
            this.recipesMenu.setItemAt(cookingRecipeFireIconOffset, FIRE_ICON);
            bannerCustomModelData = 1001;
            blockMaterial = Material.CAMPFIRE;
        } else if (recipe instanceof SmithingRecipe) {
            bannerCustomModelData = 1002;
            blockMaterial = Material.SMITHING_TABLE;
        } else if (recipe instanceof StonecuttingRecipe) {
            bannerCustomModelData = 1003;
            blockMaterial = Material.STONECUTTER;
        } else {
            bannerCustomModelData = 1000;
            blockMaterial = Material.BARRIER;
        }

        ItemStack itemStack = RECIPE_BANNER;
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setCustomModelData(bannerCustomModelData);
        itemStack.setItemMeta(itemMeta);

        this.recipesMenu.setItemAt(recipeBannerOffset, itemStack);
        this.recipesMenu.setItemAt(RecipesMenu.RECIPE_BLOCK_OFFSET, blockMaterial);
    }
}



