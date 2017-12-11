package max.bettermagnets.items;

import java.util.ArrayList;
import java.util.List;

import max.bettermagnets.items.ItemMagnet.ItemMagnet;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class ModItemRegistry {

	public static List<Item> items = new ArrayList<Item>();
	
	public static Item itemMagnet;
	
	public static void initItems() {
		itemMagnet = new ItemMagnet("item_magnet");
	}
	
	@SubscribeEvent
	public void onItemRegistry(RegistryEvent.Register<Item> e) {
		IForgeRegistry<Item> reg = e.getRegistry();
		reg.registerAll(items.toArray(new Item[0]));
	}
	
	@SubscribeEvent
	public void onModelRegister(ModelRegistryEvent e) {
		for (Item item : items) {
			ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
		}
	}
	
}
