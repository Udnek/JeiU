package me.udnek.jeiu.util;

import me.udnek.itemscoreu.customevent.CustomEvent;
import me.udnek.jeiu.visualizer.abstraction.Visualizable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MenuQueryEvent extends CustomEvent {
    protected static final HandlerList HANDLER_LIST = CustomEvent.HANDLER_LIST;

    private final @NotNull MenuQuery query;
    private final @NotNull List<Visualizable> allRecipes;

    public MenuQueryEvent(@NotNull MenuQuery query, @NotNull List<Visualizable> allRecipes){
        this.query = query;
        this.allRecipes = allRecipes;
    }
    public @NotNull List<Visualizable> getAllRecipes() {return allRecipes;}
    public @NotNull MenuQuery getQuery(){
        return query;
    }
}
