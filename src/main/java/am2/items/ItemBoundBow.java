package am2.items;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import am2.playerextensions.ExtendedProperties;
import am2.texture.ResourceManager;
import am2.utility.InventoryUtilities;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBoundBow extends ItemBow implements IBoundItem{

	public ItemBoundBow() {
		this.setMaxDamage(0);
	}

	public ItemBoundBow setUnlocalizedAndTextureName(String name){
		this.setUnlocalizedName(name);
		setTextureName(name);
		return this;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister) {
		this.itemIcon = ResourceManager.RegisterTexture("bound_bow", par1IconRegister);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack par1ItemStack) {
		return EnumRarity.rare;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack par1ItemStack) {
		return true;
	}

	@Override
	public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
		return false;
	}

	@Override
	public int getItemEnchantability() {
		return 0;
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
		return IBoundItem.normalMaintain;
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
}
