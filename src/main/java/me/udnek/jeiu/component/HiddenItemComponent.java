package me.udnek.jeiu.component;

import me.udnek.itemscoreu.customcomponent.CustomComponent;
import me.udnek.itemscoreu.customcomponent.CustomComponentType;
import me.udnek.itemscoreu.customitem.CustomItem;
import org.jetbrains.annotations.NotNull;

public class HiddenItemComponent implements CustomComponent<CustomItem> {
    public static HiddenItemComponent INSTANCE = new HiddenItemComponent();

    private HiddenItemComponent(){}

    @Override
    public @NotNull CustomComponentType<CustomItem, ?> getType() {
        return Components.HIDDEN_ITEM;
    }
}
