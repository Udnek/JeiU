package me.udnek.jeiu.util;

import me.udnek.coreu.custom.event.CustomEvent;
import me.udnek.jeiu.visualizer.Visualizable;
import me.udnek.jeiu.visualizer.Visualizer;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MenuQueryEvent extends CustomEvent {
    protected static final HandlerList HANDLER_LIST = CustomEvent.HANDLER_LIST;

    private final @NotNull MenuQuery query;
    private final @NotNull List<Visualizer> allRecipes;

    public MenuQueryEvent(@NotNull MenuQuery query, @NotNull List<Visualizer> visualizers){
        this.query = query;
        this.allRecipes = visualizers;
    }
    public @NotNull List<Visualizer> getAllRecipes() {
        return allRecipes;
    }
    public @NotNull MenuQuery getQuery(){
        return query;
    }
}
