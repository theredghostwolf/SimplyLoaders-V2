package ghostwolf.simplyloaders.blocks;

import ghostwolf.simplyloaders.tileentities.TileEntityEntityLoader;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockEntityLoader extends BlockMachineBase {

	public BlockEntityLoader() {
		super("entityLoader");
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityEntityLoader();
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		//only handle server side
		if (! worldIn.isRemote) {
			Item i = playerIn.getHeldItem(hand).getItem();
			if ( i == Item.getItemFromBlock(Blocks.REDSTONE_TORCH)) {
				//change emit mode
				TileEntity te = worldIn.getTileEntity(pos);
				if (te instanceof TileEntityEntityLoader) {
					((TileEntityEntityLoader) te).toggleEmitMode(playerIn);
				}
			} else if (i == Items.EGG) {
				//change baby mode
				TileEntity te = worldIn.getTileEntity(pos);
				if (te instanceof TileEntityEntityLoader) {
					((TileEntityEntityLoader) te).toggleBabyMode(playerIn);
				}
			}
		}
		if (playerIn.isSneaking()) {
			return false;
		}
		return true;
	}
}
