package am2.items;

import am2.api.power.IPowerNode;
import am2.api.power.PowerTypes;
import am2.api.spell.component.interfaces.ISpellShape;
import am2.api.spell.enums.SpellCastResult;
import am2.network.AMNetHandler;
import am2.playerextensions.SkillData;
import am2.power.PowerNodeRegistry;
import am2.spell.SkillManager;
import am2.spell.SkillTreeManager;
import am2.spell.SpellHelper;
import am2.spell.SpellUtils;
import am2.texture.ResourceManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class ItemSpellStaff extends ArsMagicaItem{

	private final int castingMode;
	private int staffHeadIndex;
	private final int maxCharge;

	private static final String NBT_CHARGE = "current_charge";
	private static final String NBT_SPELL = "spell_to_cast";
	private static final String NBT_SPELL_NAME = "spell_name";

	@SideOnly(Side.CLIENT)
	private IIcon[] icons;
	@SideOnly(Side.CLIENT)
	private String[] textureFiles;

	public ItemSpellStaff(int charge, int castingMode){
		super();
		this.setMaxDamage(charge);
		this.maxCharge = charge;
		this.maxStackSize = 1;
		this.castingMode = castingMode;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister){
		textureFiles = new String[]{"staff_lesser", "staff_standard", "staff_greater", "staff_magitech"};
		icons = new IIcon[textureFiles.length];

		for (int i = 0; i < icons.length; ++i){
			icons[i] = ResourceManager.RegisterTexture(textureFiles[i], par1IconRegister);
		}
	}

	/**
	 * Sets the displayed head of the staff.
	 *
	 * @param index The index.  Valid values: 1 (lesser), 2 (standard), 3 (greater)
	 * @return
	 */
	public ItemSpellStaff setStaffHeadIndex(int index){
		if (index > 3 || index < 0){
			index = 0;
		}
		this.staffHeadIndex = index;
		return this;
	}

	@Override
	public boolean hasEffect(ItemStack par1ItemStack, int pass){
		if (pass == 0) return false;
		else return isMagiTechStaff() || getSpellStack(par1ItemStack) != null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses(){
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamageForRenderPass(int dmg, int pass){
		return icons[staffHeadIndex];
	}

	public boolean isMagiTechStaff(){
		return this.castingMode == -1;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack stack, int pass){
		return 0xFFFFFF;
		/*}else{
			if (this.isMagiTechStaff())
				return 0x5798cb;
			switch(stack.getItemDamage()){
			case 1: //purple
				return 0xa718bc;
			case 2: //cyan
				return 0x16d9c9;
			case 3: //gray
				return 0x9b9b9b;
			case 4: //light blue
				return 0x5798cb;
			case 5: //white
				return 0xffffff;
			case 6: //black
				return 0x000000;
			case 7: //orange
				return 0xde8317;
			case 8: //brown
				return 0x744c14;
			case 9: //blue
				return 0x0b11ff;
			case 10: //green
				return 0x1bbf1b;
			case 11: //yellow
				return 0xe8dd29;
			case 12: //red
				return 0xde1717;
			case 13: //lime
				return 0x00ff0c;
			case 14: //pink
				return 0xffc0cb;
			case 15: //magenta
				return 0xFF00FF;
			case 16: //light gray
				return 0xd4d4d4;
			default:
				return 0xa718bc;
			}
		}*/
	}

	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack){
		if (isMagiTechStaff())
			return EnumAction.none;
		return EnumAction.block;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldRotateAroundWhenRendering(){
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isFull3D(){
		return true;
	}

	public void setSpellScroll(ItemStack stack, ItemStack spell){
		if (isMagiTechStaff()) return;
		if (stack.stackTagCompound == null){
			stack.stackTagCompound = new NBTTagCompound();
		}
		NBTTagCompound compound = stack.stackTagCompound;
		NBTTagCompound spellCompound = spell.writeToNBT(new NBTTagCompound());
		compound.setTag(NBT_SPELL, spellCompound);
		compound.setString(NBT_SPELL_NAME, spell.getDisplayName());
		if (!compound.hasKey(NBT_CHARGE))
			compound.setFloat(NBT_CHARGE, maxCharge);
	}

	public void copyChargeFrom(ItemStack my_stack, ItemStack stack){
		if (isMagiTechStaff()) return;
		if (stack.getItem() instanceof ItemSpellStaff && stack.stackTagCompound != null && stack.stackTagCompound.hasKey("current_charge")){
			if (my_stack.stackTagCompound == null){
				my_stack.stackTagCompound = new NBTTagCompound();
			}
			my_stack.stackTagCompound.setFloat("current_charge", stack.stackTagCompound.getFloat("current_charge"));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer par2EntityPlayer, List par3List, boolean par4){
		if (stack.stackTagCompound == null || isMagiTechStaff()) return;
		float chargeCost = 1;

		ItemStack spell = getSpellStack(stack);

		if (spell != null){
			chargeCost = SpellUtils.instance.getSpellRequirements(spell, par2EntityPlayer).manaCost;
		}
		if (chargeCost == 0)
			chargeCost = 1;

		float chargeRemaining = stack.stackTagCompound.getFloat(NBT_CHARGE);
		int chargesRemaining = (int)Math.ceil(chargeRemaining / chargeCost);

		par3List.add(StatCollector.translateToLocal("am2.tooltip.charge") + ": " + (int)chargeRemaining + " / " + maxCharge);
		par3List.add("" + chargesRemaining + " " + StatCollector.translateToLocal("am2.tooltip.uses") + ".");
	}

	@Override
	public String getItemStackDisplayName(ItemStack par1ItemStack){
		if (isMagiTechStaff()){
			return StatCollector.translateToLocal("item.arsmagica2:spell_staff_magitech.name");
		}
		String name = super.getItemStackDisplayName(par1ItemStack);
		if (par1ItemStack.hasTagCompound() && par1ItemStack.getTagCompound().hasKey(NBT_SPELL_NAME))
			name += " (\2479" + par1ItemStack.getTagCompound().getString(NBT_SPELL_NAME) + "\2477)";
		return name;
	}

	@Override
	public boolean getShareTag(){
		return true;
	}

	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int X, int Y, int Z, int side, float par8, float par9, float par10){
		if (isMagiTechStaff()){
			return true;
		}

		return false;
	}

	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ){
		if (isMagiTechStaff()){
			if (!world.isRemote){
				TileEntity te = world.getTileEntity(x, y, z);
				if (te != null && te instanceof IPowerNode){
					if (player.isSneaking()){
						AMNetHandler.INSTANCE.syncPowerPaths((IPowerNode)te, (EntityPlayerMP)player);
					}else{
						PowerTypes[] types = ((IPowerNode)te).getValidPowerTypes();
						for (PowerTypes type : types){
							float power = PowerNodeRegistry.For(world).getPower((IPowerNode)te, type);
							player.addChatMessage(
									new ChatComponentText(
											String.format(
													StatCollector.translateToLocal("am2.tooltip.det_eth"),
													type.chatColor(), type.name(), String.format("%.2f", power))));
						}
					}
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack itemstack, World world, EntityPlayer entityplayer, int i){
		if (isMagiTechStaff()) return;

		ItemStack spell = getSpellStack(itemstack);
		if (spell != null){

			ISpellShape shape = SpellUtils.instance.getShapeForStage(spell, 0);
			if (shape != null){
				if (!shape.isChanneled())
					if (SpellHelper.instance.applyStackStage(spell, entityplayer, null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, 0, world, false, false, i) == SpellCastResult.SUCCESS)
						consumeStaffCharge(itemstack, entityplayer);
				if (world.isRemote && shape.isChanneled()){
					//SoundHelper.instance.stopSound(shape.getSoundForAffinity(SpellUtils.instance.mainAffinityFor(spell), spell, null));
				}
			}
		}
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

	@Override
	public int getMaxItemUseDuration(ItemStack itemStack){
		return 2000;
	}

	private ItemStack getSpellStack(ItemStack staffStack){
		if (!staffStack.hasTagCompound() || !staffStack.stackTagCompound.hasKey(NBT_SPELL))
			return null;
		ItemStack stack = new ItemStack(ItemsCommonProxy.spell);
		stack.readFromNBT(staffStack.getTagCompound().getCompoundTag(NBT_SPELL));
		return stack;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer){
		if (isMagiTechStaff())
			return itemstack;

		if (getSpellStack(itemstack) != null)
			entityplayer.setItemInUse(itemstack, getMaxItemUseDuration(itemstack));

		return itemstack;
	}

	@Override
	public void onUsingTick(ItemStack itemstack, EntityPlayer player, int count){
		if (isMagiTechStaff()) return;
		ItemStack spell = getSpellStack(itemstack);
		if (spell != null){

			if (SpellHelper.instance.applyStackStageOnUsing(spell, player, player, player.posX, player.posY, player.posZ, player.worldObj, false, true, count - 1) == SpellCastResult.SUCCESS)
				consumeStaffCharge(itemstack, player);
		}
	}

	@Override
	public int getDamage(ItemStack stack){
		if (!stack.hasTagCompound())
			return super.getDamage(stack);
		float chargeRemaining = stack.stackTagCompound.getFloat(NBT_CHARGE);
		return maxCharge - (int)Math.floor(chargeRemaining);
	}

	private void consumeStaffCharge(ItemStack staffStack, EntityPlayer caster){
		float chargeCost = 1;

		ItemStack spell = getSpellStack(staffStack);

		if (spell != null){
			chargeCost = SpellUtils.instance.getSpellRequirements(spell, caster).manaCost;
		}
		if (chargeCost == 0)
			chargeCost = 1;

		float chargeRemaining = staffStack.stackTagCompound.getFloat(NBT_CHARGE);
		chargeRemaining -= chargeCost;
		staffStack.stackTagCompound.setFloat(NBT_CHARGE, chargeRemaining);

		if (chargeRemaining <= 0){
			if (!caster.worldObj.isRemote){
				caster.destroyCurrentEquippedItem();
			}
		}

	}
}
