package me.udnek.jeiu.menu;

import me.udnek.coreu.custom.inventory.CustomInventory;
import me.udnek.jeiu.item.Items;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface ClickableMenu extends CustomInventory {
    @Override
    default void onPlayerClicksItem(InventoryClickEvent event) {
        ItemStack itemStack = event.getCurrentItem();
        if (itemStack == null) return;

        event.setCancelled(true);

        if (Items.BACK.isThisItem(itemStack)) {
            pressedCallback(event);
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
    void clickedNonButtonItem(@NotNull InventoryClickEvent event);
    void pressedCallback(@NotNull InventoryClickEvent event);
    void pressedNext(@NotNull InventoryClickEvent event);
    void pressedPrevious(@NotNull InventoryClickEvent event);
    void pressedSwitch(@NotNull InventoryClickEvent event);
}
