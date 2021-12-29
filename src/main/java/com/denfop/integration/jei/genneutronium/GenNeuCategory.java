package com.denfop.integration.jei.genneutronium;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.blocks.mechanism.BlockBaseMachine;
import ic2.core.init.Localization;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GenNeuCategory extends Gui implements IRecipeCategory<GenNeuWrapper> {

    private final IDrawableStatic bg;

    public  GenNeuCategory(
            final IGuiHelper guiHelper
    ) {
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/NeutronGeneratorGUI" +
                        ".png"), 5, 5, 140,
                75);
    }

    @Override
    public  String getUid() {
        return BlockBaseMachine.neutron_generator.getName();
    }

    @Override
    public String getTitle() {
        return Localization.translate(new ItemStack(IUItem.machines,1,5).getUnlocalizedName());
    }
    @Override
    public String getModName() {
        return Constants.MOD_NAME;
    }

    @Override
    public IDrawable getBackground() {
        return bg;
    }



    @Override
    public void drawExtras(final Minecraft mc) {


        mc.getTextureManager().bindTexture(getTexture());


    }

    @Override
    public void setRecipe(
            final IRecipeLayout layout,
            final  GenNeuWrapper recipes,
            final IIngredients ingredients
    ) {


        IGuiFluidStackGroup fff = layout.getFluidStacks();

        fff.init(0, false, 95, 21, 12, 47, 10000, true, null);
        fff.set(0, recipes.getInput2());


    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/NeutronGeneratorGUI.png");
    }



}
