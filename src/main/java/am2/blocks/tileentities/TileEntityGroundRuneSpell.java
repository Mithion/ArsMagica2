package am2.blocks.tileentities;

import am2.entities.EntityDummyCaster;
import am2.playerextensions.ExtendedProperties;
import am2.spell.SpellHelper;
import am2.utility.DummyEntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityGroundRuneSpell extends TileEntity{
	private ItemStack spellStack;
	private EntityPlayer caster;
	private String placedByName;

	private int numTriggers = 1;
	private boolean isPermanent = false;

	public TileEntityGroundRuneSpell(){

	}

	public void setSpellStack(ItemStack spellStack){
		this.spellStack = spellStack.copy();
	}

	public void setNumTriggers(int triggers){
		this.numTriggers = triggers;
	}

	public int getNumTriggers(){
		return this.numTriggers;
	}

	public void setPermanent(boolean permanent){
		this.isPermanent = permanent;
	}

	public boolean getPermanent(){
		return this.isPermanent;
	}

	private void prepForActivate(){
		if (placedByName != null)
			caster = worldObj.getPlayerEntityByName(placedByName);
		if (caster == null){
			caster = DummyEntityPlayer.fromEntityLiving(new EntityDummyCaster(worldObj));
			ExtendedProperties.For(caster).setMagicLevelWithMana(ExtendedProperties.maxMagicLevel);
		}
	}

	public boolean applySpellEffect(EntityLivingBase target){
		if (spellStack == null) return false;
		prepForActivate();
		SpellHelper.instance.applyStackStage(spellStack, caster, target, target.posX, target.posY, target.posZ, 0, worldObj, false, false, 0);
		return true;
	}

	public void setPlacedBy(EntityLivingBase caster){
		if (caster instanceof EntityPlayer) this.placedByName = ((EntityPlayer)caster).getCommandSenderName();
	}

	@Override
	public void writeToNBT(NBTTagCompound compound){
		compound.setString("placedByName", placedByName);
		NBTTagCompound stackCompound = new NBTTagCompound();
		spellStack.writeToNBT(stackCompound);
		compound.setTag("spellStack", stackCompound);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound){
		placedByName = compound.getString("placedByName");
		spellStack = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("spellStack"));
	}
}
