package ghostwolf.simplyloaders.entities;

import ghostwolf.simplyloaders.Config;
import ghostwolf.simplyloaders.init.ModBlocks;
import ghostwolf.simplyloaders.init.ModItems;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class EntityMinecartTank extends EntityMinecart {
	
	private FluidTank tank;
	private int tankSize = Config.TankcartSize;
	
	public EntityMinecartTank(World worldIn) {
		
		super(worldIn);
		this.tank = new FluidTank (Fluid.BUCKET_VOLUME * this.tankSize);
	}
	
	public EntityMinecartTank(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
		this.tank = new FluidTank (Fluid.BUCKET_VOLUME * this.tankSize);
		
	}

	@Override
	public Type getType() {
		return null;
	}
	
	@Override
	public IBlockState getDefaultDisplayTile() {
		return ModBlocks.raintank.getDefaultState();
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return (T) this.tank;
		}
		return super.getCapability(capability, facing);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return true;
		} else {
			return super.hasCapability(capability, facing);
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound = this.tank.writeToNBT(compound);
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		this.tank.readFromNBT(compound);
		super.readFromNBT(compound);
	}
	
	@Override
	public ItemStack getCartItem() {
		return new ItemStack(ModItems.tankcart, 1);
	}
	
	@Override
	public void killMinecart(DamageSource source) {
		 super.killMinecart(source);

	        if (this.world.getGameRules().getBoolean("doEntityDrops"))
	        {
	            this.dropItemWithOffset(Item.getItemFromBlock(ModBlocks.raintank), 1, 0.0F);
	        }
	}
	
	
	

}
