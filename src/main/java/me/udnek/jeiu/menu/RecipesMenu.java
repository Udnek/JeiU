package me.udnek.jeiu.menu;

import me.udnek.itemscoreu.custominventory.ConstructableCustomInventory;
import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.customloot.LootTableUtils;
import me.udnek.itemscoreu.customrecipe.RecipeManager;
import me.udnek.itemscoreu.util.ComponentU;
import me.udnek.jeiu.JeiU;
import me.udnek.jeiu.component.ComponentTypes;
import me.udnek.jeiu.item.Items;
import me.udnek.jeiu.util.BackCallable;
import me.udnek.jeiu.util.MenuQuery;
import me.udnek.jeiu.util.MenuQueryEvent;
import me.udnek.jeiu.util.Utils;
import me.udnek.jeiu.visualizer.abstraction.Visualizable;
import me.udnek.jeiu.visualizer.abstraction.Visualizer;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.loot.LootTable;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RecipesMenu extends ConstructableCustomInventory implements JeiUMenu {

    // TODO: 2/11/2024 DYNAMIC CRAFTING MATRIX AND RESULT

    public static final int RECIPE_STATION_POSITION = 4;
    public static final int PREVIOUS_BUTTON_POSITION = 7;
    public static final int NEXT_BUTTON_POSITION = 8;
    public static final int HELP_BUTTON_POSITION = 1;
    public static final int BACK_BUTTON_POSITION = 0;
    public static final int RECIPE_INFO_POSITION = 3;
    public static final int BANNER_POSITION = 2;

    private BukkitTask animatorTicker = null;
    private List<Visualizable> visualizers;
    private int recipeIndex;
    private Visualizer currentRecipe;

    private MenuQuery query;
    private final Player player;

    public RecipesMenu(@NotNull Player player) {
        this.player = player;
    }

    @Override
    public void openBack(@NotNull InventoryClickEvent event) {
        BackCallable backCallable = query.getBackCall();
        if (backCallable != null) backCallable.backCall();
    }
    @Override
    public void openNext(@NotNull InventoryClickEvent event) {openRecipeNumber(recipeIndex+1);}
    @Override
    public void openPrevious(@NotNull InventoryClickEvent event) {openRecipeNumber(recipeIndex-1);}

    protected void openRecipeNumber(int recipeIndex) {
        this.recipeIndex = Math.clamp(recipeIndex, 0, Math.max(0, visualizers.size()-1));
        runPage();
    }

    private boolean isTechnical(@Nullable ItemStack itemStack){
        if (itemStack == null) return false;
        return CustomItem.isCustom(itemStack) && CustomItem.get(itemStack).hasComponent(ComponentTypes.TECHNICAL_ITEM);
    }

    @Override
    public void runNewQuery(@NotNull MenuQuery query, @Nullable InventoryClickEvent event){
        List<Visualizable> newRecipes = new ArrayList<>();
        if (!isTechnical(query.getItemStack())) {

            List<Recipe> rawRecipes = new ArrayList<>();
            if (query.getType() == MenuQuery.Type.USAGES){
                RecipeManager.getInstance().getRecipesAsIngredient(query.getItemStack(), rawRecipes::add);
                Utils.toVisualizables(rawRecipes, List.of(), newRecipes::add);
            } else {
                List<LootTable> lootTables = LootTableUtils.getWhereItemOccurs(query.getItemStack());
                RecipeManager.getInstance().getRecipesAsResult(query.getItemStack(), rawRecipes::add);
                Utils.toVisualizables(rawRecipes, lootTables, newRecipes::add);
            }
        }

        new MenuQueryEvent(query, newRecipes).callEvent();
        if (newRecipes.isEmpty() && !query.isOpenIfNothingFound()) return;
        this.query = query;
        visualizers = newRecipes;
        recipeIndex = 0;
        runPage();
        open(player);

    }


    @Override
    public void clickedNonButtonItem(@NotNull InventoryClickEvent event) {
        if (event.isLeftClick()) {
            runNewQuery(new MenuQuery(event.getCurrentItem(), MenuQuery.Type.RECIPES, getBackCall(), false), event);
        } else if (event.isRightClick()) {
            runNewQuery(new MenuQuery(event.getCurrentItem(), MenuQuery.Type.USAGES, getBackCall(), false), event);
        }
    }

    @Override
    public @Nullable BackCallable getBackCall() {return query.getBackCall();}

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
        if (recipeIndex < visualizers.size() - 1) setThemedItem(NEXT_BUTTON_POSITION, Items.NEXT);
        else setItem(NEXT_BUTTON_POSITION, (ItemStack) null);

        if (recipeIndex > 0) setThemedItem(PREVIOUS_BUTTON_POSITION, Items.PREVIOUS);
        else setItem(PREVIOUS_BUTTON_POSITION, (ItemStack) null);

        if (query.getBackCall() != null) setThemedItem(BACK_BUTTON_POSITION, Items.BACK);
        else setItem(BACK_BUTTON_POSITION, (ItemStack) null);

        setThemedItem(HELP_BUTTON_POSITION, Items.HELP);
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
        setThemedItem(RECIPE_INFO_POSITION, itemStack);
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
                Component.text("0", RecipesMenu.MAIN_COLOR).font(Key.key("jeiu:font")),
                175
        ).append(Component.translatable("gui.jeiu.recipes_title", NamedTextColor.BLACK).font(NamespacedKey.minecraft("default")));
    }
}
