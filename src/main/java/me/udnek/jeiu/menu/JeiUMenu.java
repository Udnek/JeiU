package me.udnek.jeiu.menu;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.DyedItemColor;
import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.jeiu.visualizer.animator.Animator;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public interface JeiUMenu extends ClickableMenu{
    @NotNull TextColor MAIN_COLOR = Objects.requireNonNull(TextColor.fromHexString("#a3806a"));

    default @NotNull ItemStack colorItemToTheme(@NotNull ItemStack itemStack){
        itemStack.setData(DataComponentTypes.DYED_COLOR,
                DyedItemColor.dyedItemColor(Color.fromRGB(MAIN_COLOR.value()), false));
        return itemStack;
    }

    default void setThemedItem(int index, @Nullable ItemStack itemStack){
        if (itemStack == null) setItem(index, (ItemStack) null);
        else setItem(index, colorItemToTheme(itemStack));
    }
    default void setThemedItem(int index, @Nullable CustomItem customItem){
        if (customItem == null) setItem(index, (ItemStack) null);
        else setThemedItem(index, customItem.getItem());
    }

    default void setItem(int index, @Nullable ItemStack itemStack) {
        getInventory().setItem(index, itemStack);
    }
    default void setItem(int index, @Nullable RecipeChoice recipeChoice) {
        if (recipeChoice == null) return;
        setItem(index, recipeChoice.getItemStack());
    }
    default void setItem(int index, @NotNull Material material) {
        if (!material.isItem()) return;
        setItem(index, new ItemStack(material));
    }
    default void setItem(@NotNull Animator animator) {
        setItem(animator.getPosition(), animator.getNextFrame());
    }
    default void setItem(int index, @NotNull CustomItem customItem){
        setItem(index, customItem.getItem());
    }

    @Override
    default boolean shouldAutoUpdateItems() {
        return false;
    }
}
