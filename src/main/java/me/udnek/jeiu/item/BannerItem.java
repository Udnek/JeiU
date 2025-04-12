package me.udnek.jeiu.item;

import me.udnek.itemscoreu.customcomponent.instance.AutoGeneratingFilesItem;
import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.resourcepack.path.VirtualRpJsonFile;
import me.udnek.jeiu.JeiU;
import net.kyori.adventure.key.Key;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BannerItem extends TechnicalItem{

    public static final Key ANVIL = new NamespacedKey(JeiU.getInstance(), "banner/anvil");
    public static final Key CRAFTING_TABLE = new NamespacedKey(JeiU.getInstance(), "banner/crafting_table");
    public static final Key BIG_LOOT_TABLE = new NamespacedKey(JeiU.getInstance(), "banner/big_loot_table");
    public static final Key FURNACE = new NamespacedKey(JeiU.getInstance(), "banner/furnace");
    public static final Key MIDDLE_LOOT_TABLE = new NamespacedKey(JeiU.getInstance(), "banner/middle_loot_table");
    public static final Key SMALL_LOOT_TABLE = new NamespacedKey(JeiU.getInstance(), "banner/small_loot_table");
    public static final Key SMITHING_TABLE = new NamespacedKey(JeiU.getInstance(), "banner/smithing_table");
    public static final Key STONECUTTER = new NamespacedKey(JeiU.getInstance(), "banner/stonecutter");

    public BannerItem() {
        super("banner", false);
    }

    @Override
    public @Nullable DataSupplier<Key> getItemModel() {
        return DataSupplier.of(new NamespacedKey(JeiU.getInstance(), "banner/template"));
    }

    @Override
    public void initializeComponents() {
        super.initializeComponents();
        getComponents().set(new AutoGeneratingFilesItem.CustomModelDataColorable(){

            @Override
            public @NotNull List<VirtualRpJsonFile> getFiles(@NotNull CustomItem customItem) {
                ArrayList<VirtualRpJsonFile> files = new ArrayList<>(super.getFiles(customItem));
                files.add(getNewFile(ANVIL));
                files.add(getNewFile(CRAFTING_TABLE));
                files.add(getNewFile(BIG_LOOT_TABLE));
                files.add(getNewFile(FURNACE));
                files.add(getNewFile(MIDDLE_LOOT_TABLE));
                files.add(getNewFile(SMALL_LOOT_TABLE));
                files.add(getNewFile(SMITHING_TABLE));
                files.add(getNewFile(STONECUTTER));
                return files;
            }

            public @NotNull VirtualRpJsonFile getNewFile(@NotNull Key key){
                return new VirtualRpJsonFile(getDefinition(key), getDefinitionPath(key));
            }
        });
    }
}
