package com.zhichaoxi.ae_oddities.init;

import appeng.block.AEBaseBlockItem;
import appeng.core.definitions.BlockDefinition;
import appeng.core.definitions.ItemDefinition;
import com.zhichaoxi.ae_oddities.AE_Oddities;
import com.zhichaoxi.ae_oddities.blocks.MEStorageExposerBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public final class AEOBlocks {
    public static final DeferredRegister.Blocks DR = DeferredRegister.createBlocks(AE_Oddities.MODID);

    private static final List<BlockDefinition<?>> BLOCKS = new ArrayList<>();

    public static List<BlockDefinition<?>> getBlocks() {
        return Collections.unmodifiableList(BLOCKS);
    }

    public static final BlockDefinition<MEStorageExposerBlock> ME_STORAGE_EXPOSER = block(
            "ME Storage Exposer",
            "me_storage_exposer",
            () -> new MEStorageExposerBlock(BlockBehaviour.Properties.of()),
            AEBaseBlockItem::new);

    private static <T extends Block> BlockDefinition<T> block(
            String englishName,
            String id,
            Supplier<T> blockSupplier,
            BiFunction<Block, Item.Properties, BlockItem> itemFactory) {
        var block = DR.register(id, blockSupplier);
        var item = AEOItems.DR.register(id, () -> itemFactory.apply(block.get(), new Item.Properties()));

        var definition = new BlockDefinition<>(englishName, block, new ItemDefinition<>(englishName, item));
        BLOCKS.add(definition);
        return definition;
    }

}
