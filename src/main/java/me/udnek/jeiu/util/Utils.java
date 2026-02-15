package me.udnek.jeiu.util;

import io.papermc.paper.registry.keys.StructureKeys;
import me.udnek.coreu.custom.recipe.CustomRecipe;
import me.udnek.coreu.nms.Nms;
import me.udnek.jeiu.component.Components;
import me.udnek.jeiu.component.VisualizableRecipeType;
import me.udnek.jeiu.event.UnknownLootTableIconEvent;
import me.udnek.jeiu.event.UnknownStructureIconEvent;
import me.udnek.jeiu.visualizer.Visualizer;
import me.udnek.jeiu.visualizer.implementation.LootTableVisualizer;
import me.udnek.jeiu.visualizer.implementation.VanillaRecipeVisualizer;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.*;
import org.bukkit.loot.LootTable;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@NullMarked
public class Utils {

    public static boolean isVanillaRecipe(Recipe recipe) {
        return switch (recipe) {
            case CookingRecipe<?> ignored -> true;
            case StonecuttingRecipe ignored -> true;
            case SmithingRecipe ignored -> true;
            case CraftingRecipe ignored -> true;
            default -> false;
        };
    }

    public static void toVisualizers(List<Recipe> recipes, List<LootTable> lootTables, Consumer<Visualizer> consumer){
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

    public static void getLootTablesBasedOnName(NamespacedKey structureId, Consumer<LootTable> consumer){
        for (LootTable lootTable : Nms.get().getRegisteredLootTables()) {
            Key lootTableId = lootTable.key();
            if (!lootTableId.namespace().equals(structureId.namespace())) continue;
            if (!lootTableId.value().contains(structureId.value())) continue;
            consumer.accept(lootTable);
        }
    }

    public static @Nullable Material chooseIconForVanillaStructure(Key structure){
        if (structure.equals(StructureKeys.ANCIENT_CITY)) return Material.DEEPSLATE_BRICKS;
        if (structure.equals(StructureKeys.BASTION_REMNANT)) return Material.POLISHED_BLACKSTONE_BRICKS;
        if (structure.equals(StructureKeys.BURIED_TREASURE)) return Material.SAND;
        if (structure.equals(StructureKeys.DESERT_PYRAMID)) return Material.CHISELED_SANDSTONE;
        if (structure.equals(StructureKeys.END_CITY)) return Material.PURPUR_BLOCK;
        if (structure.equals(StructureKeys.FORTRESS)) return Material.NETHER_BRICKS;
        if (structure.equals(StructureKeys.IGLOO)) return Material.SNOW_BLOCK;
        if (structure.equals(StructureKeys.JUNGLE_PYRAMID)) return Material.MOSSY_COBBLESTONE;
        if (structure.equals(StructureKeys.MANSION)) return Material.DARK_OAK_PLANKS;
        if (structure.equals(StructureKeys.MINESHAFT)) return Material.RAIL;
        if (structure.equals(StructureKeys.MINESHAFT_MESA)) return Material.RAIL;
        if (structure.equals(StructureKeys.MONUMENT)) return Material.PRISMARINE_BRICKS;
        if (structure.equals(StructureKeys.NETHER_FOSSIL)) return Material.BONE_BLOCK;
        if (structure.equals(StructureKeys.OCEAN_RUIN_COLD)) return Material.GRAVEL;
        if (structure.equals(StructureKeys.OCEAN_RUIN_WARM)) return Material.SAND;
        if (structure.equals(StructureKeys.PILLAGER_OUTPOST)) return Material.CROSSBOW;
        if (structure.equals(StructureKeys.RUINED_PORTAL)) return Material.OBSIDIAN;
        if (structure.equals(StructureKeys.RUINED_PORTAL_DESERT)) return Material.OBSIDIAN;
        if (structure.equals(StructureKeys.RUINED_PORTAL_JUNGLE)) return Material.OBSIDIAN;
        if (structure.equals(StructureKeys.RUINED_PORTAL_MOUNTAIN)) return Material.OBSIDIAN;
        if (structure.equals(StructureKeys.RUINED_PORTAL_NETHER)) return Material.OBSIDIAN;
        if (structure.equals(StructureKeys.RUINED_PORTAL_OCEAN)) return Material.OBSIDIAN;
        if (structure.equals(StructureKeys.RUINED_PORTAL_SWAMP)) return Material.OBSIDIAN;
        if (structure.equals(StructureKeys.SHIPWRECK)) return Material.SPYGLASS;
        if (structure.equals(StructureKeys.SHIPWRECK_BEACHED)) return Material.SPYGLASS;
        if (structure.equals(StructureKeys.STRONGHOLD)) return Material.END_PORTAL_FRAME;
        if (structure.equals(StructureKeys.SWAMP_HUT)) return Material.CAULDRON;
        if (structure.equals(StructureKeys.TRAIL_RUINS)) return Material.SUSPICIOUS_GRAVEL;
        if (structure.equals(StructureKeys.TRIAL_CHAMBERS)) return Material.COPPER_BLOCK;
        if (structure.equals(StructureKeys.VILLAGE_DESERT)) return Material.VILLAGER_SPAWN_EGG;
        if (structure.equals(StructureKeys.VILLAGE_PLAINS)) return Material.VILLAGER_SPAWN_EGG;
        if (structure.equals(StructureKeys.VILLAGE_SAVANNA)) return Material.VILLAGER_SPAWN_EGG;
        if (structure.equals(StructureKeys.VILLAGE_SNOWY)) return Material.VILLAGER_SPAWN_EGG;
        if (structure.equals(StructureKeys.VILLAGE_TAIGA)) return Material.VILLAGER_SPAWN_EGG;
        return null;
    }

    public static ItemStack chooseIconForStructure(Key structure) {
        Material materialIcon = null;
        if (structure.namespace().equals(Key.MINECRAFT_NAMESPACE)){
            materialIcon = chooseIconForVanillaStructure(structure);
        }
        if (materialIcon != null) {
            return new ItemStack(materialIcon);
        }
        UnknownStructureIconEvent event = new UnknownStructureIconEvent(structure);
        event.callEvent();
        ItemStack icon = event.getIcon();
        if (icon != null) return icon;
        return new ItemStack(Material.BARRIER);
    }

    public static ItemStack chooseIconForLootTable(LootTable lootTable) {
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
        if (icon != null) return icon;
        return new ItemStack(Material.BARRIER);
    }

    public static @Nullable Material chooseIconForLootTable(String category, String subtype) {
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

    public static @Nullable Material spawnEggByName(String name){
        EntityType entityType = EntityType.fromName(name);
        if (entityType == null) return null;
        ItemStack egg = Nms.get().getSpawnEggByType(entityType);
        if (egg == null) return null;
        return egg.getType();
    }
}
