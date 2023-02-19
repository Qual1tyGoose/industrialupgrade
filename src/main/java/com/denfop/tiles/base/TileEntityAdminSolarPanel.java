package com.denfop.tiles.base;


import com.denfop.IUItem;
import com.denfop.tiles.panels.entity.EnumSolarPanels;
import com.denfop.tiles.panels.entity.TileEntitySolarPanel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;
import java.util.List;

public class TileEntityAdminSolarPanel extends TileEntitySolarPanel {

    public TileEntityAdminSolarPanel() {
        super(14, EnumSolarPanels.QUARK_SOLAR_PANEL.genday * 4, EnumSolarPanels.QUARK_SOLAR_PANEL.producing * 4,
                EnumSolarPanels.QUARK_SOLAR_PANEL.maxstorage * 16, null
        );
    }

    protected boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    protected boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    @Override
    protected ItemStack getPickBlock(final EntityPlayer player, final RayTraceResult target) {
        return new ItemStack(IUItem.blockadmin);
    }

    @Override
    protected boolean isNormalCube() {
        return false;
    }

}
