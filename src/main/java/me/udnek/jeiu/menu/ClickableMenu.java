package me.udnek.jeiu.menu;

import me.udnek.itemscoreu.custominventory.CustomInventory;
import me.udnek.jeiu.item.Items;
import me.udnek.jeiu.util.BackCallable;
import me.udnek.jeiu.util.MenuQuery;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ClickableMenu extends CustomInventory {
    @Override
    default void onPlayerClicksItem(InventoryClickEvent event) {
        ItemStack itemStack = event.getCurrentItem();
        if (itemStack == null) return;

        event.setCancelled(true);

        if (Items.BACK_BUTTON.isThisItem(itemStack)) {
            openBack(event);
        } else if (Items.NEXT_BUTTON.isThisItem(itemStack)) {
            openNext(event);
        } else if (Items.PREVIOUS_BUTTON.isThisItem(itemStack)) {
            openPrevious(event);
        } else if (event.isLeftClick()) {
            clickedNonButtonItem(event);
        } else if (event.isRightClick()) {
            clickedNonButtonItem(event);
        }
    }

    @Nullable BackCallable getBackCall();
    void clickedNonButtonItem(@NotNull InventoryClickEvent event);
    void openBack(@NotNull InventoryClickEvent event);
    void openNext(@NotNull InventoryClickEvent event);
    void openPrevious(@NotNull InventoryClickEvent event);
    void runNewQuery(@NotNull MenuQuery menuQuery, @Nullable InventoryClickEvent event);
}
