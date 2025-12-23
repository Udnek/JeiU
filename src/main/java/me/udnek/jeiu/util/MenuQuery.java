package me.udnek.jeiu.util;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MenuQuery {

    private final ItemStack itemStack;
    private final Type type;
    private BackCallable backCall;
    public final boolean openIfNothingFound;

    public MenuQuery(@NotNull ItemStack itemStack, @NotNull Type type, @Nullable BackCallable backCall, boolean openIfNothingFound){
        this.itemStack = itemStack;
        this.type = type;
        this.backCall = backCall;
        this.openIfNothingFound = openIfNothingFound;
    }

    public MenuQuery(@NotNull ItemStack itemStack, @NotNull Type type, boolean openIfNothingFound){
        this(itemStack, type, null, openIfNothingFound);
    }

    public @NotNull Type getType() {
        return type;
    }
    public @NotNull ItemStack getItemStack() {
        return itemStack;
    }
    public @Nullable BackCallable getCallback() {
        return backCall;
    }
    public void setBackCall(@Nullable BackCallable backCall){
        this.backCall = backCall;
    }

    public enum Type{
        USAGES,
        RECIPES
    }
}
