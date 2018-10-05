package ghostwolf.simplyloaders.init;

import ghostwolf.simplyloaders.Reference;
import ghostwolf.simplyloaders.items.ItemTankcart;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItems {
	
	@GameRegistry.ObjectHolder(Reference.MOD_ID + ":tankcart")
	public static ItemTankcart tankcart;

	public void init () {
		
	}
	
	@SideOnly(Side.CLIENT)
	public static void initModels () {
		tankcart.initModel();
	}
	
}
