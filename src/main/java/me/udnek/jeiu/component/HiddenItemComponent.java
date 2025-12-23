package me.udnek.jeiu.component;

import me.udnek.coreu.custom.component.CustomComponent;
import me.udnek.coreu.custom.component.CustomComponentType;
import me.udnek.coreu.custom.item.CustomItem;
import org.jetbrains.annotations.NotNull;

public class HiddenItemComponent implements CustomComponent<CustomItem> {
    public static HiddenItemComponent INSTANCE = new HiddenItemComponent();

    private HiddenItemComponent(){}

    @Override
    public @NotNull CustomComponentType<? super CustomItem, ? extends CustomComponent<? super CustomItem>> getType() {
        return Components.HIDDEN_FROM_NORMAL_PLAYERS_ITEM;
    }
}
