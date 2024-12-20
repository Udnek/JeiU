package me.udnek.jeiu.visualizer.animator;

import com.google.common.base.Preconditions;
import me.udnek.itemscoreu.customrecipe.choice.CustomRecipeChoice;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ListAnimator implements Animator{

    protected int position;
    protected int frameIndex = -1;
    protected @NotNull List<ItemStack> items = new ArrayList<>();

    public ListAnimator(int position){
        this.position = position;
    }

    public void clear(){
        items.clear();
    }

    public void add(@NotNull ItemStack itemStack){
        items.add(itemStack);
    }

    public void addAll(@NotNull Iterable<ItemStack> itemStacks){
        for (ItemStack stack : itemStacks) items.add(stack);
    }


    protected int getNewIndex(){
        frameIndex ++;
        if (frameIndex > items.size()-1) frameIndex = 0;
        return frameIndex;
    }

    @Override
    public @Nullable ItemStack getNextFrame(){
        return items.get(getNewIndex()).clone();
    }


    @Override
    public int getPosition() {
        return position;
    }
}
