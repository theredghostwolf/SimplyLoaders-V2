package ghostwolf.simplyloaders.containers;

import ghostwolf.simplyloaders.tileentities.TileEntityMachineBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerLoader extends Container {
	
	private TileEntityMachineBase te;

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);
	
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
	
			int containerSlots = inventorySlots.size() - player.inventory.mainInventory.size();
	
			if (index < containerSlots) {
				if (!this.mergeItemStack(itemstack1, containerSlots, inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, 0, containerSlots, false)) {
				return ItemStack.EMPTY;
			}
	
			if (itemstack1.getCount() == 0) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
	
			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}
	
			slot.onTake(player, itemstack1);
		}
	
		return itemstack;
	}
	
	 public ContainerLoader(IInventory playerInventory, TileEntityMachineBase te) {
	        this.te = te;

	        // This container references items out of our own inventory (the 9 slots we hold ourselves)
	        // as well as the slots from the player inventory so that the user can transfer items between
	        // both inventories. The two calls below make sure that slots are defined for both inventories.
	        addOwnSlots();
	        addPlayerSlots(playerInventory);
	    }

	    private void addPlayerSlots(IInventory playerInventory) {
	        // Slots for the main inventory
	        for (int row = 0; row < 3; ++row) {
	            for (int col = 0; col < 9; ++col) {
	                int x = 8 + col * 18;
	                int y = row * 18 + 84;
	                this.addSlotToContainer(new Slot(playerInventory, col + row * 9 + 10, x, y));
	            }
	        }

	        // Slots for the hotbar
	        for (int row = 0; row < 9; ++row) {
	            int x = 8 + row * 18;
	            int y = 58 + 84;
	            this.addSlotToContainer(new Slot(playerInventory, row, x, y));
	        }
	    }

	    private void addOwnSlots() {
	        IItemHandler itemHandler = this.te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
	        int startx = 62;
	        int starty = 17;
	        
	        int width = 3;
	        double height = Math.floor(itemHandler.getSlots() / width);
	        
	        // Add our own slots
	        int slotIndex = 0;
	        
	        int y = starty;
	        for (int i = 0; i < height; i++) {
	        	int x = startx;
	        	if (i == height - 1 && itemHandler.getSlots() % width > 0) {
	        		//add one extra slot on last row if we cannot divide by width
	        		width++;
	        	}
				for (int j = 0; j < width; j++) {
					addSlotToContainer(new SlotItemHandler(itemHandler, slotIndex, x, y));
		            slotIndex++;
		            x += 18;
				}
				y += 18;
			}
	    }

}
