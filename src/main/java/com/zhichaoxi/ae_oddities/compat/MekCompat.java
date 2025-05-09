package com.zhichaoxi.ae_oddities.compat;

import appeng.api.networking.IGrid;
import appeng.api.networking.storage.IStorageService;
import appeng.api.storage.MEStorage;
import com.zhichaoxi.ae_oddities.blocks.entity.MEStorageExposerBlockEntity;
import com.zhichaoxi.ae_oddities.capablity.MEChemicalHandler;
import com.zhichaoxi.ae_oddities.init.AEOBlockEntities;
import me.ramidzkh.mekae2.MekCapabilities;
import mekanism.api.chemical.IChemicalHandler;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

import javax.annotation.Nullable;

public class MekCompat {
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(MekCapabilities.CHEMICAL.block(),
                AEOBlockEntities.ME_STORAGE_EXPOSER.get(), MekCompat::getChemicalHandler);
    }

    public static @Nullable IChemicalHandler getChemicalHandler(MEStorageExposerBlockEntity be, Direction direction) {
        IGrid grid = be.getMainNode().getGrid();
        if (grid != null) {
            IStorageService storageService = grid.getStorageService();
            MEStorage storage = storageService.getInventory();
            return new MEChemicalHandler(storage);
        }
        return null;
    }
}
