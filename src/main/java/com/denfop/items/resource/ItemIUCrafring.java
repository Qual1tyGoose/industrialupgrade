package com.denfop.items.resource;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.blocks.IIdProvider;
import ic2.core.init.BlocksItems;
import ic2.core.item.ItemMulti;
import ic2.core.ref.ItemName;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Locale;

public class ItemIUCrafring extends ItemMulti<ItemIUCrafring.Types> implements IModelRegister {

    protected static final String NAME = "ItemIUCrafring".toLowerCase();

    public ItemIUCrafring() {
        super(null, Types.class);
        this.setCreativeTab(IUCore.ItemTab);
        BlocksItems.registerItem((Item) this, IUCore.getIdentifier(NAME)).setUnlocalizedName(NAME);
        IUCore.proxy.addIModelRegister(this);
    }

    @Override
    public void registerModels() {
        registerModels(null);
    }

    public String getUnlocalizedName() {
        return "iu." + super.getUnlocalizedName().substring(4);
    }

    @SideOnly(Side.CLIENT)
    protected void registerModel(final int meta, final ItemName name, final String extraName) {
        ModelLoader.setCustomModelResourceLocation(
                this,
                meta,
                new ModelResourceLocation(Constants.MOD_ID + ":" + NAME + "/" + Types.getFromID(meta).getName(), null)
        );
    }

    public enum Types implements IIdProvider {
        itemIrradiantUranium(0),
        itemIrradiantGlassPane(1),
        itemUranIngot(2),
        itemMTCore(3),

        ;

        private final String name;
        private final int ID;

        Types(final int ID) {
            this.name = this.name().toLowerCase(Locale.US);
            this.ID = ID;
        }

        public static Types getFromID(final int ID) {
            return values()[ID % values().length];
        }

        public String getName() {
            return this.name;
        }

        public int getId() {
            return this.ID;
        }
    }

}
