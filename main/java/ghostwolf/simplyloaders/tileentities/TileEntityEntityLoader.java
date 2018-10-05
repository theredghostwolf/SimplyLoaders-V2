package ghostwolf.simplyloaders.tileentities;

import java.util.List;

import ghostwolf.simplyloaders.Config;
import ghostwolf.simplyloaders.blocks.BlockMachineBase;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class TileEntityEntityLoader extends TileEntityMachineBase {
	
	public int updateRate = Config.EntityLoaderRate;
	private int updateCooldown = 0;
	
	public boolean EmitNoEntities = false;
	public boolean onlyBaby = false;
	
	public boolean getBabyMode () {
		return this.onlyBaby;
	}
	
	public void setBabyMode (boolean mode) {
		this.onlyBaby = mode;
		this.markDirty();
	}
	
	public boolean getEmitMode () {
		return this.EmitNoEntities;
	}
	
	public void setEmitMode (boolean mode) {
		this.EmitNoEntities = mode;
		this.markDirty();
	}
	
	public void toggleBabyMode() {
		this.onlyBaby = !this.onlyBaby;
		this.markDirty();
	}
	
	public void toggleEmitMode () {
		this.EmitNoEntities = !this.EmitNoEntities;
		this.markDirty();
	}
	
	public void toggleEmitMode (EntityPlayer p) {
		toggleEmitMode();
		p.sendMessage(new TextComponentString("Emit on no entities: " + Boolean.toString(this.EmitNoEntities)));
	}
	
	public void toggleBabyMode(EntityPlayer p) {
		toggleBabyMode();
		p.sendMessage(new TextComponentString("Only babies: " + Boolean.toString(this.onlyBaby)));
	}
	
	
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("onlybaby")) {
			this.onlyBaby = compound.getBoolean("onlybaby");
		}
		if (compound.hasKey("emitNoEntities")) {
			this.EmitNoEntities = compound.getBoolean("emitNoEntities");
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setBoolean("onlybaby", onlyBaby);
		compound.setBoolean("emitNoEntities", EmitNoEntities);
		return super.writeToNBT(compound);
	}
	
	public TileEntityEntityLoader() {
		super(0);
	}
	
	@Override
	public void update() {
		
			World w = getWorld();
		
			//only update server side
			if (! w.isRemote) {
				if (this.updateCooldown <= 0) {
					this.updateCooldown = updateRate;
				EnumFacing f = w.getBlockState(getPos()).getValue(BlockMachineBase.FACING);
				EntityMinecart c = getCart(f);
				if (c != null) {
					if (c.canBeRidden() && c.getPassengers().isEmpty()) {
						//found empty ridable cart
						AxisAlignedBB bb = new AxisAlignedBB(getPos());
						BlockPos p = getPos();
						switch (f.getOpposite()) {
						case UP:
							bb = new AxisAlignedBB(p.add(-1, 1, -1), p.add(2, 4, 2));
							break;
						case DOWN:
							bb = new AxisAlignedBB(p.add(-1, 0, -1), p.add(2, -3, 2));
							break;
						case EAST:
							bb = new AxisAlignedBB(p.add(1, -1, -1), p.add(4, 2, 2));
							break;
						case NORTH:
							bb = new AxisAlignedBB(p.add(-1, -1, 0), p.add(1, 2, -3));
							break;
						case SOUTH:
							bb = new AxisAlignedBB(p.add(1, -1, 1), p.add(-1,2, 4));
							break;
						case WEST:
							bb = new AxisAlignedBB(p.add(0, -1, 1), p.add(-3, 2, -2));
							break;
						default:
							//do nothing
							break;
						}
		
						List<EntityLivingBase> l = w.getEntitiesWithinAABB(EntityLivingBase.class, bb);
						boolean movedEntity = false;
						if (! l.isEmpty()) {
							for (int i = 0; i < l.size(); i++) {
								EntityLivingBase el = l.get(i);
								if (this.onlyBaby) {
									
									if (el.isChild()) {
										el.startRiding(c);
										movedEntity = true;
										break;
									}
								} else {
									el.startRiding(c);
									movedEntity = true;
									break;
								}
							}	
						}
						if (l.isEmpty() && this.EmitNoEntities || ! movedEntity && this.EmitNoEntities) {
							setRedstone(true);
						}
					} else {
						//cart is full or cant be ridden
						setRedstone(true);
					}
				
				} else {
					//if there is no cart then reset redstone to false
					setRedstone(false);
				}
			} else {
				this.updateCooldown--;
			}

		} 
	}
	
}
