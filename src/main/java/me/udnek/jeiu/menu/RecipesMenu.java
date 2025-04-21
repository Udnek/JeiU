package me.udnek.jeiu.menu;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.udnek.itemscoreu.custominventory.ConstructableCustomInventory;
import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.customitem.ItemUtils;
import me.udnek.itemscoreu.customloot.LootTableUtils;
import me.udnek.itemscoreu.customrecipe.RecipeManager;
import me.udnek.itemscoreu.util.ComponentU;
import me.udnek.jeiu.JeiU;
import me.udnek.jeiu.component.Components;
import me.udnek.jeiu.item.Items;
import me.udnek.jeiu.util.BackCallable;
import me.udnek.jeiu.util.MenuQuery;
import me.udnek.jeiu.util.MenuQueryEvent;
import me.udnek.jeiu.util.Utils;
import me.udnek.jeiu.visualizer.RepairVisualizer;
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
import org.bukkit.loot.LootTable;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RecipesMenu extends ConstructableCustomInventory implements JeiUMenu {

    private static final int BACK_BUTTON_POSITION = 9*1-1;
    private static final int BANNER_AND_INFO_POSITION = 9*2-1;
    private static final int RECIPE_STATION_POSITION = 9*3-1;
    private static final int HELP_BUTTON_POSITION = 9*4-1;
    private static final int PREVIOUS_BUTTON_POSITION = 9*5-1;
    private static final int NEXT_BUTTON_POSITION = 9*6-1;

    private BukkitTask animatorTicker = null;
    private List<Visualizable> visualizables;
    private int recipeIndex;
    private Visualizer currentRecipe;

    private MenuQuery query;
    private final Player player;

    public RecipesMenu(@NotNull Player player) {
        this.player = player;
    }

    @Override
    public void pressedBack(@NotNull InventoryClickEvent event) {
        BackCallable backCall = query.getBackCall();
        if (backCall != null) backCall.backCall();
    }
    @Override
    public void pressedNext(@NotNull InventoryClickEvent event) {openRecipeNumber(recipeIndex+1);}
    @Override
    public void pressedPrevious(@NotNull InventoryClickEvent event) {openRecipeNumber(recipeIndex-1);}

    protected void openRecipeNumber(int recipeIndex) {
        this.recipeIndex = Math.clamp(recipeIndex, 0, Math.max(0, visualizables.size()-1));
        runPage();
    }

    private boolean isTechnical(@Nullable ItemStack itemStack){
        if (itemStack == null) return false;
        return CustomItem.isCustom(itemStack) && CustomItem.get(itemStack).getComponents().has(Components.TECHNICAL_ITEM);
    }

    @Override
    public void runNewQuery(@NotNull MenuQuery query, @Nullable InventoryClickEvent event){
        List<Visualizable> newRecipes = new ArrayList<>();

        List<Recipe> rawRecipes = new ArrayList<>();
        if (query.getType() == MenuQuery.Type.USAGES){
            RecipeManager.getInstance().getRecipesAsIngredient(query.getItemStack(), rawRecipes::add);
            Utils.toVisualizables(rawRecipes, List.of(), newRecipes::add);
        } else {
            List<LootTable> lootTables = LootTableUtils.getWhereItemOccurs(query.getItemStack());
            RecipeManager.getInstance().getRecipesAsResult(query.getItemStack(), rawRecipes::add);
            Utils.toVisualizables(rawRecipes, lootTables, newRecipes::add);
            if (ItemUtils.isRepairable(query.getItemStack())){
                newRecipes.add(new Visualizable.Simple(new RepairVisualizer(query.getItemStack())));
            }
        }


        new MenuQueryEvent(query, newRecipes).callEvent();
        if (newRecipes.isEmpty() && !query.isOpenIfNothingFound()) return;
        this.query = query;
        visualizables = newRecipes;
        recipeIndex = 0;
        runPage();
        open(player);

    }


    @Override
    public void clickedNonButtonItem(@NotNull InventoryClickEvent event) {
        if (isTechnical(event.getCurrentItem())) return;
        if (event.isLeftClick()) {
            runNewQuery(new MenuQuery(event.getCurrentItem(), MenuQuery.Type.RECIPES, getBackCall(), false), event);
        } else if (event.isRightClick()) {
            runNewQuery(new MenuQuery(event.getCurrentItem(), MenuQuery.Type.USAGES, getBackCall(), false), event);
        }
    }

    @Override
    public @Nullable BackCallable getBackCall() {return query.getBackCall();}

    public boolean hasViewers() {return !getInventory().getViewers().isEmpty();}
    protected void runPage() {
        if (animatorTicker != null) animatorTicker.cancel();
        getInventory().clear();
        if (!visualizables.isEmpty()) {
            currentRecipe = visualizables.get(recipeIndex).getVisualizer();
            currentRecipe.visualize(this);
        }
        setPageButtons();
        animateRecipes();
    }
    protected void setPageButtons() {
        if (recipeIndex < visualizables.size() - 1) setThemedItem(NEXT_BUTTON_POSITION, Items.NEXT);
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
        ItemStack item = getInventory().getItem(getBannerPosition());
        if (item == null) item = Items.BANNER.getItem();

        List<Component> formattedInformation = new ArrayList<>();
        for (Component component : information) {
            if (component.color() == null) component = component.color(NamedTextColor.GRAY);
            formattedInformation.add(component);
        }
        item.setData(DataComponentTypes.LORE, ItemLore.lore(formattedInformation));
        setThemedItem(getBannerPosition(), item);
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

    public static int getBannerPosition() {return BANNER_AND_INFO_POSITION;}
    public static int getRecipeStationPosition() {return RECIPE_STATION_POSITION;}

    @Override
    public void pressedSwitch(@NotNull InventoryClickEvent event) {}
    @Override
    public int getInventorySize() {return 9 * 6;}
    @Override
    public @Nullable Component getTitle() {
        return ComponentU.textWithNoSpaceSpaceFont(
                -8,
                Component.text("0", RecipesMenu.MAIN_COLOR).font(Key.key("jeiu:font")),
                175
        ).append(Component.translatable("gui.jeiu.recipes_title", NamedTextColor.BLACK).font(NamespacedKey.minecraft("default")));
    }
}
