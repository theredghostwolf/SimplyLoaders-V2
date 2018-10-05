package ghostwolf.simplyloaders.tileentities;

import ghostwolf.simplyloaders.Config;
import ghostwolf.simplyloaders.blocks.BlockFluidLoader;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class TileEntityFluidLoader extends TileEntityMachineBase {
	
	private static int tankSize = Config.FluidLoaderSize;
	private int transferRate = Config.FluidLoaderRate;
	private int transferSpeed = Config.FluidLoaderSpeed;
	private int cooldown = 0;
	
	public TileEntityFluidLoader() {
		super(0, tankSize);
	}
	
	@Override
	public void update() {

		World w = getWorld();
		if (! w.isRemote) {
			if (this.cooldown <= 0) {
				this.cooldown =  this.transferRate;
			EnumFacing f = w.getBlockState(getPos()).getValue(BlockFluidLoader.FACING);
			EnumFacing o = f.getOpposite();
			EntityMinecart c = getCart(f);
			if (c != null) {
				//cart
				if (c.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, o)) {
					//try to load cart
					if (this.tank.getFluidAmount() <= 0) {
						//nothing to load
						setRedstone(true);
					} else {
						IFluidHandler cartTank = c.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, o);
						FluidStack drained = this.tank.drain(this.transferSpeed, false);
						
						int filled = cartTank.fill(drained, true);
						this.tank.drain(filled, true);
						this.markDirty();
						if (filled <= 0) {
							//cart is full
							setRedstone(true);
						}
					}
					
				} else {
					//cart has no tank
					setRedstone(true);
				}
			} else {
				//no cart
				setRedstone(false);
			}
		} else {
			this.cooldown--;
		}
		} 
	}

}
