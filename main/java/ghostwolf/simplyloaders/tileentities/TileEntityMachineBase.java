package ghostwolf.simplyloaders.tileentities;

import java.util.List;

import javax.swing.plaf.basic.BasicComboBoxUI.ItemHandler;

import ghostwolf.simplyloaders.blocks.BlockMachineBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityMachineBase extends TileEntity implements ITickable{
	
	public int invSize = 0;
	private ItemStackHandler itemStackHandler = null;
	protected FluidTank tank = null;
	
	@Override
	public void update() {
		
	}
	
	public TileEntityMachineBase(int invSize) {
		this.invSize = invSize;
		
		if (invSize > 0) {
			this.itemStackHandler = new ItemStackHandler(invSize) {
			 @Override
		        protected void onContentsChanged(int slot) {
		            // We need to tell the tile entity that something has changed so
		            // that the chest contents is persisted
		            markDirty();
			 	}
			};
		}
	}
	
	public TileEntityMachineBase (int invSize, int tankSize) {
		this(invSize);
		this.tank = new FluidTank (tankSize * Fluid.BUCKET_VOLUME);
	}
	

    public ItemStackHandler getInv () {
    	return this.itemStackHandler;
    }
	
	public EntityMinecart getCart (EnumFacing side) {
		World w = getWorld();
		if (! w.isRemote) {
			//only run on server side
			BlockPos p = getPos();
			//create a dummy target pos
			BlockPos tp = p;
			switch (side) {
			case UP:
				tp = p.add(1,2,1);
				break;
			case DOWN:
				tp = p.add(1,-1,1);
				break;
			case NORTH:
				tp = p.add(1,1,-1);
				break;
			case SOUTH:
				tp = p.add(1,1,2);
				break;
			case WEST:
				tp = p.add(-1,1,1);	
				break;
			case EAST:
				tp = p.add(2,1,1);
				break;
			default:
				//nothing
			}
			
			//creates a new bb out of current pos and target pos
			AxisAlignedBB bb = new AxisAlignedBB(p,tp);
			
			List<EntityMinecart> l = w.getEntitiesWithinAABB(EntityMinecart.class, bb);
			if (!l.isEmpty()) {
				return l.get(0);
			} else {
				//there are no carts so return null
				return null;
			}
		}
		//return null on client side
		return null;
	}
	


    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("items")) {
            itemStackHandler.deserializeNBT((NBTTagCompound) compound.getTag("items"));
        }
        if (this.tank != null) {
        	this.tank.readFromNBT(compound);
    	}
 
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        if (this.itemStackHandler != null) {
        	compound.setTag("items", itemStackHandler.serializeNBT());
        }
        if (this.tank != null) {
        	this.tank.writeToNBT(compound);
        }
        return compound;
    }

    public boolean canInteractWith(EntityPlayer playerIn) {
        // If we are too far away from this tile entity you cannot use it
        return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
        	if (this.itemStackHandler != null) {
        		return true;
        	}
        }
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
        	if (this.tank != null) {
        		return true;
        	}
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
        	if (this.itemStackHandler != null) {
        		return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(itemStackHandler);
        	}
        }
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
        	if (this.tank != null) {
        		return (T) this.tank;
        	}
        }
        return super.getCapability(capability, facing);
    }
    
    public boolean cartIsEmpty (EntityMinecart cart) {
		 if (cart.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {		 
			 return invIsEmpty (cart.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null));
		 }
		 return true;
	 }
    
    //returns if an inventory is empty
    public boolean invIsEmpty (IItemHandler inv) {
    	for (int i = 0; i < inv.getSlots(); i++) {
			if (! inv.getStackInSlot(i).isEmpty()) {
				return false;
			}
		}
    	return true;
    }
    
    public boolean cartIsFull (EntityMinecart cart) {
    	if (cart.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
    		return invIsFull(cart.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null));
    	}
    	return true;
    }
    
    //returns if an inventory is full
    public boolean invIsFull (IItemHandler inv) {
    	for (int i = 0; i < inv.getSlots(); i++) {
			if ( inv.getStackInSlot(i).isEmpty()) {
				return false;
			}
		}
    	return true;
    }
    
    //sets the redstone property of the block
    public void setRedstone (boolean val) {
    	World w = getWorld();
    	if (! w.isRemote) {
    		IBlockState b = w.getBlockState(getPos());
    		if (b.getValue(BlockMachineBase.POWER) != val) {
    			w.setBlockState(getPos(), b.withProperty(BlockMachineBase.POWER, val));
    		}
    	}
    }
    
    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
    	if (oldState.getBlock() != newState.getBlock()) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public boolean cartIsFull (EntityMinecart cart, FluidStack fluid) {
    	if (cart.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)) {
    		IFluidHandler t = cart.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
    		int filled = t.fill(fluid, false);
    		if (filled <= 0) {
    			return true;
    		}
    	}
    	return false;
    }
    
    

}
