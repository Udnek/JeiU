package me.udnek.jeiu.menu;

import io.papermc.paper.datacomponent.DataComponentTypes;
import me.udnek.itemscoreu.custominventory.ConstructableCustomInventory;
import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.customitem.ItemUtils;
import me.udnek.itemscoreu.customitem.VanillaItemManager;
import me.udnek.itemscoreu.customrecipe.CustomRecipe;
import me.udnek.itemscoreu.customrecipe.RecipeManager;
import me.udnek.itemscoreu.customregistry.CustomRegistries;
import me.udnek.itemscoreu.util.ComponentU;
import me.udnek.jeiu.JeiU;
import me.udnek.jeiu.component.ComponentTypes;
import me.udnek.jeiu.item.Items;
import me.udnek.jeiu.util.BackCallable;
import me.udnek.jeiu.util.MenuQuery;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AllItemsMenu extends ConstructableCustomInventory implements JeiUMenu {

    public static final int PREVIOUS_BUTTON_POSITION = 9-1;
    public static final int NEXT_BUTTON_POSITION = 2*9-1;
    public static final int SWITCH_BUTTON_POSITION = 3*9-1;

    public static final int MAX_COLUMN = 9-2;
    public static final int ITEMS_PER_PAGE = 8*6;

    protected int firstItemIndex;
    protected int lastItemIndex;

    protected Mode mode = Mode.CUSTOM_ITEMS;

    public void openAndShow(@NotNull Player player){
        open(player);
        showItems(0);
    }

    public @NotNull List<ItemStack> getAll(){
        return mode.getAll(this);
    }

    public void showItems(int startIndex){
        inventory.clear();

        List<ItemStack> all = getAll();
        all.sort((o1, o2) -> String.CASE_INSENSITIVE_ORDER.compare(ItemUtils.getId(o1), ItemUtils.getId(o2)));

        int column = 0;
        int row = 0;
        int index = startIndex;
        firstItemIndex = index;
        while (index < all.size()) {
            if (row*9+column >= getInventorySize()) break;
            ItemStack customItem = all.get(index);
            setItem(row*9+column, customItem);

            column++;
            index++;
            if (column > MAX_COLUMN){
                column = 0;
                row++;
            }

        }
        lastItemIndex = index-1;
        setButtons();
    }

    @Override
    public void onPlayerClicksItem(InventoryClickEvent event) {
        if (event.isShiftClick() && event.getWhoClicked().getGameMode() == GameMode.CREATIVE && event.getCurrentItem() != null){
            event.getWhoClicked().getInventory().addItem(event.getCurrentItem());
            event.setCancelled(true);
            return;
        }
        JeiUMenu.super.onPlayerClicksItem(event);
    }

    public void setButtons(){
        List<ItemStack> items = getAll();
        if (lastItemIndex < items.size()-1) setThemedItem(NEXT_BUTTON_POSITION, Items.NEXT);
        if (lastItemIndex - ITEMS_PER_PAGE >= 0) setThemedItem(PREVIOUS_BUTTON_POSITION, Items.PREVIOUS);
        setThemedItem(SWITCH_BUTTON_POSITION, mode.getIcon(this));
    }

    @Override
    public void clickedNonButtonItem(@NotNull InventoryClickEvent event) {
        if (event.isLeftClick()) {
            runNewQuery(new MenuQuery(event.getCurrentItem(), MenuQuery.Type.RECIPES, getBackCall(), true), event);
        } else if (event.isRightClick()) {
            runNewQuery(new MenuQuery(event.getCurrentItem(), MenuQuery.Type.USAGES, getBackCall(), true), event);
        }
    }

    @Override
    public void pressedSwitch(@NotNull InventoryClickEvent event) {
        mode = mode.next();
        showItems(0);
    }

    @Override
    public void runNewQuery(@NotNull MenuQuery menuQuery, @Nullable InventoryClickEvent event) {
        if (event == null) return;
        menuQuery.setBackCall(() -> {
            AllItemsMenu menu = new AllItemsMenu();
            menu.mode = this.mode;
            menu.open((Player) event.getWhoClicked());
            menu.showItems(firstItemIndex);
        });
        new RecipesMenu((Player) event.getWhoClicked()).runNewQuery(menuQuery, event);
    }

    @Override
    public void pressedPrevious(@NotNull InventoryClickEvent event) {
        showItems(firstItemIndex-ITEMS_PER_PAGE);
    }
    @Override
    public void pressedNext(@NotNull InventoryClickEvent event) {
        showItems(lastItemIndex+1);
    }
    @Override
    public void pressedBack(@NotNull InventoryClickEvent event) {}
    @Override
    public @Nullable BackCallable getBackCall() {return null;}
    @Override
    public int getInventorySize() {
        return 9*6;
    }
    @Override
    public Component getDisplayName() {
        return ComponentU.textWithNoSpace(
                -8,
                Component.text("1", RecipesMenu.MAIN_COLOR).font(Key.key("jeiu:font")),
                175
        ).append(Component.translatable("gui.jeiu.items_title", NamedTextColor.BLACK).font(NamespacedKey.minecraft("default")));
    }

    public enum Mode{
        CUSTOM_ITEMS {
            @Override
            public @NotNull List<ItemStack> getAll(@NotNull AllItemsMenu context) {
                boolean forceShowHidden = context.inventory.getViewers().stream().anyMatch(humanEntity -> humanEntity.getGameMode() == GameMode.CREATIVE);

                List<ItemStack> all = new ArrayList<>();
                CustomRegistries.ITEM.getAll(customItem -> {
                    if (customItem.getComponents().has(ComponentTypes.TECHNICAL_ITEM)) return;
                    if (!forceShowHidden && customItem.getComponents().has(ComponentTypes.HIDDEN_ITEM)) return;
                    all.add(customItem.getItem());
                });

                return all;
            }

            @Override
            public @NotNull ItemStack getIcon(@NotNull AllItemsMenu context) {
                ItemStack item = Items.SWITCH.getItem();
                item.setData(DataComponentTypes.ITEM_NAME, Component.translatable("item.jeiu.custom_items"));
                item.setData(DataComponentTypes.ITEM_MODEL, new NamespacedKey(JeiU.getInstance(), "custom_items"));
                return item;
            }
        },
        RECIPES {
            @Override
            public @NotNull List<ItemStack> getAll(@NotNull AllItemsMenu context) {
                List<Recipe> recipes = new ArrayList<>();
                RecipeManager.getInstance().getAll(recipes::add);

                List<ItemStack> stacks = new ArrayList<>();

                Set<Material> materials = new HashSet<>();
                Set<CustomItem> customItems = new HashSet<>();
                for (Recipe recipe : recipes) {
                    if (!(recipe instanceof Keyed keyed)) continue;
                    if (keyed.key().namespace().equals(NamespacedKey.MINECRAFT_NAMESPACE)) continue;

                    List<ItemStack> toProceed = new ArrayList<>();

                    if (recipe instanceof CustomRecipe<?> customRecipe){
                        toProceed.addAll(customRecipe.getResults());
                    } else {
                        toProceed.add(recipe.getResult());
                    }

                    for (ItemStack itemStack : toProceed) {
                        CustomItem customItem = CustomItem.get(itemStack);
                        if (customItem != null) {
                            if (VanillaItemManager.isReplaced(customItem)) customItems.add(customItem);
                        } else {
                            if (itemStack.getType() != Material.ENCHANTED_BOOK) materials.add(itemStack.getType());
                        }
                    }
                }
                for (Material material : materials) stacks.add(new ItemStack(material));
                for (CustomItem customItem : customItems) stacks.add(customItem.getItem());
                return stacks;
            }

            @Override
            public @NotNull ItemStack getIcon(@NotNull AllItemsMenu context) {
                ItemStack item = Items.SWITCH.getItem();
                item.setData(DataComponentTypes.ITEM_NAME, Component.translatable("item.jeiu.custom_recipes"));
                item.setData(DataComponentTypes.ITEM_MODEL, new NamespacedKey(JeiU.getInstance(), "custom_recipes"));
                return item;
            }
        },
        ENCHANTED_BOOKS {
            @Override
            public @NotNull List<ItemStack> getAll(@NotNull AllItemsMenu context) {
                List<Recipe> recipes = new ArrayList<>();
                RecipeManager.getInstance().getAll(recipes::add);

                List<ItemStack> stacks = new ArrayList<>();
                for (Recipe recipe : recipes) {
                    List<ItemStack> toProceed = new ArrayList<>();
                    if (recipe instanceof CustomRecipe<?> customRecipe){
                        toProceed.addAll(customRecipe.getResults());
                    } else {
                        toProceed.add(recipe.getResult());
                    }

                    for (ItemStack itemStack : toProceed) {
                        if (itemStack.getType() == Material.ENCHANTED_BOOK) stacks.add(itemStack);
                    }
                }
                return stacks;
            }

            @Override
            public @NotNull ItemStack getIcon(@NotNull AllItemsMenu context) {
                ItemStack item = Items.SWITCH.getItem();
                item.setData(DataComponentTypes.ITEM_NAME, Component.translatable("item.jeiu.enchanted_books"));
                item.setData(DataComponentTypes.ITEM_MODEL, new NamespacedKey(JeiU.getInstance(), "enchanted_books"));
                return item;
            }
        };

        public static @NotNull Mode next(@NotNull Mode current){
            return switch (current) {
                case CUSTOM_ITEMS -> RECIPES;
                case RECIPES -> ENCHANTED_BOOKS;
                case ENCHANTED_BOOKS -> CUSTOM_ITEMS;
            };
        }

        public abstract @NotNull List<ItemStack> getAll(@NotNull AllItemsMenu context);
        public abstract @NotNull ItemStack getIcon(@NotNull AllItemsMenu context);
        public @NotNull Mode next(){
            return next(this);
        }
    }
}
