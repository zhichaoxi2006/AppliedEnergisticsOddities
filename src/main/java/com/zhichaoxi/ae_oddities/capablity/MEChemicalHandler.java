package com.zhichaoxi.ae_oddities.capablity;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEKey;
import appeng.api.storage.MEStorage;
import me.ramidzkh.mekae2.ae2.MekanismKey;
import mekanism.api.Action;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.IChemicalHandler;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MEChemicalHandler implements IChemicalHandler {

    protected MEStorage storage;
    protected IActionSource source;

    public MEChemicalHandler(MEStorage storage) {
        this.storage = storage;
        this.source = IActionSource.empty();
    }

    @Override
    public int getChemicalTanks() {
        return getStackFromMEStorage().size();
    }

    @Override
    public ChemicalStack getChemicalInTank(int i) {
        var stacks = getStackFromMEStorage();
        int size = stacks.size();
        if (i >= size) {
            return ChemicalStack.EMPTY;
        }
        return stacks.get(i);
    }

    @Override
    public void setChemicalInTank(int i, ChemicalStack chemicalStack) {
    }

    @Override
    public long getChemicalTankCapacity(int i) {
        return getChemicalInTank(i).getAmount();
    }

    @Override
    public boolean isValid(int i, ChemicalStack chemicalStack) {
        return true;
    }

    @Override
    public ChemicalStack insertChemical(int i, ChemicalStack chemicalStack, Action action) {
        return insertChemical(chemicalStack, action);
    }

    @Override
    public ChemicalStack insertChemical(ChemicalStack stack, Action action) {
        boolean sim = action.simulate();
        if (stack == ChemicalStack.EMPTY) {
            return stack.copy();
        }
        long num = stack.getAmount();
        long result = storage.insert(MekanismKey.of(stack),
                num, sim ? Actionable.SIMULATE : Actionable.MODULATE, source);
        var r = stack.copy();
        r.setAmount(num - result);
        return r;
    }

    @Override
    public ChemicalStack extractChemical(ChemicalStack stack, Action action) {
        boolean sim = action.simulate();
        if (stack == ChemicalStack.EMPTY) {
            return stack.copy();
        }
        long l = stack.getAmount();
        long result = storage.extract(MekanismKey.of(stack),
                l, sim ? Actionable.SIMULATE : Actionable.MODULATE, source);
        var r = stack.copy();
        r.setAmount(result);
        return r;
    }

    @Override
    public ChemicalStack extractChemical(int i, long l, Action action) {
        boolean sim = action.simulate();
        ChemicalStack chemicalStack = getChemicalInTank(i);
        if (chemicalStack == ChemicalStack.EMPTY) {
            return chemicalStack.copy();
        }
        long result = storage.extract(MekanismKey.of(chemicalStack),
                l, sim ? Actionable.SIMULATE : Actionable.MODULATE, source);
        var r = chemicalStack.copy();
        r.setAmount(result);
        return r;
    }

    private @NotNull ArrayList<ChemicalStack> getStackFromMEStorage() {
        ArrayList<ChemicalStack> stacks = new ArrayList<>();
        storage.getAvailableStacks().forEach(aeKeyEntry -> {
            AEKey key = aeKeyEntry.getKey();
            if (key instanceof MekanismKey mekanismKey) {
                long amount =  storage.extract(mekanismKey, (long) Integer.MAX_VALUE, Actionable.SIMULATE, IActionSource.empty());
                ChemicalStack stack =  mekanismKey.getStack();
                stack.setAmount(amount);
                stacks.add(stack);
            }
        });
        stacks.add(ChemicalStack.EMPTY);
        return stacks;
    }
}
