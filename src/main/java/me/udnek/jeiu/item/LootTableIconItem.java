package me.udnek.jeiu.item;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import me.udnek.coreu.custom.component.instance.TranslatableThing;
import me.udnek.coreu.custom.item.ConstructableCustomItem;
import me.udnek.coreu.custom.item.CustomItem;
import me.udnek.coreu.nms.Nms;
import me.udnek.jeiu.JeiU;
import me.udnek.jeiu.component.Components;
import me.udnek.jeiu.component.RecipeAndUsagesItem;
import me.udnek.jeiu.util.StructureCache;
import me.udnek.jeiu.util.Utils;
import me.udnek.jeiu.visualizer.Visualizer;
import me.udnek.jeiu.visualizer.implementation.LootTableVisualizer;
import me.udnek.jeiu.visualizer.implementation.StructureVisualizer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.generator.structure.Structure;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTable;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class LootTableIconItem extends ConstructableCustomItem {

    public static final NamespacedKey LOOT_TABLE_ID_KEY = new NamespacedKey(JeiU.getInstance(), "loot_table_id");

    public static @NotNull ItemStack withLootTable(@NotNull LootTable lootTable){
        ItemStack item = Items.LOOT_TABLE_ICON.getItem();
        item.editPersistentDataContainer(container ->
                container.set(LOOT_TABLE_ID_KEY, PersistentDataType.STRING, lootTable.getKey().asString())
        );
        Material material = Utils.chooseIconForLootTable(lootTable);
        item.setData(DataComponentTypes.ITEM_MODEL, Objects.requireNonNull(material.getDefaultData(DataComponentTypes.ITEM_MODEL)));
        item.setData(DataComponentTypes.LORE, ItemLore.lore(
                List.of(Component.text(lootTable.getKey().asString()).color(NamedTextColor.YELLOW))
        ));
        return item;
    }

    public static @Nullable NamespacedKey getLootTableId(@NotNull ItemStack stack){
        String rawId = stack.getPersistentDataContainer().get(LOOT_TABLE_ID_KEY, PersistentDataType.STRING);
        return rawId == null ? null : NamespacedKey.fromString(rawId);
    }

    @Override
    public @Nullable TranslatableThing getTranslations() {
        return TranslatableThing.ofEngAndRu("Loot Table", "Лут тейбл");
    }

    @Override
    public @NotNull String getRawId() {
        return "loot_table_icon";
    }

    @Override
    public void initializeComponents() {
        super.initializeComponents();
        getComponents().set(Components.ALWAYS_HIDDEN_ITEM.getDefault());
        getComponents().set(new RecipeAndUsagesItem() {
            @Override
            public void getRecipes(@NotNull CustomItem customItem, @NotNull ItemStack stack, @NotNull Consumer<Visualizer> consumer) {
                NamespacedKey lootTableId = LootTableIconItem.getLootTableId(stack);
                if (lootTableId == null) return;
                LootTable lootTable = Nms.get().getLootTable(lootTableId);
                if (lootTable == null) return;
                consumer.accept(new LootTableVisualizer(lootTable));
            }

            @Override
            public void getUsages(@NotNull CustomItem customItem, @NotNull ItemStack stack, @NotNull Consumer<Visualizer> consumer) {
                NamespacedKey lootTableId = LootTableIconItem.getLootTableId(stack);
                if (lootTableId == null) return;
                for (NamespacedKey structureId : StructureCache.getInstance().structuresWithLootTable(lootTableId)) {
                    Structure structure = RegistryAccess.registryAccess().getRegistry(RegistryKey.STRUCTURE).get(structureId);
                    assert structure != null;
                    consumer.accept(new StructureVisualizer(structureId, structure));
                }
            }
        });
    }
}
