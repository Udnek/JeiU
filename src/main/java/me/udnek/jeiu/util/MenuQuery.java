package me.udnek.jeiu.util;

import com.google.common.base.Preconditions;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MenuQuery {

    private final ItemStack itemStack;
    private final Type type;

    public MenuQuery(@NotNull ItemStack itemStack, @NotNull Type type){
        Preconditions.checkArgument(itemStack != null, "Item can not be null!");
        Preconditions.checkArgument(type != null, "Type can not be null!");
        this.itemStack = itemStack;
        this.type = type;
    }
    public Type getType() {
        return type;
    }
    public ItemStack getItemStack() {
        return itemStack.clone();
    }
    public enum Type{
        USAGES,
        RECIPES
    }
}
