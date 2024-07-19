package me.udnek.jeiu.commands;

import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.utils.LogUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;

import java.util.ArrayList;
import java.util.List;

public class RecipeCommandUtils {

    public static List<String> getOptions(CommandSender commandSender, String[] args) {
        if (args.length > 1) return new ArrayList<>();

        final String search = args[0];
        List<String> options = new ArrayList<>();

        for (String id : CustomItem.getAllIds()) {
            if (id.contains(search)) options.add(id);
        }

        for (Material material : Material.values()) {
            String materialName = material.toString().toLowerCase();
            if (materialName.contains(search)) options.add(materialName);
        }

        return options;
    }

}
