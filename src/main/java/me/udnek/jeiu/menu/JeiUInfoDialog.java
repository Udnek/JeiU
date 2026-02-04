package me.udnek.jeiu.menu;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import me.udnek.jeiu.util.MenuQuery;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class JeiUInfoDialog {

    private final List<ActionButton> buttons = new ArrayList<>();
    private final DialogBase.Builder dialogBase = DialogBase.builder(Component.translatable("dialog_menu.jeiu.title"));

    public JeiUInfoDialog(@NotNull ItemStack itemStack) {
        dialogBase.body(List.of(
                DialogBody.item(itemStack, null, true, true, 16, 16),
                DialogBody.plainMessage(itemStack.displayName())
        ));

        buttons.add(
                ActionButton.create(
                        Component.translatable("dialog_menu.jeiu.recipe"),
                        Component.translatable("dialog_menu.jeiu.recipe.description"),
                        200,
                        DialogAction.customClick((view, audience) ->
                                        new RecipesMenu((Player) audience).runNewQuery(new MenuQuery(itemStack, MenuQuery.Type.RECIPES, true)),
                                ClickCallback.Options.builder().uses(1).lifetime(ClickCallback.DEFAULT_LIFETIME).build())
                ));
        buttons.add(
                ActionButton.create(
                        Component.translatable("dialog_menu.jeiu.recipe_usages"),
                        Component.translatable("dialog_menu.jeiu.recipe_usages.description"),
                        200,
                        DialogAction.customClick((view, audience) ->
                                        new RecipesMenu((Player) audience).runNewQuery(new MenuQuery(itemStack, MenuQuery.Type.USAGES, true)),
                                ClickCallback.Options.builder().uses(1).lifetime(ClickCallback.DEFAULT_LIFETIME).build())
                ));

        for(int i = 0; i < 2; i++) {
            buttons.add(ActionButton.create(Component.empty(), null, 1,
                    DialogAction.customClick((view, audience) ->
                                    new JeiUInfoDialog(itemStack).show(((Player) audience)),
                            ClickCallback.Options.builder().uses(1).lifetime(ClickCallback.DEFAULT_LIFETIME).build()
                    )));
        }
    }

    public JeiUInfoDialog() {
        /*dialogBase.body(List.of(DialogBody.plainMessage(Component.empty()), DialogBody.plainMessage(Component.empty()),
                DialogBody.plainMessage(Component.empty()), DialogBody.plainMessage(Component.empty())));*/
        buttons.add(
                ActionButton.create(
                        Component.translatable("dialog_menu.jeiu.recipe.left_hand"),
                        Component.translatable("dialog_menu.jeiu.recipe.left_hand.description"),
                        200,
                        DialogAction.customClick(
                                (view, audience) ->
                                        new RecipesMenu((Player) audience).runNewQuery(
                                                new MenuQuery(((Player) audience).getInventory().getItemInOffHand(), MenuQuery.Type.RECIPES, true)
                                        ),
                                ClickCallback.Options.builder().uses(ClickCallback.UNLIMITED_USES).lifetime(ClickCallback.DEFAULT_LIFETIME).build()
                        )
                ));
        buttons.add(
                ActionButton.create(
                        Component.translatable("dialog_menu.jeiu.recipe.right_hand"),
                        Component.translatable("dialog_menu.jeiu.recipe.right_hand.description"),
                        200,
                        DialogAction.customClick(
                                (view, audience) ->
                                        new RecipesMenu((Player) audience).runNewQuery(
                                                new MenuQuery(((Player) audience).getInventory().getItemInMainHand(), MenuQuery.Type.RECIPES, true)
                                        ),
                                ClickCallback.Options.builder().uses(ClickCallback.UNLIMITED_USES).lifetime(ClickCallback.DEFAULT_LIFETIME).build()
                        )
                ));
        buttons.add(
                ActionButton.create(
                        Component.translatable("dialog_menu.jeiu.recipe_usages.left_hand"),
                        Component.translatable("dialog_menu.jeiu.recipe_usages.left_hand.description"),
                        200,
                        DialogAction.customClick(
                                (view, audience) ->
                                        new RecipesMenu((Player) audience).runNewQuery(
                                                new MenuQuery(((Player) audience).getInventory().getItemInOffHand(), MenuQuery.Type.USAGES, true)
                                        ),
                                ClickCallback.Options.builder().uses(ClickCallback.UNLIMITED_USES).lifetime(ClickCallback.DEFAULT_LIFETIME).build()
                        )
                ));
        buttons.add(
                ActionButton.create(
                        Component.translatable("dialog_menu.jeiu.recipe_usages.right_hand"),
                        Component.translatable("dialog_menu.jeiu.recipe_usages.right_hand.description"),
                        200,
                        DialogAction.customClick(
                                (view, audience) ->
                                        new RecipesMenu((Player) audience).runNewQuery(
                                                new MenuQuery(((Player) audience).getInventory().getItemInMainHand(), MenuQuery.Type.USAGES, true)
                                        ),
                                ClickCallback.Options.builder().uses(ClickCallback.UNLIMITED_USES).lifetime(ClickCallback.DEFAULT_LIFETIME).build()
                        )
                ));

        for(int i = 0; i < 2; i++) {
            buttons.add(ActionButton.create(Component.empty(), null, 1,
                    DialogAction.customClick((view, audience) ->
                                    new JeiUInfoDialog().show(((Player) audience)),
                            ClickCallback.Options.builder().uses(ClickCallback.UNLIMITED_USES).lifetime(ClickCallback.DEFAULT_LIFETIME).build()
                    )));
        }
    }

    public void show(@NotNull Player player) {
        for (Category category : Category.REGISTRY.getAll()) {
            buttons.add(
                    ActionButton.create(
                            category.getCategoryName(),
                            Component.empty(),
                            200,
                            DialogAction.customClick(
                                    (view, audience) -> {
                                        AllItemsMenu allItemsMenu = new AllItemsMenu();
                                        allItemsMenu.category = category;
                                        allItemsMenu.openAndShow(player);
                                    },
                                    ClickCallback.Options.builder().uses(ClickCallback.UNLIMITED_USES).lifetime(ClickCallback.DEFAULT_LIFETIME).build()
                            )
                    ));
        }

        Dialog dialog = Dialog.create(builder -> builder.empty()
                .base(dialogBase.build())
                .type(DialogType.multiAction(
                        buttons,
                        ActionButton.create(Component.translatable("dialog_menu.jeiu.exit"), null, 200, null),
                        2
                ))
        );

        player.showDialog(dialog);
    }

    public Dialog build(){
        for (Category category : Category.REGISTRY.getAll()) {
            buttons.add(
                    ActionButton.create(
                            category.getCategoryName(),
                            Component.empty(),
                            200,
                            DialogAction.customClick(
                                    (view, audience) -> {
                                        AllItemsMenu allItemsMenu = new AllItemsMenu();
                                        allItemsMenu.category = category;
                                        allItemsMenu.openAndShow(((Player) audience));
                                    },
                                    ClickCallback.Options.builder().uses(ClickCallback.UNLIMITED_USES).lifetime(ClickCallback.DEFAULT_LIFETIME).build()
                            )
                    ));
        }
        
        return Dialog.create(builder -> builder.empty()
                .base(dialogBase.build())
                .type(DialogType.multiAction(
                        buttons,
                        ActionButton.create(Component.translatable("dialog_menu.jeiu.exit"), null, 200, null),
                        2
                ))
        );
    }
}
