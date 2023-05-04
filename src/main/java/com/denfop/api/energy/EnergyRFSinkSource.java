package com.denfop.api.energy;

import cofh.redstoneflux.api.IEnergyProvider;
import cofh.redstoneflux.api.IEnergyReceiver;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class EnergyRFSinkSource extends BasicSinkSource {

    private final IEnergyProvider energyfe1;
    IEnergyReceiver energyfe;

    public EnergyRFSinkSource(final TileEntity parent) {
        super(parent, 0, Integer.MAX_VALUE, Integer.MAX_VALUE);
        this.energyfe = (IEnergyReceiver) parent;
        this.energyfe1 = (IEnergyProvider) parent;
    }

    public double getDemandedEnergy() {
        return this.energyfe.receiveEnergy(null, Integer.MAX_VALUE, true) / 4D;
    }

    public double injectEnergy(EnumFacing directionFrom, double amt, double voltage) {
        this.energyfe.receiveEnergy(directionFrom, (int) (amt * 4), false);
        return 0;
    }

    public boolean acceptsEnergyFrom(IEnergyEmitter iEnergyEmitter, EnumFacing side) {
        return true;
    }

    public boolean emitsEnergyTo(IEnergyAcceptor receiver, EnumFacing direction) {
        return true;
    }

    public double getOfferedEnergy() {
        try {
            return Math.min(
                    energyfe1.getEnergyStored(null),
                    energyfe1.extractEnergy(null, energyfe1.getEnergyStored(null), true)
            ) / 4D;
        } catch (Exception ignored) {
            return 0;
        }
    }

    public void drawEnergy(double amount) {
        energyfe1.extractEnergy(null, (int) (amount * 4), false);
    }

}
