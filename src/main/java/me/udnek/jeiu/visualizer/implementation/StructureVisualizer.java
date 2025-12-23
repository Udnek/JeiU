package me.udnek.jeiu.visualizer.implementation;

import me.udnek.coreu.nms.Nms;
import me.udnek.coreu.nms.NmsUtils;
import me.udnek.jeiu.item.BannerItem;
import me.udnek.jeiu.item.LootTableIconItem;
import me.udnek.jeiu.item.StructureIconItem;
import me.udnek.jeiu.menu.RecipesMenu;
import me.udnek.jeiu.util.StructureCache;
import me.udnek.jeiu.visualizer.Visualizer;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.generator.structure.Structure;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class StructureVisualizer implements Visualizer {

    protected Structure structure;
    protected NamespacedKey id;

    public StructureVisualizer(@NotNull NamespacedKey structureId, @NotNull Structure structure){
        this.id = structureId;
        this.structure = structure;
    }

    @Override
    public void visualize(@NotNull RecipesMenu recipesMenu) {
        recipesMenu.setThemedItem(RecipesMenu.getBannerPosition(), BannerItem.withModel(BannerItem.BIG_LOOT_TABLE));
        recipesMenu.setItem(RecipesMenu.getRecipeStationPosition(), StructureIconItem.withStructure(id));

        List<LootTable> lootTables = new ArrayList<>();
        for (NamespacedKey key : StructureCache.getInstance().getLootTablesForStructure(id)) {
            lootTables.add(Bukkit.getLootTable(key));
        }
        int collum = 0;
        int row = 0;
        final int xSize = 8;
        for (int i = 0; i < Math.min(6*xSize, lootTables.size()); i++) {
            LootTable lootTable = lootTables.get(i);
            ItemStack icon = LootTableIconItem.withLootTable(lootTable);

            recipesMenu.setItem(row * 9 + collum + RecipesMenu.VISUALIZER_X_OFFSET, icon);
            collum++;
            if (collum % xSize == 0) {
                collum = 0;
                row++;
            }
        }
    }

    @Override
    public @Nullable List<Component> getInformation() {
        return List.of(Component.text("ID: " + id.asString()));
    }

    @Override
    public void tickAnimation() {}
}
