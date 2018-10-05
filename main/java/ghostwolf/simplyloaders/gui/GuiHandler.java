package ghostwolf.simplyloaders.gui;

import ghostwolf.simplyloaders.containers.ContainerLoader;
import ghostwolf.simplyloaders.tileentities.TileEntityLoader;
import ghostwolf.simplyloaders.tileentities.TileEntityMachineBase;
import ghostwolf.simplyloaders.tileentities.TileEntityUnloader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
	
	public static final int LOADER = 0;
	public static final int UNLOADER = 1;

	@Override
	public Container getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
		case LOADER:
			return new ContainerLoader(player.inventory, (TileEntityLoader)world.getTileEntity(new BlockPos(x, y, z)));
		case UNLOADER:
			return new ContainerLoader(player.inventory, (TileEntityUnloader) world.getTileEntity(new BlockPos(x,y,z)));
		default:
			return null;
	}
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
		case LOADER:
			return new GuiLoader(getServerGuiElement(ID, player, world, x, y, z), player.inventory);
		case UNLOADER:
			return new GuiUnloader(getServerGuiElement(ID, player, world,x ,y, z), player.inventory);
		default:
			return null;
	}
	}

}
