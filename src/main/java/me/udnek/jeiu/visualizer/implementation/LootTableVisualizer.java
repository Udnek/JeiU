package me.udnek.jeiu.visualizer.implementation;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.set.RegistryKeySet;
import me.udnek.coreu.custom.item.ItemUtils;
import me.udnek.coreu.nms.Nms;
import me.udnek.coreu.nms.loot.condition.LootConditionPortrait;
import me.udnek.coreu.nms.loot.condition.LootConditionWrapper;
import me.udnek.coreu.nms.loot.util.LootInfo;
import me.udnek.coreu.util.LogUtils;
import me.udnek.jeiu.item.BannerItem;
import me.udnek.jeiu.item.LootTableIconItem;
import me.udnek.jeiu.menu.RecipesMenu;
import me.udnek.jeiu.visualizer.Visualizer;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Registry;
import org.bukkit.block.Biome;
import org.bukkit.generator.structure.Structure;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class LootTableVisualizer implements Visualizer {

    private static final Layout SMALL_LAYOUT = new Layout(5, 3, 9*1 + 1 + RecipesMenu.VISUALIZER_X_OFFSET, BannerItem.SMALL_LOOT_TABLE);
    private static final Layout MIDDLE_LAYOUT = new Layout(7, 5, 1 + RecipesMenu.VISUALIZER_X_OFFSET, BannerItem.MIDDLE_LOOT_TABLE);
    private static final Layout BIG_LAYOUT = new Layout(8, 6, RecipesMenu.VISUALIZER_X_OFFSET, BannerItem.BIG_LOOT_TABLE);

    private static final int MAX_CAPACITY = BIG_LAYOUT.getCapacity();

    private final LootTable lootTable;
    private RecipesMenu recipesMenu;

    public LootTableVisualizer(@NotNull LootTable lootTable){
        this.lootTable = lootTable;
    }

    @Override
    public @Nullable List<Component> getInformation() {
        return List.of(Component.text("ID: " + lootTable.getKey().asString()));
    }

    @Override
    public void tickAnimation() {}

    public void visualize(@NotNull RecipesMenu recipesMenu) {
        this.recipesMenu = recipesMenu;


        List<Pair<ItemStack, LootInfo>> possibleLoot = new ArrayList<>();
        Nms.get().getLootTableWrapper(lootTable).extractItems(possibleLoot::add);


        if (possibleLoot.size() > MAX_CAPACITY) {
            LogUtils.log(String.format("LootTable (%s) overloaded max capacity (%d): %d. Clearing duplicates.", lootTable.key(), MAX_CAPACITY, possibleLoot.size()));
            //possibleLoot = clearDuplicates(possibleLoot);
            if (possibleLoot.size() > MAX_CAPACITY) {
                LogUtils.log(String.format("Still overloads capacity (%d): %d. Cutting.", MAX_CAPACITY, possibleLoot.size()));
                possibleLoot = possibleLoot.subList(0, MAX_CAPACITY);
            }
        }

        Layout layout = BIG_LAYOUT;
        if (SMALL_LAYOUT.willFitIn(possibleLoot.size())) layout = SMALL_LAYOUT;
        else if (MIDDLE_LAYOUT.willFitIn(possibleLoot.size())) layout = MIDDLE_LAYOUT;

        int collum = 0;
        int row = 0;

        for (Pair<ItemStack, LootInfo> pair : possibleLoot) {
            ItemStack itemStack = pair.getLeft();
            itemStack.setAmount(1);
            ItemLore lore = itemStack.getDataOrDefault(DataComponentTypes.LORE, ItemLore.lore().build());
            List<Component> lines = new ArrayList<>(lore.lines());

            float probability = pair.getRight().probability();


            for (LootConditionWrapper condition : pair.getRight().conditions()) {
                LootConditionPortrait portrait = condition.getPortrait();
//                for (String s : condition.toString().split(",")) {
//                    lines.add(Component.text(s));
//                }
                for (Biome biome : portrait.biomes) {
                    lines.add(Component.text("Biome: ").append(Component.translatable(biome.translationKey())));
                }
                for (Structure structure : portrait.structures) {
                    lines.add(Component.text("Structure: "
                            + RegistryAccess.registryAccess().getRegistry(RegistryKey.STRUCTURE).getKey(structure)));
                }
                if (portrait.randomChance != null){
                    probability *= portrait.randomChance;
                }
                if (portrait.unenchantedRandomChance != null){
                    probability *= portrait.unenchantedRandomChance;
                }
            }

            lines.add(Component.text("Prob: " + probability *100 + "%"));

            itemStack.setData(DataComponentTypes.LORE, ItemLore.lore(lines));
            recipesMenu.setItem(row * 9 + collum + layout.offset, itemStack);
            collum++;
            if (collum % layout.x == 0) {
                collum = 0;
                row++;
            }
        }


//        List<ItemStack> possibleLoot = Nms.get().getPossibleLoot(lootTable);
//
//        if (possibleLoot.size() > MAX_CAPACITY) {
//            LogUtils.log(String.format("LootTable (%s) overloaded max capacity (%d): %d. Clearing duplicates.", lootTable.key(), MAX_CAPACITY, possibleLoot.size()));
//            possibleLoot = clearDuplicates(possibleLoot);
//            if (possibleLoot.size() > MAX_CAPACITY) {
//                LogUtils.log(String.format("Still overloads capacity (%d): %d. Cutting.", MAX_CAPACITY, possibleLoot.size()));
//                possibleLoot = possibleLoot.subList(0, MAX_CAPACITY);
//            }
//        }
//
//        Layout layout = BIG_LAYOUT;
//        if (SMALL_LAYOUT.willFitIn(possibleLoot.size())) layout = SMALL_LAYOUT;
//        else if (MIDDLE_LAYOUT.willFitIn(possibleLoot.size())) layout = MIDDLE_LAYOUT;
//
//        int collum = 0;
//        int row = 0;
//
//        for (ItemStack itemStack : possibleLoot) {
//            recipesMenu.setItem(row * 9 + collum + layout.offset, itemStack);
//            collum++;
//            if (collum % layout.x == 0) {
//                collum = 0;
//                row++;
//            }
//        }

        recipesMenu.setThemedItem(RecipesMenu.getBannerPosition(), BannerItem.withModel(layout.model));
        recipesMenu.setItem(RecipesMenu.getRecipeStationPosition(), LootTableIconItem.withLootTable(lootTable));
    }

    public @NotNull List<ItemStack> clearDuplicates(@NotNull List<ItemStack> itemStacks) {
        List<ItemStack> newItems = new ArrayList<>();
        for (ItemStack itemStack : itemStacks) {
            boolean contains = newItems.stream().anyMatch(addedItem -> ItemUtils.isSameIds(addedItem, itemStack));
            if (!contains) newItems.add(itemStack);
        }
        return newItems;
    }


    public static class Layout {
        public final int x;
        public final int y;
        public final int offset;
        public final @NotNull Key model;

        Layout(int x, int y, int offset, @NotNull Key key) {
            this.x = x;
            this.y = y;
            this.offset = offset;
            this.model = key;
        }

        int getCapacity() {
            return x * y;
        }

        boolean willFitIn(int capacity) {
            return capacity <= getCapacity();
        }
    }
}
































