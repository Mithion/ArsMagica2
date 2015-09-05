package am2.items;

import am2.enchantments.AMEnchantments;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ItemHellCowHorn extends Item{

	public ItemHellCowHorn(){
		super();
		this.setTextureName("arsmagica2:hellcowhorn");
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player){
		//testSound(stack, world, player);
		return stack;
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player,
									 Entity entity){
		if (entity instanceof EntityLivingBase){
			/*double dx = player.posX - entity.posX;
			double dz = player.posZ - entity.posZ;
			float angle = (float) Math.atan2(dz, dx);
			((EntityLivingBase)entity).addVelocity(-Math.cos(angle) * 3, 0.4f, -Math.sin(angle) * 3);
			//entity.attackEntityFrom(DamageSource.generic, 7);
*/			
			/*if (player.worldObj.rand.nextInt(10) < 3) 
				SoundHelper.instance.playSoundAtEntity(player.worldObj, player, player.worldObj.rand.nextBoolean() ? "mob.moo.neutral" : "mob.moo.death", 1.0f);*/
		}
		return false;
	}

	public ItemStack createItemStack(){
		ItemStack stack = new ItemStack(this, 1, 0);
		Map map = new LinkedHashMap();
		map.put(AMEnchantments.soulbound.effectId, 1);
		map.put(Enchantment.fireAspect.effectId, 3);
		EnchantmentHelper.setEnchantments(map, stack);
		return stack;
	}

	@Override
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List){
		par3List.add(createItemStack());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldRotateAroundWhenRendering(){
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isFull3D(){
		return true;
	}
}
