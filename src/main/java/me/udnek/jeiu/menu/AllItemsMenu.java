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
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public class AllItemsMenu extends ConstructableCustomInventory implements ClickableMenu {

    public static final int START_INDEX = 9;
    public static final int STOP_INDEX = 9*6-1;
    public static final int ITEMS_PER_PAGE = STOP_INDEX-START_INDEX+1;

    public AllItemsMenu(){

        int i = START_INDEX;
        for (CustomItem customItem : CustomRegistries.ITEM.getAll()) {
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
