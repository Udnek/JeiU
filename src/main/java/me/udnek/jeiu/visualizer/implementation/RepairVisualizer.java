package me.udnek.jeiu.visualizer.implementation;

import com.google.common.base.Preconditions;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Repairable;
import io.papermc.paper.registry.TypedKey;
import me.udnek.coreu.custom.item.CustomItem;
import me.udnek.coreu.custom.item.RepairData;
import me.udnek.jeiu.JeiU;
import me.udnek.jeiu.item.BannerItem;
import me.udnek.jeiu.item.Items;
import me.udnek.jeiu.menu.RecipesMenu;
import me.udnek.jeiu.visualizer.animator.ListAnimator;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class RepairVisualizer extends AbstractRecipeVisualizer {

    public static final int RESULTS_POSITION = VanillaRecipeVisualizer.CRAFTING_RESULT_OFFSET;
    public static final int REPAIRING_POSITION = RESULTS_POSITION -2;
    public static final int TO_BE_REPAIRED_POSITION = REPAIRING_POSITION-2;

    protected @NotNull ItemStack item;

    public RepairVisualizer(@NotNull ItemStack itemStack){
        this.item = itemStack;
    }

    @Override
    public void visualize(@NotNull RecipesMenu recipesMenu) {
        super.visualize(recipesMenu);
        CustomItem customItem = CustomItem.get(item);
        RepairData repairData = null;
        Repairable repairable = item.getDataOrDefault(DataComponentTypes.REPAIRABLE, item.getType().getDefaultData(DataComponentTypes.REPAIRABLE));
        if (customItem != null){repairData = customItem.getRepairData();}

        Preconditions.checkArgument(repairData != null || repairable != null, "Both RepairData and repairable is null!");

        ItemStack result = item.clone();
        result.setData(DataComponentTypes.DAMAGE, 0);
        ItemStack toBeRepaired = item.clone();
        toBeRepaired.setData(DataComponentTypes.DAMAGE,
                toBeRepaired.getDataOrDefault(DataComponentTypes.MAX_DAMAGE, toBeRepaired.getType().getDefaultData(DataComponentTypes.MAX_DAMAGE))/2
        );
        recipesMenu.setItem(RESULTS_POSITION, result);
        recipesMenu.setItem(TO_BE_REPAIRED_POSITION, toBeRepaired);
        ListAnimator animator = new ListAnimator(REPAIRING_POSITION);
        if (repairData != null){
            animator.addAll(repairData.getStacks());
        } else {
            Iterator<TypedKey<ItemType>> iterator = repairable.types().iterator();
            iterator.forEachRemaining(itemTypeTypedKey -> {
                ItemType itemType = Objects.requireNonNull(Registry.ITEM.get(itemTypeTypedKey));
                animator.add(itemType.createItemStack());
            });
        }
        addAnimator(animator);
        setBanner();
    }

    void setBanner(){
        menu.setThemedItem(RecipesMenu.getBannerPosition(), BannerItem.withModel(BannerItem.ANVIL));
        menu.setItem(RecipesMenu.getRecipeStationPosition(), Material.ANVIL);
    }

    @Override
    public @Nullable List<Component> getInformation() {
        return null;
    }
}
















