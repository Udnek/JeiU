package me.udnek.jeiu.component;

import me.udnek.coreu.custom.component.CustomComponent;
import me.udnek.coreu.custom.component.CustomComponentType;
import me.udnek.coreu.custom.item.CustomItem;
import me.udnek.coreu.custom.item.ItemUtils;
import me.udnek.coreu.custom.loot.LootTableUtils;
import me.udnek.coreu.custom.recipe.RecipeManager;
import me.udnek.jeiu.util.Utils;
import me.udnek.jeiu.visualizer.Visualizer;
import me.udnek.jeiu.visualizer.implementation.RepairVisualizer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.loot.LootTable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public interface RecipeAndUsagesItem extends CustomComponent<CustomItem> {

    RecipeAndUsagesItem DEFAULT = new RecipeAndUsagesItem() {
        @Override
        public void getRecipes(@NotNull ItemStack stack, @NotNull Consumer<Visualizer> consumer) {
            List<LootTable> lootTables = LootTableUtils.getWhereItemOccurs(stack);
            List<Recipe> recipes = new ArrayList<>();
            RecipeManager.getInstance().getRecipesAsResult(stack, recipes::add);
            Utils.toVisualizers(recipes, lootTables, consumer);
            if (ItemUtils.isRepairable(stack)){
                consumer.accept(new RepairVisualizer(stack));
            }
        }

        @Override
        public void getUsages(@NotNull ItemStack stack, @NotNull Consumer<Visualizer> consumer) {
            List<Recipe> recipes = new ArrayList<>();
            RecipeManager.getInstance().getRecipesAsIngredient(stack, recipes::add);
            Utils.toVisualizers(recipes, List.of(), consumer);
        }
    };

    RecipeAndUsagesItem EMPTY = new RecipeAndUsagesItem() {
        @Override
        public void getRecipes(@NotNull ItemStack stack, @NotNull Consumer<Visualizer> consumer) {}

        @Override
        public void getUsages(@NotNull ItemStack stack, @NotNull Consumer<Visualizer> consumer) {}
    };


    void getRecipes(@NotNull ItemStack stack, @NotNull Consumer<Visualizer> consumer);
    void getUsages(@NotNull ItemStack stack, @NotNull Consumer<Visualizer> consumer);

    @Override
    default @NotNull CustomComponentType<CustomItem, ?> getType(){
        return Components.RECIPE_AND_USAGES_ITEM;
    }
}
