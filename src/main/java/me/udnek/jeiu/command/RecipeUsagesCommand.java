package me.udnek.jeiu.command;

import me.udnek.jeiu.menu.RecipesMenu;
import me.udnek.jeiu.util.MenuQuery;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RecipeUsagesCommand implements TabExecutor, CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        ItemStack targetItem = RecipeCommandUtils.getTargetItem(commandSender, args);
        if (targetItem == null) return false;
        new RecipesMenu((Player) commandSender).runNewQuery(new MenuQuery(targetItem, MenuQuery.Type.USAGES, true), null);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return RecipeCommandUtils.getOptions(commandSender, args);
    }
}
