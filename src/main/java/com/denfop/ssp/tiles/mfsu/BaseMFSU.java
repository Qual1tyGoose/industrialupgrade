package com.denfop.ssp.tiles.mfsu;

import com.denfop.ssp.SuperSolarPanels;
import com.denfop.ssp.gui.BackgroundlessDynamicGUI;
import com.denfop.ssp.tiles.InvSlotMultiCharge;
import ic2.api.tile.IEnergyStorage;
import ic2.core.ContainerBase;
import ic2.core.IHasGui;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.comp.Energy;
import ic2.core.block.comp.TileEntityComponent;
import ic2.core.block.invslot.InvSlot;
import ic2.core.block.invslot.InvSlotDischarge;
import ic2.core.gui.dynamic.DynamicContainer;
import ic2.core.gui.dynamic.GuiParser;
import ic2.core.init.Localization;
import ic2.core.init.MainConfig;
import ic2.core.ref.TeBlock;
import ic2.core.util.ConfigUtil;
import ic2.core.util.StackUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumSet;
import java.util.List;

public class BaseMFSU extends TileEntityInventory implements IEnergyStorage, IHasGui {
	private final int output;
	private final Energy energy;

	public BaseMFSU(int output, int tier, double capacity) {
		this.output = output;
		InvSlotMultiCharge chargeSlots = new InvSlotMultiCharge(this, tier, 4, InvSlot.Access.IO);
		InvSlotDischarge dischargeSlot = new InvSlotDischarge(this, InvSlot.Access.IO, tier, InvSlot.InvSide.BOTTOM);
		this.energy = (Energy) addComponent((TileEntityComponent) (new Energy(this, capacity, EnumSet.complementOf(EnumSet.of(EnumFacing.DOWN)),
				EnumSet.of(EnumFacing.DOWN), tier, tier, false)).addManagedSlot(chargeSlots).addManagedSlot(dischargeSlot));
	}

	protected void updateEntityServer() {
		super.updateEntityServer();
		if (this.energy.getEnergy() > this.energy.getCapacity())
			this.energy.addEnergy(this.energy.getEnergy() - this.energy.getCapacity());
	}

	public void onPlaced(ItemStack stack, EntityLivingBase placer, EnumFacing facing) {
		super.onPlaced(stack, placer, facing);
		if (!(getWorld()).isRemote) {
			NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
			this.energy.addEnergy(nbt.getDouble("energy"));
		}
	}

	protected ItemStack adjustDrop(ItemStack drop, boolean wrench) {
		drop = super.adjustDrop(drop, wrench);
		if (wrench || this.teBlock.getDefaultDrop() == TeBlock.DefaultDrop.Self) {
			double retainedRatio = ConfigUtil.getDouble(MainConfig.get(), "balance/energyRetainedInStorageBlockDrops");
			double totalEnergy = this.energy.getEnergy();
			if (retainedRatio > 0.0D && totalEnergy > 0.0D) {
				NBTTagCompound nbt = StackUtil.getOrCreateNbtData(drop);
				nbt.setDouble("energy", Math.round(totalEnergy * retainedRatio));
			}
		}
		return drop;
	}

	public void setFacing(EnumFacing facing) {
		super.setFacing(facing);
		this.energy.setDirections(EnumSet.complementOf(EnumSet.of(getFacing())), EnumSet.of(getFacing()));
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
		super.addInformation(stack, tooltip, advanced);
		tooltip.add(String.format("%s %s %s %s %s %s", Localization.translate("ic2.item.tooltip.Output"), (long) this.output,
				Localization.translate("ic2.generic.text.EUt"),
				Localization.translate("ic2.item.tooltip.Capacity"), (long) this.energy.getCapacity(),
				Localization.translate("ic2.generic.text.EU")));
		tooltip.add(Localization.translate("ic2.item.tooltip.Store") + " " +
				StackUtil.getOrCreateNbtData(stack).getDouble("energy") + " " +
				Localization.translate("ic2.generic.text.EU"));
	}

	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.energy.setDirections(EnumSet.complementOf(EnumSet.of(getFacing())), EnumSet.of(getFacing()));
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		return nbt;
	}

	public ContainerBase<?> getGuiContainer(EntityPlayer player) {
		try {
			return DynamicContainer.create(this, player, GuiParser.parse(this.teBlock));
		} catch (Exception exception) {
			return null;
		}
	}

	@SideOnly(Side.CLIENT)
	public GuiScreen getGui(EntityPlayer player, boolean b) {
		try {
			return BackgroundlessDynamicGUI.create((IInventory) this, player, GuiParser.parse(SuperSolarPanels.getIdentifier("guidef/UltimateMFSU.xml"), this.teBlock.getTeClass()));
		} catch (Exception exception) {
			return null;
		}
	}

	public void onGuiClosed(EntityPlayer player) {
	}

	public int getStored() {
		return (int) this.energy.getEnergy();
	}

	public void setStored(int energy) {
	}

	public int addEnergy(int amount) {
		this.energy.addEnergy(amount);
		return amount;
	}

	public int getCapacity() {
		return (int) this.energy.getCapacity();
	}

	public int getOutput() {
		return this.output;
	}

	public double getOutputEnergyUnitsPerTick() {
		return this.output;
	}

	public boolean isTeleporterCompatible(EnumFacing side) {
		return true;
	}

	public String getStorageText() {
		return String.format("%s %d / %d", Localization.translate("super_solar_panels.gui.storage"), (long) this.energy.getEnergy(), (long) this.energy.getCapacity());
	}

	public String getOutputText() {
		return String.format("%s %d %s", Localization.translate("super_solar_panels.gui.maxOutput"), (long) this.output, Localization.translate("ic2.generic.text.EUt"));
	}
}