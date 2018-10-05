package ghostwolf.simplyloaders.proxy;

import java.io.File;

import ghostwolf.simplyloaders.Config;
import ghostwolf.simplyloaders.Reference;
import ghostwolf.simplyloaders.SimplyloadersMod;
import ghostwolf.simplyloaders.blocks.BlockEntityLoader;
import ghostwolf.simplyloaders.blocks.BlockEntityUnloader;
import ghostwolf.simplyloaders.blocks.BlockFluidLoader;
import ghostwolf.simplyloaders.blocks.BlockFluidUnloader;
import ghostwolf.simplyloaders.blocks.BlockLoader;
import ghostwolf.simplyloaders.blocks.BlockRaintank;
import ghostwolf.simplyloaders.blocks.BlockUnloader;
import ghostwolf.simplyloaders.gui.GuiHandler;
import ghostwolf.simplyloaders.init.ModBlocks;
import ghostwolf.simplyloaders.init.ModEntities;
import ghostwolf.simplyloaders.init.ModRecipes;
import ghostwolf.simplyloaders.init.ModTileEntities;
import ghostwolf.simplyloaders.items.ItemTankcart;
import ghostwolf.simplyloaders.tileentities.TileEntityEntityLoader;
import ghostwolf.simplyloaders.tileentities.TileEntityEntityUnloader;
import ghostwolf.simplyloaders.tileentities.TileEntityFluidLoader;
import ghostwolf.simplyloaders.tileentities.TileEntityFluidUnloader;
import ghostwolf.simplyloaders.tileentities.TileEntityLoader;
import ghostwolf.simplyloaders.tileentities.TileEntityRaintank;
import ghostwolf.simplyloaders.tileentities.TileEntityUnloader;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber
public class CommonProxy {
	
	// Config instance
    public static Configuration config;

public void preInit(FMLPreInitializationEvent e) {
		
    	File directory = e.getModConfigurationDirectory();
    	config = new Configuration(new File(directory.getPath(), Reference.ConfigFile));
    	Config.readConfig();
    	
    	ModEntities.init();
		ModBlocks.init();
		ModTileEntities.init();

    }

    public void init(FMLInitializationEvent e) {
    	ModRecipes.init();
    	

    }

    public void postInit(FMLPostInitializationEvent e) {
    	   if (config.hasChanged()) {
               config.save();
           }
    }
    
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        //register blocks here
    	//event.getRegistry().register(new FirstBl());
    	event.getRegistry().register(new BlockLoader());
    	event.getRegistry().register(new BlockUnloader());
    	event.getRegistry().register(new BlockEntityLoader());
    	event.getRegistry().register(new BlockEntityUnloader());
    	event.getRegistry().register(new BlockRaintank());
    	event.getRegistry().register(new BlockFluidLoader());
    	event.getRegistry().register(new BlockFluidUnloader());
    	//register tileentities here
    	// GameRegistry.registerTileEntity(DataTileEntity.class, ModTut.MODID + "_datablock");
    	
    	GameRegistry.registerTileEntity(TileEntityFluidLoader.class, Reference.MOD_ID + ":TileEntityFluidLoader");
    	GameRegistry.registerTileEntity(TileEntityLoader.class, Reference.MOD_ID + ":TileEntityLoader");
    	GameRegistry.registerTileEntity(TileEntityUnloader.class, Reference.MOD_ID + ":TileEntityUnloader");
    	GameRegistry.registerTileEntity(TileEntityEntityLoader.class, Reference.MOD_ID + ":TileEntityEntityLoader");
    	GameRegistry.registerTileEntity(TileEntityEntityUnloader.class, Reference.MOD_ID + ":TileEntityEntityUnloader");
    	GameRegistry.registerTileEntity(TileEntityRaintank.class, Reference.MOD_ID + ":TileEntityRaintank");
    	GameRegistry.registerTileEntity(TileEntityFluidUnloader.class, Reference.MOD_ID + ":TileEntityFluidUnloader");
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
    	//register itemblocks here
        //event.getRegistry().register(new ItemBlock(ModBlocks.firstBlock).setRegistryName(ModBlocks.firstBlock.getRegistryName()));
    	event.getRegistry().register(new ItemBlock(ModBlocks.loader).setRegistryName(ModBlocks.loader.getRegistryName()));
    	event.getRegistry().register(new ItemBlock(ModBlocks.unloader).setRegistryName(ModBlocks.unloader.getRegistryName()));
    	event.getRegistry().register(new ItemBlock(ModBlocks.entityLoader).setRegistryName(ModBlocks.entityLoader.getRegistryName()));
    	event.getRegistry().register(new ItemBlock(ModBlocks.entityUnloader).setRegistryName(ModBlocks.entityUnloader.getRegistryName()));
    	event.getRegistry().register(new ItemBlock(ModBlocks.raintank).setRegistryName(ModBlocks.raintank.getRegistryName()));
    	event.getRegistry().register(new ItemBlock(ModBlocks.fluidLoader).setRegistryName(ModBlocks.fluidLoader.getRegistryName()));
    	event.getRegistry().register(new ItemBlock(ModBlocks.fluidunloader).setRegistryName(ModBlocks.fluidunloader.getRegistryName()));
    	
    	event.getRegistry().register(new ItemTankcart());
    }


}
