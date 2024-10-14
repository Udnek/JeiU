package me.udnek.jeiu.component;

import me.udnek.itemscoreu.customcomponent.ConstructableComponentType;
import me.udnek.itemscoreu.customcomponent.CustomComponentType;
import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.customregistry.CustomRegistries;
import me.udnek.jeiu.JeiU;


// TODO: 10/12/2024 USE INSTEADOF HARD INHERITANCE
public class ComponentTypes {
    public static final CustomComponentType<Object, ?>
            VISUALIZABLE_IN_RECIPES_MENU = register(new ConstructableComponentType("visualizable_in_recipes_menu", VisualizableInRecipesMenuComponent.DEFAULT));
    public static final CustomComponentType<CustomItem, ?>
            TECHNICAL_ITEM = register(new ConstructableComponentType("technical_item", TechnicalItemComponent.INSTANCE));

    private static CustomComponentType register(CustomComponentType type){
        return CustomRegistries.COMPONENT_TYPE.register(JeiU.getInstance(), type);
    }
}
