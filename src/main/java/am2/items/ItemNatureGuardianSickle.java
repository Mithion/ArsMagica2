package am2.items;

import am2.AMCore;
import am2.entities.EntityThrownSickle;
import am2.playerextensions.ExtendedProperties;
import am2.utility.DummyEntityPlayer;
import com.google.common.collect.Multimap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.List;

public class ItemNatureGuardianSickle extends ArsMagicaItem{

	public ItemNatureGuardianSickle(){
		super();
		setMaxStackSize(1);
	}

	@Override
	public Multimap getItemAttributeModifiers(){
		Multimap multimap = super.getItemAttributeModifiers();
		multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Weapon modifier", 7, 0));
		return multimap;
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4){
		par3List.add(StatCollector.translateToLocal("am2.tooltip.nature_scythe"));
		super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
	}

	@Override
	public boolean onBlockDestroyed(ItemStack par1ItemStack, World par2World, Block par3, int par4, int par5, int par6, EntityLivingBase par7EntityLivingBase){

		int radius = 1;

		for (int i = -radius; i <= radius; ++i){
			for (int j = -radius; j <= radius; ++j){
				for (int k = -radius; k <= radius; ++k){

					if (ExtendedProperties.For(par7EntityLivingBase).getCurrentMana() < 5f){
						if (par2World.isRemote)
							AMCore.proxy.flashManaBar();
						return false;
					}

					Block nextBlock = par2World.getBlock(par4 + i, par5 + j, par6 + k);
					if (nextBlock == null) continue;
					if (nextBlock instanceof BlockLeaves){
						if (ForgeEventFactory.doPlayerHarvestCheck(DummyEntityPlayer.fromEntityLiving(par7EntityLivingBase), nextBlock, true))
							if (!par2World.isRemote)
								par2World.func_147478_e(par4 + i, par5 + j, par6 + k, true);
						ExtendedProperties.For(par7EntityLivingBase).deductMana(5f);
					}
				}
			}
		}

		return false;
	}

	@Override
	public void registerIcons(IIconRegister par1IconRegister){
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer){
		if (flingSickle(par1ItemStack, par2World, par3EntityPlayer)){
			par3EntityPlayer.inventory.setInventorySlotContents(par3EntityPlayer.inventory.currentItem, null);
		}
		return super.onItemRightClick(par1ItemStack, par2World, par3EntityPlayer);
	}

	public boolean flingSickle(ItemStack stack, World world, EntityPlayer player){
		if (ExtendedProperties.For(player).getCurrentMana() < 250 && !player.capabilities.isCreativeMode){
			if (world.isRemote)
				AMCore.proxy.flashManaBar();
			return false;
		}
		if (!world.isRemote){
			EntityThrownSickle projectile = new EntityThrownSickle(world, player, 1.25f);
			projectile.setThrowingEntity(player);
			projectile.setProjectileSpeed(2.0);
			world.spawnEntityInWorld(projectile);
		}
		ExtendedProperties.For(player).deductMana(250f);
		return true;
	}

	@Override
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List){
		par3List.add(ItemsCommonProxy.natureScytheEnchanted.copy());
	}
}
