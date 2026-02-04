package me.udnek.jeiu;

import me.udnek.coreu.custom.help.CustomHelpCommand;
import me.udnek.coreu.custom.item.CustomItem;
import me.udnek.coreu.mgu.MGUItems;
import me.udnek.coreu.nms.Nms;
import me.udnek.coreu.resourcepack.ResourcePackablePlugin;
import me.udnek.jeiu.command.*;
import me.udnek.jeiu.component.HiddenItemComponent;
import me.udnek.jeiu.item.Items;
import me.udnek.jeiu.menu.Category;
import me.udnek.jeiu.menu.JeiUInfoDialog;
import me.udnek.jeiu.util.Listener;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
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

        new Listener(this);

        MGUItems.COORDINATE_WAND.getComponents().set(HiddenItemComponent.INSTANCE);

        getCommand("recipe").setExecutor(new RecipeCommand());
        getCommand("recipe_usages").setExecutor(new RecipeUsagesCommand());
        getCommand("all_items").setExecutor(new AllItemsCommand());
        getCommand("structure").setExecutor(new StructureCommand());
        getCommand("loot_table").setExecutor(new LootTableCommand());

        CustomHelpCommand.getInstance().addLine(Component.text("/recipe"));
        CustomHelpCommand.getInstance().addLine(Component.text("/recipe_usages"));
        CustomHelpCommand.getInstance().addLine(Component.text("/items"));
        CustomHelpCommand.getInstance().addLine(Component.text("/structure"));
        CustomHelpCommand.getInstance().addLine(Component.text("/loot_table"));

        Nms.get().addDialogToQuickActions(new NamespacedKey(JeiU.getInstance(), "info"), new JeiUInfoDialog().build());
    }

    @Override
    public void onDisable() {}

    @Override
    public @NotNull Priority getPriority() {
        return Priority.TECHNICAL;
    }
}

