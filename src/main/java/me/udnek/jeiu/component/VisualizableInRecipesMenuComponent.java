package me.udnek.jeiu.component;

import me.udnek.itemscoreu.customcomponent.CustomComponent;
import me.udnek.itemscoreu.customcomponent.CustomComponentType;
import me.udnek.jeiu.menu.RecipesMenu;
import me.udnek.jeiu.visualizer.abstraction.Visualizer;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface VisualizableInRecipesMenuComponent extends CustomComponent<Object> {

    VisualizableInRecipesMenuComponent DEFAULT = new VisualizableInRecipesMenuComponent() {
        @Override
        public @NotNull Visualizer getVisualizer() {
            throw new UnsupportedOperationException("This is a default VisualizableComponent");
        }
    };


    @NotNull Visualizer getVisualizer();

    @Override
    default @NotNull CustomComponentType<Object, ?> getType(){
        return ComponentTypes.VISUALIZABLE_IN_RECIPES_MENU;
    }
}
