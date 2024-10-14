package me.udnek.jeiu.menu;

import me.udnek.itemscoreu.custominventory.ConstructableCustomInventory;
import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.customregistry.CustomRegistries;
import me.udnek.itemscoreu.utils.ComponentU;
import me.udnek.jeiu.component.ComponentTypes;
import me.udnek.jeiu.util.BackCallable;
import me.udnek.jeiu.util.MenuQuery;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.apache.commons.lang3.ArraySorter;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class AllItemsMenu extends ConstructableCustomInventory implements ClickableMenu {

    public static final int START_INDEX = 9;
    public static final int STOP_INDEX = 9*6-1;
    public static final int ITEMS_PER_PAGE = STOP_INDEX-START_INDEX+1;

    @Override
    public void onPlayerClicksItem(InventoryClickEvent event) {
        if (event.isShiftClick() && event.getWhoClicked().getGameMode() == GameMode.CREATIVE && event.getCurrentItem() != null){
            event.getWhoClicked().getInventory().addItem(event.getCurrentItem());
            event.setCancelled(true);
            return;
        }
        ClickableMenu.super.onPlayerClicksItem(event);
    }

    public AllItemsMenu(){

        int i = START_INDEX;
        List<CustomItem> all = new ArrayList<>(CustomRegistries.ITEM.getAll());
        all.sort((o1, o2) -> String.CASE_INSENSITIVE_ORDER.compare(o1.getId(), o2.getId()));
        for (CustomItem customItem : all) {
            if (i > STOP_INDEX) break;
            if (customItem.hasComponent(ComponentTypes.TECHNICAL_ITEM)) continue;

            inventory.setItem(i, customItem.getItem());
            i++;
        }

    }

    @Override
    public void runNewQuery(@NotNull MenuQuery menuQuery, @NotNull InventoryClickEvent event) {
        new RecipesMenu(menuQuery, (Player) event.getWhoClicked(), new BackCallable() {
            @Override
            public void backCall() {
                new AllItemsMenu().open(((Player) event.getWhoClicked()).getPlayer());
            }
        });
    }

    @Override
    public void openPrevious(@NotNull InventoryClickEvent event) {}
    @Override
    public void openNext(@NotNull InventoryClickEvent event) {}
    @Override
    public void openBack(@NotNull InventoryClickEvent event) {}

    @Override
    public int getInventorySize() {
        return 9*6;
    }
    @Override
    public Component getDisplayName() {
        return ComponentU.textWithNoSpace(
                -8,
                Component.text("1", TextColor.color(1f, 1f, 1f)).font(Key.key("jeiu:font")),
                175
        ).append(Component.translatable("gui.jeiu.items_title", TextColor.color(0, 0, 0)).font(NamespacedKey.minecraft("default")));
    }
}
