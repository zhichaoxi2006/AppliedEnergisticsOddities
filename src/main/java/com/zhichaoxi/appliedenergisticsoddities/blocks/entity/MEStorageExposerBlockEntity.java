package com.zhichaoxi.appliedenergisticsoddities.blocks.entity;

import appeng.api.networking.IGrid;
import appeng.api.networking.storage.IStorageService;
import appeng.api.storage.MEStorage;
import appeng.blockentity.grid.AENetworkedBlockEntity;
import com.zhichaoxi.appliedenergisticsoddities.capablity.MEFluidHandler;
import com.zhichaoxi.appliedenergisticsoddities.capablity.MEItemHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;

import javax.annotation.Nullable;

public class MEStorageExposerBlockEntity extends AENetworkedBlockEntity {

    public MEStorageExposerBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState blockState) {
        super(blockEntityType, pos, blockState);
    }

    public @Nullable IItemHandler getItemHandler(Direction direction) {
        IGrid grid = getMainNode().getGrid();
        if (grid != null) {
            IStorageService storageService = grid.getStorageService();
            MEStorage storage = storageService.getInventory();
            return new MEItemHandler(storage);
        }
        return null;
    }

    public @Nullable IFluidHandler getFluidHandler(Direction direction) {
        IGrid grid = getMainNode().getGrid();
        if (grid != null) {
            IStorageService storageService = grid.getStorageService();
            MEStorage storage = storageService.getInventory();
            return new MEFluidHandler(storage);
        }
        return null;
    }

}
