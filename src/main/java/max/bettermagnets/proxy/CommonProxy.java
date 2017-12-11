package max.bettermagnets.proxy;

import max.bettermagnets.items.ModItemRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
		ModItemRegistry.initItems();
		MinecraftForge.EVENT_BUS.register(new ModItemRegistry());
	}
	
	public void Init(FMLInitializationEvent event) {
				
	}
	
	public void postInit(FMLPostInitializationEvent event) {
		
	}
	
}
