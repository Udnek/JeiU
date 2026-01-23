package me.udnek.jeiu.component;

import me.udnek.coreu.custom.component.CustomComponent;
import me.udnek.coreu.custom.component.CustomComponentType;
import me.udnek.coreu.custom.recipe.CustomRecipeType;
import me.udnek.jeiu.menu.RecipesMenu;
import me.udnek.jeiu.visualizer.Visualizer;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VisualizableRecipeType implements CustomComponent<CustomRecipeType<?>> {

    public static VisualizableRecipeType EMPTY = new VisualizableRecipeType(new Visualizer() {
        @Override
        public void visualize(@NotNull RecipesMenu recipesMenu) {
            throw new RuntimeException("do not use default VisualizableRecipe component");
        }

        @Override
        public @Nullable List<Component> getInformation() {
            return null;
        }

        @Override
        public void tickAnimation() {}
    });

    protected @NotNull Visualizer visualizer;

    public VisualizableRecipeType(@NotNull Visualizer visualizer){
        this.visualizer = visualizer;
    }

    public @NotNull Visualizer getVisualizer() {
        return visualizer;
    }

    @Override
    public @NotNull CustomComponentType<? super CustomRecipeType<?>, ? extends CustomComponent<? super CustomRecipeType<?>>> getType() {
        return Components.VISUALIZABLE_RECIPE_TYPE;
    }
}
