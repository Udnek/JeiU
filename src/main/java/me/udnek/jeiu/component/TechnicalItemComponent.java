package me.udnek.jeiu.component;

import me.udnek.coreu.custom.component.CustomComponentType;
import me.udnek.coreu.custom.item.CustomItem;
import me.udnek.coreu.custom.item.CustomItemComponent;
import org.jetbrains.annotations.NotNull;

public class TechnicalItemComponent implements CustomItemComponent {

    public static TechnicalItemComponent INSTANCE = new TechnicalItemComponent();

    private TechnicalItemComponent(){}

    @Override
    public @NotNull CustomComponentType<CustomItem, ?> getType() {
        return Components.TECHNICAL_ITEM;
    }
}
