package me.udnek.jeiu.commands;

import me.udnek.itemscoreu.utils.CustomItemUtils;
import me.udnek.jeiu.recipefeatures.RecipesMenu;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RecipeUsagesCommand implements TabExecutor, CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(commandSender instanceof Player)){
            return false;
        }
        Player player = (Player) commandSender;

        if (args.length == 0){
            RecipesMenu.openNewItemUsagesMenu(player, player.getEquipment().getItemInMainHand());
            return true;
        }
        String id = args[0];
        if (!CustomItemUtils.isItemNameExists(id)) {
            return false;
        }
        RecipesMenu.openNewItemUsagesMenu(player, CustomItemUtils.getItemStackFromItemName(id));


        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length > 1) return new ArrayList<>();

        final String search = args[0];
        List<String> arguments = new ArrayList<>();

        for (String id : CustomItemUtils.getAllIds()) {
            if (id.contains(search)){
                arguments.add(id);
            }
        }

        for(Material material : Material.values())
        {
            String materialName = material.toString().toLowerCase();
            if (materialName.contains(search)){
                arguments.add(materialName);
            }
        }

        return arguments;
    }
}
