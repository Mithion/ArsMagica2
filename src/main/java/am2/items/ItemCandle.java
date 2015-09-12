package am2.items;

import am2.AMCore;
import am2.armor.ArmorHelper;
import am2.armor.infusions.GenericImbuement;
import am2.blocks.BlocksCommonProxy;
import am2.particles.AMParticle;
import am2.particles.ParticleHoldPosition;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class ItemCandle extends ArsMagicaItem{

	private static final int radius = 10;
	private static final int short_radius = 5;
	private static final float immediate_radius = 2.5f;

	public ItemCandle(){
		super();
		setMaxStackSize(1);
		setMaxDamage(18000); //15 minutes (20 * 60 * 15)
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ){

		if (!stack.hasTagCompound() || !stack.stackTagCompound.hasKey("search_block")){
			Block block = world.getBlock(x, y, z);
			if (player.isSneaking() && block != null && block.getBlockHardness(world, x, y, z) > 0f && world.getTileEntity(x, y, z) == null){
				if (!world.isRemote){
					setSearchBlock(world.getBlock(x, y, z), world.getBlockMetadata(x, y, z), stack);
					world.setBlockToAir(x, y, z);
				}else{
					AMParticle particle = (AMParticle)AMCore.proxy.particleManager.spawn(world, "radiant", x + 0.5, y + 0.5, z + 0.5);
					if (particle != null){
						particle.AddParticleController(new ParticleHoldPosition(particle, 20, 1, false));
						particle.setRGBColorF(0, 0.5f, 1);
					}
				}
				return true;
			}
		}

		if (!world.isRemote){

			if (stack.hasTagCompound() && stack.stackTagCompound.hasKey("search_block")){
				player.addChatMessage(new ChatComponentText("am2.tooltip.candlecantplace"));
				return false;
			}

			switch (side){
			case 0:
				y--;
				break;
			case 1:
				y++;
				break;
			case 2:
				z--;
				break;
			case 3:
				z++;
				break;
			case 4:
				x--;
				break;
			case 5:
				x++;
				break;
			}

			Block block = world.getBlock(x, y, z);
			if (block == null || block.isReplaceable(world, x, y, z)){
				int newMeta = (int)Math.ceil(stack.getItemDamage() / 1200);
				world.setBlock(x, y, z, BlocksCommonProxy.candle, newMeta, 2);
				if (!player.capabilities.isCreativeMode)
					player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
			}
			return true;
		}
		return false;
	}

	public void setSearchBlock(Block block, int meta, ItemStack item){
		if (!item.hasTagCompound())
			item.setTagCompound(new NBTTagCompound());

		setFlameColor(item, 0, 1, 0);
		item.stackTagCompound.setInteger("search_block", Block.getIdFromBlock(block));
		item.stackTagCompound.setInteger("search_meta", meta);
	}

	private void setFlameColor(ItemStack stack, float r, float g, float b){
		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());

		stack.stackTagCompound.setFloat("flame_red", r);
		stack.stackTagCompound.setFloat("flame_green", g);
		stack.stackTagCompound.setFloat("flame_blue", b);
	}

	public void search(EntityPlayer player, ItemStack stack, World world, int cx, int cy, int cz, Block block, int meta){

		boolean found = false;

		for (int i = -radius; i <= radius; ++i){
			for (int j = -1; j <= 1; ++j){
				for (int k = -radius; k <= radius; ++k){
					Block f_block = world.getBlock(cx + i, cy + j, cz + k);
					int f_meta = world.getBlockMetadata(cx + i, cy + j, cz + k);

					if (block == f_block && (meta == Short.MAX_VALUE || meta == f_meta)){
						if (Math.abs(i) <= immediate_radius && Math.abs(k) <= immediate_radius && player.getCurrentArmor(3) != null && ArmorHelper.isInfusionPreset(player.getCurrentArmor(3), GenericImbuement.pinpointOres)){
							setFlameColor(stack, 0, 0, 0);
						}else if (Math.abs(i) <= short_radius && Math.abs(k) <= short_radius){
							setFlameColor(stack, 1, 0, 0);
							return;
						}else{
							setFlameColor(stack, 0, 0.5f, 1f);
							found = true;
						}
					}
				}
			}
		}

		if (!found)
			setFlameColor(stack, 0, 1, 0);
	}

	@Override
	public boolean getShareTag(){
		return true;
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int indexInInventory, boolean isCurrentlyHeld){
		if (isCurrentlyHeld && entity instanceof EntityPlayer){
			if (!world.isRemote && stack.hasTagCompound() && stack.getItemDamage() % 40 == 0){
				search((EntityPlayer)entity, stack, world,
						(int)Math.round(entity.posX),
						(int)Math.round(entity.posY),
						(int)Math.round(entity.posZ),
						Block.getBlockById(stack.stackTagCompound.getInteger("search_block")),
						stack.stackTagCompound.getInteger("search_meta"));
			}
			stack.damageItem(1, (EntityPlayer)entity);
			if (!world.isRemote && stack.getItemDamage() >= this.getMaxDamage())
				((EntityPlayer)entity).inventory.setInventorySlotContents(indexInInventory, null);
			if (!world.isRemote && AMCore.config.candlesAreRovingLights() &&
					world.isAirBlock((int)Math.round(entity.posX), (int)Math.round(entity.posY), (int)Math.round(entity.posZ)) &&
					world.getBlockLightValue((int)Math.round(entity.posX), (int)Math.round(entity.posY), (int)Math.round(entity.posZ)) < 14){
				world.setBlock((int)Math.round(entity.posX), (int)Math.round(entity.posY), (int)Math.round(entity.posZ), BlocksCommonProxy.invisibleUtility, 2, 2);
			}
		}
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack){
		String name = StatCollector.translateToLocal("item.arsmagica2:warding_candle.name");
		if (stack.hasTagCompound() && stack.stackTagCompound.hasKey("search_block")){
			name += " (" + StatCollector.translateToLocal("am2.tooltip.attuned") + ")";
		}else{
			name += " (" + StatCollector.translateToLocal("am2.tooltip.unattuned") + ")";
		}

		return name;
	}

	@Override
	public boolean getHasSubtypes(){
		return true;
	}

	@Override
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List){
		ItemStack unattuned = new ItemStack(this, 1, 0);
		par3List.add(unattuned);
	}

	@Override
	public void registerIcons(IIconRegister par1IconRegister){
	}
}

