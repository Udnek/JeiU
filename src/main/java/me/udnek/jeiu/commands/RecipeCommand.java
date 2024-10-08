package me.udnek.jeiu.commands;

import me.udnek.itemscoreu.utils.ItemUtils;
import me.udnek.jeiu.recipe.RecipesMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RecipeCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(commandSender instanceof Player player)) {
            return false;
        }

        if (args.length == 0) {
            RecipesMenu.openNewItemRecipesMenu(player, player.getEquipment().getItemInMainHand());
            return true;
        }
        String id = args[0];
        if (!ItemUtils.isCustomItemOrMaterial(id)) {
            return false;
        }
        RecipesMenu.openNewItemRecipesMenu(player, ItemUtils.getFromCustomItemOrMaterial(id));


        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return RecipeCommandUtils.getOptions(commandSender, args);
    }
}
