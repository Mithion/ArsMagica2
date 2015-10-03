package am2.items;

import am2.AMCore;
import am2.api.spell.ItemSpellBase;
import am2.containers.InventorySpellBook;
import am2.enchantments.AMEnchantmentHelper;
import am2.enchantments.AMEnchantments;
import am2.guis.ArsMagicaGuiIdList;
import am2.playerextensions.SkillData;
import am2.spell.SkillManager;
import am2.spell.SkillTreeManager;
import am2.texture.ResourceManager;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.List;
import java.util.Map;


public class ItemSpellBook extends ArsMagicaItem{

	public static final byte ID_NEXT_SPELL = 0;
	public static final byte ID_PREV_SPELL = 1;

	@SideOnly(Side.CLIENT)
	private IIcon[] npc_icons;
	private final String[] npc_textureFiles = {"affinity_tome_general", "affinity_tome_ice", "affinity_tome_life", "affinity_tome_fire", "affinity_tome_lightning", "affinity_tome_ender"};

	@SideOnly(Side.CLIENT)
	private IIcon[] player_icons;
	private final String[] player_textureFiles = {"spell_book_cover", "spell_book_decoration"};

	public ItemSpellBook(){
		super();
		this.setMaxDurability(0);
		this.setMaxStackSize(1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses(){
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister){
		npc_icons = new IIcon[npc_textureFiles.length];

		for (int i = 0; i < npc_textureFiles.length; ++i){
			npc_icons[i] = ResourceManager.RegisterTexture(npc_textureFiles[i], par1IconRegister);
		}

		player_icons = new IIcon[player_textureFiles.length];

		for (int i = 0; i < player_textureFiles.length; ++i){
			player_icons[i] = ResourceManager.RegisterTexture(player_textureFiles[i], par1IconRegister);
		}
	}

	/**
	 * Required for light/dark magi
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int par1){
		switch (par1){
		case 6: //orange
			return npc_icons[3];
		case 11: //red
			return npc_icons[4];
		case 5: //black
			return npc_icons[5];
		case 1: //light blue
			return npc_icons[0];
		case 8: //blue
			return npc_icons[1];
		case 4: //white
			return npc_icons[2];
		}
		return npc_icons[0];
	}

	@Override
	public String getItemStackDisplayName(ItemStack par1ItemStack){
		ItemStack activeSpell = GetActiveItemStack(par1ItemStack);
		if (activeSpell != null){
			return String.format("\2477%s (" + activeSpell.getDisplayName() + "\2477)", StatCollector.translateToLocal("item.arsmagica2:spellBook.name"));
		}
		return StatCollector.translateToLocal("item.arsmagica2:spellBook.name");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamageForRenderPass(int damage, int renderPass){
		if (renderPass == 0){
			return player_icons[0];
		}else{
			return player_icons[1];
		}
	}

	@Override
	public int getRenderPasses(int metadata){
		return 2;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack par1ItemStack, int renderPass){
		int meta = par1ItemStack.getMetadata();
		if (renderPass == 0){
			switch (meta){
			case 0: //brown
				return 0x744c14;
			case 1: //cyan
				return 0x16d9c9;
			case 2: //gray
				return 0x9b9b9b;
			case 3: //light blue
				return 0x5798cb;
			case 4: //white
				return 0xffffff;
			case 5: //black
				return 0x000000;
			case 6: //orange
				return 0xde8317;
			case 7: //purple
				return 0xa718bc;
			case 8: //blue
				return 0x0b11ff;
			case 9: //green
				return 0x1bbf1b;
			case 10: //yellow
				return 0xe8dd29;
			case 11: //red
				return 0xde1717;
			case 12: //lime
				return 0x00ff0c;
			case 13: //pink
				return 0xffc0cb;
			case 14: //magenta
				return 0xFF00FF;
			case 15: //light gray
				return 0xd4d4d4;
			default:
				return 0x744c14;
			}
		}else{
			return 0xFFFFFF;
		}
	}

	@Override
	public EnumAction getItemUseAction(ItemStack itemstack){
		if (getMaxItemUseDuration(itemstack) == 0){
			return EnumAction.none;
		}
		return EnumAction.block;
	}

	@Override
	public final int getMaxItemUseDuration(ItemStack itemstack){
		ItemSpellBase scroll = GetActiveScroll(itemstack);
		if (scroll != null){
			return scroll.getMaxItemUseDuration(itemstack);
		}
		return 0;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack bookstack, World world, EntityPlayer entityplayer){
		if (entityplayer.isSneaking()){
			FMLNetworkHandler.openGui(entityplayer, AMCore.instance, ArsMagicaGuiIdList.GUI_SPELL_BOOK, world, (int)entityplayer.posX, (int)entityplayer.posY, (int)entityplayer.posZ);
			return bookstack;
		}

		entityplayer.setItemInUse(bookstack, getMaxItemUseDuration(bookstack));

		return bookstack;
	}

	private ItemStack[] getMyInventory(ItemStack itemStack){
		return ReadFromStackTagCompound(itemStack);
	}

	public ItemStack[] getActiveScrollInventory(ItemStack bookStack){
		ItemStack[] inventoryItems = getMyInventory(bookStack);
		ItemStack[] returnArray = new ItemStack[8];
		for (int i = 0; i < 8; ++i){
			returnArray[i] = inventoryItems[i];
		}
		return returnArray;
	}

	public ItemSpellBase GetActiveScroll(ItemStack bookStack){
		ItemStack[] inventoryItems = getMyInventory(bookStack);
		if (inventoryItems[GetActiveSlot(bookStack)] == null){
			return null;
		}
		return (ItemSpellBase)inventoryItems[GetActiveSlot(bookStack)].getItem();
	}

	public ItemStack GetActiveItemStack(ItemStack bookStack){
		ItemStack[] inventoryItems = getMyInventory(bookStack);
		if (inventoryItems[GetActiveSlot(bookStack)] == null){
			return null;
		}
		return inventoryItems[GetActiveSlot(bookStack)].copy();
	}

	public void replaceAciveItemStack(ItemStack bookStack, ItemStack newstack){
		ItemStack[] inventoryItems = getMyInventory(bookStack);
		int index = GetActiveSlot(bookStack);
		inventoryItems[index] = newstack;
		UpdateStackTagCompound(bookStack, inventoryItems);
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack itemstack, World world, EntityPlayer entityplayer, int i){
		if (entityplayer.isSneaking()){
			FMLNetworkHandler.openGui(entityplayer, AMCore.instance, ArsMagicaGuiIdList.GUI_SPELL_BOOK, world, (int)entityplayer.posX, (int)entityplayer.posY, (int)entityplayer.posZ);
		}else{
			ItemStack currentSpellStack = GetActiveItemStack(itemstack);
			if (currentSpellStack != null){
				ItemsCommonProxy.spell.onPlayerStoppedUsing(currentSpellStack, world, entityplayer, i);
			}
		}
	}

	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ){
		return false;
	}

	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int X, int Y, int Z, int side, float par8, float par9, float par10){
		return false;
	}

	public void UpdateStackTagCompound(ItemStack itemStack, ItemStack[] values){
		if (itemStack.stackTagCompound == null){
			itemStack.setTagCompound(new NBTTagCompound());
		}

		NBTTagList list = new NBTTagList();
		for (int i = 0; i < values.length; ++i){
			ItemStack stack = values[i];
			NBTTagCompound spell = new NBTTagCompound();
			if (stack != null){
				spell.setInteger("meta", stack.getMetadata());
				spell.setInteger("index", i);
				if (stack.stackTagCompound != null){
					spell.setTag("data", stack.stackTagCompound);
				}
				list.appendTag(spell);
			}
		}

		itemStack.stackTagCompound.setTag("spell_book_inventory", list);

		ItemStack active = GetActiveItemStack(itemStack);
		boolean Soulbound = EnchantmentHelper.getEnchantmentLevel(AMEnchantments.soulbound.effectId, itemStack) > 0;
		if (active != null)
			AMEnchantmentHelper.copyEnchantments(active, itemStack);
		if (Soulbound)
			AMEnchantmentHelper.soulbindStack(itemStack);
	}

	public void SetActiveSlot(ItemStack itemStack, int slot){
		if (itemStack.stackTagCompound == null){
			itemStack.setTagCompound(new NBTTagCompound());
		}
		if (slot < 0) slot = 0;
		if (slot > 7) slot = 7;
		itemStack.stackTagCompound.setInteger("spellbookactiveslot", slot);

		ItemStack active = GetActiveItemStack(itemStack);
		boolean Soulbound = EnchantmentHelper.getEnchantmentLevel(AMEnchantments.soulbound.effectId, itemStack) > 0;
		if (active != null)
			AMEnchantmentHelper.copyEnchantments(active, itemStack);
		if (Soulbound)
			AMEnchantmentHelper.soulbindStack(itemStack);
	}

	public int SetNextSlot(ItemStack itemStack){
		int slot = GetActiveSlot(itemStack);
		int newSlot = slot;

		do{
			newSlot++;
			if (newSlot > 7) newSlot = 0;
			SetActiveSlot(itemStack, newSlot);
		}while (GetActiveScroll(itemStack) == null && newSlot != slot);
		return slot;
	}

	public int SetPrevSlot(ItemStack itemStack){
		int slot = GetActiveSlot(itemStack);
		int newSlot = slot;

		do{
			newSlot--;
			if (newSlot < 0) newSlot = 7;
			SetActiveSlot(itemStack, newSlot);
		}while (GetActiveScroll(itemStack) == null && newSlot != slot);
		return slot;
	}

	public int GetActiveSlot(ItemStack itemStack){
		if (itemStack.stackTagCompound == null){
			SetActiveSlot(itemStack, 0);
			return 0;
		}
		return itemStack.stackTagCompound.getInteger("spellbookactiveslot");
	}

	public ItemStack[] ReadFromStackTagCompound(ItemStack itemStack){
		if (itemStack.stackTagCompound == null){
			return new ItemStack[InventorySpellBook.inventorySize];
		}
		ItemStack[] items = new ItemStack[InventorySpellBook.inventorySize];
		/*for (int i = 0; i < items.length; ++i){
			if (!itemStack.stackTagCompound.hasKey("spellbookitem" + i) || itemStack.stackTagCompound.getInteger("spellbookitem" + i) == -1){
				items[i] = null;
				continue;
			}
			int id = itemStack.stackTagCompound.getInteger("spellbookitem" + i);
			int meta = 0;
			NBTTagCompound compound = null;

			if (itemStack.stackTagCompound.hasKey("spellbookmeta" + i))
				meta = itemStack.stackTagCompound.getInteger("spellbookmeta" + i);
			if (itemStack.stackTagCompound.hasKey("spellbooktag" + i))
				compound = itemStack.stackTagCompound.getCompoundTag("spellbooktag" + i);
			items[i] = new ItemStack(Item.itemsList[id], 1, meta);
			if (compound != null){
				items[i].stackTagCompound = compound;
			}
		}*/

		NBTTagList list = itemStack.stackTagCompound.getTagList("spell_book_inventory", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < list.tagCount(); ++i){
			NBTTagCompound spell = (NBTTagCompound)list.getCompoundTagAt(i);
			int meta = spell.getInteger("meta");
			NBTTagCompound tag = spell.getCompoundTag("data");
			int index = spell.getInteger("index");
			items[index] = new ItemStack(ItemsCommonProxy.spell, 1, meta);
			items[index].setTagCompound(tag);

		}
		return items;
	}

	public InventorySpellBook ConvertToInventory(ItemStack bookStack){
		InventorySpellBook isb = new InventorySpellBook();
		isb.SetInventoryContents(getMyInventory(bookStack));
		return isb;
	}

	@Override
	public boolean getShareTag(){
		return true;
	}

	public String GetActiveSpellName(ItemStack bookStack){
		ItemStack stack = GetActiveItemStack(bookStack);
		if (stack == null){
			return StatCollector.translateToLocal("am2.tooltip.none");
		}
		return stack.getDisplayName();
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4){
		ItemSpellBase activeScroll = GetActiveScroll(par1ItemStack);
		ItemStack stack = GetActiveItemStack(par1ItemStack);

		String s = StatCollector.translateToLocal("am2.tooltip.open");
		par3List.add((new StringBuilder()).append("\2477").append(s).toString());

		if (activeScroll != null){
			activeScroll.addInformation(stack, par2EntityPlayer, par3List, par4);
		}

		par3List.add("\247c" + StatCollector.translateToLocal("am2.tooltip.spellbookWarning1") + "\247f");
		par3List.add("\247c" + StatCollector.translateToLocal("am2.tooltip.spellbookWarning2") + "\247f");
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityPlayer player, int count){
		ItemStack scrollStack = GetActiveItemStack(stack);
		if (scrollStack != null){
			ItemsCommonProxy.spell.onUsingTick(scrollStack, player, count);
		}
	}

	@Override
	public boolean isBookEnchantable(ItemStack bookStack, ItemStack enchantBook){
		Map enchantMap = EnchantmentHelper.getEnchantments(enchantBook);
		for (Object o : enchantMap.keySet()){
			if (o instanceof Integer){
				if ((Integer)o == AMCore.proxy.enchantments.soulbound.effectId){
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public int getItemEnchantability(){
		return 1;
	}

	@Override
	public boolean isItemTool(ItemStack par1ItemStack){
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5){
		super.onUpdate(stack, world, entity, par4, par5);
		if (entity instanceof EntityPlayerSP){
			EntityPlayerSP player = (EntityPlayerSP)entity;
			ItemStack usingItem = player.getItemInUse();
			if (usingItem != null && usingItem.getItem() == this){
				if (SkillData.For(player).isEntryKnown(SkillTreeManager.instance.getSkillTreeEntry(SkillManager.instance.getSkill("SpellMotion")))){
					player.movementInput.moveForward *= 2.5F;
					player.movementInput.moveStrafe *= 2.5F;
				}
			}
		}
	}
}









