package ghostwolf.simplyloaders.tileentities;

import ghostwolf.simplyloaders.Config;
import ghostwolf.simplyloaders.blocks.BlockMachineBase;
import ghostwolf.simplyloaders.blocks.BlockUnloader;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class TileEntityUnloader extends TileEntityMachineBase {
	
	public int transferSpeed = Config.UnloaderTransferSpeed; // amount of items to transfer per operation
	public int transferRate = Config.UnloaderTransferRate; // ticks between each transfer
	public int transferCooldown = 0;
	
	public boolean autoOutput = true;
	
	public TileEntityUnloader() {
		super(9);
	}
	
	public void update() {
		
			//do stuff
			World w = getWorld();
			if (! w.isRemote) {
				if (this.transferCooldown <= 0) {
					this.transferCooldown = transferRate;
				//only execute on server side
				EnumFacing f = w.getBlockState(getPos()).getValue(BlockMachineBase.FACING);
				EnumFacing o = f.getOpposite();
				EntityMinecart c = getCart(f);
				if (c != null) {
					//there is a cart
					if (c.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, o)) {
						//cart has inventory
						IItemHandler cartInv = c.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, o);
						IItemHandler inv = getInv();
						if (invIsEmpty(cartInv)) {
							//cart is empty - no items to unload
							//or unloader is full
							setRedstone(true);
						} else {
							//cart has items we can unload
							boolean transferedItem = false;
							//loop over all cart slots
							for (int i = 0; i < cartInv.getSlots(); i++) {
								ItemStack extracted = cartInv.extractItem(i, transferSpeed, true);
								if (! extracted.isEmpty()) {
									//extracted item
									
									//loop over unloader inv to find a spot
									for (int j = 0; j < inv.getSlots(); j++) {
										ItemStack remainder = inv.insertItem(j, extracted, true);
										if (remainder.isEmpty() || remainder.getCount() < extracted.getCount()) {
											//moved an item
											ItemStack removed = cartInv.extractItem(i, extracted.getCount() - remainder.getCount(), false);
											ItemStack r = inv.insertItem(j, removed, false);
											transferedItem = true;
											this.markDirty();
											break;
										}
									}
									if (transferedItem) {
										break;
									}
								}
							}
							if (! transferedItem) {
								//didnt move item while there where items to move to unloader must be full
								setRedstone(true);
							}
						}
					} else {
						//cart cant be loaded
						//emit redstone
						setRedstone(true);
					}
				} else {
					//no cart - reset redstone to false
					setRedstone(false);
				}
				if (this.autoOutput && !invIsEmpty(getInv())) {
					//auto output items
					BlockPos p = getPos();
					TileEntity te = w.getTileEntity(p.add(o.getFrontOffsetX(), o.getFrontOffsetY(), o.getFrontOffsetZ()));
					if (te != null && te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, o)) {
						IItemHandler targetInv = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, o);
						IItemHandler inv = getInv();
						boolean movedItem = false;
						for (int i = 0; i < inv.getSlots(); i++) {
							ItemStack extracted = inv.extractItem(i, transferSpeed * 2,  true);
							if (! extracted.isEmpty()) {
								//skip if the stack is empty
								for (int j = 0; j < targetInv.getSlots(); j++) {
									 ItemStack remainder = targetInv.insertItem(j, extracted, true);
									 if (remainder.isEmpty() || remainder.getCount() < extracted.getCount()) {
										 ItemStack moved = inv.extractItem(i,extracted.getCount() - remainder.getCount(), false);
									 	targetInv.insertItem(j, moved, false);
									 	movedItem = true;
									 	break;
									 }
								}
							}
							if (movedItem) {
								break;
							}
						}
					}
				}
			} else {
				this.transferCooldown--;
			}	
		} 
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setBoolean("autoOutput", this.autoOutput);
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("autoOutput")) {
			this.autoOutput = compound.getBoolean("autoOutput");
		}
	}
	
	public void toggleAutoOutput () {
		this.autoOutput = !this.autoOutput;
	}
	
	public void toggleAutoOutput(EntityPlayer p) {
		toggleAutoOutput();
		p.sendMessage(new TextComponentString("AutoOuput: " + Boolean.toString(this.autoOutput)));
	}
	
}
