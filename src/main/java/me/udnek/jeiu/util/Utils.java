package me.udnek.jeiu.util;

import me.udnek.coreu.nms.Nms;
import me.udnek.jeiu.visualizer.Visualizable;
import me.udnek.jeiu.visualizer.Visualizer;
import me.udnek.jeiu.visualizer.implementation.LootTableVisualizer;
import me.udnek.jeiu.visualizer.implementation.VanillaRecipeVisualizer;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.*;
import org.bukkit.loot.LootTable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Utils {

    public static boolean isVanillaRecipe(@NotNull Recipe recipe) {
        return switch (recipe) {
            case ShapedRecipe ignored -> true;
            case ShapelessRecipe ignored -> true;
            case FurnaceRecipe ignored -> true;
            case BlastingRecipe ignored -> true;
            case SmokingRecipe ignored -> true;
            case CampfireRecipe ignored -> true;
            case StonecuttingRecipe ignored -> true;
            case SmithingTrimRecipe ignored -> true;
            case SmithingTransformRecipe ignored -> true;
            case TransmuteRecipe ignored -> true;
            default -> false;
        };
    }

    public static void toVisualizers(@NotNull List<Recipe> recipes, @NotNull List<LootTable> lootTables, @NotNull Consumer<Visualizer> consumer){
        List<Visualizer> result = new ArrayList<>();

        for (LootTable lootTable : lootTables) {
            if (lootTable instanceof Visualizable visualizable) result.add(visualizable.getVisualizer());
            else result.add(new LootTableVisualizer(lootTable));
        }

        for (Recipe recipe : recipes) {
            if (Utils.isVanillaRecipe(recipe) && !(recipe instanceof Visualizable)){
                // ADD TO LAST
                if (recipe instanceof SmithingTrimRecipe) result.add(new VanillaRecipeVisualizer(recipe));
                else result.addFirst(new VanillaRecipeVisualizer(recipe));
            }
            else if (recipe instanceof Visualizable visualizable) result.addFirst(visualizable.getVisualizer());
        }

        result.forEach(consumer);
    }

    public static void getLootTablesBasedOnName(@NotNull NamespacedKey structureId, @NotNull Consumer<LootTable> consumer){
        for (LootTable lootTable : Nms.get().getRegisteredLootTables()) {
            Key lootTableId = lootTable.key();
            if (!lootTableId.namespace().equals(structureId.namespace())) continue;
            if (!lootTableId.value().contains(structureId.value())) continue;
            consumer.accept(lootTable);
        }
    }

    public static @NotNull Material chooseIconForLootTable(@NotNull LootTable lootTable) {
        String key = lootTable.getKey().getKey();
        String[] split = key.split("/");

        String category = split[0];
        String subtype = "";
        if (split.length >= 2) subtype = split[1];

        return chooseIconForLootTable(category, subtype);
    }

    public static @NotNull Material chooseIconForLootTable(@NotNull String category, @NotNull String subtype) {
        return switch (category) {
            case "archaeology" ->  Material.BRUSH;
            case "blocks" -> {
                Material material = Material.getMaterial(subtype.toUpperCase());
                if (material != null && material.isItem()) yield material;
                yield Material.STRUCTURE_VOID;
            }
            case "chests" -> Material.CHEST;
            case "dispensers" -> Material.DISPENSER;
            case "entities"-> {
                EntityType entityType = EntityType.fromName(subtype);
                if (entityType == null) yield Material.EGG;
                ItemStack egg = Nms.get().getSpawnEggByType(entityType);
                if (egg == null) yield Material.EGG;
                yield egg.getType();
            }
            case "equipment" -> Material.IRON_CHESTPLATE;
            case "gameplay" -> switch (subtype) {
                case "fishing" -> Material.FISHING_ROD;
                case "hero_of_the_village" -> Material.EMERALD;
                case "armadillo_shed" -> Material.ARMADILLO_SPAWN_EGG;
                case "cat_morning_gift" -> Material.CAT_SPAWN_EGG;
                case "chicken_lay" -> Material.CHICKEN_SPAWN_EGG;
                case "panda_sneeze" -> Material.PANDA_SPAWN_EGG;
                case "piglin_bartering" -> Material.GOLD_INGOT;
                case "sniffer_digging" -> Material.SNIFFER_SPAWN_EGG;
                default -> Material.BARRIER;
            };
            case "pots" -> Material.DECORATED_POT;
            case "shearing" -> Material.SHEARS;
            case "spawners" -> Material.TRIAL_SPAWNER;

            default -> Material.BARRIER;
        };
    }
}
