package me.udnek.jeiu.command;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import me.udnek.jeiu.item.StructureIconItem;
import me.udnek.jeiu.menu.RecipesMenu;
import me.udnek.jeiu.util.MenuQuery;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.generator.structure.Structure;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class StructureCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) return false;
        String rawId = args[0];
        NamespacedKey id = NamespacedKey.fromString(rawId);
        if (id == null) return false;
        Structure structure = RegistryAccess.registryAccess().getRegistry(RegistryKey.STRUCTURE).get(id);
        if (structure == null) return false;
        new RecipesMenu((Player) commandSender).runNewQuery(
                new MenuQuery(StructureIconItem.withStructure(id), MenuQuery.Type.USAGES, true)
        );
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> keys = new ArrayList<>();
        String arg = args.length > 0 ? args[0] : "";
        RegistryAccess.registryAccess().getRegistry(RegistryKey.STRUCTURE).keyStream().forEach(
            key -> {
                if (key.toString().contains(arg)) keys.add(key.toString());
            }
        );
        return keys;
    }
}
