package am2.items;

import am2.AMCore;
import am2.buffs.BuffEffectFrostSlowed;
import am2.entities.EntityWinterGuardianArm;
import am2.particles.AMParticle;
import am2.playerextensions.ExtendedProperties;
import com.google.common.collect.Multimap;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class ItemWinterGuardianArm extends ArsMagicaItem{

	public ItemWinterGuardianArm(){
		super();
		setMaxStackSize(1);
	}

	@Override
	public Multimap getItemAttributeModifiers(){
		Multimap multimap = super.getItemAttributeModifiers();
		multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Weapon modifier", 6, 0));
		return multimap;
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4){
		par3List.add(StatCollector.translateToLocal("am2.tooltip.winter_arm"));
		super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity){
		if (entity instanceof EntityLivingBase){
			((EntityLivingBase)entity).addPotionEffect(new BuffEffectFrostSlowed(60, 3));
			if (player.worldObj.isRemote){
				for (int i = 0; i < 5; ++i){
					AMParticle particle = (AMParticle)AMCore.proxy.particleManager.spawn(player.worldObj, "snowflakes", entity.posX + 0.5, entity.posY + 0.5, entity.posZ + 0.5);
					if (particle != null){
						particle.addRandomOffset(1, 0.5, 1);
						particle.addVelocity(player.worldObj.rand.nextDouble() * 0.2 - 0.1, 0.3, player.worldObj.rand.nextDouble() * 0.2 - 0.1);
						particle.setAffectedByGravity();
						particle.setDontRequireControllers();
						particle.setMaxAge(10);
						particle.setParticleScale(0.1f);
					}
				}
			}
		}
		return super.onLeftClickEntity(stack, player, entity);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer){
		if (flingArm(par1ItemStack, par2World, par3EntityPlayer)){
			par3EntityPlayer.inventory.setInventorySlotContents(par3EntityPlayer.inventory.currentItem, null);
		}
		return super.onItemRightClick(par1ItemStack, par2World, par3EntityPlayer);
	}

	public boolean flingArm(ItemStack stack, World world, EntityPlayer player){
		if (ExtendedProperties.For(player).getCurrentMana() < 250 && !player.capabilities.isCreativeMode){
			if (world.isRemote)
				AMCore.proxy.flashManaBar();
			return false;
		}
		if (!world.isRemote){
			EntityWinterGuardianArm projectile = new EntityWinterGuardianArm(world, player, 1.25f);
			projectile.setThrowingEntity(player);
			projectile.setProjectileSpeed(2.0);
			world.spawnEntityInWorld(projectile);
		}
		ExtendedProperties.For(player).deductMana(250f);
		return true;
	}

	@Override
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List){
		par3List.add(ItemsCommonProxy.winterArmEnchanted.copy());
	}

	@Override
	public void registerIcons(IIconRegister par1IconRegister){
	}
}
