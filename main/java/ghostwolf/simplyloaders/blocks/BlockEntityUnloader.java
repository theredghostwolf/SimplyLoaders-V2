package ghostwolf.simplyloaders.blocks;

import ghostwolf.simplyloaders.tileentities.TileEntityEntityUnloader;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockEntityUnloader extends BlockMachineBase{

	public BlockEntityUnloader() {
		super("entityUnloader");
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityEntityUnloader();
	}

}
