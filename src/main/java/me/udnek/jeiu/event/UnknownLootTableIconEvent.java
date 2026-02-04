package me.udnek.jeiu.event;

import me.udnek.coreu.custom.event.CustomEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class UnknownLootTableIconEvent extends CustomEvent {

    public final LootTable lootTable;
    private @Nullable ItemStack icon;

    public UnknownLootTableIconEvent(LootTable lootTable) {
        this.lootTable = lootTable;
    }

    public void setIcon(ItemStack stack){
        this.icon = stack;
    }

    public @Nullable ItemStack getIcon(){
        return icon;
    }
}
