package me.udnek.jeiu.recipe_feature;

import org.bukkit.inventory.Recipe;
import org.bukkit.loot.LootTable;

import java.util.ArrayList;
import java.util.List;

public class RecipeHolder {

    private Recipe recipe;
    private LootTable lootTable;
    public final Type type;

    public static List<RecipeHolder> of(List<Recipe> recipes, List<LootTable> lootTables){
        List<RecipeHolder> recipeHolders = new ArrayList<>();
        for (Recipe recipe : recipes) {
            recipeHolders.add(new RecipeHolder(recipe));
        }
        for (LootTable lootTable : lootTables) {
            recipeHolders.add(new RecipeHolder(lootTable));
        }
        return recipeHolders;
    }


    public RecipeHolder(Recipe recipe){
        this.recipe = recipe;
        type = Type.RECIPE;
    }
    public RecipeHolder(LootTable lootTable){
        this.lootTable = lootTable;
        type = Type.LOOT_TABLE;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public LootTable getLootTable() {
        return lootTable;
    }


    public enum Type{
        RECIPE,
        LOOT_TABLE
    }
}
