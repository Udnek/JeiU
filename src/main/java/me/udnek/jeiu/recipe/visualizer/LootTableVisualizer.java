package me.udnek.jeiu.recipe.visualizer;

import me.udnek.itemscoreu.customloot.LootTableUtils;
import me.udnek.itemscoreu.customloot.table.CustomLootTable;
import me.udnek.itemscoreu.nms.Nms;
import me.udnek.itemscoreu.utils.LogUtils;
import me.udnek.jeiu.recipe.RecipesMenu;
import me.udnek.jeiu.recipe.Visualizable;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTable;

import java.util.ArrayList;
import java.util.List;

public class LootTableVisualizer implements Visualizable{

    private static final Layout SMALL_LAYOUT = new Layout(5, 3, 9 * 2 + 1);
    private static final Layout MIDDLE_LAYOUT = new Layout(7, 3, 9 * 2);
    private static final Layout BIG_LAYOUT = new Layout(8, 5, 9);

    private static final int MAX_CAPACITY = BIG_LAYOUT.getCapacity();

    private final LootTable lootTable;
    private RecipesMenu recipesMenu;

    public LootTableVisualizer(LootTable lootTable){
        this.lootTable = lootTable;
    }

    public void visualize(RecipesMenu recipesMenu) {
        this.recipesMenu = recipesMenu;


        List<ItemStack> possibleLoot = LootTableUtils.getPossibleLoot(lootTable);
        //possibleLoot = clearDuplicates(possibleLoot);

        if (possibleLoot.size() > MAX_CAPACITY) {
            LogUtils.log("OVERLOAD CAPACITY " + possibleLoot.size());
            possibleLoot = possibleLoot.subList(0, MAX_CAPACITY);
        }

        Layout layout = BIG_LAYOUT;
        if (SMALL_LAYOUT.willFitIn(possibleLoot.size())) layout = SMALL_LAYOUT;
        else if (MIDDLE_LAYOUT.willFitIn(possibleLoot.size())) layout = MIDDLE_LAYOUT;

        int i = 0;
        int row = 0;

        for (ItemStack itemStack : possibleLoot) {


            recipesMenu.setItemAt(row * 9 + i + layout.getOffset(), itemStack);

            i++;
            if (i % layout.getX() == 0) {
                i = 0;
                row++;
            }
        }

        setDecoration();
    }

    public List<ItemStack> clearDuplicates(List<ItemStack> itemStacks) {
        List<ItemStack> newItems = new ArrayList<>();
        for (ItemStack itemStack : itemStacks) {
            if (!newItems.contains(itemStack)) newItems.add(itemStack);
        }
        return newItems;
    }


    private void setDecoration() {
        String key = lootTable.getKey().getKey();
        String[] split = key.split("/");

        String category = split[0];
        String subtype = "";
        if (split.length >= 2) subtype = split[1];

        String customText = (lootTable instanceof CustomLootTable ? " (Custom)" : " (Vanilla)");
        LogUtils.log(lootTable.getKey().asString() + " ( " + category + ", " + subtype + " )" + customText);


        recipesMenu.setItemAt(RecipesMenu.RECIPE_BLOCK_OFFSET, chooseIcon(category, subtype));

    }


    public Material chooseIcon(String category, String subtype) {

        switch (category) {
            case "chests":
                return Material.CHEST;
            case "entities":
                EntityType entityType = EntityType.fromName(subtype);
                if (entityType == null) return Material.EGG;
                ItemStack egg = Nms.get().getSpawnEggByType(entityType);
                if (egg == null) return Material.EGG;
                return egg.getType();
            case "archaeology":
                return Material.BRUSH;
            case "pots":
                return Material.DECORATED_POT;
            case "spawners":
                return Material.TRIAL_SPAWNER;
            case "dispensers":
                return Material.DISPENSER;
            case "shearing":
                return Material.SHEARS;
            case "blocks":
                Material material = Material.getMaterial(subtype.toUpperCase());
                if (material != null) return material;
                return Material.STRUCTURE_VOID;
            case "gameplay":
                switch (subtype) {
                    case "hero_of_the_village":
                        return Material.EMERALD;
                    case "fishing":
                        return Material.FISHING_ROD;
                    case "sniffer_digging":
                        return Material.SNIFFER_SPAWN_EGG;
                    case "piglin_bartering":
                        return Material.GOLD_INGOT;
                    case "cat_morning_gift":
                        return Material.CAT_SPAWN_EGG;
                }
            default:
                return Material.BARRIER;
        }
    }

    public static class Layout {

        private final int x;
        private final int y;
        private final int offset;

        Layout(int x, int y, int offset) {
            this.x = x;
            this.y = y;
            this.offset = offset;
        }

        int getCapacity() {
            return x * y;
        }

        boolean willFitIn(int capacity) {
            return capacity <= getCapacity();
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getOffset() {
            return offset;
        }


    }
}
































