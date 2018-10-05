package ghostwolf.simplyloaders.blocks;

import ghostwolf.simplyloaders.tileentities.TileEntityFluidLoader;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class BlockFluidLoader extends BlockMachineBase {

	public BlockFluidLoader() {
		super("fluidloader");
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityFluidLoader();
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote) {
			TileEntity te = worldIn.getTileEntity(pos);
			if (te != null && te instanceof TileEntityFluidLoader) {
				ItemStack s = playerIn.getHeldItem(hand);
				if (s.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
					IFluidHandler itemTank = s.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
					IFluidHandler teTank = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
					
					FluidStack drained = itemTank.drain(Fluid.BUCKET_VOLUME, false);
					
					int filled = teTank.fill(drained, true);
					
					itemTank.drain(filled, true);
					te.markDirty();
				}
			}
		}
		return true;
	}

}