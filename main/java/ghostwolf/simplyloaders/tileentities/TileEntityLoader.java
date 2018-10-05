package ghostwolf.simplyloaders.tileentities;

import ghostwolf.simplyloaders.Config;
import ghostwolf.simplyloaders.blocks.BlockLoader;
import ghostwolf.simplyloaders.blocks.BlockMachineBase;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class TileEntityLoader extends TileEntityMachineBase {
	
	public int transferSpeed = Config.LoaderTransferSpeed; // amount of items to transfer per operation
	public int transferRate = Config.LoaderTransferRate; // ticks between each transfer
	public int transferCooldown = 0;
	
	public TileEntityLoader() {
		super(9); //set inv size to 9
	}
	
	@Override
	public void update() {
		
	
		//only update server side
		World w = getWorld();
		if (! w.isRemote) {
			if (this.transferCooldown <= 0) {
				this.transferCooldown = transferRate;
			
			EnumFacing f = w.getBlockState(this.getPos()).getValue(BlockMachineBase.FACING);
			EnumFacing o = f.getOpposite();
			EntityMinecart c = this.getCart(f);
		if (c != null) {
			if (c.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, o)) {
				IItemHandler cartInv = c.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, o);
				if (invIsEmpty(this.getInv())) {
					//no items to load
					//emit redstone
					setRedstone(true);
				} else {
						//load items
						
						IItemHandler inv = getInv();
						boolean transferedItem = false;
						for (int i = 0; i < inv.getSlots(); i++) {
							ItemStack extracted = inv.extractItem(i, transferSpeed, true);
							if (! extracted.isEmpty()) {
								//we extracted a stack
								for (int j = 0; j < cartInv.getSlots(); j++) {
									ItemStack remainder = cartInv.insertItem(j, extracted, true);
									if (remainder.isEmpty() || remainder.getCount() < extracted.getCount()) {
										//items have been moved
										ItemStack removed = inv.extractItem(i, extracted.getCount() - remainder.getCount(), false);
										ItemStack r = cartInv.insertItem(j, removed, false);
										transferedItem = true;
										this.markDirty(); //tell the game our data has changed
										break;
									}
								}
							}
							if (transferedItem) {
								break;
							}
						}
						if (!transferedItem) {
							//didnt move item so cart must be full
							setRedstone(true);
						}
					
					
				}
			} else {
				//cart has no inv
				//set redstone to true
				setRedstone(true);
			}
			} else {
				//no cart
				//set redstone to false
				setRedstone(false);
			}
		} else {
			this.transferCooldown--;
		}
	} 
	} 

}
