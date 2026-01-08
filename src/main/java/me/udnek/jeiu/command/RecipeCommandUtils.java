package me.udnek.jeiu.command;

import me.udnek.coreu.custom.item.ItemUtils;
import me.udnek.coreu.custom.registry.CustomRegistries;
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
        return ItemUtils.getFromCustomItemOrMaterial(id);
    }

    public static @NotNull List<String> getOptions(@NotNull CommandSender commandSender, @NotNull String[] args) {
        if (args.length > 1) return List.of();

        final String search = args[0];
        List<String> options = new ArrayList<>();

        for (String id : CustomRegistries.ITEM.getIds()) {
            if (id.contains(search)) options.add(id);
        }

        for (Material type : Material.values()) {
            if (!type.isItem()) continue;
            String materialName = type.toString().toLowerCase();
            if (materialName.contains(search)) options.add(materialName);
        }

        return options;
    }

}
