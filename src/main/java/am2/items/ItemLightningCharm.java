package am2.items;

import am2.api.math.AMVector3;
import am2.utility.MathUtilities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class ItemLightningCharm extends ArsMagicaItem{

	private static final String KEY_ACTIVE = "IsActive";

	public ItemLightningCharm(){
		super();
	}

	private boolean isActive(ItemStack stack){
		if (!stack.hasTagCompound())
			return false;

		return stack.stackTagCompound.getByte(KEY_ACTIVE) == (byte)1;
	}

	private void toggleActive(ItemStack stack){
		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		if (isActive(stack))
			stack.stackTagCompound.setByte(KEY_ACTIVE, (byte)0);
		else
			stack.stackTagCompound.setByte(KEY_ACTIVE, (byte)1);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer){
		if (par3EntityPlayer.isSneaking())
			toggleActive(par1ItemStack);
		return par1ItemStack;
	}

	private void attractItems(World world, Entity ent){
		double distance = 16;
		int hDist = 5;
		List<Entity> entities = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(ent.posX - distance, ent.posY - hDist, ent.posZ - distance, ent.posX + distance, ent.posY + hDist, ent.posZ + distance));
		for (Entity e : entities){
			EntityItem item = (EntityItem)e;
			if (item.age < 10){
				continue;
			}
			AMVector3 movement = MathUtilities.GetMovementVectorBetweenPoints(new AMVector3(e), new AMVector3(ent.posX, ent.posY, ent.posZ));

			if (!world.isRemote){
				float factor = 0.35f;
				if (movement.y > 0) movement.y = 0;
				double x = -(movement.x * factor);
				double y = -(movement.y * factor);
				double z = -(movement.z * factor);
				e.addVelocity(x, y, z);
				item.delayBeforeCanPickup = 0;
				if (Math.abs(e.motionX) > Math.abs(x * 2)){
					e.motionX = x * (e.motionX / e.motionX);
				}
				if (Math.abs(e.motionY) > Math.abs(y * 2)){
					e.motionY = y * (e.motionY / e.motionY);
				}
				if (Math.abs(e.motionZ) > Math.abs(z * 2)){
					e.motionZ = z * (e.motionZ / e.motionZ);
				}
			}
		}
	}

	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5){
		if (isActive(par1ItemStack))
			attractItems(par2World, par3Entity);
	}

	@Override
	public boolean hasEffect(ItemStack par1ItemStack, int pass){
		return isActive(par1ItemStack);
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4){
		par3List.add(StatCollector.translateToLocal("am2.tooltip.lightning_charm"));
		super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
	}
}
