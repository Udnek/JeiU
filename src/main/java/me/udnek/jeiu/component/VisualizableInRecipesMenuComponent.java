package me.udnek.jeiu.component;

import me.udnek.itemscoreu.customcomponent.CustomComponent;
import me.udnek.itemscoreu.customcomponent.CustomComponentType;
import me.udnek.jeiu.visualizer.abstraction.Visualizer;
import org.jetbrains.annotations.NotNull;

public interface VisualizableInRecipesMenuComponent extends CustomComponent<Object> {

    VisualizableInRecipesMenuComponent DEFAULT = () -> {
        throw new UnsupportedOperationException("This is a default VisualizableComponent");
    };


    @NotNull Visualizer getVisualizer();

    @Override
    default @NotNull CustomComponentType<Object, ?> getType(){
        return Components.VISUALIZABLE_IN_RECIPES_MENU;
    }
}
