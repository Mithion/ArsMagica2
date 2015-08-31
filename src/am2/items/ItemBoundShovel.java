package am2.items;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import am2.playerextensions.ExtendedProperties;
import am2.spell.SpellHelper;
import am2.spell.SpellUtils;
import am2.texture.ResourceManager;
import am2.utility.InventoryUtilities;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBoundShovel extends ItemSpade implements IBoundItem{

	public ItemBoundShovel(ToolMaterial par2ToolMaterial) {
		super(par2ToolMaterial);
		this.setMaxDamage(0);
	}

	public ItemBoundShovel setUnlocalizedAndTextureName(String name){
		this.setUnlocalizedName(name);
		setTextureName(name);
		return this;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister) {
		this.itemIcon = ResourceManager.RegisterTexture("bound_shovel", par1IconRegister);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack par1ItemStack) {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack par1ItemStack) {
		return EnumRarity.rare;
	}

	@Override
	public boolean getIsRepairable(ItemStack par1ItemStack,
			ItemStack par2ItemStack) {
		return false;
	}

	@Override
	public int getItemEnchantability() {
		return 0;
	}

	@Override
	public int getItemStackLimit() {
		return 1;
	}

	@Override
	public boolean isItemTool(ItemStack par1ItemStack) {
		return true;
	}

	@Override
	public boolean isRepairable() {
		return false;
	}

	@Override
	public boolean onDroppedByPlayer(ItemStack item, EntityPlayer player) {
		UnbindItem(item, player, player.inventory.currentItem);
		return false;
	}

	@Override
	public float maintainCost() {
		if (this.toolMaterial == ToolMaterial.STONE) return IBoundItem.diminishedMaintain;
		if (this.toolMaterial == ToolMaterial.IRON) return IBoundItem.normalMaintain;
		if (this.toolMaterial == ToolMaterial.EMERALD) return IBoundItem.augmentedMaintain;
		return 0;
	}

	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int slotIndex, boolean par5) {
		if (par3Entity instanceof EntityPlayer){
			EntityPlayer player = (EntityPlayer)par3Entity;
			if (player.capabilities.isCreativeMode) return;
			ExtendedProperties props = ExtendedProperties.For(player);
			if (props.getCurrentMana() + props.getBonusCurrentMana() < this.maintainCost()){
				UnbindItem(par1ItemStack, (EntityPlayer) par3Entity, slotIndex);
				return;
			}else{
				props.deductMana(this.maintainCost());
			}
			if (par1ItemStack.getItemDamage() > 0)
				par1ItemStack.damageItem(-1, (EntityLivingBase)par3Entity);
		}
	}

	@Override
	public void UnbindItem(ItemStack itemstack, EntityPlayer player, int inventorySlot) {
		itemstack = InventoryUtilities.replaceItem(itemstack, ItemsCommonProxy.spell);
		player.inventory.setInventorySlotContents(inventorySlot, itemstack);
	}

	@Override
	public boolean onBlockStartBreak(ItemStack itemstack, int X, int Y, int Z, EntityPlayer player) {

		MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(player.worldObj, player, false);

		if (mop != null){
			if (!itemstack.hasTagCompound())
				itemstack.setTagCompound(new NBTTagCompound());

			itemstack.stackTagCompound.setInteger("block_break_face", mop.sideHit);
		}else{
			//default to top
			itemstack.stackTagCompound.setInteger("block_break_face", 0);
		}

		return super.onBlockStartBreak(itemstack, X, Y, Z, player);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		MovingObjectPosition mop = ItemsCommonProxy.spell.getMovingObjectPosition(player, world, 4.0f, true, false);

		if (mop != null && stack.hasTagCompound()){
			ItemStack castStack = getApplicationStack(stack);
			if (mop.typeOfHit == MovingObjectType.BLOCK)
				SpellHelper.instance.applyStackStage(castStack, player, null, mop.hitVec.xCoord, mop.hitVec.yCoord, mop.hitVec.zCoord, mop.sideHit, world, true, true, 0);
			else if (mop.typeOfHit == MovingObjectType.ENTITY && mop.entityHit instanceof EntityLivingBase)
				SpellHelper.instance.applyStackStage(castStack, player, (EntityLivingBase) mop.entityHit, mop.entityHit.posX, mop.entityHit.posY, mop.entityHit.posZ, mop.sideHit, world, true, true, 0);
		}

		return super.onItemRightClick(stack, world, player);
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, Block block, int x, int y, int z, EntityLivingBase living) {

		if (!living.isSneaking() && this.getDigSpeed(stack, block, world.getBlockMetadata(x, y, z)) == this.efficiencyOnProperMaterial){
			ItemStack castStack = getApplicationStack(stack);
			SpellHelper.instance.applyStackStage(castStack, living, null, x, y, z, stack.stackTagCompound.getInteger("block_break_face"), world, true, true, 0);
		}

		return super.onBlockDestroyed(stack, world, block, x, y, z, living);
	}

	private ItemStack getApplicationStack(ItemStack boundStack){
		ItemStack castStack = SpellUtils.instance.constructSpellStack(boundStack.copy());
		castStack = SpellUtils.instance.popStackStage(castStack);
		castStack = InventoryUtilities.replaceItem(castStack, ItemsCommonProxy.spell);

		return castStack;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldRotateAroundWhenRendering() {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isFull3D() {
		return true;
	}
	
	@Override
	public Set<String> getToolClasses(ItemStack stack) {
		HashSet<String> set = new HashSet<String>();
		set.add("shovel");
		return set;
	}
}
