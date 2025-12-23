package me.udnek.jeiu.component;

import me.udnek.coreu.custom.component.CustomComponent;
import me.udnek.coreu.custom.component.CustomComponentType;
import me.udnek.coreu.custom.item.CustomItem;
import org.jetbrains.annotations.NotNull;

public class TechnicalItemComponent implements CustomComponent<CustomItem> {

    public static TechnicalItemComponent INSTANCE = new TechnicalItemComponent();

    private TechnicalItemComponent(){}

    @Override
    public @NotNull CustomComponentType<CustomItem, ?> getType() {
        return Components.ALWAYS_HIDDEN_ITEM;
    }
}
