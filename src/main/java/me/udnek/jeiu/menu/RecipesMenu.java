package me.udnek.jeiu.menu;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.udnek.coreu.custom.inventory.ConstructableCustomInventory;
import me.udnek.coreu.custom.item.CustomItem;
import me.udnek.coreu.util.ComponentU;
import me.udnek.jeiu.JeiU;
import me.udnek.jeiu.component.Components;
import me.udnek.jeiu.component.RecipeAndUsagesItem;
import me.udnek.jeiu.item.Items;
import me.udnek.jeiu.util.BackCallable;
import me.udnek.jeiu.util.MenuQuery;
import me.udnek.jeiu.event.MenuQueryEvent;
import me.udnek.jeiu.visualizer.Visualizer;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RecipesMenu extends ConstructableCustomInventory implements JeiUMenu {

    public static final int BANNER_AND_INFO_POSITION = 0;
    public static final int HELP_BUTTON_POSITION = 9*1;
    public static final int RECIPE_STATION_POSITION = 9*2;
    public static final int BACK_BUTTON_POSITION = 9*3;
    public static final int PREVIOUS_BUTTON_POSITION = 9*4;
    public static final int NEXT_BUTTON_POSITION = 9*5;
    public static final int VISUALIZER_X_OFFSET = 1;

    private BukkitTask animatorTicker = null;
    private List<Visualizer> recipes;
    private int recipeIndex;
    private Visualizer currentRecipe;

    private MenuQuery query;
    private final Player player;

    public RecipesMenu(@NotNull Player player) {
        this.player = player;
    }

    public @NotNull MenuQuery getQuery() {
        return query;
    }

    @Override
    public void pressedCallback(@NotNull InventoryClickEvent event) {
        BackCallable backCall = query.getCallback();
        if (backCall != null) backCall.callback();
    }
    @Override
    public void pressedNext(@NotNull InventoryClickEvent event) {
        openRecipe(recipeIndex+1);
    }
    @Override
    public void pressedPrevious(@NotNull InventoryClickEvent event) {
        openRecipe(recipeIndex-1);
    }

    protected void openRecipe(int recipeIndex) {
        this.recipeIndex = Math.clamp(recipeIndex, 0, Math.max(0, recipes.size()-1));
        runPage();
    }

    public void runNewQuery(@NotNull MenuQuery query){
        List<Visualizer> newRecipes = new ArrayList<>();

        switch (query.getType()) {
            case USAGES -> {
                CustomItem customItem = CustomItem.get(query.getItemStack());
                if (customItem != null){
                    customItem.getComponents().getOrDefault(Components.RECIPE_AND_USAGES_ITEM)
                            .getUsages(customItem, query.getItemStack(), newRecipes::add);
                } else { // DEFAULT BEHAVIOUR
                    RecipeAndUsagesItem.getRecipesAsIngredient(query.getItemStack(), newRecipes::add);
                }
            }
            case RECIPES -> {
                CustomItem customItem = CustomItem.get(query.getItemStack());
                if (customItem != null) {
                    customItem.getComponents().getOrDefault(Components.RECIPE_AND_USAGES_ITEM)
                            .getRecipes(customItem, query.getItemStack(), newRecipes::add);
                } else { // DEFAULT BEHAVIOUR
                    RecipeAndUsagesItem.getRecipesAsResult(query.getItemStack(), newRecipes::add);
                }
            }
        }

        new MenuQueryEvent(query, newRecipes).callEvent();
        if (newRecipes.isEmpty() && !query.openIfNothingFound) return;
        this.query = query;
        recipes = newRecipes;
        recipeIndex = 0;
        runPage();
        open(player);
    }


    @Override
    public void clickedNonButtonItem(@NotNull InventoryClickEvent event) {
        BackCallable newCallBack = new BackCallable() {
            //int oldRecipeIndex = RecipesMenu.this.recipeIndex;
            final MenuQuery oldQuery = RecipesMenu.this.query;
            @Override
            public void callback() {
                runNewQuery(oldQuery);
            }
        };
        if (event.isLeftClick()) {
            runNewQuery(new MenuQuery(event.getCurrentItem(), MenuQuery.Type.RECIPES, newCallBack, false));
        } else if (event.isRightClick()) {
            runNewQuery(new MenuQuery(event.getCurrentItem(), MenuQuery.Type.USAGES, newCallBack, false));
        }
    }

    public boolean hasViewers() {return !getInventory().getViewers().isEmpty();}

    protected void runPage() {
        if (animatorTicker != null) animatorTicker.cancel();
        getInventory().clear();
        if (!recipes.isEmpty()) {
            currentRecipe = recipes.get(recipeIndex);
            currentRecipe.visualize(this);
        }
        setPageButtons();
        animateRecipes();
    }
    protected void setPageButtons() {
        if (recipeIndex < recipes.size() - 1) setThemedItem(NEXT_BUTTON_POSITION, Items.NEXT);
        else setItem(NEXT_BUTTON_POSITION, (ItemStack) null);

        if (recipeIndex > 0) setThemedItem(PREVIOUS_BUTTON_POSITION, Items.PREVIOUS);
        else setItem(PREVIOUS_BUTTON_POSITION, (ItemStack) null);

        if (query.getCallback() != null) setThemedItem(BACK_BUTTON_POSITION, Items.BACK);
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
