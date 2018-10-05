package ghostwolf.simplyloaders.blocks;

import ghostwolf.simplyloaders.SimplyloadersMod;
import ghostwolf.simplyloaders.gui.GuiHandler;
import ghostwolf.simplyloaders.tileentities.TileEntityLoader;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

public class BlockLoader extends BlockMachineBase {
	
	public BlockLoader() {
		super("loader");
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityLoader();
	}	   
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote) {
			// ...
 			if (!playerIn.isSneaking()) {
  				playerIn.openGui(SimplyloadersMod.instance, GuiHandler.LOADER, worldIn, pos.getX(), pos.getY(), pos.getZ());
  			} else {
  				
  			}
  		}
		return true;
	}

}
