package com.denfop.ssp.tiles.airpanel;

import com.denfop.ssp.tiles.TileEntityAirPanel;
import com.denfop.ssp.tiles.TileEntityMoonPanel;
import com.denfop.ssp.tiles.TileEntitySolarPanel;
import com.denfop.ssp.tiles.TileEntitySolarPanelsun;

public class TileEntityphotonicair extends TileEntityAirPanel
{
    public static SolarConfig settings;
    
    public TileEntityphotonicair() {
        super(TileEntityphotonicair.settings);
    }

	
}