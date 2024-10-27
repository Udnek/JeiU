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
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public class AllItemsMenu extends ConstructableCustomInventory implements ClickableMenu {

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
        ClickableMenu.super.onPlayerClicksItem(event);
    }

    public void setButtons(){
        List<CustomItem> items = getAll();
        if (lastItemIndex < items.size()-1) setItem(NEXT_BUTTON_POSITION, Items.NEXT_BUTTON);
        if (lastItemIndex - ITEMS_PER_PAGE >= 0) setItem(PREVIOUS_BUTTON_POSITION, Items.PREVIOUS_BUTTON);
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

    public void setItem(int index, @Nullable ItemStack itemStack) {
        inventory.setItem(index, itemStack);
    }
    public void setItem(int index, @NotNull Material material) {
        if (!material.isItem()) return;
        setItem(index, new ItemStack(material));
    }
    public void setItem(int index, @NotNull CustomItem customItem){
        setItem(index, customItem.getItem());
    }

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
