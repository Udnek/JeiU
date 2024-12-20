package me.udnek.jeiu.visualizer.animator;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface Animator {

    @Nullable ItemStack getNextFrame();

    int getPosition();
}
