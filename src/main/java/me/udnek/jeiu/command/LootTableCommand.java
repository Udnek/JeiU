package me.udnek.jeiu.command;

import me.udnek.coreu.nms.Nms;
import me.udnek.jeiu.item.LootTableIconItem;
import me.udnek.jeiu.menu.RecipesMenu;
import me.udnek.jeiu.util.MenuQuery;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.loot.LootTable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class LootTableCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, String @NotNull [] args) {
        if (args.length == 0) return false;
        String rawId = args[0];
        NamespacedKey id = NamespacedKey.fromString(rawId);
        if (id == null) return false;
        @Nullable LootTable lootTable = Nms.get().getLootTable(id);
        if (lootTable == null) return false;
        new RecipesMenu((Player) commandSender).runNewQuery(
                new MenuQuery(LootTableIconItem.withLootTable(lootTable), MenuQuery.Type.USAGES, true)
        );
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, String @NotNull [] args) {
        List<String> keys = new ArrayList<>();
        String arg = args.length > 0 ? args[0] : "";
        Nms.get().getRegisteredLootTableIds().forEach(key -> {
            if (key.contains(arg)) keys.add(key);
        });
        return keys;
    }
}
