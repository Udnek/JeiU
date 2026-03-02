package me.udnek.jeiu.menu;

import me.udnek.coreu.custom.inventory.CustomInventory;
import me.udnek.jeiu.item.Items;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
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
        } else if (Items.BANNER.isThisItem(itemStack)) {
            pressedBanner(event);
        } else if (event.isLeftClick() || event.isRightClick()) {
            clickedNonButtonItem(event);
        }
    }
    void clickedNonButtonItem(InventoryClickEvent event);
    void pressedCallback(InventoryClickEvent event);
    void pressedNext(InventoryClickEvent event);
    void pressedPrevious(InventoryClickEvent event);
    void pressedSwitch(InventoryClickEvent event);
    void pressedBanner(InventoryClickEvent event);
}
