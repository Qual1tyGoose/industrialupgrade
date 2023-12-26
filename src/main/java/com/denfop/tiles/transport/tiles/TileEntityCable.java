package com.denfop.tiles.transport.tiles;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.energy.IEnergyAcceptor;
import com.denfop.api.energy.IEnergyConductor;
import com.denfop.api.energy.IEnergyEmitter;
import com.denfop.api.energy.IEnergyTile;
import com.denfop.api.energy.NodeStats;
import com.denfop.api.energy.event.EnergyTileLoadEvent;
import com.denfop.api.energy.event.EnergyTileUnLoadEvent;
import com.denfop.api.item.IHazmatLike;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockCable;
import com.denfop.damagesource.IUDamageSource;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketCableSound;
import com.denfop.tiles.transport.types.CableType;
import com.denfop.tiles.transport.types.ICableItem;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import java.io.IOException;


public class TileEntityCable extends TileEntityMultiCable implements IEnergyConductor {


    public boolean addedToEnergyNet;
    public int type;
    protected CableType cableType;
    private boolean needUpdate;

    public TileEntityCable(CableType cableType) {
        super(cableType);
        this.addedToEnergyNet = false;
        this.cableType = cableType;
        this.type = cableType.ordinal();
    }

    public TileEntityCable() {
        super(CableType.glass);
        this.addedToEnergyNet = false;
        this.cableType = CableType.glass;
        this.type = cableType.ordinal();
    }

    @Override
    public void onEntityCollision(final Entity entity) {
        super.onEntityCollision(entity);
        if (!this.getWorld().isRemote && entity instanceof EntityLivingBase) {
            if (cableType == CableType.tin || cableType == CableType.copper || cableType == CableType.gold || cableType == CableType.iron) {
                NodeStats stats = EnergyNetGlobal.instance.getNodeStats(this);
                if (entity instanceof EntityPlayer) {
                    EntityPlayer player = (EntityPlayer) entity;
                    if (!IHazmatLike.hasCompleteHazmat(player))
                        if (stats.getEnergyIn() > 0) {
                            entity.attackEntityFrom(IUDamageSource.radiation, 0.25f)
                            ;
                        }
                } else if (stats.getEnergyIn() > 0) {
                    entity.attackEntityFrom(IUDamageSource.radiation, 0.25f)
                    ;
                }
            }

        }
    }

    public static TileEntityCable delegate(CableType cableType) {
        return new TileEntityCable(cableType);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockCable.cable_iu;
    }

    public BlockTileEntity getBlock() {
        return IUItem.cableblock;
    }

    public ICableItem getCableItem() {
        return cableType;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.cableType = CableType.values[nbt.getByte("cableType") & 255];
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setByte("cableType", (byte) this.cableType.ordinal());
        return nbt;
    }

    @Override
    public void updateTileServer(final EntityPlayer var1, final double var2) {
        super.updateTileServer(var1, var2);
        MinecraftForge.EVENT_BUS.post(new EnergyTileUnLoadEvent(this.getWorld(), this));
        this.needUpdate = true;
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.needUpdate) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this.getWorld(), this, this));
            this.needUpdate = false;
            this.updateConnectivity();
        }
    }

    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isRemote) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this.getWorld(), this, this));
            this.addedToEnergyNet = true;
            this.updateConnectivity();

        }

    }

    public void onUnloaded() {
        if (IUCore.proxy.isSimulating() && this.addedToEnergyNet) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnLoadEvent(this.getWorld(), this));
            this.addedToEnergyNet = false;
            this.updateConnectivity();
        }


        super.onUnloaded();
    }

    public SoundType getBlockSound(Entity entity) {
        return SoundType.CLOTH;
    }

    public void onPlaced(ItemStack stack, EntityLivingBase placer, EnumFacing facing) {
        super.onPlaced(stack, placer, facing);
    }

    public ItemStack getPickBlock(EntityPlayer player, RayTraceResult target) {
        return new ItemStack(IUItem.cable, 1, this.cableType.ordinal());
    }

    @Override
    public ItemStack getItem(final EntityPlayer player, final RayTraceResult target) {
        return new ItemStack(IUItem.cable, 1, this.cableType.ordinal());
    }

    public void onNeighborChange(Block neighbor, BlockPos neighborPos) {
        super.onNeighborChange(neighbor, neighborPos);
        if (!this.getWorld().isRemote) {
            this.updateConnectivity();
        }

    }


    public void updateConnectivity() {
        World world = this.getWorld();
        byte newConnectivity = 0;
        EnumFacing[] var4 = EnumFacing.VALUES;

        for (EnumFacing dir : var4) {
            newConnectivity = (byte) (newConnectivity << 1);
            IEnergyTile tile = EnergyNetGlobal.instance.getTile(world, this.pos.offset(dir));
            if (!this.getBlackList().contains(dir)) {
                if ((tile instanceof IEnergyAcceptor && ((IEnergyAcceptor) tile).acceptsEnergyFrom(
                        this,
                        dir.getOpposite()
                ) || tile instanceof IEnergyEmitter && ((IEnergyEmitter) tile).emitsEnergyTo(
                        this,
                        dir.getOpposite()
                ))) {
                    newConnectivity = (byte) (newConnectivity + 1);
                }
            }


        }
        setConnectivity(newConnectivity);

    }

    public boolean wrenchCanRemove(EntityPlayer player) {
        return false;
    }

    public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing direction) {
        return !getBlackList().contains(direction);
    }

    public boolean emitsEnergyTo(IEnergyAcceptor receiver, EnumFacing direction) {
        return !getBlackList().contains(direction);
    }


    public double getConductionLoss() {
        return this.cableType.loss;
    }

    public double getConductorBreakdownEnergy() {
        return this.cableType.capacity + 1;
    }

    public void removeConductor() {
        this.getWorld().setBlockToAir(this.pos);
        new PacketCableSound(this.getWorld(), this.pos,
                0.5F,
                2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F
        );
    }


    public CustomPacketBuffer writePacket() {
        final CustomPacketBuffer packet = super.writePacket();
        try {
            EncoderHandler.encode(packet, cableType);
            EncoderHandler.encode(packet, connectivity);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public void readPacket(CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);
        try {
            cableType = CableType.values[(int) DecoderHandler.decode(customPacketBuffer)];
            connectivity = (byte) DecoderHandler.decode(customPacketBuffer);
            this.rerender();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void update_render() {
        if (!this.getWorld().isRemote) {
            this.updateConnectivity();
        }
    }

    @Override
    public TileEntity getTileEntity() {
        return this;
    }

    @Override
    public BlockPos getBlockPos() {
        return this.pos;
    }

}
