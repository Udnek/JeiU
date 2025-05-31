package me.udnek.jeiu.component;

import me.udnek.coreu.custom.component.CustomComponent;
import me.udnek.coreu.custom.component.CustomComponentType;
import me.udnek.coreu.custom.item.CustomItem;
import me.udnek.coreu.custom.item.CustomItemComponent;
import org.jetbrains.annotations.NotNull;

public class HiddenItemComponent implements CustomItemComponent {
    public static HiddenItemComponent INSTANCE = new HiddenItemComponent();

    private HiddenItemComponent(){}

    @Override
    public @NotNull CustomComponentType<? extends CustomItem, ? extends CustomComponent<CustomItem>> getType() {
        return Components.HIDDEN_ITEM;
    }
}
