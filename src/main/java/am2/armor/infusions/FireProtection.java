package am2.armor.infusions;

import am2.api.items.armor.IArmorImbuement;
import am2.api.items.armor.ImbuementApplicationTypes;
import am2.api.items.armor.ImbuementTiers;
import am2.playerextensions.ExtendedProperties;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.EnumSet;

public class FireProtection implements IArmorImbuement{

	@Override
	public String getID(){
		return "fireprot";
	}

	@Override
	public int getIconIndex(){
		return 22;
	}

	@Override
	public ImbuementTiers getTier(){
		return ImbuementTiers.FOURTH;
	}

	@Override
	public EnumSet<ImbuementApplicationTypes> getApplicationTypes(){
		return EnumSet.of(ImbuementApplicationTypes.ON_TICK);
	}

	@Override
	public boolean applyEffect(EntityPlayer player, World world, ItemStack stack, ImbuementApplicationTypes matchedType, Object... params){

		if (world.isRemote)
			return false;

		boolean effectApplied = false;

		if (ExtendedProperties.For(player).armorProcCooldowns[1] == 0){
			int damage = 75;

			int x = (int)Math.floor(player.posX);
			int y = (int)Math.floor(player.posY);
			int z = (int)Math.floor(player.posZ);

			if (player.isInsideOfMaterial(Material.lava)){
				player.motionY = 0;
				player.fallDistance = 0;
				for (int i = -1; i <= 1; ++i){
					for (int j = -2; j <= 2; ++j){
						for (int k = -1; k <= 1; ++k){
							BlockPos position = new BlockPos(x + i, y + j, z + k);
							Block block = world.getBlockState(position).getBlock();
							if (block == Blocks.flowing_lava){
								if (i == 0 && k == 0 && j != -2){
									world.setBlockToAir(position);
								}else{
									world.setBlockState(position, Blocks.cobblestone.getDefaultState());
								}
							}else if (block == Blocks.lava){
								if (i == 0 && k == 0 && j != -2){
									world.setBlockToAir(position);
								}else{
									world.setBlockState(position, Blocks.obsidian.getDefaultState());
								}
							}
						}
					}
				}
				effectApplied = true;
			}
		}
		if (player.isBurning()){
			player.extinguish();
		}
		return effectApplied;
	}

	@Override
	public int[] getValidSlots(){
		return new int[]{ImbuementRegistry.SLOT_LEGS};
	}

	@Override
	public boolean canApplyOnCooldown(){
		return true;
	}

	@Override
	public int getCooldown(){
		return 900;
	}

	@Override
	public int getArmorDamage(){
		return 40;
	}
}
