package am2.items;

import am2.AMCore;
import am2.guis.ArsMagicaGuiIdList;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class ItemEssenceBag extends ArsMagicaItem{

	public ItemEssenceBag(){
		super();
		setMaxStackSize(1);
		setMaxDurability(0);
	}

	@Override
	public boolean getShareTag(){
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack,
							   EntityPlayer par2EntityPlayer, List par3List, boolean par4){
		par3List.add(StatCollector.translateToLocal("am2.tooltip.rupees"));
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer entityplayer){
		FMLNetworkHandler.openGui(entityplayer, AMCore.instance, ArsMagicaGuiIdList.GUI_ESSENCE_BAG, world, (int)entityplayer.posX, (int)entityplayer.posY, (int)entityplayer.posZ);
		return stack;
	}

	private ItemStack[] getMyInventory(ItemStack itemStack){
		return ReadFromStackTagCompound(itemStack);
	}

	public void UpdateStackTagCompound(ItemStack itemStack, ItemStack[] values){
		if (itemStack.stackTagCompound == null){
			itemStack.stackTagCompound = new NBTTagCompound();
		}
		for (int i = 0; i < values.length; ++i){
			ItemStack stack = values[i];
			if (stack == null){
				itemStack.stackTagCompound.removeTag("essencebagstacksize" + i);
				itemStack.stackTagCompound.removeTag("essencebagmeta" + i);
				continue;
			}else{
				itemStack.stackTagCompound.setInteger("essencebagstacksize" + i, stack.stackSize);
				itemStack.stackTagCompound.setInteger("essencebagmeta" + i, stack.getMetadata());
			}
		}
	}

	public void UpdateStackTagCompound(ItemStack itemStack, InventoryEssenceBag inventory){
		if (itemStack.stackTagCompound == null){
			itemStack.stackTagCompound = new NBTTagCompound();
		}
		for (int i = 0; i < inventory.getSizeInventory(); ++i){
			ItemStack stack = inventory.getStackInSlot(i);
			if (stack == null){
				continue;
			}else{
				itemStack.stackTagCompound.setInteger("essencebagstacksize" + i, stack.stackSize);
				itemStack.stackTagCompound.setInteger("essencebagmeta" + i, stack.getMetadata());
			}
		}
	}

	public ItemStack[] ReadFromStackTagCompound(ItemStack itemStack){
		if (itemStack.stackTagCompound == null){
			return new ItemStack[InventoryEssenceBag.inventorySize];
		}
		ItemStack[] items = new ItemStack[InventoryEssenceBag.inventorySize];
		for (int i = 0; i < items.length; ++i){
			if (!itemStack.stackTagCompound.hasKey("essencebagmeta" + i) || itemStack.stackTagCompound.getInteger("essencebagmeta" + i) == -1){
				items[i] = null;
				continue;
			}
			int stacksize = itemStack.stackTagCompound.getInteger("essencebagstacksize" + i);
			int meta = 0;
			if (itemStack.stackTagCompound.hasKey("essencebagmeta" + i))
				meta = itemStack.stackTagCompound.getInteger("essencebagmeta" + i);
			items[i] = new ItemStack(ItemsCommonProxy.essence, stacksize, meta);
		}
		return items;
	}

	public InventoryEssenceBag ConvertToInventory(ItemStack essenceBagStack){
		InventoryEssenceBag ieb = new InventoryEssenceBag();
		ieb.SetInventoryContents(getMyInventory(essenceBagStack));
		return ieb;
	}
}
