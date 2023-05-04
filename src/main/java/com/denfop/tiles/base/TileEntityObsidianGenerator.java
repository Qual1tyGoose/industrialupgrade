package com.denfop.tiles.base;

import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseFluidMachineRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.InputFluid;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.container.ContainerObsidianGenerator;
import com.denfop.gui.GuiObsidianGenerator;
import ic2.api.upgrade.UpgradableProperty;
import ic2.core.init.Localization;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumSet;
import java.util.Set;

public class TileEntityObsidianGenerator extends TileEntityBaseObsidianGenerator implements IHasRecipe {


    public TileEntityObsidianGenerator() {
        super(1, 300, 1);
        Recipes.recipes.getRecipeFluid().addInitRecipes(this);
    }

    public void init() {


        Recipes.recipes.getRecipeFluid().addRecipe("obsidian", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidRegistry.WATER, 1000), new FluidStack(FluidRegistry.LAVA, 1000)
        ), new RecipeOutput(null, new ItemStack(Blocks.OBSIDIAN))));


    }

    public String getInventoryName() {

        return Localization.translate("iu.blockObsGen.name");
    }


    @SideOnly(Side.CLIENT)
    public GuiObsidianGenerator getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiObsidianGenerator(new ContainerObsidianGenerator(entityPlayer, this));
    }

    public String getStartSoundFile() {
        return "Machines/gen_obsidiant.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }

    public float getWrenchDropRate() {
        return 0.85F;
    }

    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(UpgradableProperty.Processing, UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage, UpgradableProperty.FluidConsuming, UpgradableProperty.ItemConsuming,
                UpgradableProperty.ItemProducing, UpgradableProperty.FluidProducing
        );
    }

    @Override
    public IFluidTankProperties[] getTankProperties() {
        return new IFluidTankProperties[]{this.fluidTank1.getTankProperties()[0], this.fluidTank2.getTankProperties()[0]};
    }

}
