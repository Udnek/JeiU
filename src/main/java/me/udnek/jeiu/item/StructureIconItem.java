package me.udnek.jeiu.item;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.udnek.coreu.custom.item.ConstructableCustomItem;
import me.udnek.coreu.custom.item.CustomItem;
import me.udnek.jeiu.JeiU;
import me.udnek.jeiu.component.Components;
import me.udnek.jeiu.component.RecipeAndUsagesItem;
import me.udnek.jeiu.util.StructureCache;
import me.udnek.jeiu.visualizer.Visualizer;
import me.udnek.jeiu.visualizer.implementation.LootTableVisualizer;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class StructureIconItem extends ConstructableCustomItem {

    public static final NamespacedKey STRUCTURE_ID_KEY = new NamespacedKey(JeiU.getInstance(), "structure_id");

    public static @NotNull ItemStack withStructure(@NotNull NamespacedKey structureId){
        ItemStack icon = Items.STRUCTURE_ICON.getItem();
        icon.editPersistentDataContainer(container ->
                container.set(STRUCTURE_ID_KEY, PersistentDataType.STRING, structureId.asString())
        );
        icon.setData(DataComponentTypes.ITEM_NAME, Component.text(structureId.asString()));

        List<Component> lore = new ArrayList<>();
        for (NamespacedKey lootTableId : StructureCache.getInstance().getLootTablesForStructure(structureId)) {
            lore.add(Component.text(lootTableId.asString()).color(NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false));
        }
        icon.setData(DataComponentTypes.LORE, ItemLore.lore(lore));

        return icon;
    }

    public static @Nullable NamespacedKey getStructureId(@NotNull ItemStack stack){
        String rawId = stack.getPersistentDataContainer().get(STRUCTURE_ID_KEY, PersistentDataType.STRING);
        return rawId == null ? null : NamespacedKey.fromString(rawId);
    }

    @Override
    public @NotNull String getRawId() {
        return "structure_icon";
    }

    @Override
    public @Nullable DataSupplier<Key> getItemModel() {
        return DataSupplier.of(Material.DEEPSLATE.getDefaultData(DataComponentTypes.ITEM_MODEL));
    }

    @Override
    public void initializeComponents() {
        super.initializeComponents();
        getComponents().set(Components.ALWAYS_HIDDEN_ITEM.getDefault());
        getComponents().set(new RecipeAndUsagesItem() {
            @Override
            public void getRecipes(@NotNull CustomItem customItem, @NotNull ItemStack stack, @NotNull Consumer<Visualizer> consumer) {
                NamespacedKey structureId = StructureIconItem.getStructureId(stack);
                if (structureId == null) return;
                for (NamespacedKey lootTableId : StructureCache.getInstance().getLootTablesForStructure(structureId)) {
                    consumer.accept(new LootTableVisualizer(Objects.requireNonNull(Bukkit.getLootTable(lootTableId))));
                }
//                Structure structure = RegistryAccess.registryAccess().getRegistry(RegistryKey.STRUCTURE).get(structureId);
//                if (structure == null) return;
//                consumer.accept(new StructureVisualizer(structureId, structure));
            }

            @Override
            public void getUsages(@NotNull CustomItem customItem, @NotNull ItemStack stack, @NotNull Consumer<Visualizer> consumer) {
                getRecipes(customItem, stack, consumer);
            }
        });
    }
}
