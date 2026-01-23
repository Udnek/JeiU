package me.udnek.jeiu.component;

import me.udnek.coreu.custom.component.ConstructableComponentType;
import me.udnek.coreu.custom.component.CustomComponent;
import me.udnek.coreu.custom.component.CustomComponentType;
import me.udnek.coreu.custom.item.CustomItem;
import me.udnek.coreu.custom.recipe.CustomRecipeType;
import me.udnek.coreu.custom.registry.CustomRegistries;
import me.udnek.jeiu.JeiU;


public class Components {

    public static final ConstructableComponentType<CustomRecipeType<?>, VisualizableRecipeType> VISUALIZABLE_RECIPE_TYPE;
    public static final ConstructableComponentType<CustomItem, RecipeAndUsagesItem> RECIPE_AND_USAGES_ITEM;
    public static final CustomComponentType<CustomItem, HiddenItemComponent> HIDDEN_FROM_NORMAL_PLAYERS_ITEM;
    public static final CustomComponentType<CustomItem, TechnicalItemComponent> ALWAYS_HIDDEN_ITEM;

    static {
        VISUALIZABLE_RECIPE_TYPE = register(new ConstructableComponentType<>("visualizable_recipe_type", VisualizableRecipeType.EMPTY));
        RECIPE_AND_USAGES_ITEM = register(new ConstructableComponentType<>("recipe_and_usages_item", RecipeAndUsagesItem.DEFAULT));
        HIDDEN_FROM_NORMAL_PLAYERS_ITEM = register(new ConstructableComponentType<>("hidden_from_normal_players_item", HiddenItemComponent.INSTANCE));
        ALWAYS_HIDDEN_ITEM = register(new ConstructableComponentType<>("always_hidden_item", TechnicalItemComponent.INSTANCE));
    }

    private static <A, B extends CustomComponent<A>, T extends CustomComponentType<A, B>> T register(T type){
        return CustomRegistries.COMPONENT_TYPE.register(JeiU.getInstance(), type);
    }
}
