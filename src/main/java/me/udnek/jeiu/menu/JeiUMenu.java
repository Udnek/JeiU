package me.udnek.jeiu.menu;

import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.jeiu.util.RecipeChoiceAnimator;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.meta.ColorableArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;

public interface JeiUMenu extends ClickableMenu{
    @NotNull TextColor MAIN_COLOR = Objects.requireNonNull(TextColor.fromHexString("#a3806a"));

    default @NotNull ItemStack colorItemToTheme(@NotNull ItemStack itemStack){
        itemStack.editMeta(ColorableArmorMeta.class, colorableArmorMeta ->
                colorableArmorMeta.setColor(Color.fromRGB(MAIN_COLOR.value())));
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
    default void setItem(@NotNull RecipeChoiceAnimator animator) {
        setItem(animator.getPosition(), animator.getFrame());
    }
    default void setItem(int index, @NotNull CustomItem customItem){
        setItem(index, customItem.getItem());
    }
}
