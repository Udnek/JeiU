package me.udnek.jeiu.visualizer.abstraction;

import me.udnek.jeiu.menu.RecipesMenu;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface Visualizer {
    void visualize(@NotNull RecipesMenu recipesMenu);
    @Nullable List<Component> getInformation();
    void tickAnimation();
}
