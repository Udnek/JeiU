package me.udnek.jeiu.component;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemEnchantments;
import me.udnek.coreu.custom.component.CustomComponent;
import me.udnek.coreu.custom.component.CustomComponentType;
import me.udnek.coreu.custom.item.CustomItem;
import me.udnek.coreu.custom.item.ItemUtils;
import me.udnek.coreu.custom.recipe.RecipeManager;
import me.udnek.jeiu.util.Utils;
import me.udnek.jeiu.visualizer.Visualizer;
import me.udnek.jeiu.visualizer.implementation.RepairVisualizer;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.loot.LootTable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public interface RecipeAndUsagesItem extends CustomComponent<CustomItem> {

     static void getRecipesAsResult(@NotNull ItemStack stack, @NotNull Consumer<Visualizer> consumer){
         List<LootTable> lootTables;
         List<Recipe> recipes = new ArrayList<>();

         if (stack.getType() == Material.ENCHANTED_BOOK){
             lootTables = List.of();
             ItemEnchantments itemEnchantments = stack.getData(DataComponentTypes.STORED_ENCHANTMENTS);
             if (itemEnchantments != null) {
                 Set<Enchantment> targetEnchants = itemEnchantments.enchantments().keySet();
                 RecipeManager.getInstance().getRecipesAsResult(
                         input -> {
                             ItemEnchantments enchantments = input.getData(DataComponentTypes.STORED_ENCHANTMENTS);
                             if (enchantments == null) return false;
                             return enchantments.enchantments().keySet().containsAll(targetEnchants);
                         },
                         recipes::add
                 );
             }
         } else {
             lootTables = ItemUtils.getWhereItemOccurs(stack);
             RecipeManager.getInstance().getRecipesAsResult(stack, recipes::add);
         }

         Utils.toVisualizers(recipes, lootTables, consumer);
         if (ItemUtils.isRepairable(stack)){
             consumer.accept(new RepairVisualizer(stack));
         }
    }

    static void getRecipesAsIngredient(@NotNull ItemStack stack, @NotNull Consumer<Visualizer> consumer){
        List<Recipe> recipes = new ArrayList<>();
        RecipeManager.getInstance().getRecipesAsIngredient(stack, recipes::add);
        Utils.toVisualizers(recipes, List.of(), consumer);
    }

    RecipeAndUsagesItem DEFAULT = new RecipeAndUsagesItem() {
        @Override
        public void getRecipes(@NotNull CustomItem customItem, @NotNull ItemStack stack, @NotNull Consumer<Visualizer> consumer) {
            getRecipesAsResult(stack, consumer);
        }

        @Override
        public void getUsages(@NotNull CustomItem customItem, @NotNull ItemStack stack, @NotNull Consumer<Visualizer> consumer) {
            getRecipesAsIngredient(stack, consumer);
        }
    };

    RecipeAndUsagesItem EMPTY = new RecipeAndUsagesItem() {
        @Override
        public void getRecipes(@NotNull CustomItem customItem, @NotNull ItemStack stack, @NotNull Consumer<Visualizer> consumer) {}

        @Override
        public void getUsages(@NotNull CustomItem customItem, @NotNull ItemStack stack, @NotNull Consumer<Visualizer> consumer) {}
    };


    void getRecipes(@NotNull CustomItem customItem, @NotNull ItemStack stack, @NotNull Consumer<Visualizer> consumer);
    void getUsages(@NotNull CustomItem customItem, @NotNull ItemStack stack, @NotNull Consumer<Visualizer> consumer);

    @Override
    default @NotNull CustomComponentType<CustomItem, ?> getType(){
        return Components.RECIPE_AND_USAGES_ITEM;
    }
}
