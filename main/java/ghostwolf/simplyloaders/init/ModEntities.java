package ghostwolf.simplyloaders.init;

import ghostwolf.simplyloaders.Reference;
import ghostwolf.simplyloaders.SimplyloadersMod;
import ghostwolf.simplyloaders.entities.EntityMinecartTank;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class ModEntities {
	
	public static void init () {
		
		int id = 1;
		
	    EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID + ":tankcart"), EntityMinecartTank.class, "tankcart", id++, SimplyloadersMod.instance, 64, 1, true);
	}

}
