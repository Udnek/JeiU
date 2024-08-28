package me.udnek.jeiu.recipe;

import me.udnek.itemscoreu.custominventory.ConstructableCustomInventory;
import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.customloot.LootTableUtils;
import me.udnek.itemscoreu.customrecipe.RecipeManager;
import me.udnek.itemscoreu.utils.LogUtils;
import me.udnek.jeiu.item.Items;
import me.udnek.jeiu.recipe.visualizer.LootTableVisualizer;
import me.udnek.jeiu.recipe.visualizer.VanillaRecipeVisualizer;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.SmithingTrimRecipe;
import org.bukkit.loot.LootTable;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class RecipesMenu extends ConstructableCustomInventory {

    // TODO: 2/11/2024 DYNAMIC CRAFTING MATRIX AND RESULT 

    public static final int RECIPE_BLOCK_OFFSET = 4;


    private static Plugin plugin;

    private static final int previousRecipeButtonPosition = 9 * 4 + 8;
    private static final int nextRecipeButtonPosition = 9 * 2 + 8;
    private static final int helpButtonPosition = 7;
    ////
    public static final ItemStack NEXT_PAGE_BUTTON = Items.NEXT_BUTTON.getItem();
    public static final ItemStack PREVIOUS_PAGE_BUTTON = Items.PREVIOUS_BUTTON.getItem();
    public static final ItemStack RECIPE_INFO = new ItemStack(Material.LIGHT);
    public static final ItemStack HELP_BUTTON = Items.HELP.getItem();
    public static final int RECIPE_INFO_OFFSET = 9;

    private final List<RecipeHolder> recipeHolders;
    private int recipeIndex;
    private ItemStack targetItemStack = null;

    private final HashMap<Integer, RecipeChoice> toAnimateItems = new HashMap<>();

    private BukkitTask animatedItemsRunningTask = null;

    @Override
    public Component getDisplayName() {
        return Component.translatable("space.-8").append(Component.text("0", TextColor.color(1f, 1f, 1f)).font(Key.key("jeiu:font")))
                .append(Component.translatable("space.-170"))
                .append(Component.translatable("gui.jeiu.title", TextColor.color(0, 0, 0)));
    }


    @Override
    public void onPlayerClicksItem(InventoryClickEvent event) {
        ItemStack itemStack = event.getCurrentItem();
        if (itemStack == null) return;

        event.setCancelled(true);


        if (Items.NEXT_BUTTON.isThisItem(itemStack)) {
            this.openNextRecipe(this.recipeIndex + 1);
            return;
        }
        if (Items.PREVIOUS_BUTTON.isThisItem(itemStack)) {
            this.openNextRecipe(this.recipeIndex - 1);
            return;
        }
        if (event.isLeftClick()) {
            this.openOtherItemRecipes(itemStack);
            return;
        }
        if (event.isRightClick()) {
            this.openOtherItemUsages(itemStack);
        }


    }

    public Player getViewer() {
        return (Player) this.inventory.getViewers().get(0);
    }

    public boolean hasViewer() {
        return !this.inventory.getViewers().isEmpty();
    }

    public static void initialize(Plugin plugin) {
        RecipesMenu.plugin = plugin;
    }

    private RecipesMenu(int recipeIndex, ItemStack targetItemStack, List<RecipeHolder> recipeHolders) {
        this.recipeIndex = recipeIndex;
        if (targetItemStack != null) targetItemStack.setAmount(1);
        this.targetItemStack = targetItemStack;
        this.recipeHolders = recipeHolders;
        this.run();
    }


    private void openNextRecipe(int recipeIndex) {
        this.recipeIndex = recipeIndex;
        this.run();
    }

    private void openOtherItemRecipes(ItemStack itemStack) {
        openNewItemRecipesMenu(this.getViewer(), itemStack);
    }

    private void openOtherItemUsages(ItemStack itemStack) {
        openNewItemUsagesMenu(this.getViewer(), itemStack);
    }

    public static void openNewItemUsagesMenu(Player player, ItemStack itemStack) {
        List<Recipe> itemInRecipesUsages = RecipeManager.getInstance().getRecipesAsIngredient(itemStack);
        clearSmithingRecipes(itemInRecipesUsages);

        List<RecipeHolder> recipeHolders = RecipeHolder.of(itemInRecipesUsages, new ArrayList<>());

        if (recipeHolders.isEmpty()) return;
        new RecipesMenu(0, itemStack, recipeHolders).open(player);
    }

    // TODO: 6/18/2024 VISUALIZE INSTEAD OF REMOVING
    public static void clearSmithingRecipes(List<Recipe> recipes) {
        recipes.removeIf((recipe) -> (recipe instanceof SmithingTrimRecipe));
    }


    public static void openNewItemRecipesMenu(Player player, ItemStack itemStack) {
        List<Recipe> recipesFromItemStack = RecipeManager.getInstance().getRecipesAsResult(itemStack);
        long currentTimeMillis = System.currentTimeMillis();
        List<LootTable> whereItemStackInLootTable = LootTableUtils.getWhereItemOccurs(itemStack);
        LogUtils.log("Took:" + (System.currentTimeMillis() - currentTimeMillis));
/*        LogUtils.log(whereItemStackInLootTable.size());
        for (LootTable lootTable : whereItemStackInLootTable) {
            Bukkit.getLogger().info(lootTable.getKey().asString());
        }*/


        List<RecipeHolder> recipeHolders = RecipeHolder.of(recipesFromItemStack, whereItemStackInLootTable);


        if (recipeHolders.isEmpty()) return;
        new RecipesMenu(0, null, recipeHolders).open(player);
    }

    private void run() {
        this.toAnimateItems.clear();
        if (this.animatedItemsRunningTask != null) this.animatedItemsRunningTask.cancel();
        this.inventory.clear();
        this.setPageButtons();
        this.setInfoItem();

        RecipeHolder recipeHolder = getRecipeHolder();
        switch (recipeHolder.getType()){
            case LOOT_TABLE ->
                new LootTableVisualizer(recipeHolder.getLootTable()).visualize(this);
            case RECIPE ->
                new VanillaRecipeVisualizer(recipeHolder.getRecipe()).visualize(this);
            case VISUALIZABLE ->
                recipeHolder.getVisualizable().visualize(this);
        }

        this.animateRecipes();
    }


    public RecipeHolder getRecipeHolder() {
        return this.recipeHolders.get(recipeIndex);
    }


    private void setPageButtons() {
        if (this.recipeIndex != this.recipeHolders.size() - 1) {
            this.setItemAt(nextRecipeButtonPosition, NEXT_PAGE_BUTTON);
        }
        if (this.recipeIndex != 0) {
            this.setItemAt(previousRecipeButtonPosition, PREVIOUS_PAGE_BUTTON);
        }
        this.setItemAt(helpButtonPosition, HELP_BUTTON);
    }

    private void setInfoItem() {
        // TODO: 3/10/2024 CREATE INFO
/*        Recipe recipe = this.getRecipe();
        ItemStack itemStack = RECIPE_INFO;
        ItemMeta itemMeta = itemStack.getItemMeta();

        List<Component> lore = new ArrayList<>();
        lore.add(Component.text(recipe.toString()));
        if (recipe instanceof Keyed){
            lore.add(Component.text(((Keyed) recipe).getKey().asString()));
        }
        itemMeta.lore(lore);
        itemStack.setItemMeta(itemMeta);
        this.setItemAt(recipeInfoOffset, itemStack);*/
    }


    public void setItemAt(int index, ItemStack itemStack) {
        this.inventory.setItem(index, itemStack);
    }

    public void setItemAt(int index, RecipeChoice recipeChoice) {
        if (recipeChoice == null) return;
        if ((recipeChoice instanceof RecipeChoice.MaterialChoice)) {
            if (((RecipeChoice.MaterialChoice) recipeChoice).getChoices().size() == 1) {
                this.setItemAt(index, ((RecipeChoice.MaterialChoice) recipeChoice).getChoices().get(0));
                return;
            } else if (this.targetItemStack != null && !CustomItem.isCustom(this.targetItemStack)) {
                if (((RecipeChoice.MaterialChoice) recipeChoice).getChoices().contains(this.targetItemStack.getType())) {
                    this.setItemAt(index, this.targetItemStack);
                    return;
                }
            }
        } else {
            if (((RecipeChoice.ExactChoice) recipeChoice).getChoices().size() == 1) {
                this.setItemAt(index, ((RecipeChoice.ExactChoice) recipeChoice).getChoices().get(0));
                return;
            } else if (this.targetItemStack == null) {
                if (((RecipeChoice.ExactChoice) recipeChoice).getChoices().contains(this.targetItemStack)) {
                    this.setItemAt(index, this.targetItemStack);
                    return;
                }
            }
        }
        this.toAnimateItems.put(index, recipeChoice);
    }

    public void setItemAt(int index, Material material) {
        if (!material.isItem()) return;
        this.setItemAt(index, new ItemStack(material));
    }

    private ItemStack getRandomItemFromChoice(RecipeChoice recipeChoice) {
        if (recipeChoice instanceof RecipeChoice.ExactChoice) {
            List<ItemStack> itemStacks = ((RecipeChoice.ExactChoice) recipeChoice).getChoices();
            return itemStacks.get(new Random().nextInt(itemStacks.size()));

        } else {
            List<Material> materials = ((RecipeChoice.MaterialChoice) recipeChoice).getChoices();
            Material material = materials.get(new Random().nextInt(materials.size()));
            return new ItemStack(material);
        }
    }

    private void animateRecipes() {
        this.animatedItemsRunningTask = new BukkitRunnable() {

            @Override
            public void run() {
                if (!RecipesMenu.this.hasViewer()) {
                    this.cancel();
                }

                RecipesMenu.this.animationFrame();


            }

        }.runTaskTimer(plugin, 0, 20);
    }

    private void animationFrame() {
        for (Map.Entry<Integer, RecipeChoice> choiceEntry : RecipesMenu.this.toAnimateItems.entrySet()) {
            RecipesMenu.this.setItemAt(choiceEntry.getKey(), RecipesMenu.this.getRandomItemFromChoice(choiceEntry.getValue()));
        }
    }

    @Override
    public int getInventorySize() {
        return 9 * 6;
    }

}
