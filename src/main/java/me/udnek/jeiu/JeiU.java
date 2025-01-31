package me.udnek.jeiu;

import me.udnek.itemscoreu.customhelp.CustomHelpCommand;
import me.udnek.itemscoreu.resourcepack.ResourcePackablePlugin;
import me.udnek.jeiu.commands.AllItemsCommand;
import me.udnek.jeiu.commands.RecipeCommand;
import me.udnek.jeiu.commands.RecipeUsagesCommand;
import me.udnek.jeiu.component.ComponentTypes;
import me.udnek.jeiu.item.Items;
import net.kyori.adventure.text.Component;
import org.bukkit.plugin.java.JavaPlugin;


public final class JeiU extends JavaPlugin implements ResourcePackablePlugin {

    private static JeiU instance;

    public static JeiU getInstance() {
        return instance;
    }


    @Override
    public void onEnable() {
        instance = this;
        ComponentTypes.TECHNICAL_ITEM.getDefault();
        Items.BANNER.getItem();

        getCommand("recipe").setExecutor(new RecipeCommand());
        getCommand("recipe_usages").setExecutor(new RecipeUsagesCommand());
        getCommand("all_items").setExecutor(new AllItemsCommand());

        CustomHelpCommand.getInstance().addLine(Component.text("/recipe"));
        CustomHelpCommand.getInstance().addLine(Component.text("/recipe_usages"));
        CustomHelpCommand.getInstance().addLine(Component.text("/items"));
    }

    @Override
    public void onDisable() {
    }
}

