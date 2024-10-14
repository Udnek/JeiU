package me.udnek.jeiu;

import me.udnek.itemscoreu.resourcepack.ResourcePackablePlugin;
import me.udnek.itemscoreu.utils.LogUtils;
import me.udnek.jeiu.commands.AllItemsCommand;
import me.udnek.jeiu.commands.RecipeCommand;
import me.udnek.jeiu.commands.RecipeUsagesCommand;
import me.udnek.jeiu.item.Items;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;


public final class JeiU extends JavaPlugin implements ResourcePackablePlugin {

    private static JeiU instance;

    public static JeiU getInstance() {
        return instance;
    }


    @Override
    public void onEnable() {
        instance = this;
        ItemStack item = Items.INFORMATION.getItem();

        getCommand("recipe").setExecutor(new RecipeCommand());
        getCommand("recipe_usages").setExecutor(new RecipeUsagesCommand());
        getCommand("all_items").setExecutor(new AllItemsCommand());
    }

    @Override
    public void onDisable() {
    }
}

