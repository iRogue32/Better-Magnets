package max.bettermagnets.items.ItemMagnet;

import net.minecraftforge.items.ItemStackHandler;

public class ItemStackHandlerBlacklist extends ItemStackHandler {

	public ItemStackHandlerBlacklist(int size) {
		super(size);
	}
	
	@Override
	public int getSlotLimit(int slot) {
		return 1;
	}
	
}
