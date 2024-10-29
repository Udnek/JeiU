package me.udnek.jeiu.menu;

import me.udnek.itemscoreu.custominventory.ConstructableCustomInventory;
import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.customregistry.CustomRegistries;
import me.udnek.itemscoreu.util.ComponentU;
import me.udnek.jeiu.component.ComponentTypes;
import me.udnek.jeiu.item.Items;
import me.udnek.jeiu.util.BackCallable;
import me.udnek.jeiu.util.MenuQuery;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AllItemsMenu extends ConstructableCustomInventory implements JeiUMenu {

    public static final int PREVIOUS_BUTTON_POSITION = 7;
    public static final int NEXT_BUTTON_POSITION = 8;

    public static final int START_SLOT = 9;
    public static final int STOP_SLOT = 9*6-1;
    public static final int ITEMS_PER_PAGE = STOP_SLOT - START_SLOT +1;

    int firstItemIndex;
    int lastItemIndex;


    public AllItemsMenu(){
        showItems(0);
    }

    public List<CustomItem> getAll(){
        List<CustomItem> all = new ArrayList<>();
        CustomRegistries.ITEM.getAll(customItem -> {
            if (!customItem.hasComponent(ComponentTypes.TECHNICAL_ITEM)) all.add(customItem);
        });
        return all;
    }

    public void showItems(int startIndex){
        inventory.clear();

        List<CustomItem> all = getAll();
        all.sort((o1, o2) -> String.CASE_INSENSITIVE_ORDER.compare(o1.getId(), o2.getId()));

        int index = startIndex;
        int slot = START_SLOT;
        firstItemIndex = index;
        while (index < all.size()) {
            if (slot > STOP_SLOT) break;
            CustomItem customItem = all.get(index);
            setItem(slot, customItem);
            slot++;
            index++;
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
        List<CustomItem> items = getAll();
        if (lastItemIndex < items.size()-1) setThemedItem(NEXT_BUTTON_POSITION, Items.NEXT_BUTTON);
        if (lastItemIndex - ITEMS_PER_PAGE >= 0) setThemedItem(PREVIOUS_BUTTON_POSITION, Items.PREVIOUS_BUTTON);
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
    public void runNewQuery(@NotNull MenuQuery menuQuery, @Nullable InventoryClickEvent event) {
        if (event == null) return;
        menuQuery.setBackCall(() -> new AllItemsMenu().open((Player) event.getWhoClicked()));
        new RecipesMenu((Player) event.getWhoClicked()).runNewQuery(menuQuery, event);
    }

    @Override
    public void openPrevious(@NotNull InventoryClickEvent event) {
        showItems(firstItemIndex-ITEMS_PER_PAGE);
    }
    @Override
    public void openNext(@NotNull InventoryClickEvent event) {
        showItems(lastItemIndex+1);
    }
    @Override
    public void openBack(@NotNull InventoryClickEvent event) {}

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
}
