package me.udnek.jeiu.menu;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import me.udnek.coreu.custom.item.CustomItem;
import me.udnek.coreu.custom.item.VanillaItemManager;
import me.udnek.coreu.custom.recipe.CustomRecipe;
import me.udnek.coreu.custom.recipe.RecipeManager;
import me.udnek.coreu.custom.registry.AbstractRegistrable;
import me.udnek.coreu.custom.registry.CustomRegistries;
import me.udnek.coreu.custom.registry.CustomRegistry;
import me.udnek.coreu.custom.registry.MappedCustomRegistry;
import me.udnek.jeiu.JeiU;
import me.udnek.jeiu.component.Components;
import me.udnek.jeiu.item.Items;
import me.udnek.jeiu.item.StructureIconItem;
import me.udnek.jeiu.item.SwitchItem;
import net.kyori.adventure.key.Keyed;
import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.generator.structure.Structure;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Category extends AbstractRegistrable {

    public static final CustomRegistry<Category> REGISTRY = CustomRegistries.addRegistry(JeiU.getInstance(), new MappedCustomRegistry<>("category"));

    public static final Category ALL_ITEMS = register(new Category("all_items", Component.translatable("item.jeiu.custom_items")) {
        @Override
        public @NotNull ItemStack getIcon(@NotNull AllItemsMenu context) {
            ItemStack item = Items.SWITCH.getItem();
            item.setData(DataComponentTypes.ITEM_NAME, categoryName);
            item.setData(DataComponentTypes.ITEM_MODEL, SwitchItem.CUSTOM_ITEMS);
            return item;
        }

        @Override
        public @NotNull List<ItemStack> getAll(@NotNull AllItemsMenu context) {
            boolean forceShowHidden = context.getInventory().getViewers().stream().anyMatch(humanEntity -> humanEntity.getGameMode() == GameMode.CREATIVE);

            List<ItemStack> all = new ArrayList<>();
            CustomRegistries.ITEM.getAll(customItem -> {
                if (customItem.getComponents().has(Components.ALWAYS_HIDDEN_ITEM)) return;
                if (!forceShowHidden && customItem.getComponents().has(Components.HIDDEN_FROM_NORMAL_PLAYERS_ITEM)) return;
                all.add(customItem.getItem());
            });

            return all;
        }
    });
    public static final Category CUSTOM_RECIPES = register(new Category("custom_recipes", Component.translatable("item.jeiu.custom_recipes")) {
        @Override
        public @NotNull ItemStack getIcon(@NotNull AllItemsMenu context) {
            ItemStack item = Items.SWITCH.getItem();
            item.setData(DataComponentTypes.ITEM_NAME, categoryName);
            item.setData(DataComponentTypes.ITEM_MODEL, SwitchItem.CUSTOM_RECIPES);
            return item;
        }

        @Override
        public @NotNull List<ItemStack> getAll(@NotNull AllItemsMenu context) {
            List<Recipe> recipes = new ArrayList<>();
            RecipeManager.getInstance().getAll(recipes::add);

            List<ItemStack> stacks = new ArrayList<>();

            Set<Material> materials = new HashSet<>();
            Set<CustomItem> customItems = new HashSet<>();
            for (Recipe recipe : recipes) {
                if (!(recipe instanceof Keyed keyed)) continue;
                if (keyed.key().namespace().equals(NamespacedKey.MINECRAFT_NAMESPACE)) continue;

                List<ItemStack> toProceed = new ArrayList<>();

                if (recipe instanceof CustomRecipe<?> customRecipe){
                    toProceed.addAll(customRecipe.getResults());
                } else {
                    toProceed.add(recipe.getResult());
                }

                for (ItemStack itemStack : toProceed) {
                    CustomItem customItem = CustomItem.get(itemStack);
                    if (customItem != null) {
                        if (VanillaItemManager.isReplaced(customItem)) customItems.add(customItem);
                    } else {
                        if (itemStack.getType() != Material.ENCHANTED_BOOK) materials.add(itemStack.getType());
                    }
                }
            }
            for (Material material : materials) stacks.add(new ItemStack(material));
            for (CustomItem customItem : customItems) stacks.add(customItem.getItem());
            return stacks;
        }
    });
    public static final Category ENCHANTED_BOOKS = register(new Category("enchanted_books", Component.translatable("item.jeiu.enchanted_books")) {
        @Override
        public @NotNull ItemStack getIcon(@NotNull AllItemsMenu context) {
            ItemStack item = Items.SWITCH.getItem();
            item.setData(DataComponentTypes.ITEM_NAME, categoryName);
            item.setData(DataComponentTypes.ITEM_MODEL, SwitchItem.ENCHANTED_BOOKS);
            return item;
        }

        @Override
        public @NotNull List<ItemStack> getAll(@NotNull AllItemsMenu context) {
            List<Recipe> recipes = new ArrayList<>();
            RecipeManager.getInstance().getAll(recipes::add);

            List<ItemStack> stacks = new ArrayList<>();
            for (Recipe recipe : recipes) {
                List<ItemStack> toProceed = new ArrayList<>();
                if (recipe instanceof CustomRecipe<?> customRecipe){
                    toProceed.addAll(customRecipe.getResults());
                } else {
                    toProceed.add(recipe.getResult());
                }

                for (ItemStack itemStack : toProceed) {
                    if (itemStack.getType() == Material.ENCHANTED_BOOK) stacks.add(itemStack);
                }
            }
            return stacks;
        }
    });
    public static final Category STRUCTURES = register(new Category("structures", Component.translatable("item.jeiu.structures")) {
        @Override
        public @NotNull ItemStack getIcon(@NotNull AllItemsMenu context) {
            ItemStack item = Items.SWITCH.getItem();
            item.setData(DataComponentTypes.ITEM_NAME, categoryName);
            item.setData(DataComponentTypes.ITEM_MODEL, SwitchItem.STRUCTURES);
            return item;
        }

        @Override
        public @NotNull List<ItemStack> getAll(@NotNull AllItemsMenu context) {
            List<ItemStack> items = new ArrayList<>();
            Registry<@NotNull Structure> registry = RegistryAccess.registryAccess().getRegistry(RegistryKey.STRUCTURE);
            registry.keyStream().forEach(structure -> {
                items.add(StructureIconItem.withStructure(structure));
            });
            return items;
        }
    });

    private static @NotNull Category register(@NotNull Category category){
        return REGISTRY.register(JeiU.getInstance(), category);
    }

    private final String rawId;
    protected final Component categoryName;

    public Category(@NotNull String rawId, @NotNull Component categoryName) {
        this.rawId = rawId;
        this.categoryName = categoryName;
    }

    @Override
    @NotNull
    public String getRawId() {
        return rawId;
    }

    @NotNull
    public Component getCategoryName() {
        return categoryName;
    }


    public abstract @NotNull ItemStack getIcon(@NotNull AllItemsMenu context);
    public abstract @NotNull List<ItemStack> getAll(@NotNull AllItemsMenu context);
}
