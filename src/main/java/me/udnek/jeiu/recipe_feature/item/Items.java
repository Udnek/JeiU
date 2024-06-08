package me.udnek.jeiu.recipe_feature.item;

import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.customitem.CustomItemManager;
import me.udnek.jeiu.JeiU;

public class Items {
    public static final CustomItem help = register(new HelpItem());
    public static final CustomItem fireIcon = register(new FireIconItem());
    public static final CustomItem previousButton = register(new PreviousRecipeItem());
    public static final CustomItem nextButton = register(new NextRecipeItem());
    public static final CustomItem banner = register(new RecipeBannerItem());

    private static CustomItem register(CustomItem customItem){
        return CustomItemManager.registerItem(JeiU.getInstance(), customItem);
    }

}
