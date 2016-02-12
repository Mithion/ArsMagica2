package am2.items;

import am2.AMCore;
import am2.guis.ArsMagicaGuiIdList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;

public class ItemRuneBag extends Item{

	public ItemRuneBag(){
		super();
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer entityplayer){
		if (entityplayer.isSneaking()){
			FMLNetworkHandler.openGui(entityplayer, AMCore.instance, ArsMagicaGuiIdList.GUI_RUNE_BAG, world, (int)entityplayer.posX, (int)entityplayer.posY, (int)entityplayer.posZ);
		}
		return stack;
	}

	private ItemStack[] getMyInventory(ItemStack itemStack){
		return ReadFromStackTagCompound(itemStack);
	}

	public void UpdateStackTagCompound(ItemStack itemStack, ItemStack[] values){
		/*if (itemStack.getTagCompound() == null){
            itemStack.stackTagCompound = new NBTTagCompound();
        }*/
        for (int i = 0; i < values.length; ++i){
			ItemStack stack = values[i];
			if (stack == null){
				itemStack.getTagCompound().removeTag("runebagmeta" + i);
				continue;
			}else{
				itemStack.getTagCompound().setInteger("runebagmeta" + i, stack.getItemDamage());
			}
		}
	}

	@Override
	public boolean getShareTag(){
		return true;
	}

	public void UpdateStackTagCompound(ItemStack itemStack, InventoryRuneBag inventory){
		for (int i = 0; i < inventory.getSizeInventory(); ++i){
			ItemStack stack = inventory.getStackInSlot(i);
			if (stack == null){
				continue;
			}else{
				itemStack.getTagCompound().setInteger("runebagmeta" + i, stack.getItemDamage());
			}
		}
	}

	public ItemStack[] ReadFromStackTagCompound(ItemStack itemStack){
		if (itemStack.getTagCompound() == null){
			return new ItemStack[InventoryRuneBag.inventorySize];
		}
		ItemStack[] items = new ItemStack[InventoryRuneBag.inventorySize];
		for (int i = 0; i < items.length; ++i){
			if (!itemStack.getTagCompound().hasKey("runebagmeta" + i) || itemStack.getTagCompound().getInteger("runebagmeta" + i) == -1){
				items[i] = null;
				continue;
			}
			int meta = 0;
			meta = itemStack.getTagCompound().getInteger("runebagmeta" + i);
			items[i] = new ItemStack(ItemsCommonProxy.rune, 1, meta);
		}
		return items;
	}

	public InventoryRuneBag ConvertToInventory(ItemStack runeBagStack){
		InventoryRuneBag irb = new InventoryRuneBag();
		irb.SetInventoryContents(getMyInventory(runeBagStack));
		return irb;
	}

}
