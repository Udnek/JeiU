package me.udnek.jeiu.item;

import me.udnek.itemscoreu.customitem.ConstructableCustomItem;
import me.udnek.jeiu.component.TechnicalItemComponent;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class TechnicalItem extends ConstructableCustomItem {

    Material material;
    String rawId;
    String rawItemName;
    Integer customModelData;
    List<Component> lore;
    boolean hideTooltip;
    public TechnicalItem(@NotNull Material material, @NotNull String rawId, @NotNull String rawItemName, @Nullable Integer customModelData, boolean hideTooltip, @Nullable List<Component> lore){
        this.material = material;
        this.rawId = rawId;
        this.rawItemName = rawItemName;
        this.customModelData = customModelData;
        this.hideTooltip = hideTooltip;
        this.lore = lore;
    }
    public TechnicalItem(Material material, String rawId, String rawItemName, Integer customModelData, boolean hideTooltip){
        this(material, rawId, rawItemName, customModelData, hideTooltip, null);
    }

    @Override
    public void afterInitialization() {
        super.afterInitialization();
        setComponent(TechnicalItemComponent.INSTANCE);
    }

    @Override
    public boolean getAddDefaultAttributes() {return true;}

    @Override
    public @Nullable ItemFlag[] getTooltipHides() {
        return new ItemFlag[]{ItemFlag.HIDE_ADDITIONAL_TOOLTIP, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DYE};
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

    @Nullable
    @Override
    public Integer getCustomModelData() {
        return customModelData;
    }

    @Nullable
    @Override
    public String getRawItemName() {
        return rawItemName;
    }

    @Override
    public boolean getHideTooltip() {
        return hideTooltip;
    }
}
