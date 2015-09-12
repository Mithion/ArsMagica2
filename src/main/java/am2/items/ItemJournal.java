package am2.items;

import am2.utility.EntityUtilities;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class ItemJournal extends ArsMagicaItem{

	private static final String KEY_NBT_XP = "Stored_XP";
	private static final String KEY_NBT_OWNER = "Owner";

	public ItemJournal(){
		super();
	}

	@Override
	public IIcon getIconFromDamage(int par1){
		return Items.book.getIconFromDamage(par1);
	}

	@Override
	public boolean hasEffect(ItemStack par1ItemStack, int pass){
		return true;
	}

	@Override
	public boolean getShareTag(){
		return true;
	}

	@Override
	public void addInformation(ItemStack journal, EntityPlayer player, List list, boolean par4){
		String owner = getOwner(journal);
		if (owner == null){
			list.add(StatCollector.translateToLocal("am2.tooltip.unowned"));
			list.add(StatCollector.translateToLocal("am2.tooltip.journalUse"));
			return;
		}else{
			list.add(String.format(StatCollector.translateToLocal("am2.tooltip.journalOwner")));
			list.add(String.format(StatCollector.translateToLocal("am2.tooltip.journalOwner2"), owner));
		}

		if (owner.equals(player.getCommandSenderName()))
			list.add(String.format(StatCollector.translateToLocal("am2.tooltip.containedXP"), getXPInJournal(journal)));

		if (owner == null || owner.equals(player.getCommandSenderName()))
			list.add(StatCollector.translateToLocal("am2.tooltip.journalUse"));
	}

	@Override
	public ItemStack onItemRightClick(ItemStack journal, World world, EntityPlayer player){

		if (!player.worldObj.isRemote){
			if (getOwner(journal) == null){
				setOwner(journal, player);
			}else if (!getOwner(journal).equals(player.getCommandSenderName())){
				player.addChatMessage(new ChatComponentText("am2.tooltip.notYourJournal"));
				return super.onItemRightClick(journal, world, player);
			}

			if (player.isSneaking()){
				try{
					int amt = Math.min(player.experienceTotal, 10);
					if (amt > 0){
						EntityUtilities.deductXP(amt, player);
						addXPToJournal(journal, amt);
					}
				}catch (Throwable t){
					t.printStackTrace();
				}
			}else{
				int amt = Math.min(getXPInJournal(journal), 10);
				if (amt > 0){
					player.addExperience(amt);
					deductXPFromJournal(journal, amt);
				}
			}
		}

		return super.onItemRightClick(journal, world, player);
	}

	private void addXPToJournal(ItemStack journal, int amount){
		if (!journal.hasTagCompound())
			journal.stackTagCompound = new NBTTagCompound();
		journal.stackTagCompound.setInteger(KEY_NBT_XP, journal.stackTagCompound.getInteger(KEY_NBT_XP) + amount);
	}

	private void deductXPFromJournal(ItemStack journal, int amount){
		addXPToJournal(journal, -amount);
	}

	private int getXPInJournal(ItemStack journal){
		if (!journal.hasTagCompound())
			return 0;
		return journal.stackTagCompound.getInteger(KEY_NBT_XP);
	}

	private String getOwner(ItemStack journal){
		if (!journal.hasTagCompound())
			return null;
		return journal.stackTagCompound.getString(KEY_NBT_OWNER);
	}

	private void setOwner(ItemStack journal, EntityPlayer player){
		if (!journal.hasTagCompound())
			journal.stackTagCompound = new NBTTagCompound();
		journal.stackTagCompound.setString(KEY_NBT_OWNER, player.getCommandSenderName());
	}

	@Override
	public void registerIcons(IIconRegister par1IconRegister){
	}

}
