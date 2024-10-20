package me.udnek.jeiu.commands;

import me.udnek.itemscoreu.customregistry.CustomRegistries;
import me.udnek.itemscoreu.util.ItemUtils;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RecipeCommandUtils {

    public static @Nullable ItemStack getTargetItem(@NotNull CommandSender commandSender, @NotNull String[] args){
        if (!(commandSender instanceof Player player)) {
            return null;
        }
        if (args.length == 0) {
            ItemStack itemInMainHand = player.getEquipment().getItemInMainHand();
            if (itemInMainHand.getType() == Material.AIR) return null;
            return itemInMainHand;
        }
        String id = args[0];
        if (!ItemUtils.isCustomItemOrMaterial(id)) return null;
        ItemStack customItemOrMaterial = ItemUtils.getFromCustomItemOrMaterial(id);
        if (customItemOrMaterial.getType() == Material.AIR) return null;
        return customItemOrMaterial;
    }

    public static List<String> getOptions(CommandSender commandSender, String[] args) {
        if (args.length > 1) return new ArrayList<>();

        final String search = args[0];
        List<String> options = new ArrayList<>();

        for (String id : CustomRegistries.ITEM.getIds()) {
            if (id.contains(search)) options.add(id);
        }

        for (Material material : Material.values()) {
            String materialName = material.toString().toLowerCase();
            if (materialName.contains(search)) options.add(materialName);
        }

        return options;
    }

}
