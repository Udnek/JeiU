package me.udnek.jeiu.command;

import me.udnek.jeiu.menu.AllItemsMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AllItemsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
//        Nms.get().modifyVaultsInStructure(NamespacedKey.fromString(StructureKeys.TRIAL_CHAMBERS.key().asString()), new Function<ItemStack, ItemStack>() {
//            @Override
//            public ItemStack apply(ItemStack input) {
//                System.out.println("ASKED");
//                return new ItemStack(Material.DIAMOND_PICKAXE);
//            }
//        });
        if (!(commandSender instanceof Player player)) return false;
        new AllItemsMenu().openAndShow(player);
        return true;
    }
}
