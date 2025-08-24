package me.udnek.jeiu.item;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemAttributeModifiers;
import io.papermc.paper.datacomponent.item.TooltipDisplay;
import me.udnek.coreu.custom.component.instance.AutoGeneratingFilesItem;
import me.udnek.coreu.custom.item.ConstructableCustomItem;
import me.udnek.jeiu.component.Components;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class TechnicalItem extends ConstructableCustomItem {

    protected String rawId;
    protected List<Component> lore;
    protected boolean hideTooltip;

    public TechnicalItem(@NotNull String rawId, boolean hideTooltip, @Nullable List<Component> lore){
        this.rawId = rawId;
        this.hideTooltip = hideTooltip;
        this.lore = lore;
    }
    public TechnicalItem(@NotNull String rawId, boolean hideTooltip){
        this(rawId, hideTooltip, null);
    }
    
    @Override
    public void initializeComponents() {
        super.initializeComponents();
        getComponents().set(Components.TECHNICAL_ITEM.getDefault());
        getComponents().set(AutoGeneratingFilesItem.CUSTOM_MODEL_DATA_COLORABLE);
    }

    @Override
    public @Nullable DataSupplier<ItemAttributeModifiers> getAttributeModifiers() {
        return DataSupplier.of(null);
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

    @Override
    public @Nullable DataSupplier<io.papermc.paper.datacomponent.item.TooltipDisplay> getTooltipDisplay() {
        return DataSupplier.of(TooltipDisplay.tooltipDisplay()
                .hideTooltip(hideTooltip)
                .addHiddenComponents(DataComponentTypes.ATTRIBUTE_MODIFIERS)
                .build()
        );
    }
}
