package me.udnek.jeiu.menu;

import me.udnek.itemscoreu.custominventory.ConstructableCustomInventory;
import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.customloot.LootTableUtils;
import me.udnek.itemscoreu.customrecipe.RecipeManager;
import me.udnek.itemscoreu.util.ComponentU;
import me.udnek.jeiu.JeiU;
import me.udnek.jeiu.component.ComponentTypes;
import me.udnek.jeiu.item.Items;
import me.udnek.jeiu.util.*;
import me.udnek.jeiu.visualizer.abstraction.Visualizable;
import me.udnek.jeiu.visualizer.abstraction.Visualizer;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.loot.LootTable;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RecipesMenu extends ConstructableCustomInventory implements ClickableMenu {

    // TODO: 2/11/2024 DYNAMIC CRAFTING MATRIX AND RESULT

    public static final int RECIPE_STATION_POSITION = 4;
    public static final int PREVIOUS_RECIPE_BUTTON_POSITION = 7;
    public static final int NEXT_RECIPE_BUTTON_POSITION = 8;
    public static final int HELP_BUTTON_POSITION = 1;
    public static final int BACK_BUTTON_POSITION = 0;
    public static final int RECIPE_INFO_POSITION = 3;

    private BukkitTask animatorTicker = null;
    private List<Visualizable> visualizers;
    private int recipeIndex;
    private Visualizer currentRecipe;
    private BackCallable backPage = null;
    private List<LootTable> asyncFoundLootTables = null;

    public RecipesMenu(@NotNull MenuQuery query, @NotNull Player player, @Nullable BackCallable backCallable) {
        this.backPage = backCallable;
        open(player);
        runNewQuery(query, null);
    }
    public RecipesMenu(@NotNull MenuQuery query, @NotNull Player player) {
        this(query, player, null);
    }

    protected void openRecipeNumber(int recipeIndex) {
        this.recipeIndex = Math.clamp(recipeIndex, 0, Math.max(0, visualizers.size()-1));
        runPage();
    }


    @Override
    public void openBack(@NotNull InventoryClickEvent event) {backPage.backCall();}
    @Override
    public void openNext(@NotNull InventoryClickEvent event) {openRecipeNumber(recipeIndex+1);}
    @Override
    public void openPrevious(@NotNull InventoryClickEvent event) {openRecipeNumber(recipeIndex-1);}

    @Override
    public void runNewQuery(@NotNull MenuQuery query, @Nullable InventoryClickEvent event){
        if (CustomItem.isCustom(query.getItemStack()) &&
                CustomItem.get(query.getItemStack()).hasComponent(ComponentTypes.TECHNICAL_ITEM)){
            return;
        }

        if (query.getType() == MenuQuery.Type.USAGES){

            List<Recipe> rawRecipes = new ArrayList<>();
            RecipeManager.getInstance().getRecipesAsIngredient(query.getItemStack(), rawRecipes::add);
            List<Visualizable> newRecipes = new ArrayList<>();
            Utils.toVisualizables(rawRecipes, List.of(), newRecipes::add);

            new MenuQueryEvent(query, newRecipes).callEvent();
            if (newRecipes.isEmpty()) return;
            visualizers = newRecipes;
            recipeIndex = 0;
            runPage();

        } else {
            asyncFoundLootTables = null;

            new BukkitRunnable() {
                @Override
                public void run() {
                    List<LootTable> thisLootTables = LootTableUtils.getWhereItemOccurs(query.getItemStack());
                    asyncLootTablesFound(thisLootTables);
                }
            }.run(); // TODO REPLACE WITH ASYNC?

            final int period = 1;
            new Runnable() {
                int totalWaited = 0;
                @Override
                public void run() {
                    totalWaited += 1;
                    if (totalWaited*period > 20*10 || !RecipesMenu.this.hasViewers()) {
                        //cancel();
                        return;
                    }
                    if (asyncFoundLootTables == null) return;

                    List<Recipe> rawRecipes = new ArrayList<>();
                    RecipeManager.getInstance().getRecipesAsResult(query.getItemStack(), rawRecipes::add);
                    List<Visualizable> newRecipes = new ArrayList<>();
                    Utils.toVisualizables(rawRecipes, asyncFoundLootTables, newRecipes::add);

                    new MenuQueryEvent(query, newRecipes).callEvent();
                    if (newRecipes.isEmpty()) return;
                    visualizers = newRecipes;
                    recipeIndex = 0;
                    runPage();
                    //cancel();
                }
            }.run(); //.runTaskTimer(JeiU.getInstance(), 1, period);
        }
    }

    protected void asyncLootTablesFound(@NotNull List<LootTable> lootTables){
        asyncFoundLootTables = lootTables;
    }
    public boolean hasViewers() {return !inventory.getViewers().isEmpty();}
    protected void runPage() {
        if (animatorTicker != null) animatorTicker.cancel();
        inventory.clear();
        if (!visualizers.isEmpty()) {
            currentRecipe = visualizers.get(recipeIndex).getVisualizer();
            currentRecipe.visualize(this);
        }
        setPageButtons();
        animateRecipes();
    }
    protected void setPageButtons() {
        if (recipeIndex < visualizers.size() - 1) setItem(NEXT_RECIPE_BUTTON_POSITION, Items.NEXT_BUTTON);
        else setItem(NEXT_RECIPE_BUTTON_POSITION, (ItemStack) null);

        if (recipeIndex > 0) setItem(PREVIOUS_RECIPE_BUTTON_POSITION, Items.PREVIOUS_BUTTON);
        else setItem(PREVIOUS_RECIPE_BUTTON_POSITION, (ItemStack) null);

        if (backPage != null) setItem(BACK_BUTTON_POSITION, Items.BACK_BUTTON);
        else setItem(BACK_BUTTON_POSITION, (ItemStack) null);

        setItem(HELP_BUTTON_POSITION, Items.HELP);
        setInfoItem();
    }
    protected void setInfoItem() {
        if (currentRecipe == null) return;
        List<Component> information = currentRecipe.getInformation();
        if (information == null) return;
        ItemStack itemStack = Items.INFORMATION.getItem();
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<Component> formattedInformation = new ArrayList<>();
        for (Component component : information) {
            if (component.color() == null) component = component.color(NamedTextColor.GRAY);
            formattedInformation.add(component);
        }
        itemMeta.lore(formattedInformation);
        itemStack.setItemMeta(itemMeta);
        setItem(RECIPE_INFO_POSITION, itemStack);
    }
    public void setItem(int index, @Nullable ItemStack itemStack) {
        inventory.setItem(index, itemStack);
    }
    public void setItem(int index, @Nullable RecipeChoice recipeChoice) {
        if (recipeChoice == null) return;
        setItem(index, recipeChoice.getItemStack());
    }
    public void setItem(int index, @NotNull Material material) {
        if (!material.isItem()) return;
        setItem(index, new ItemStack(material));
    }
    public void setItem(@NotNull RecipeChoiceAnimator animator) {
        setItem(animator.getPosition(), animator.getFrame());
    }
    public void setItem(int index, @NotNull CustomItem customItem){
        setItem(index, customItem.getItem());
    }
    protected void animateRecipes() {
        this.animatorTicker = new BukkitRunnable() {
            @Override
            public void run() {
                if (!RecipesMenu.this.hasViewers()) {
                    cancel();
                    return;
                }
                if (RecipesMenu.this.currentRecipe != null) RecipesMenu.this.currentRecipe.tickAnimation();
            }

        }.runTaskTimer(JeiU.getInstance(), 0, 20);
    }
    @Override
    public int getInventorySize() {return 9 * 6;}
    @Override
    public Component getDisplayName() {
        return ComponentU.textWithNoSpace(
                -8,
                Component.text("0", TextColor.color(1f, 1f, 1f)).font(Key.key("jeiu:font")),
                175
        ).append(Component.translatable("gui.jeiu.recipes_title", TextColor.color(0, 0, 0)).font(NamespacedKey.minecraft("default")));
    }
}
