package ghostwolf.simplyloaders.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

public class GuiUnloader extends GuiLoader {

	public GuiUnloader(Container inventorySlotsIn, InventoryPlayer playerInv) {
		super(inventorySlotsIn, playerInv);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String name = "Unloader";
		fontRenderer.drawString(name, xSize / 2 - fontRenderer.getStringWidth(name) / 2, 6, 0x404040);
		fontRenderer.drawString(playerInv.getDisplayName().getUnformattedText(), 8, ySize - 94, 0x404040);
	}

}
