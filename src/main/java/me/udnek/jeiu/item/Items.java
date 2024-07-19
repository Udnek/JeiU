package me.udnek.jeiu.item;

import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.customitem.CustomItemRegistry;
import me.udnek.jeiu.JeiU;

public class Items {
    public static final CustomItem HELP = register(new HelpItem());
    public static final CustomItem FIRE_ICON = register(new FireIconItem());
    public static final CustomItem PREVIOUS_BUTTON = register(new PreviousRecipeItem());
    public static final CustomItem NEXT_BUTTON = register(new NextRecipeItem());
    public static final CustomItem BANNER = register(new RecipeBannerItem());

    private static CustomItem register(CustomItem customItem) {
        return CustomItemRegistry.getInstance().register(JeiU.getInstance(), customItem);
    }

}
