package me.udnek.jeiu.menu;

import me.udnek.coreu.custom.inventory.ConstructableCustomInventory;
import me.udnek.coreu.custom.item.ItemUtils;
import me.udnek.coreu.util.ComponentU;
import me.udnek.jeiu.item.Items;
import me.udnek.jeiu.util.BackCallable;
import me.udnek.jeiu.util.MenuQuery;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AllItemsMenu extends ConstructableCustomInventory implements JeiUMenu {

    public static final int SWITCH_BUTTON_POSITION = 3*9;
    public static final int PREVIOUS_BUTTON_POSITION = 4*9;
    public static final int NEXT_BUTTON_POSITION = 5*9;

    public static final int CONTENT_OFFSET_X = 1;

    public static final int MAX_COLUMN = 9-2;
    public static final int ITEMS_PER_PAGE = 8*6;

    protected int firstItemIndex;
    protected int lastItemIndex;

    protected Category category = Category.ALL_ITEMS;

    public void openAndShow(@NotNull Player player){
        open(player);
        showItems(0);
    }

    public @NotNull List<ItemStack> getAll(){
        return category.getAll(this);
    }

    public void showItems(int startIndex){
        getInventory().clear();

        List<ItemStack> all = getAll();
        all.sort((o1, o2) -> String.CASE_INSENSITIVE_ORDER.compare(ItemUtils.getId(o1), ItemUtils.getId(o2)));

        int column = 0;
        int row = 0;
        int index = startIndex;
        firstItemIndex = index;
        while (index < all.size()) {
            if (row*9+column >= getInventorySize()) break;
            ItemStack customItem = all.get(index);
            setItem(row*9+column+CONTENT_OFFSET_X, customItem);

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
    public void onPlayerClicksItem(@NotNull InventoryClickEvent event) {
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
        setThemedItem(SWITCH_BUTTON_POSITION, category.getIcon(this));
    }

    @Override
    public void clickedNonButtonItem(@NotNull InventoryClickEvent event) {
        if (event.isLeftClick()) {
            runNewQuery(event.getCurrentItem(), MenuQuery.Type.RECIPES, event);
        } else if (event.isRightClick()) {
            runNewQuery(event.getCurrentItem(), MenuQuery.Type.USAGES, event);
        }
    }

    @Override
    public void pressedSwitch(@NotNull InventoryClickEvent event) {
        int index = Category.REGISTRY.getIndex(category);
        if (index < Category.REGISTRY.getAll().size()-1) index++;
        else index = 0;
        category = Category.REGISTRY.get(index);
        showItems(0);
    }


    public void runNewQuery(@NotNull ItemStack stack, @NotNull MenuQuery.Type type, @NotNull InventoryClickEvent event) {
        var query = new MenuQuery(
                stack,
                type,
                () -> {
                    AllItemsMenu menu = new AllItemsMenu();
                    menu.category = this.category;
                    menu.open((Player) event.getWhoClicked());
                    menu.showItems(firstItemIndex);
                },
                true
        );
        new RecipesMenu((Player) event.getWhoClicked()).runNewQuery(query);
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
    public void pressedCallback(@NotNull InventoryClickEvent event) {}
    @Override
    public int getInventorySize() {
        return 9*6;
    }
    @Override
    public @Nullable Component getTitle() {
        return ComponentU.textWithNoSpaceSpaceFont(
                -8,
                Component.text("1", RecipesMenu.MAIN_COLOR).font(Key.key("jeiu:font")),
                175
        ).append(Component.translatable("gui.jeiu.items_title", NamedTextColor.BLACK).font(NamespacedKey.minecraft("default")));
    }
}
