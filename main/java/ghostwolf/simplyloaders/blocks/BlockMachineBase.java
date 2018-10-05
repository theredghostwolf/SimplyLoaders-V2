package ghostwolf.simplyloaders.blocks;

import ghostwolf.simplyloaders.Reference;
import ghostwolf.simplyloaders.tileentities.TileEntityLoader;
import ghostwolf.simplyloaders.tileentities.TileEntityMachineBase;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class BlockMachineBase extends Block implements ITileEntityProvider {

	public static final PropertyDirection FACING = PropertyDirection.create("facing");
	public static final PropertyBool POWER = PropertyBool.create("power");
	
	public BlockMachineBase(String name) {
		super(Material.IRON);
		setHarvestLevel("pickaxe", 0);
		setRegistryName(name);
		setUnlocalizedName(Reference.MOD_ID + "." + name);
		setCreativeTab(CreativeTabs.TRANSPORTATION);
		setHardness(1.2F);
		setResistance(10F);
		setSoundType(blockSoundType.METAL);	
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.UP).withProperty(POWER, false));
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return null;
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
	
	//drops an inventory in the world
	public void dropInv (ItemStackHandler inv, World worldIn, BlockPos pos) {
		if (! worldIn.isRemote) {
    	for (int i = 0; i < inv.getSlots(); i++) {
			ItemStack s = inv.getStackInSlot(i);
			if (! s.isEmpty()) {
				worldIn.spawnEntity(new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), s));
			}
		}
		}
    }
	
	@Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        world.setBlockState(pos, state.withProperty(FACING, getFacingFromEntity(pos, placer)), 2);
    }
	
	 public static EnumFacing getFacingFromEntity(BlockPos clickedBlock, EntityLivingBase entity) {
	        return EnumFacing.getFacingFromVector(
	            (float) (entity.posX - clickedBlock.getX()),
	            (float) (entity.posY - clickedBlock.getY()),
	            (float) (entity.posZ - clickedBlock.getZ()));
	    }

	 @Override
	    public IBlockState getStateFromMeta(int meta) {
	        return getDefaultState()
	                .withProperty(FACING, EnumFacing.getFront(meta & 7))
	                .withProperty(POWER, (meta & 8) != 0);
	    }

	    @Override
	    public int getMetaFromState(IBlockState state) {
	    	return state.getValue(FACING).getIndex() + (state.getValue(POWER) ? 8 : 0);
	    }

	    @Override
	    protected BlockStateContainer createBlockState() {
	        return new BlockStateContainer(this, FACING, POWER);
	    }
	    
		 @Override
		 public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		    	TileEntity te = worldIn.getTileEntity(pos);
		    	if (te instanceof TileEntityMachineBase) {
		    		ItemStackHandler inv = ((TileEntityMachineBase) te).getInv();
		    		if (inv != null) {
		    			dropInv(inv,worldIn,pos);
		    		}
		    	}
		    	super.breakBlock(worldIn, pos, state);
		    }
		 
		 @Override
		public boolean canProvidePower(IBlockState state) {
			return true;
		}
		 
		 @Override
		public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
			 //only emit redstone to facing and the back
			 if (side == blockState.getValue(FACING) || side == blockState.getValue(FACING).getOpposite()) {
				 if (blockState.getValue(POWER)) {
					 return 15;
				 } else {
					return 0;
				}
			 } else {
				 return 0;
			 }
		}
		 
		@SideOnly(Side.CLIENT)
		   public void initModel() {
			 ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
		 }
		 
		 
	
		 
		 
		

}
