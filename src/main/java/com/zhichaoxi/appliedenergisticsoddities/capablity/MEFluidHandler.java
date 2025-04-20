package com.zhichaoxi.appliedenergisticsoddities.capablity;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEKey;
import appeng.api.storage.MEStorage;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MEFluidHandler implements IFluidHandler {
    protected MEStorage storage;
    protected IActionSource source;

    public MEFluidHandler(MEStorage storage) {
        this.storage = storage;
        this.source = IActionSource.empty();
    }

    @Override
    public int getTanks() {
        int size = getStackFromMEStorage().size();
        return size + 1;
    }

    @Override
    public @NotNull FluidStack getFluidInTank(int tank) {
        ArrayList<FluidStack> FluidStacks = getStackFromMEStorage();
        if (tank >= FluidStacks.size()) {
            return FluidStack.EMPTY;
        }
        return FluidStacks.get(tank);
    }

    @Override
    public int getTankCapacity(int tank) {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isFluidValid(int tank, FluidStack stack) {
        return true;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        boolean sim = action.simulate();
        int count = resource.getAmount();
        long result = storage.insert(AEFluidKey.of(resource),
                count, sim ? Actionable.SIMULATE : Actionable.MODULATE, source);
        return (int) result;
    }

    @Override
    public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
        boolean sim = action.simulate();
        int num = resource.getAmount();
        long result = storage.extract(AEFluidKey.of(resource),
                num, sim ? Actionable.SIMULATE : Actionable.MODULATE, source);
        var r = resource.copy();
        r.setAmount((int) result);
        return r;
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        boolean sim = action.simulate();
        ArrayList<FluidStack> FluidStacks = getStackFromMEStorage();
        FluidStack resource = FluidStacks.getFirst();
        long result = storage.extract(AEFluidKey.of(resource),
                maxDrain, sim ? Actionable.SIMULATE : Actionable.MODULATE, source);
        var r = resource.copy();
        r.setAmount((int) result);
        return r;
    }

    private @NotNull ArrayList<FluidStack> getStackFromMEStorage() {
        ArrayList<FluidStack> stacks = new ArrayList<>();
        storage.getAvailableStacks().forEach(aeKeyEntry -> {
            AEKey key = aeKeyEntry.getKey();
            if (key instanceof AEFluidKey fluidKey) {
                int amount = (int) storage.extract(fluidKey, (long) Integer.MAX_VALUE, Actionable.SIMULATE, IActionSource.empty());
                stacks.add(fluidKey.toStack(amount));
            }
        });
        return stacks;
    }
}
