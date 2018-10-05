package ghostwolf.simplyloaders.blocks;

import java.util.List;

import ghostwolf.simplyloaders.Reference;
import ghostwolf.simplyloaders.tileentities.TileEntityRaintank;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockRaintank extends Block implements ITileEntityProvider{

	public BlockRaintank() {
		super(Material.WOOD);
		setHarvestLevel("axe", 0);
		setRegistryName("raintank");
		setUnlocalizedName(Reference.MOD_ID + "." + "raintank");
		setCreativeTab(CreativeTabs.DECORATIONS);
		setHardness(1.2F);
		setResistance(10F);
		setSoundType(blockSoundType.WOOD);	
		
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityRaintank();
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		
		if (! worldIn.isRemote) {
			TileEntity te = worldIn.getTileEntity(pos);
			if (te instanceof TileEntityRaintank) {
				ItemStack s = playerIn.getHeldItem(hand);
				if (s.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
					IFluidHandlerItem itemTank = s.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
					IFluidHandler rainTank =  te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
					
					FluidStack drained = rainTank.drain(Fluid.BUCKET_VOLUME, false);
					int inserted = itemTank.fill(drained, true);
					
					rainTank.drain(inserted, true);
					
					playerIn.setHeldItem(hand, itemTank.getContainer());
					
				} else if (s.getItem() == Item.getItemFromBlock(Blocks.HOPPER)) {
					((TileEntityRaintank) te).toggleAutoOutput();
				}
			}
		}
		
		return true;
	}
	
	@SideOnly(Side.CLIENT)
    public void initModel() {
	 ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
 }
 
 
 @Override
public boolean isNormalCube(IBlockState state) {
	
	return false;
	}
 
 @Override
public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
	
	return false;
	}
 
 @Override
public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
		List<AxisAlignedBB> collidingBoxes, Entity entityIn, boolean isActualState) {
	addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0,0,0,0.1,1,1));
	addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0,0,0,1,1,0.1));
	addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(1,0,0,0.9,1,1));
	addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0,0,1,1,1,0.9));
	//floor
	addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0,0,0,1,0.1,1));
	
}
	

}
