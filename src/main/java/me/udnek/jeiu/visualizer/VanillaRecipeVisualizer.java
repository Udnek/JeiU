package me.udnek.jeiu.visualizer;

import me.udnek.jeiu.item.Items;
import me.udnek.jeiu.menu.RecipesMenu;
import me.udnek.jeiu.visualizer.abstraction.AbstractVisualizer;
import net.kyori.adventure.text.Component;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class VanillaRecipeVisualizer extends AbstractVisualizer {

    public static final int CRAFTING_MATRIX_OFFSET = 9 * 1 + 1;
    public static final int CRAFTING_RESULT_OFFSET = 9 * 2 + 5;

    public static final int COOKING_INPUT_OFFSET = 9 * 1 + 2;
    public static final int COOKING_RESULT_OFFSET = 9 * 2 + 5;

    public static final int SMITHING_TEMPLATE_OFFSET = 9 * 2 + 1;
    public static final int SMITHING_BASE_OFFSET = 9 * 2 + 2;
    public static final int SMITHING_ADDITION_OFFSET = 9 * 2 + 3;
    public static final int SMITHING_RESULT_OFFSET = 9 * 2 + 5;

    public static final int STONECUTTING_INPUT_OFFSET = 9 * 2 + 2;
    public static final int STONECUTTING_RESULT_OFFSET = 9 * 2 + 5;

    public static final int RECIPE_BANNER_OFFSET = RecipesMenu.getBannerPosition();
    public static final int COOKING_RECIPE_FIRE_ICON_OFFSET = 9 * 2 + 2;

    public static final ItemStack RECIPE_BANNER = Items.BANNER.getItem();
    public static final ItemStack FIRE_ICON = Items.FIRE_ICON.getItem();

    private final @NotNull Recipe recipe;

    public VanillaRecipeVisualizer(@NotNull Recipe recipe){
        this.recipe = recipe;
    }
    @Override
    public @Nullable List<Component> getInformation() {
        if (recipe instanceof Keyed keyed) return List.of(Component.text("ID: " + keyed.getKey().asString()));
        return null;
    }

    public void visualize(@NotNull RecipesMenu recipesMenu) {
        super.visualize(recipesMenu);

        setDecorItems();

        // SHAPED
        switch (recipe) {
            case ShapedRecipe shapedRecipe -> {
                String[] shape = shapedRecipe.getShape();
                Map<Character, RecipeChoice> recipeChoiceMap = shapedRecipe.getChoiceMap();
                RecipeChoice recipeChoice;

                int extraXOffset = 0;
                int extraYOffset = 0;

                if (shape.length != 3) extraYOffset = 1;
                if (shape[0].length() == 1) extraXOffset = 1;

                for (int y = 0; y < shape.length; y++) {
                    for (int x = 0; x < shape[0].length(); x++) {

                        recipeChoice = recipeChoiceMap.get(shape[y].charAt(x));

                        setItemInCraftingMatrix(x + extraXOffset, y + extraYOffset, recipeChoice);

                    }
                }
                menu.setItem(CRAFTING_RESULT_OFFSET, recipe.getResult());
            }
            // SHAPELESS
            case ShapelessRecipe shapelessRecipe -> {
                List<RecipeChoice> recipeChoiceList = shapelessRecipe.getChoiceList();

                int extraOffset = 0;
                if (recipeChoiceList.size() == 1) extraOffset = 1;

                int y;
                int x;
                for (int i = 0; i < recipeChoiceList.size(); i++) {
                    y = (int) Math.ceil((float) (i + 1) / 3) - 1;
                    x = i % 3;
                    setItemInCraftingMatrix(x + extraOffset, y + extraOffset, recipeChoiceList.get(i));
                }
                menu.setItem(CRAFTING_RESULT_OFFSET, recipe.getResult());
            }
            case TransmuteRecipe transmuteRecipe -> {
                setItemInCraftingMatrix(0, 0, transmuteRecipe.getInput());
                setItemInCraftingMatrix(1, 0, transmuteRecipe.getMaterial());
                menu.setItem(CRAFTING_RESULT_OFFSET, recipe.getResult());
            }

            // COOKING
            case CookingRecipe<?> cookingRecipe -> {
                RecipeChoice inputChoice = cookingRecipe.getInputChoice();
                setChoice(COOKING_INPUT_OFFSET, inputChoice);
                menu.setItem(COOKING_RESULT_OFFSET, recipe.getResult());
            }

            //SMITHING
            case SmithingTransformRecipe transformRecipe -> {
                RecipeChoice base = transformRecipe.getBase();
                RecipeChoice addition = transformRecipe.getAddition();
                RecipeChoice template = transformRecipe.getTemplate();
                setChoice(SMITHING_TEMPLATE_OFFSET, template);
                setChoice(SMITHING_BASE_OFFSET, base);
                setChoice(SMITHING_ADDITION_OFFSET, addition);
                menu.setItem(SMITHING_RESULT_OFFSET, recipe.getResult());
            }
            case SmithingTrimRecipe trimRecipe -> {
                RecipeChoice base = trimRecipe.getBase();
                RecipeChoice addition = trimRecipe.getAddition();
                RecipeChoice template = trimRecipe.getTemplate();
                setChoice(SMITHING_TEMPLATE_OFFSET, template);
                setChoice(SMITHING_BASE_OFFSET, base);
                setChoice(SMITHING_ADDITION_OFFSET, addition);
                if (recipe.getResult().isEmpty()) setChoice(SMITHING_RESULT_OFFSET, base);
                else menu.setItem(SMITHING_RESULT_OFFSET, recipe.getResult());
            }

            //STONECUTTING
            case StonecuttingRecipe stonecuttingRecipe -> {
                RecipeChoice inputChoice = stonecuttingRecipe.getInputChoice();
                setChoice(STONECUTTING_INPUT_OFFSET, inputChoice);
                menu.setItem(STONECUTTING_RESULT_OFFSET, recipe.getResult());
            }
            default -> {
            }
        }
    }
    private void setItemInCraftingMatrix(int x, int y, RecipeChoice recipeChoice) {
        setChoice(y * 9 + x + CRAFTING_MATRIX_OFFSET, recipeChoice);
    }
    private void setDecorItems() {
        @NotNull String bannerModel;
        Material blockMaterial;
        if (recipe instanceof ShapelessRecipe || recipe instanceof ShapedRecipe || recipe instanceof TransmuteRecipe) {
            bannerModel = "jeiu:crafting_table_banner";
            blockMaterial = Material.CRAFTING_TABLE;
        } else if (recipe instanceof FurnaceRecipe) {
            menu.setThemedItem(COOKING_RECIPE_FIRE_ICON_OFFSET, FIRE_ICON);
            bannerModel = "jeiu:furnace_banner";
            blockMaterial = Material.FURNACE;
        } else if (recipe instanceof BlastingRecipe) {
            menu.setThemedItem(COOKING_RECIPE_FIRE_ICON_OFFSET, FIRE_ICON);
            bannerModel = "jeiu:furnace_banner";
            blockMaterial = Material.BLAST_FURNACE;
        } else if (recipe instanceof SmokingRecipe) {
            menu.setThemedItem(COOKING_RECIPE_FIRE_ICON_OFFSET, FIRE_ICON);
            bannerModel = "jeiu:furnace_banner";
            blockMaterial = Material.SMOKER;
        } else if (recipe instanceof CampfireRecipe) {
            menu.setThemedItem(COOKING_RECIPE_FIRE_ICON_OFFSET, FIRE_ICON);
            bannerModel = "jeiu:furnace_banner";
            blockMaterial = Material.CAMPFIRE;
        } else if (recipe instanceof SmithingRecipe) {
            bannerModel = "jeiu:smithing_table_banner";
            blockMaterial = Material.SMITHING_TABLE;
        } else if (recipe instanceof StonecuttingRecipe) {
            bannerModel = "jeiu:stonecutter_banner";
            blockMaterial = Material.STONECUTTER;
        } else {
            bannerModel = "jeiu:banner";
            blockMaterial = Material.BARRIER;
        }

        ItemStack itemStack = RECIPE_BANNER;
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setItemModel(NamespacedKey.fromString(bannerModel));
        itemStack.setItemMeta(itemMeta);

        menu.setThemedItem(RECIPE_BANNER_OFFSET, itemStack);
        menu.setItem(RecipesMenu.getRecipeStationPosition(), blockMaterial);
    }
}



