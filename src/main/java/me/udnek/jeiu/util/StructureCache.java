package me.udnek.jeiu.util;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import me.udnek.coreu.nms.Nms;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.generator.structure.Structure;
import org.bukkit.loot.LootTable;
import org.checkerframework.checker.units.qual.N;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StructureCache {

    private static StructureCache instance;

    public static StructureCache getInstance() {
        if(instance == null) instance = new StructureCache();
        return instance;
    }

    private Map<NamespacedKey, List<NamespacedKey>> structureToLootTables = null;

    public @NotNull Map<NamespacedKey, List<NamespacedKey>> getAllWithLootTable(){
        if (structureToLootTables == null){
            structureToLootTables = new HashMap<>();
            Registry<@NotNull Structure> registry = RegistryAccess.registryAccess().getRegistry(RegistryKey.STRUCTURE);
            registry.keyStream().forEach(structureId -> {
                Structure structure = registry.get(structureId);
                assert structure != null;
                List<NamespacedKey> lootTables = new ArrayList<>();
                Nms.get().getAllPossibleLootTablesInStructure(structure, lootTable -> {
                    lootTables.add(lootTable.getKey());
                });
                if (lootTables.isEmpty()){
                    Utils.getLootTablesBasedOnName(structureId, lootTable -> {
                        lootTables.add(lootTable.getKey());
                    });
                }
                if (!lootTables.isEmpty()){
                    structureToLootTables.put(structureId, lootTables);
                }
            });
        }
        return structureToLootTables;
    }

    public @NotNull List<NamespacedKey> structuresWithLootTable(@NotNull NamespacedKey lootTable){
        List<NamespacedKey> structures = new ArrayList<>();
        getAllWithLootTable().forEach((structure, lootTables) -> {
            if (lootTables.contains(lootTable)) structures.add(structure);
        });
        return structures;
    }

    public @NotNull List<NamespacedKey> getLootTablesForStructure(@NotNull NamespacedKey structure){
        return getAllWithLootTable().getOrDefault(structure, List.of());
    }
}







