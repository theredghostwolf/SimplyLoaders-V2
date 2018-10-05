package ghostwolf.simplyloaders.proxy;

import ghostwolf.simplyloaders.init.ModBlocks;
import ghostwolf.simplyloaders.init.ModItems;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)

public class ClientProxy extends CommonProxy {
	
	 @Override
	    public void preInit(FMLPreInitializationEvent e) {
	        super.preInit(e);
	        
	    }
	 
	 @Override
	public void init(FMLInitializationEvent e) {
		// TODO Auto-generated method stub
		super.init(e);
		
	}
	 
	  @SubscribeEvent
	    public static void registerModels(ModelRegistryEvent event) {
	 	   ModBlocks.initModels();
	 	   ModItems.initModels();
	 	  	
	    }
	


}
