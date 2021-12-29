package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.container.ContainerDoubleElectricMachine;
import ic2.core.GuiIC2;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GUIAlloySmelter extends GuiIC2<ContainerDoubleElectricMachine> {

    public final ContainerDoubleElectricMachine container;

    public GUIAlloySmelter(ContainerDoubleElectricMachine container1) {
        super(container1);
        this.container = container1;
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        int xoffset = (this.width - this.xSize) / 2;
        int yoffset = (this.height - this.ySize) / 2;
        this.mc.getTextureManager().bindTexture(getTexture());
        int chargeLevel = (int) (14.0F * this.container.base.getChargeLevel());
        int progress = (int) (24.0F * this.container.base.getProgress());
        if (chargeLevel > 0) {
            drawTexturedModalRect(xoffset + 56 + 1, yoffset + 36 + 14 - chargeLevel, 176, 14 - chargeLevel,
                    14, chargeLevel
            );
        }
        if (progress > 0) {
            drawTexturedModalRect(xoffset + 79, yoffset + 34, 176, 14, progress + 1, 16);
        }
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/GUIAlloySmelter.png");
    }

    public String getName() {
        return this.container.base.getInventoryName();
    }

}
