package ghostwolf.simplyloaders.tileentities;

import java.util.List;

import ghostwolf.simplyloaders.Config;
import ghostwolf.simplyloaders.blocks.BlockMachineBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityEntityUnloader extends TileEntityMachineBase {
	
	public int updateRate = Config.EntityUnloaderRate;
	private int updateCooldown = 0;

	public TileEntityEntityUnloader() {
		super(0);
	}
	
	@Override
	public void update() {
			World w = getWorld();
			if (! w.isRemote) {
				if (this.updateCooldown <= 0) {
					this.updateCooldown = updateRate;
				///only update server side
				EnumFacing f = w.getBlockState(getPos()).getValue(BlockMachineBase.FACING);
				EntityMinecart c = getCart(f);
				if (c != null) {
					if (c.isBeingRidden()) {
						//dismount
						List<Entity> l = c.getPassengers();
						c.removePassengers();
						for (int i = 0; i < l.size(); i++) {
							EnumFacing o = f.getOpposite();
							BlockPos p = new BlockPos(getPos().add(o.getFrontOffsetX(), o.getFrontOffsetY(), o.getFrontOffsetZ()));
							l.get(i).setPosition(p.getX() + 0.5D,p.getY() + 0.7D ,p.getZ() + 0.5D);
						}
					} else {
						//empty cart
						setRedstone(true);
					}
				} else {
					//no cart
					setRedstone(false);
				}
			} else {
				this.updateCooldown--;
			}	
		} 
	}
}
