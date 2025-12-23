package me.udnek.jeiu;

import me.udnek.coreu.custom.help.CustomHelpCommand;
import me.udnek.coreu.custom.item.CustomItem;
import me.udnek.coreu.mgu.MGUItems;
import me.udnek.coreu.resourcepack.ResourcePackablePlugin;
import me.udnek.jeiu.command.AllItemsCommand;
import me.udnek.jeiu.command.RecipeCommand;
import me.udnek.jeiu.command.RecipeUsagesCommand;
import me.udnek.jeiu.component.HiddenItemComponent;
import me.udnek.jeiu.item.Items;
import me.udnek.jeiu.menu.Category;
import net.kyori.adventure.text.Component;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;


public final class JeiU extends JavaPlugin implements ResourcePackablePlugin {

    private static JeiU instance;

    public static JeiU getInstance() {
        return instance;
    }


    @Override
    public void onEnable() {
        instance = this;
        CustomItem item = Items.BANNER;
        Category category = Category.ALL_ITEMS;

        MGUItems.COORDINATE_WAND.getComponents().set(HiddenItemComponent.INSTANCE);

        getCommand("recipe").setExecutor(new RecipeCommand());
        getCommand("recipe_usages").setExecutor(new RecipeUsagesCommand());
        getCommand("all_items").setExecutor(new AllItemsCommand());

        CustomHelpCommand.getInstance().addLine(Component.text("/recipe"));
        CustomHelpCommand.getInstance().addLine(Component.text("/recipe_usages"));
        CustomHelpCommand.getInstance().addLine(Component.text("/items"));
    }

    @Override
    public void onDisable() {}

    @Override
    public @NotNull Priority getPriority() {
        return Priority.TECHNICAL;
    }
}

