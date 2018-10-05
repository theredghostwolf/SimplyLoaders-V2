package ghostwolf.simplyloaders.tileentities;


import ghostwolf.simplyloaders.Config;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class TileEntityRaintank extends TileEntity implements ITickable{

	private FluidTank tank;
	private int tankSize = Config.RaintankSize;
	
	private int generationRate = Config.RaintankGenerationRate;
	private int generationSpeed = Config.RaintankGenerationSpeed;
	private int updateCooldown = 0;
	
	public boolean autoOutput = true;
	
	public TileEntityRaintank() {
		this.tank = new FluidTank(tankSize * Fluid.BUCKET_VOLUME) {
			@Override
			public boolean canFillFluidType(FluidStack fluid) {
				if (fluid.getFluid() == FluidRegistry.WATER) {
					return true;
				}
				return false;
			}
		};
	}
	
	@Override
	public void update() {
		
			World w = getWorld();
			if (! w.isRemote) {
				if (this.updateCooldown <= 0) {
					this.updateCooldown = this.generationRate;
				//handle rain
				if (w.isRainingAt(getPos().add(0, 1, 0))) {
					double str = w.getRainStrength(1);
					int b = this.tank.getFluidAmount();
					this.tank.fill(new FluidStack(FluidRegistry.WATER, generationSpeed), true);
					if (this.tank.getFluidAmount() != b) {
						this.markDirty();
					}
				}
				
				if (this.autoOutput) {
					TileEntity te = w.getTileEntity(getPos().add(0, -1, 0));
					if (te != null && te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP)) {
						FluidStack drained = this.tank.drain(this.generationSpeed * 2, false);
						IFluidHandler outputTank = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP);
						int filled = outputTank.fill(drained, true);
						this.tank.drain(filled, true);
						if (filled > 0) {
							this.markDirty();
						}
					}
				}
			} else {
				this.updateCooldown--;
			}
		} 
		
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return true;
		}
		return false;
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && facing != EnumFacing.UP) {
			return (T) this.tank;
		}
		return null;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound = this.tank.writeToNBT(compound);
		compound.setBoolean("autoOutput", this.autoOutput);
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("autoOutput")) {
			this.autoOutput = compound.getBoolean("autoOutput");
		}
		this.tank.readFromNBT(compound);
	}
	
	public void toggleAutoOutput () {
		this.autoOutput = !this.autoOutput;
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		if (oldState.getBlock() != newState.getBlock()) {
    		return true;
    	} else {
    		return false;
    	}
	}
	


}
