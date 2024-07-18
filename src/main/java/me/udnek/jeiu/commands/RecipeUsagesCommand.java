package me.udnek.jeiu.commands;

import me.udnek.itemscoreu.customitem.ItemUtils;
import me.udnek.jeiu.recipe_feature.RecipesMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RecipeUsagesCommand implements TabExecutor, CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(commandSender instanceof Player player)) {
            return false;
        }

        if (args.length == 0) {
            RecipesMenu.openNewItemUsagesMenu(player, player.getEquipment().getItemInMainHand());
            return true;
        }
        String id = args[0];
        if (!ItemUtils.isCustomItemOrMaterial(id)) {
            return false;
        }
        RecipesMenu.openNewItemUsagesMenu(player, ItemUtils.getFromCustomItemOrMaterial(id));


        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return RecipeCommandUtils.getOptions(args);
    }
}
