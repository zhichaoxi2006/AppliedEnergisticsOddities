package com.zhichaoxi.appliedenergisticsoddities.init;

import appeng.block.AEBaseEntityBlock;
import appeng.blockentity.AEBaseBlockEntity;
import appeng.core.definitions.BlockDefinition;
import com.zhichaoxi.appliedenergisticsoddities.AppliedEnergisticsOddities;
import com.zhichaoxi.appliedenergisticsoddities.blocks.entity.MEStorageExposerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public final class AEOBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> DR =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, AppliedEnergisticsOddities.MODID);

    public static final Supplier<BlockEntityType<MEStorageExposerBlockEntity>> ME_STORAGE_EXPOSER = create(
            "me_storage_exposer",
            MEStorageExposerBlockEntity.class,
            MEStorageExposerBlockEntity::new,
            AEOBlocks.ME_STORAGE_EXPOSER);

    @SuppressWarnings({"DataFlowIssue", "unchecked"})
    @SafeVarargs
    private static <T extends AEBaseBlockEntity> Supplier<BlockEntityType<T>> create(
            String id,
            Class<T> entityClass,
            BlockEntityFactory<T> factory,
            BlockDefinition<? extends AEBaseEntityBlock<?>>... blockDefs) {
        if (blockDefs.length == 0) {
            throw new IllegalArgumentException();
        }

        return DR.register(id, () -> {
            var blocks = Arrays.stream(blockDefs).map(BlockDefinition::block).toArray(AEBaseEntityBlock[]::new);

            var typeHolder = new AtomicReference<BlockEntityType<T>>();
            var type = BlockEntityType.Builder.of((pos, state) -> factory.create(typeHolder.get(), pos, state), blocks)
                    .build(null);
            typeHolder.set(type);

            AEBaseBlockEntity.registerBlockEntityItem(type, blockDefs[0].asItem());

            for (var block : blocks) {
                block.setBlockEntity(entityClass, type, null, null);
            }

            return type;
        });
    }

    private interface BlockEntityFactory<T extends AEBaseBlockEntity> {
        T create(BlockEntityType<T> type, BlockPos pos, BlockState state);
    }
}

