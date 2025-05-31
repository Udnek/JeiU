package me.udnek.jeiu.component;

import me.udnek.coreu.custom.component.ConstructableComponentType;
import me.udnek.coreu.custom.component.CustomComponent;
import me.udnek.coreu.custom.component.CustomComponentType;
import me.udnek.coreu.custom.item.CustomItem;
import me.udnek.coreu.custom.item.CustomItemComponent;
import me.udnek.coreu.custom.registry.CustomRegistries;
import me.udnek.jeiu.JeiU;


public class Components {
    // TODO: 10/12/2024 USE INSTEADOF HARD INHERITANCE
    public static final CustomComponentType<Object, ?> VISUALIZABLE_IN_RECIPES_MENU;
    public static final CustomComponentType<CustomItem, CustomItemComponent> HIDDEN_ITEM;
    public static final CustomComponentType<CustomItem, CustomItemComponent> TECHNICAL_ITEM;

    static {
        VISUALIZABLE_IN_RECIPES_MENU = register(new ConstructableComponentType<>("visualizable_in_recipes_menu", VisualizableInRecipesMenuComponent.DEFAULT));
        HIDDEN_ITEM = register(new ConstructableComponentType<>("hidden_item", HiddenItemComponent.INSTANCE));
        TECHNICAL_ITEM = register(new ConstructableComponentType<>("technical_item", TechnicalItemComponent.INSTANCE));
    }



    private static <A, B extends CustomComponent<A>, T extends CustomComponentType<A, B>> T register(T type){
        return CustomRegistries.COMPONENT_TYPE.register(JeiU.getInstance(), type);
    }
}
