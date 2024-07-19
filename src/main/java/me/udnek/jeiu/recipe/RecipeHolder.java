package me.udnek.jeiu.recipe;

import org.bukkit.inventory.Recipe;
import org.bukkit.loot.LootTable;

import java.util.ArrayList;
import java.util.List;

public class RecipeHolder {

    private Recipe recipe;
    private LootTable lootTable;
    private Visualizable visualizable;

    public static List<RecipeHolder> of(List<Recipe> recipes, List<LootTable> lootTables, List<Visualizable> visualizables) {
        List<RecipeHolder> recipeHolders = new ArrayList<>();
        for (Recipe recipe : recipes) {
            if (recipe instanceof Visualizable visualizable) visualizables.add(visualizable);
            else recipeHolders.add(new RecipeHolder(recipe));
        }
        for (LootTable lootTable : lootTables) {
            if (lootTable instanceof Visualizable visualizable) visualizables.add(visualizable);
            else recipeHolders.add(new RecipeHolder(lootTable));
        }
        for (Visualizable visualizable : visualizables) {
            recipeHolders.add(new RecipeHolder(visualizable));
        }
        return recipeHolders;
    }

    public static List<RecipeHolder> of(List<Recipe> recipes, List<LootTable> lootTables){
        return of(recipes, lootTables, new ArrayList<>());
    }

    public RecipeHolder(Recipe recipe) {this.recipe = recipe;}
    public RecipeHolder(LootTable lootTable) {this.lootTable = lootTable;}
    public RecipeHolder(Visualizable visualizable) {this.visualizable = visualizable;}

    public Recipe getRecipe() {
        return recipe;
    }
    public LootTable getLootTable() {
        return lootTable;
    }
    public Visualizable getVisualizable(){return visualizable;}

    public Type getType(){
        if (recipe != null) return Type.RECIPE;
        if (lootTable != null) return Type.LOOT_TABLE;
        return Type.VISUALIZABLE;
    }

    public enum Type {
        RECIPE,
        LOOT_TABLE,
        VISUALIZABLE
    }
}
