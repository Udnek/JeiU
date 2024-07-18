package me.udnek.jeiu;

import me.udnek.jeiu.commands.RecipeCommand;
import me.udnek.jeiu.commands.RecipeUsagesCommand;
import me.udnek.jeiu.recipe_feature.RecipesMenu;
import org.bukkit.plugin.java.JavaPlugin;


public final class JeiU extends JavaPlugin {

    private static JeiU instance;

    public static JeiU getInstance() {
        return instance;
    }


    @Override
    public void onEnable() {
        instance = this;

        this.getCommand("recipe").setExecutor(new RecipeCommand());
        this.getCommand("recipe_usages").setExecutor(new RecipeUsagesCommand());

        RecipesMenu.initialize(this);
    }

    @Override
    public void onDisable() {
    }
}

