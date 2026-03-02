package me.udnek.jeiu.command;

import me.udnek.coreu.nms.Nms;
import me.udnek.jeiu.item.LootTableIconItem;
import me.udnek.jeiu.menu.RecipesMenu;
import me.udnek.jeiu.util.MenuQuery;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.loot.LootTable;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.List;

@NullMarked
public class LootTableCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!(commandSender instanceof Player player)) return false;
        if (args.length == 0) return false;
        String rawId = args[0];
        NamespacedKey id = NamespacedKey.fromString(rawId);
        if (id == null) return false;
        @Nullable LootTable lootTable = Nms.get().getLootTable(id);
        if (lootTable == null) return false;
        new RecipesMenu(player).runNewQuery(
                new MenuQuery(LootTableIconItem.withLootTable(lootTable), MenuQuery.Type.RECIPES, true)
        );
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        List<String> keys = new ArrayList<>();
        String arg = args.length > 0 ? args[0] : "";
        Nms.get().getRegisteredLootTableIds().forEach(key -> {
            if (key.contains(arg)) keys.add(key);
        });
        return keys;
    }
}
