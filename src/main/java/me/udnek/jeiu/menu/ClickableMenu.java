package me.udnek.jeiu.menu;

import me.udnek.coreu.custom.inventory.CustomInventory;
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

        if (Items.BACK.isThisItem(itemStack)) {
            pressedBack(event);
        } else if (Items.NEXT.isThisItem(itemStack)) {
            pressedNext(event);
        } else if (Items.PREVIOUS.isThisItem(itemStack)) {
            pressedPrevious(event);
        } else if (Items.SWITCH.isThisItem(itemStack)) {
            pressedSwitch(event);
        } else if (event.isLeftClick() || event.isRightClick()) {
            clickedNonButtonItem(event);
        }
    }

    @Nullable BackCallable getBackCall();
    void clickedNonButtonItem(@NotNull InventoryClickEvent event);
    void pressedBack(@NotNull InventoryClickEvent event);
    void pressedNext(@NotNull InventoryClickEvent event);
    void pressedPrevious(@NotNull InventoryClickEvent event);
    void pressedSwitch(@NotNull InventoryClickEvent event);
    void runNewQuery(@NotNull MenuQuery menuQuery, @Nullable InventoryClickEvent event);
}
