package max.bettermagnets.items.ItemMagnet;

import java.util.List;

import max.bettermagnets.BetterMagnets;
import max.bettermagnets.handlers.BetterMagnetGuiHandler;
import max.bettermagnets.items.ModItemRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEndRod;
import net.minecraft.client.particle.ParticleFirework;
import net.minecraft.client.particle.ParticleSpell;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class ItemMagnet extends Item {

	private static final int BLACKLIST_SIZE = 9;
	
	private boolean isActive = false;
	private static int range;
	private static double velocity = 0.05;
	
	public ItemMagnet(String name) {
		super();
		
		setUnlocalizedName(name);
		setRegistryName(name);
		
		setMaxStackSize(1);
		range = 15;
		
		ModItemRegistry.items.add(this);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if(isActive) {
			tooltip.add("Active");
		}
		else {
			tooltip.add("Inactive");
		}
	}
	
	@Override
	public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
		if (!stack.hasTagCompound()){
			stack.setTagCompound(new NBTTagCompound()); 
	    }
		stack.getTagCompound().setBoolean("state", isActive);
	}
	
	public static void writeBlacklistToNBT(ItemStackHandlerBlacklist blacklist, ItemStack stack) {
		NBTTagCompound compound = stack.getTagCompound();
		if(compound != null) {
			NBTTagList list = new NBTTagList();
			for(int i = 0; i < blacklist.getSlots(); ++i) {
				ItemStack stackInSlot = blacklist.getStackInSlot(i);
				NBTTagCompound compound1 = new NBTTagCompound();
				if(stackInSlot != ItemStack.EMPTY) {
					stackInSlot.writeToNBT(compound1);
				}
				list.appendTag(compound1);
			}
			compound.setTag("Blacklist", list);
		}
	}
	
	public static void readBlacklistFromNBT(ItemStackHandlerBlacklist blacklist, ItemStack stack) {
		NBTTagCompound compound = stack.getTagCompound();
		if(compound != null) {
			NBTTagList list = compound.getTagList("Blacklist", 10);
			for(int i = 0; i < blacklist.getSlots(); ++i) {
				NBTTagCompound compound1 = list.getCompoundTagAt(i);
				blacklist.setStackInSlot(i, compound1 != null && compound1.hasKey("id") ? new ItemStack(compound1) : ItemStack.EMPTY);
			}
		}
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		if(!world.isRemote) {
			if(player.isSneaking()) {
				if(isActive) {
					isActive = false;	
				}
				else if(!isActive) {
					isActive = true;
				}
			}
			else {
				player.openGui(BetterMagnets.instance, BetterMagnetGuiHandler.ITEM_MAGNET, world, player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ());
			}
		}
		player.getHeldItem(hand).getTagCompound().setBoolean("state", isActive);
		return super.onItemRightClick(world, player, hand);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (!stack.hasTagCompound()){
			stack.setTagCompound(new NBTTagCompound()); 
	    }
		if(stack.getTagCompound().getBoolean("state")) {
			isActive = true;
		}
		else {
			isActive = false;
		}
		if(isActive) {
			if(entityIn instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) entityIn;
				double X = player.getPosition().getX();
				double Y = player.getPosition().getY();
				double Z = player.getPosition().getZ();
				List<EntityItem> nearbyItems = player.world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(player.getPosition().getX() - range, player.getPosition().getY() - range, player.getPosition().getZ() - range, player.getPosition().getX() + range, player.getPosition().getY() + range, player.getPosition().getZ() + range));
				for(EntityItem i : nearbyItems) {
					if(!blacklisted(i, stack)) {
						i.addVelocity((X - i.posX) * velocity, (Y - i.posY) * velocity, (Z - i.posZ) * velocity);
						if(worldIn.isRemote) {
							ParticleEndRod particle = new ParticleEndRod(worldIn, i.posX, i.posY, i.posZ, 0, 0, 0);
							particle.setRBGColorF(244, 66, 66);
							Minecraft.getMinecraft().effectRenderer.addEffect(particle);
						}
					}
				}
			}
		}
	}
	
	//blacklist helper function
	private boolean blacklisted(EntityItem item, ItemStack magnet) {
		ItemStack stack;
		NBTTagCompound compound = magnet.getTagCompound();
		if(compound != null) {
			NBTTagList list = compound.getTagList("Blacklist", 10);
			for(int i = 0; i < 9; ++i) {
				NBTTagCompound compound1 = list.getCompoundTagAt(i);
				if(compound1 != null && compound1.hasKey("id")) {
					stack = new ItemStack(compound1);
				}
				else {
					stack = ItemStack.EMPTY;
				}
				if(item.getItem().getItem() == stack.getItem() && item.getItem().getItemDamage() == stack.getItemDamage()) {
					return true;
				}
			}
		}
		return false;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public boolean hasEffect(ItemStack stack) {
		if(isActive) {
			return true;
		}
		return false;
	}
	
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		if(stack != ItemStack.EMPTY) {
			return new BlacklistProvider(9);
		}
		return super.initCapabilities(stack, nbt);
	}
	
}
