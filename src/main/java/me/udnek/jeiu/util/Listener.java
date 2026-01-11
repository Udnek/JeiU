package me.udnek.jeiu.util;

import me.udnek.coreu.custom.event.InitializationEvent;
import me.udnek.coreu.custom.registry.InitializationProcess;
import me.udnek.coreu.util.SelfRegisteringListener;
import me.udnek.jeiu.JeiU;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class Listener extends SelfRegisteringListener {
    public Listener(@NotNull Plugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onInit(InitializationEvent event){
        if (event.getStep() == InitializationProcess.Step.AFTER_GLOBAL_INITIALIZATION) {
            Bukkit.getScheduler().runTaskAsynchronously(JeiU.getInstance(), () -> StructureCache.getInstance().getAllWithLootTable());
        }
    }
}
