package max.bettermagnets.items.ItemMagnet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ItemMagnetContainer extends Container {
	
	ItemStackHandlerBlacklist blacklist = new ItemStackHandlerBlacklist(9);
	
	public ItemMagnetContainer(InventoryPlayer inventory) {
		addSlotToContainer(new SlotItemHandler(blacklist, 0, 62, 17));
		addSlotToContainer(new SlotItemHandler(blacklist, 1, 80, 17));
		addSlotToContainer(new SlotItemHandler(blacklist, 2, 98, 17));
		addSlotToContainer(new SlotItemHandler(blacklist, 3, 62, 35));
		addSlotToContainer(new SlotItemHandler(blacklist, 4, 80, 35));
		addSlotToContainer(new SlotItemHandler(blacklist, 5, 98, 35));
		addSlotToContainer(new SlotItemHandler(blacklist, 6, 62, 53));
		addSlotToContainer(new SlotItemHandler(blacklist, 7, 80, 53));
		addSlotToContainer(new SlotItemHandler(blacklist, 8, 98, 53));
		
		for(int i = 0; i < 3; ++i) {
			for(int j = 0; j < 9; ++j) {
				addSlotToContainer(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}
		
		for(int k = 0; k < 9; ++k) {
			addSlotToContainer(new Slot(inventory, k, 8 + k * 18, 142));
		}
		ItemStack stack = inventory.getCurrentItem();
		if(stack != ItemStack.EMPTY && stack.getItem() instanceof ItemMagnet) {
			ItemMagnet.readBlacklistFromNBT(blacklist, inventory.getCurrentItem());
		}
	}
	
	@Override
	public void onContainerClosed(EntityPlayer playerIn) {
		ItemStack stack = playerIn.inventory.getCurrentItem();
		if(stack != ItemStack.EMPTY && stack.getItem() instanceof ItemMagnet) {
			ItemMagnet.writeBlacklistToNBT(blacklist, playerIn.inventory.getCurrentItem());
		}
		super.onContainerClosed(playerIn);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		// TODO Auto-generated method stub
		return true;
	}

}
