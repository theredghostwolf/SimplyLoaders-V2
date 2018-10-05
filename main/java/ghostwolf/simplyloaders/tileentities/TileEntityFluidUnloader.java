package ghostwolf.simplyloaders.tileentities;

import ghostwolf.simplyloaders.Config;
import ghostwolf.simplyloaders.blocks.BlockFluidUnloader;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class TileEntityFluidUnloader extends TileEntityMachineBase {

	private static int tankSize = Config.FluidUnloaderSize;
	private int transferRate = Config.FluidUnloaderRate;
	private int transferSpeed = Config.FluidUnloaderSpeed;
	private int cooldown = 0;
	
	public boolean autoOutput = true;
	
	public TileEntityFluidUnloader() {
		super(0, tankSize);
	}
	
	public void toggleAutoOutput () {
		this.autoOutput = !this.autoOutput;
		this.markDirty();
	}
	
	public void toggleAutoOutput (EntityPlayer p) {
		toggleAutoOutput();
		p.sendMessage(new TextComponentString("AutoOutput: " + Boolean.toString(this.autoOutput)));
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setBoolean("autoOutput", autoOutput);
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("autoOutput")) {
			this.autoOutput = compound.getBoolean("autoOutput");
		}
	}
	
	@Override
	public void update() {
		World w = getWorld();
		if (! w.isRemote) {
			if (this.cooldown <= 0) {
				this.cooldown = this.transferRate;
				//update
				EnumFacing f = w.getBlockState(getPos()).getValue(BlockFluidUnloader.FACING);
				EnumFacing o = f.getOpposite();
				EntityMinecart c = getCart(f);
				if (c != null) {
					if (c.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, o)) {
						IFluidHandler cartTank = c.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, o);
						if ( this.tank.getFluidAmount() < this.tank.getCapacity())  {
							FluidStack drained = cartTank.drain(this.transferSpeed, false);
							int filled = this.tank.fill(drained, true);
							cartTank.drain(filled, true);
							this.markDirty();
							if (filled <= 0) {
								//cart is empty
								setRedstone(true);
							}
						} else {
							//unloader tank is full
							setRedstone(true);
						}
					} else {
						//cart has no tank
						setRedstone(true);
					}
				} else {
					//no cart
					setRedstone(false);
				}
				//auto output
				if (this.tank.getFluidAmount() > 0 && this.autoOutput) {
					TileEntity te = w.getTileEntity(getPos().add(o.getFrontOffsetX(),o.getFrontOffsetY(), o.getFrontOffsetZ()));
					if (te != null && te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, f)) {
						IFluidHandler teTank = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, f);
						FluidStack drained = this.tank.drain(this.transferSpeed * 2, false);
						int filled = teTank.fill(drained, true);
						this.tank.drain(filled, true);
						this.markDirty();
					}
				}
				
			} else {
				this.cooldown--;
			}
		}
	}

}
