package ghostwolf.simplyloaders.blocks;

import ghostwolf.simplyloaders.SimplyloadersMod;
import ghostwolf.simplyloaders.gui.GuiHandler;
import ghostwolf.simplyloaders.tileentities.TileEntityUnloader;
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

public class BlockUnloader extends BlockMachineBase {

	public BlockUnloader() {
		super("unloader");
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityUnloader();
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (! worldIn.isRemote) {
			if (!playerIn.isSneaking()) {
				ItemStack s = playerIn.getHeldItem(hand);
  				if (s.getItem() == Item.getItemFromBlock(Blocks.HOPPER)) {
  					TileEntity te = worldIn.getTileEntity(pos);
  					if (te instanceof TileEntityUnloader) {
  						((TileEntityUnloader) te).toggleAutoOutput(playerIn);
  					}
  				} else {
  					playerIn.openGui(SimplyloadersMod.instance, GuiHandler.UNLOADER, worldIn, pos.getX(), pos.getY(), pos.getZ());
  				}
  			} 
		}
		return true;
	}
}