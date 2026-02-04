package me.udnek.jeiu.util;

import me.udnek.coreu.custom.recipe.CustomRecipe;
import me.udnek.coreu.nms.Nms;
import me.udnek.jeiu.component.Components;
import me.udnek.jeiu.component.VisualizableRecipeType;
import me.udnek.jeiu.event.UnknownLootTableIconEvent;
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
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Utils {

    public static boolean isVanillaRecipe(@NotNull Recipe recipe) {
        return switch (recipe) {
            case CookingRecipe<?> ignored -> true;
            case StonecuttingRecipe ignored -> true;
            case SmithingRecipe ignored -> true;
            case CraftingRecipe ignored -> true;
            default -> false;
        };
    }

    public static void toVisualizers(@NotNull List<Recipe> recipes, @NotNull List<LootTable> lootTables, @NotNull Consumer<Visualizer> consumer){
        List<Visualizer> result = new ArrayList<>();

        for (LootTable lootTable : lootTables) {
            result.add(new LootTableVisualizer(lootTable));
        }

        for (Recipe recipe : recipes) {
            if (recipe instanceof CustomRecipe customRecipe){
                VisualizableRecipeType vr = customRecipe.getType().getComponents().get(Components.VISUALIZABLE_RECIPE_TYPE);
                if (vr != null) result.addFirst(vr.getVisualizer(customRecipe));
            }
            else if (Utils.isVanillaRecipe(recipe)){
                // adds smithing result to the tail
                if (recipe instanceof SmithingTrimRecipe) result.add(new VanillaRecipeVisualizer(recipe));
                else result.addFirst(new VanillaRecipeVisualizer(recipe));
            }
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

    public static @NotNull ItemStack chooseIconForLootTable(@NotNull LootTable lootTable) {
        String key = lootTable.getKey().getKey();
        String[] split = key.split("/");

        String category = split[0];
        String subtype = "";
        if (split.length >= 2) subtype = split[1];

        Material materialIcon = chooseIconForLootTable(category, subtype);
        if (materialIcon != null) {
            return new ItemStack(materialIcon);
        }
        UnknownLootTableIconEvent event = new UnknownLootTableIconEvent(lootTable);
        event.callEvent();
        ItemStack icon = event.getIcon();
        if (icon == null) return new ItemStack(Material.BARRIER);
        return icon;
    }

    public static @Nullable Material chooseIconForLootTable(@NotNull String category, @NotNull String subtype) {
        return switch (category) {
            case "archaeology" ->  Material.BRUSH;
            case "blocks", "harvest", "carve" -> {
                Material material = Material.getMaterial(subtype.toUpperCase());
                if (material != null && material.isItem()) yield material;
                yield Material.STRUCTURE_VOID;
            }
            case "brush" -> Material.BRUSH;
            case "chests" -> Material.CHEST;
            case "dispensers" -> Material.DISPENSER;
            case "entities", "charged_creeper" -> spawnEggByName(subtype);
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
                case "turtle_grow" -> Material.TURTLE_SPAWN_EGG;
                default -> Material.BARRIER;
            };
            case "pots" -> Material.DECORATED_POT;
            case "shearing" -> Material.SHEARS;
            case "spawners" -> Material.TRIAL_SPAWNER;

            default -> null;
        };
    }

    public static @Nullable Material spawnEggByName(@NotNull String name){
        EntityType entityType = EntityType.fromName(name);
        if (entityType == null) return null;
        ItemStack egg = Nms.get().getSpawnEggByType(entityType);
        if (egg == null) return null;
        return egg.getType();
    }
}
