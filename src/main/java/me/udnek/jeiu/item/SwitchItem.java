package me.udnek.jeiu.item;

import me.udnek.coreu.custom.component.instance.AutoGeneratingFilesItem;
import me.udnek.coreu.custom.item.CustomItem;
import me.udnek.coreu.resourcepack.path.VirtualRpJsonFile;
import me.udnek.jeiu.JeiU;
import net.kyori.adventure.key.Key;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SwitchItem extends TechnicalItem{

    public static final Key ENCHANTED_BOOKS = new NamespacedKey(JeiU.getInstance(), "category/enchanted_books");
    public static final Key CUSTOM_RECIPES = new NamespacedKey(JeiU.getInstance(), "category/custom_recipes");
    public static final Key CUSTOM_ITEMS = new NamespacedKey(JeiU.getInstance(), "category/custom_items");

    public SwitchItem() {
        super("switch", false);
    }
    @Override
    public @Nullable DataSupplier<Key> getItemModel() {
        return DataSupplier.of(null);
    }

    @Override
    public void initializeComponents() {
        super.initializeComponents();
        getComponents().set(new AutoGeneratingFilesItem.CustomModelDataColorable(){

            @Override
            public @NotNull List<VirtualRpJsonFile> getFiles(@NotNull CustomItem customItem) {
                List<VirtualRpJsonFile> files = new ArrayList<>(super.getFiles(customItem));
                addNewFile(files, ENCHANTED_BOOKS);
                addNewFile(files, CUSTOM_RECIPES);
                addNewFile(files, CUSTOM_ITEMS);
                return files;
            }

            public void addNewFile(@NotNull List<VirtualRpJsonFile> list, @NotNull Key key){
                list.add(new VirtualRpJsonFile(getDefinition(key), getDefinitionPath(key)));
                list.add(new VirtualRpJsonFile(getModels(key).getFirst(), getModelPath(key)));
            }
        });
    }
}
