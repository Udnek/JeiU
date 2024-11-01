package me.udnek.jeiu.visualizer;

import me.udnek.itemscoreu.customloot.LootTableUtils;
import me.udnek.itemscoreu.customloot.table.CustomLootTable;
import me.udnek.itemscoreu.nms.Nms;
import me.udnek.itemscoreu.util.LogUtils;
import me.udnek.jeiu.item.Items;
import me.udnek.jeiu.menu.RecipesMenu;
import me.udnek.jeiu.visualizer.abstraction.Visualizer;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class LootTableVisualizer implements Visualizer {

    private static final Layout SMALL_LAYOUT = new Layout(5, 3, 9 * 1 + 1, "jeiu:small_loot_table_banner");
    private static final Layout MIDDLE_LAYOUT = new Layout(7, 5, 0, "jeiu:middle_loot_table_banner");
    private static final Layout BIG_LAYOUT = new Layout(8, 6, 0, "jeiu:big_loot_table_banner");

    private static final int MAX_CAPACITY = BIG_LAYOUT.getCapacity();

    private final LootTable lootTable;
    private RecipesMenu recipesMenu;

    public LootTableVisualizer(LootTable lootTable){
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


        List<ItemStack> possibleLoot = LootTableUtils.getPossibleLoot(lootTable);
        //possibleLoot = clearDuplicates(possibleLoot);

        if (possibleLoot.size() > MAX_CAPACITY) {
            LogUtils.log("OVERLOAD CAPACITY " + possibleLoot.size());
            possibleLoot = possibleLoot.subList(0, MAX_CAPACITY);
        }

        Layout layout = BIG_LAYOUT;
        if (SMALL_LAYOUT.willFitIn(possibleLoot.size())) layout = SMALL_LAYOUT;
        else if (MIDDLE_LAYOUT.willFitIn(possibleLoot.size())) layout = MIDDLE_LAYOUT;

        int collum = 0;
        int row = 0;

        for (ItemStack itemStack : possibleLoot) {
            recipesMenu.setItem(row * 9 + collum + layout.offset, itemStack);
            collum++;
            if (collum % layout.x == 0) {
                collum = 0;
                row++;
            }
        }

        setDecoration(layout);
    }
    public List<ItemStack> clearDuplicates(List<ItemStack> itemStacks) {
        List<ItemStack> newItems = new ArrayList<>();
        for (ItemStack itemStack : itemStacks) {
            if (!newItems.contains(itemStack)) newItems.add(itemStack);
        }
        return newItems;
    }
    private void setDecoration(Layout layout) {
        String key = lootTable.getKey().getKey();
        String[] split = key.split("/");

        String category = split[0];
        String subtype = "";
        if (split.length >= 2) subtype = split[1];

        String customText = (lootTable instanceof CustomLootTable ? " (Custom)" : " (Vanilla)");
        LogUtils.log(lootTable.getKey().asString() + " ( " + category + ", " + subtype + " )" + customText);

        ItemStack banner = Items.BANNER.getItem();
        banner.editMeta(itemMeta -> itemMeta.setItemModel(NamespacedKey.fromString(layout.model)));
        recipesMenu.setThemedItem(RecipesMenu.getBannerPosition(), banner);

        recipesMenu.setItem(RecipesMenu.getRecipeStationPosition(), chooseIcon(category, subtype));

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
            case "equipment":
                return Material.IRON_CHESTPLATE;
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

        public final int x;
        public final int y;
        public final int offset;
        public final @NotNull String model;
        Layout(int x, int y, int offset, @NotNull String model) {
            this.x = x;
            this.y = y;
            this.offset = offset;
            this.model = model;
        }
        int getCapacity() {
            return x * y;
        }
        boolean willFitIn(int capacity) {
            return capacity <= getCapacity();
        }
    }
}
































