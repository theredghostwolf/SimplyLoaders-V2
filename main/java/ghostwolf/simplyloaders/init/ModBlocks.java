package ghostwolf.simplyloaders.init;


import ghostwolf.simplyloaders.Reference;
import net.minecraftforge.fml.common.registry.GameRegistry;
import ghostwolf.simplyloaders.blocks.BlockEntityLoader;
import ghostwolf.simplyloaders.blocks.BlockEntityUnloader;
import ghostwolf.simplyloaders.blocks.BlockFluidLoader;
import ghostwolf.simplyloaders.blocks.BlockFluidUnloader;
import ghostwolf.simplyloaders.blocks.BlockLoader;
import ghostwolf.simplyloaders.blocks.BlockRaintank;
import ghostwolf.simplyloaders.blocks.BlockUnloader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModBlocks {
	
	 //reference blocks here
	 //@GameRegistry.ObjectHolder("modtut:firstblock")
	 //public static FirstBlock firstBlock;
	
	@GameRegistry.ObjectHolder(Reference.MOD_ID + ":loader")
	public static BlockLoader loader;
	
	@GameRegistry.ObjectHolder(Reference.MOD_ID + ":unloader")
	public static BlockUnloader unloader;
	
	@GameRegistry.ObjectHolder(Reference.MOD_ID + ":entityLoader")
	public static BlockEntityLoader entityLoader;
	
	@GameRegistry.ObjectHolder(Reference.MOD_ID + ":entityUnloader")
	public static BlockEntityUnloader entityUnloader;
	
	@GameRegistry.ObjectHolder(Reference.MOD_ID + ":raintank")
	public static BlockRaintank raintank;
	
	@GameRegistry.ObjectHolder(Reference.MOD_ID + ":fluidloader")
	public static BlockFluidLoader fluidLoader;
	
	@GameRegistry.ObjectHolder(Reference.MOD_ID + ":fluidunloader")
	public static BlockFluidUnloader fluidunloader;
	
	public static void init () {
	
	}
	
	@SideOnly(Side.CLIENT)
	public static void initModels () {
		raintank.initModel();
		loader.initModel();
		unloader.initModel();
		fluidLoader.initModel();
		fluidunloader.initModel();
		entityLoader.initModel();
		entityUnloader.initModel();
	}

}
