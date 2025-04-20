package com.zhichaoxi.appliedenergisticsoddities.capablity;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.storage.MEStorage;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MEItemHandler implements IItemHandler {
    protected MEStorage storage;
    protected IActionSource source;

    public MEItemHandler(MEStorage storage) {
        this.storage = storage;
        this.source = IActionSource.empty();
    }

    @Override
    public int getSlots() {
        int size = getStackFromMEStorage().size();
        return size + 1;
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int i) {
        ArrayList<ItemStack> ItemStacks = getStackFromMEStorage();
        if (i >= ItemStacks.size()) {
            return ItemStack.EMPTY;
        }
        return ItemStacks.get(i);
    }

    @Override
    public @NotNull ItemStack extractItem(int i, int num, boolean sim) {
        ArrayList<ItemStack> ItemStacks = getStackFromMEStorage();
        ItemStack stack = ItemStacks.get(i);
        long result = storage.extract(AEItemKey.of(stack),
                num, sim ? Actionable.SIMULATE : Actionable.MODULATE, source);
        var r = stack.copy();
        r.setCount((int) result);
        return r;
    }

    @Override
    public @NotNull ItemStack insertItem(int i, @NotNull ItemStack stack, boolean sim) {
        int count = stack.getCount();
        long result = storage.insert(AEItemKey.of(stack),
                count, sim ? Actionable.SIMULATE : Actionable.MODULATE, source);
        var r = stack.copy();
        r.setCount(count - (int) result);
        return r;
    }

    @Override
    public int getSlotLimit(int i) {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isItemValid(int i, @NotNull ItemStack stack) {
        return true;
    }

    private @NotNull ArrayList<ItemStack> getStackFromMEStorage() {
        ArrayList<ItemStack> stacks = new ArrayList<>();
        storage.getAvailableStacks().forEach(aeKeyEntry -> {
            AEKey key = aeKeyEntry.getKey();
            if (key instanceof AEItemKey itemKey) {
                int amount = (int) storage.extract(itemKey, Integer.MAX_VALUE, Actionable.SIMULATE, IActionSource.empty());
                stacks.add(itemKey.toStack(amount));
            }
        });
        return stacks;
    }
}
