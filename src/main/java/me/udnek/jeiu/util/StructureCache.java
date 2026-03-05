package me.udnek.jeiu.util;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import me.udnek.coreu.nms.Nms;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.generator.structure.Structure;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NullMarked
public class StructureCache {

    private static @Nullable StructureCache instance;

    public static StructureCache getInstance() {
        if (instance == null) instance = new StructureCache();
        return instance;
    }

    private @Nullable Map<NamespacedKey, List<NamespacedKey>> structureToLootTables = null;

    public Map<NamespacedKey, List<NamespacedKey>> getAllWithLootTable(){
        if (structureToLootTables == null){
            structureToLootTables = new HashMap<>();
            Registry<Structure> registry = RegistryAccess.registryAccess().getRegistry(RegistryKey.STRUCTURE);
            registry.keyStream().forEach(structureId -> {
                List<NamespacedKey> lootTables = new ArrayList<>();
                Nms.get().getAllPossibleLootTablesInStructure(structureId, lootTable -> lootTables.add(lootTable.getKey()));
                if (lootTables.isEmpty()){
                    Utils.getLootTablesBasedOnName(structureId, lootTable -> lootTables.add(lootTable.getKey()));
                }
                if (!lootTables.isEmpty()){
                    structureToLootTables.put(structureId, lootTables);
                }
            });
        }
        return structureToLootTables;
    }

    public List<NamespacedKey> structuresWithLootTable(NamespacedKey lootTable){
        List<NamespacedKey> structures = new ArrayList<>();
        getAllWithLootTable().forEach((structure, lootTables) -> {
            if (lootTables.contains(lootTable)) structures.add(structure);
        });
        return structures;
    }

    public List<NamespacedKey> getLootTablesForStructure(NamespacedKey structure){
        return getAllWithLootTable().getOrDefault(structure, List.of());
    }
}







