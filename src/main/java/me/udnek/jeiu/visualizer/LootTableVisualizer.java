package me.udnek.jeiu.visualizer;

import io.papermc.paper.datacomponent.DataComponentTypes;
import me.udnek.itemscoreu.customloot.LootTableUtils;
import me.udnek.itemscoreu.nms.Nms;
import me.udnek.itemscoreu.util.LogUtils;
import me.udnek.jeiu.item.BannerItem;
import me.udnek.jeiu.item.Items;
import me.udnek.jeiu.menu.RecipesMenu;
import me.udnek.jeiu.visualizer.abstraction.Visualizer;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class LootTableVisualizer implements Visualizer {

    private static final Layout SMALL_LAYOUT = new Layout(5, 3, 9 * 1 + 1, BannerItem.SMALL_LOOT_TABLE);
    private static final Layout MIDDLE_LAYOUT = new Layout(7, 5, 0, BannerItem.MIDDLE_LOOT_TABLE);
    private static final Layout BIG_LAYOUT = new Layout(8, 6, 0, BannerItem.BIG_LOOT_TABLE);

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
    public @NotNull List<ItemStack> clearDuplicates(@NotNull List<ItemStack> itemStacks) {
        List<ItemStack> newItems = new ArrayList<>();
        for (ItemStack itemStack : itemStacks) {
            if (!newItems.contains(itemStack)) newItems.add(itemStack);
        }
        return newItems;
    }
    private void setDecoration(@NotNull Layout layout) {
        String key = lootTable.getKey().getKey();
        String[] split = key.split("/");

        String category = split[0];
        String subtype = "";
        if (split.length >= 2) subtype = split[1];

        ItemStack banner = Items.BANNER.getItem();
        banner.setData(DataComponentTypes.ITEM_MODEL, layout.model);
        recipesMenu.setThemedItem(RecipesMenu.getBannerPosition(), banner);

        recipesMenu.setItem(RecipesMenu.getRecipeStationPosition(), chooseIcon(category, subtype));

    }
    public @NotNull Material chooseIcon(@NotNull String category, @NotNull String subtype) {
        return switch (category) {
            case "archaeology" ->  Material.BRUSH;
            case "blocks" -> {
                Material material = Material.getMaterial(subtype.toUpperCase());
                if (material != null) yield material;
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
































