package me.udnek.jeiu.command;

import me.udnek.jeiu.menu.AllItemsMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public class AllItemsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NonNull [] strings) {
        if (!(commandSender instanceof Player player)) return false;
        new AllItemsMenu().openAndShow(player);
        return true;
    }
}
