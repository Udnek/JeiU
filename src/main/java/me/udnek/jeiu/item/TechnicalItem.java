package me.udnek.jeiu.item;

import io.papermc.paper.datacomponent.item.ItemAttributeModifiers;
import me.udnek.itemscoreu.customitem.ConstructableCustomItem;
import me.udnek.jeiu.component.ComponentTypes;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class TechnicalItem extends ConstructableCustomItem {

    protected Material material;
    protected String rawId;
    protected List<Component> lore;
    boolean hideTooltip;

    public TechnicalItem(@NotNull Material material, @NotNull String rawId, boolean hideTooltip, @Nullable List<Component> lore){
        this.material = material;
        this.rawId = rawId;
        this.hideTooltip = hideTooltip;
        this.lore = lore;
    }
    public TechnicalItem(@NotNull Material material, @NotNull String rawId, boolean hideTooltip){
        this(material, rawId, hideTooltip, null);
    }


    @Override
    public void initializeComponents() {
        super.initializeComponents();
        getComponents().set(ComponentTypes.TECHNICAL_ITEM.getDefault());
    }

    @Override
    public @Nullable DataSupplier<ItemAttributeModifiers> getAttributeModifiers() {
        return DataSupplier.of(null);
    }

    @Override
    public @Nullable List<ItemFlag> getTooltipHides() {
        return List.of(ItemFlag.HIDE_ADDITIONAL_TOOLTIP, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DYE);
    }

    @Override
    public void getLore(@NotNull Consumer<Component> consumer) {
        if (lore == null) return;
        lore.forEach(consumer);
    }

    @NotNull
    @Override
    public String getRawId() {
        return rawId;
    }

    @NotNull
    @Override
    public Material getMaterial() {
        return material;
    }

    @Override
    public @Nullable Boolean getHideTooltip() {
        return hideTooltip;
    }
}
