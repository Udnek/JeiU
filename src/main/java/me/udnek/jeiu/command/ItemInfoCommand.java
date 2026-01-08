package me.udnek.jeiu.command;

import me.udnek.jeiu.menu.JeiUInfoDialog;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public class ItemInfoCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NonNull [] args) {
        ItemStack targetItem = RecipeCommandUtils.getTargetItem(commandSender, args);
        if (targetItem == null ) new JeiUInfoDialog().show(((Player) commandSender));
        else new JeiUInfoDialog(targetItem).show(((Player) commandSender));
        return true;
    }
}
