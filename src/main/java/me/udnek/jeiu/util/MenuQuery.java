package me.udnek.jeiu.util;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MenuQuery {

    private ItemStack itemStack;
    private Type type;
    private BackCallable backCall;
    private boolean openIfNothingFound;

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
        return itemStack.clone();
    }
    public @Nullable BackCallable getBackCall() {return backCall;}
    public void setBackCall(@Nullable BackCallable backCall){this.backCall = backCall;}
    public boolean isOpenIfNothingFound() {return openIfNothingFound;}
    public void setOpenIfNothingFound(boolean openIfNothingFound) {this.openIfNothingFound = openIfNothingFound;}

    public enum Type{
        USAGES,
        RECIPES
    }
}
