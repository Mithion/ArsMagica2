package am2.items;

import am2.blocks.BlocksCommonProxy;
import am2.texture.ResourceManager;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class ItemKeystoneDoor extends Item{

	public static final int KEYSTONE_DOOR = 0;
	public static final int SPELL_SEALED_DOOR = 1;

	public ItemKeystoneDoor(){
		super();
		this.maxStackSize = 1;
		this.setHasSubtypes(true);
	}


	@Override
	public String getItemStackDisplayName(ItemStack stack){
		switch (stack.getItemDamage()){
		case KEYSTONE_DOOR:
			return StatCollector.translateToLocal("item.arsmagica2:keystoneDoor.name");
		case SPELL_SEALED_DOOR:
			return StatCollector.translateToLocal("item.arsmagica2:spellSealedDoor.name");
		default:
			return StatCollector.translateToLocal("item.arsmagica2:unknown.name");
		}
	}

	/**
	 * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
	 * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
	 */
	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10){
		if (par7 != 1){
			return false;
		}else{
			++par5;
			Block block;
			if (par1ItemStack.getItemDamage() == KEYSTONE_DOOR)
				block = BlocksCommonProxy.keystoneDoor;
			else
				block = BlocksCommonProxy.spellSealedDoor;

			if (par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack) && par2EntityPlayer.canPlayerEdit(par4, par5 + 1, par6, par7, par1ItemStack)){
				if (!block.canPlaceBlockAt(par3World, par4, par5, par6)){
					return false;
				}else{
					int i1 = MathHelper.floor_double((par2EntityPlayer.rotationYaw + 180.0F) * 4.0F / 360.0F - 0.5D) & 3;
					placeDoorBlock(par3World, par4, par5, par6, i1, block);
					--par1ItemStack.stackSize;
					return true;
				}
			}else{
				return false;
			}
		}
	}

	public static void placeDoorBlock(World par0World, int par1, int par2, int par3, int par4, Block par5Block){
		byte b0 = 0;
		byte b1 = 0;

		if (par4 == 0){
			b1 = 1;
		}

		if (par4 == 1){
			b0 = -1;
		}

		if (par4 == 2){
			b1 = -1;
		}

		if (par4 == 3){
			b0 = 1;
		}

		int i1 = (par0World.isBlockNormalCubeDefault(par1 - b0, par2, par3 - b1, true) ? 1 : 0) + (par0World.isBlockNormalCubeDefault(par1 - b0, par2 + 1, par3 - b1, true) ? 1 : 0);
		int j1 = (par0World.isBlockNormalCubeDefault(par1 + b0, par2, par3 + b1, true) ? 1 : 0) + (par0World.isBlockNormalCubeDefault(par1 + b0, par2 + 1, par3 + b1, true) ? 1 : 0);
		boolean flag = par0World.getBlock(par1 - b0, par2, par3 - b1) == par5Block || par0World.getBlock(par1 - b0, par2 + 1, par3 - b1) == par5Block;
		boolean flag1 = par0World.getBlock(par1 + b0, par2, par3 + b1) == par5Block || par0World.getBlock(par1 + b0, par2 + 1, par3 + b1) == par5Block;
		boolean flag2 = false;

		if (flag && !flag1){
			flag2 = true;
		}else if (j1 > i1){
			flag2 = true;
		}

		par0World.setBlock(par1, par2, par3, par5Block, par4, 2);
		par0World.setBlock(par1, par2 + 1, par3, par5Block, 8 | (flag2 ? 1 : 0), 2);
		par0World.notifyBlocksOfNeighborChange(par1, par2, par3, par5Block);
		par0World.notifyBlocksOfNeighborChange(par1, par2 + 1, par3, par5Block);
	}

	@Override
	public void registerIcons(IIconRegister par1IconRegister){
		this.itemIcon = ResourceManager.RegisterTexture("keystoneDoor", par1IconRegister);
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list){
		list.add(new ItemStack(this, 1, KEYSTONE_DOOR));
		list.add(new ItemStack(this, 1, SPELL_SEALED_DOOR));
	}
}
