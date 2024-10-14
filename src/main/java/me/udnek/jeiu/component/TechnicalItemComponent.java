package me.udnek.jeiu.component;

import me.udnek.itemscoreu.customcomponent.CustomComponent;
import me.udnek.itemscoreu.customcomponent.CustomComponentType;
import me.udnek.itemscoreu.customitem.CustomItem;
import org.jetbrains.annotations.NotNull;

public class TechnicalItemComponent implements CustomComponent<CustomItem> {

    public static TechnicalItemComponent INSTANCE = new TechnicalItemComponent();

    private TechnicalItemComponent(){}

    @Override
    public @NotNull CustomComponentType<CustomItem, ?> getType() {
        return ComponentTypes.TECHNICAL_ITEM;
    }
}
