package me.udnek.jeiu.event;

import me.udnek.coreu.custom.event.CustomEvent;
import net.kyori.adventure.key.Key;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class UnknownStructureIconEvent extends CustomEvent {

    public final Key structure;
    private @Nullable ItemStack icon;

    public UnknownStructureIconEvent(Key structure) {
        this.structure = structure;
    }

    public void setIcon(ItemStack stack){
        this.icon = stack;
    }

    public @Nullable ItemStack getIcon(){
        return icon;
    }
}
