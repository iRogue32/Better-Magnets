package max.bettermagnets.handlers;

import max.bettermagnets.items.ItemMagnet.ItemMagnet;
import max.bettermagnets.items.ItemMagnet.ItemMagnetContainer;
import max.bettermagnets.items.ItemMagnet.ItemMagnetGui;
import max.bettermagnets.items.ItemMagnet.ItemStackHandlerBlacklist;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class BetterMagnetGuiHandler implements IGuiHandler {

	public static final int ITEM_MAGNET = 0;
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch(ID) {
			case ITEM_MAGNET :
				return new ItemMagnetContainer(player.inventory);
		}
		return null;	
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch(ID) {
			case ITEM_MAGNET :
				return new ItemMagnetGui(player.inventory);
	}
		return null;
	}

}
