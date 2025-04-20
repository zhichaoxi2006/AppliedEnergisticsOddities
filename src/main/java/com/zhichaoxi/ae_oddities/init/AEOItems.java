package com.zhichaoxi.ae_oddities.init;

import appeng.core.definitions.ItemDefinition;
import com.zhichaoxi.ae_oddities.AE_Oddities;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class AEOItems {
    public static final DeferredRegister.Items DR = DeferredRegister.createItems(AE_Oddities.MODID);

    private static final List<ItemDefinition<?>> ITEMS = new ArrayList<>();

    private static <T extends Item> ItemDefinition<T> item(
            String englishName, String id, Function<Item.Properties, T> factory) {
        var definition = new ItemDefinition<>(englishName, DR.registerItem(id, factory));
        ITEMS.add(definition);
        return definition;
    }
}
