package me.udnek.jeiu.component;

import me.udnek.coreu.custom.component.CustomComponent;
import me.udnek.coreu.custom.component.CustomComponentType;
import me.udnek.coreu.custom.recipe.CustomRecipe;
import me.udnek.coreu.custom.recipe.CustomRecipeType;
import me.udnek.jeiu.visualizer.Visualizer;
import org.jetbrains.annotations.NotNull;

public abstract class VisualizableRecipeType implements CustomComponent<CustomRecipeType<?>> {

    public static VisualizableRecipeType EMPTY = new VisualizableRecipeType() {
        @Override
        public @NotNull Visualizer getVisualizer(@NotNull CustomRecipe recipe) {
            throw new RuntimeException("Do not call default visualizer");
        }
    };

    public abstract @NotNull Visualizer getVisualizer(@NotNull CustomRecipe recipe);

    @Override
    public @NotNull CustomComponentType<? super CustomRecipeType<?>, ? extends CustomComponent<? super CustomRecipeType<?>>> getType() {
        return Components.VISUALIZABLE_RECIPE_TYPE;
    }
}
