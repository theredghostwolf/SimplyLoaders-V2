package ghostwolf.simplyloaders.blocks;

import ghostwolf.simplyloaders.tileentities.TileEntityFluidUnloader;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
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

public class BlockFluidUnloader extends BlockMachineBase {

	public BlockFluidUnloader() {
		super("fluidunloader");
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityFluidUnloader();
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (! worldIn.isRemote) {
			//only handle server side
			ItemStack s = playerIn.getHeldItem(hand);
			if (! s.isEmpty()) {
				if (s.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
					//item is a tank
					IFluidHandler itemT = s.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
					
					TileEntity te = worldIn.getTileEntity(pos);
					if (te != null) {
						IFluidHandler tank =  te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
						FluidStack drained = tank.drain(Fluid.BUCKET_VOLUME, false);
						int filled = itemT.fill(drained, true);
						tank.drain(filled, true);
						te.markDirty();
					}
				} else if (s.getItem() == Item.getItemFromBlock(Blocks.HOPPER)) {
					//toggle auto output
					TileEntity te = worldIn.getTileEntity(pos);
					if (te != null && te instanceof TileEntityFluidUnloader) {
						((TileEntityFluidUnloader) te).toggleAutoOutput(playerIn);
					}
				} else {
					//open gui?
				}
			}
		}
		return true;
	}
	

}
