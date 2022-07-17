package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.container.ContainerPlasticPlateCreator;
import com.denfop.utils.ModUtils;
import ic2.core.GuiIC2;
import ic2.core.gui.TankGauge;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiPlasticPlateCreator extends GuiIC2<ContainerPlasticPlateCreator> {

    public final ContainerPlasticPlateCreator container;

    public GuiPlasticPlateCreator(ContainerPlasticPlateCreator container1) {
        super(container1);
        this.container = container1;
    }

    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);
        TankGauge.createNormal(this, 6, 5, container.base.fluidTank).drawForeground(par1, par2);
        String tooltip2 =
                ModUtils.getString(Math.min(
                        this.container.base.energy.getEnergy(),
                        this.container.base.energy.getCapacity()
                )) + "/" + ModUtils.getString(this.container.base.energy.getCapacity()) + " " +
                        "EU";
        new AdvArea(this, 58, 35, 69, 50)
                .withTooltip(tooltip2)
                .drawForeground(par1, par2);

    }


    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        this.mc.getTextureManager().bindTexture(getTexture());
        int chargeLevel = (int) (14.0F * this.container.base.getChargeLevel());
        int progress = (int) (24.0F * this.container.base.getProgress());
        int xoffset = (this.width - this.xSize) / 2;
        int yoffset = (this.height - this.ySize) / 2;
        if (chargeLevel > 0) {
            drawTexturedModalRect(xoffset + 56 + 1, yoffset + 36 + 14 - chargeLevel, 176, 14 - chargeLevel,
                    14, chargeLevel
            );
        }
        if (progress > 0) {
            drawTexturedModalRect(xoffset + 79, yoffset + 34, 176, 14, progress + 1, 16);
        }
        TankGauge.createNormal(this, 6, 5, container.base.fluidTank).drawBackground(xoffset, yoffset);
        this.mc.getTextureManager().bindTexture(new ResourceLocation("ic2", "textures/gui/infobutton.png"));
        this.drawTexturedRect(3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);
    }

    public String getName() {
        return this.container.base.getInventoryName();
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/GUIPlasticPlate.png");
    }

}