package am2.items;

import am2.AMCore;
import am2.MeteorSpawnHelper;
import am2.api.math.AMVector3;
import am2.api.spell.enums.SkillPointTypes;
import am2.entities.*;
import am2.particles.AMLineArc;
import am2.playerextensions.ExtendedProperties;
import am2.playerextensions.SkillData;
import am2.spell.SpellUtils;
import am2.texture.ResourceManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;

import java.util.List;

public class ItemRune extends ArsMagicaItem{

	@SideOnly(Side.CLIENT)
	private String[] textures;
	@SideOnly(Side.CLIENT)
	private IIcon[] icons;

	public static final int META_BLACK = 0;
	public static final int META_BLANK = 1;
	public static final int META_BLUE = 2;
	public static final int META_BROWN = 3;
	public static final int META_CYAN = 4;
	public static final int META_GRAY = 5;
	public static final int META_GREEN = 6;
	public static final int META_LIGHTBLUE = 7;
	public static final int META_LIGHTGRAY = 8;
	public static final int META_LIME = 9;
	public static final int META_MAGENTA = 10;
	public static final int META_ORANGE = 11;
	public static final int META_PINK = 12;
	public static final int META_PURPLE = 13;
	public static final int META_RED = 14;
	public static final int META_WHITE = 15;
	public static final int META_YELLOW = 16;
	public static final int META_INF_ORB_BLUE = 17;
	public static final int META_INF_ORB_GREEN = 18;
	public static final int META_INF_ORB_RED = 19;

	private static final int META_DEBUGRUNE = 20;


	private final int keyIndex;

	public ItemRune(int i){
		super();
		this.keyIndex = i;
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack){
		int meta = stack.getItemDamage();
		switch (meta){
		case META_BROWN:
			return StatCollector.translateToLocal("item.arsmagica2:brownRune.name");
		case META_CYAN:
			return StatCollector.translateToLocal("item.arsmagica2:cyanRune.name");
		case META_GRAY:
			return StatCollector.translateToLocal("item.arsmagica2:grayRune.name");
		case META_LIGHTBLUE:
			return StatCollector.translateToLocal("item.arsmagica2:lightBlueRune.name");
		case META_LIGHTGRAY:
			return StatCollector.translateToLocal("item.arsmagica2:lightGrayRune.name");
		case META_LIME:
			return StatCollector.translateToLocal("item.arsmagica2:limeRune.name");
		case META_MAGENTA:
			return StatCollector.translateToLocal("item.arsmagica2:magentaRune.name");
		case META_PINK:
			return StatCollector.translateToLocal("item.arsmagica2:pinkRune.name");
		case META_BLANK:
			return StatCollector.translateToLocal("item.arsmagica2:blankRune.name");
		case META_BLUE:
			return StatCollector.translateToLocal("item.arsmagica2:blueRune.name");
		case META_RED:
			return StatCollector.translateToLocal("item.arsmagica2:redRune.name");
		case META_YELLOW:
			return StatCollector.translateToLocal("item.arsmagica2:yellowRune.name");
		case META_ORANGE:
			return StatCollector.translateToLocal("item.arsmagica2:orangeRune.name");
		case META_GREEN:
			return StatCollector.translateToLocal("item.arsmagica2:greenRune.name");
		case META_PURPLE:
			return StatCollector.translateToLocal("item.arsmagica2:purpleRune.name");
		case META_BLACK:
			return StatCollector.translateToLocal("item.arsmagica2:blackRune.name");
		case META_WHITE:
			return StatCollector.translateToLocal("item.arsmagica2:whiteRune.name");
		case META_DEBUGRUNE:
			return StatCollector.translateToLocal("item.arsmagica2:debugRune.name");
		case META_INF_ORB_BLUE:
		case META_INF_ORB_GREEN:
		case META_INF_ORB_RED:
			return StatCollector.translateToLocal("item.arsmagica2:infinityOrb.name");
		}
		return StatCollector.translateToLocal("item.arsmagica2:unknown.name");
	}

	public int getKeyIndex(ItemStack stack){
		return getKeyIndex(stack.getItemDamage());
	}

	public int getKeyIndex(int meta){
		switch (meta){
		case META_BLACK:
			return 0x1;
		case META_BLANK:
			return 0x2;
		case META_BLUE:
			return 0x4;
		case META_BROWN:
			return 0x8;
		case META_CYAN:
			return 0x10;
		case META_GRAY:
			return 0x20;
		case META_GREEN:
			return 0x40;
		case META_LIGHTBLUE:
			return 0x80;
		case META_LIGHTGRAY:
			return 0x100;
		case META_LIME:
			return 0x200;
		case META_MAGENTA:
			return 0x400;
		case META_ORANGE:
			return 0x800;
		case META_PINK:
			return 0x1000;
		case META_PURPLE:
			return 0x2000;
		case META_RED:
			return 0x4000;
		case META_WHITE:
			return 0x8000;
		case META_YELLOW:
			return 0x10000;
		default:
			return 0;
		}
	}

	@Override
	public boolean hasCustomEntity(ItemStack stack){
		return true;
	}

	@Override
	public Entity createEntity(World world, Entity location, ItemStack itemstack){
		EntityItem runeEntity = new EntityItemRune(world, location.posX, location.posY, location.posZ, itemstack);
		if (runeEntity instanceof EntityItem){
			EntityItem item = (EntityItem)location;
			runeEntity.delayBeforeCanPickup = item.delayBeforeCanPickup;
			runeEntity.motionX = item.motionX;
			runeEntity.motionY = item.motionY;
			runeEntity.motionZ = item.motionZ;
		}
		return runeEntity;
	}


	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister IIconRegister){
		textures = new String[]{"rune_black", "rune_blank", "rune_blue", "rune_brown", "rune_cyan", "rune_gray", "rune_green", "rune_light_blue", "rune_light_gray", "rune_lime", "rune_magenta", "rune_orange", "rune_pink", "rune_purple", "rune_red", "rune_white", "rune_yellow", "infinityorb_blue", "infinityorb_green", "infinityorb_red", "debug_placeholder"};
		this.icons = new IIcon[this.textures.length];

		for (int i = 0; i < this.textures.length; ++i){
			if (i == META_DEBUGRUNE){
				this.icons[i] = this.icons[META_RED];
			}else{
				this.icons[i] = ResourceManager.RegisterTexture(this.textures[i], IIconRegister);
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack par1ItemStack){
		return par1ItemStack.getItemDamage() > 16;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int damage){
		if (damage < 0 || damage >= this.icons.length)
			return this.icons[0];
		return this.icons[damage];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses(){
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List){
		for (int i = 0; i < this.icons.length; ++i){
			par3List.add(new ItemStack(par1, 1, i));
		}
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player){
		int meta = stack.getItemDamage();


		if (meta == META_INF_ORB_BLUE){
			stack = doGiveSkillPoints(player, stack, SkillPointTypes.BLUE);
		}else if (meta == META_INF_ORB_GREEN){
			stack = doGiveSkillPoints(player, stack, SkillPointTypes.GREEN);
		}else if (meta == META_INF_ORB_RED){
			stack = doGiveSkillPoints(player, stack, SkillPointTypes.RED);
		}

		if (meta < META_DEBUGRUNE) return stack;

		if (meta == META_DEBUGRUNE){
			doCurrentDebugOperation(stack, world, player);
		}

		return stack;
	}

	private ItemStack doGiveSkillPoints(EntityPlayer player, ItemStack stack, SkillPointTypes type){
		if (ExtendedProperties.For(player).getMagicLevel() > 0){
			SkillData.For(player).incrementSpellPoints(type);
			if (player.worldObj.isRemote){
				switch (type){
				case BLUE:
					player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("am2.tooltip.infOrbBlue")));
					break;
				case GREEN:
					player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("am2.tooltip.infOrbGreen")));
					break;
				case RED:
					player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("am2.tooltip.infOrbRed")));
					break;
				default:
					return stack;
				}
			}
			stack.stackSize--;
			if (stack.stackSize < 1){
				player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
			}
		}else{
			if (player.worldObj.isRemote){
				int message = player.worldObj.rand.nextInt(10);
				player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("am2.tooltip.infOrbFail" + message)));
			}
		}
		return stack;
	}

	//===========================================
	// Debug Operations
	//===========================================

	public void doCurrentDebugOperation(ItemStack stack, World world, EntityPlayer player){
		displayBlockMeta(stack, world, player);
	}

	public void displayBlockMeta(ItemStack stack, World world, EntityPlayer player){
		if (world.isRemote){
			MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(world, player, true);
			if (mop != null && mop.typeOfHit == MovingObjectType.BLOCK){
				Block block = world.getBlock(mop.blockX, mop.blockY, mop.blockZ);
				int meta = world.getBlockMetadata(mop.blockX, mop.blockY, mop.blockZ);
				player.addChatMessage(
						new ChatComponentText(
								String.format("%s, Meta: %d", block.getLocalizedName(), meta)
						));
			}
		}
	}

	public void spawnRibbonParticle(ItemStack stack, World world, EntityPlayer player){
		Vec3 target = player.getLookVec();
		AMCore.proxy.particleManager.RibbonFromPointToPoint(world, player.posX, player.posY, player.posZ, player.posX + (target.xCoord * 10), player.posY + (target.yCoord * 10), player.posZ + (target.zCoord * 10));
	}

	public void spawnLineArcParticle(ItemStack stack, World world, EntityPlayer player){
		if (world.isRemote){
			Vec3 look = player.getLookVec();
			double dist = 20;
			AMLineArc arc = (AMLineArc)AMCore.proxy.particleManager.spawn(world, "wipblock2", player.posX, player.posY, player.posZ, player.posX + (look.xCoord * dist), player.posY + (look.yCoord * dist), player.posZ + (look.zCoord * dist));
			if (arc != null){
				arc.setExtendToTarget();
				arc.setRBGColorF(0, 0, 0);
			}
		}
	}

	public void flingFrostArm(ItemStack stack, World world, EntityPlayer player){
		if (!world.isRemote){
			EntityWinterGuardianArm projectile = new EntityWinterGuardianArm(world, player, 0.5f);
			projectile.setThrowingEntity(player);
			projectile.setProjectileSpeed(2.0);
			world.spawnEntityInWorld(projectile);
		}
	}

	public void flingSickle(ItemStack stack, World world, EntityPlayer player){
		if (!world.isRemote){
			EntityThrownSickle projectile = new EntityThrownSickle(world, player, 2.0f);
			projectile.setThrowingEntity(player);
			projectile.setProjectileSpeed(2.0);
			world.spawnEntityInWorld(projectile);
		}
	}

	public void flingRock(ItemStack stack, World world, EntityPlayer player){
		if (!world.isRemote){
			EntityThrownRock projectile = new EntityThrownRock(world, player, 2.0f);
			world.spawnEntityInWorld(projectile);
		}
	}

	public void flingMoonstoneRock(ItemStack stack, World world, EntityPlayer player){
		if (!world.isRemote){
			EntityThrownRock projectile = new EntityThrownRock(world, player, 2.0f);
			projectile.setMoonstoneMeteor();
			//projectile.setMoonstoneMeteorTarget(new AMVector3(392, 50, 915));
			world.spawnEntityInWorld(projectile);
		}
	}

	public void flingStar(ItemStack stack, World world, EntityPlayer player){
		if (!world.isRemote){
			EntityThrownRock projectile = new EntityThrownRock(world, player, 2.0f);
			projectile.setShootingStar(12.0f);
			//projectile.setMoonstoneMeteorTarget(new AMVector3(392, 50, 915));
			world.spawnEntityInWorld(projectile);
		}
	}

	public void spawnMoonstoneMeteor(ItemStack stack, World world, EntityPlayer player){
		if (!world.isRemote)
			MeteorSpawnHelper.instance.spawnMeteor();
	}

	public void openSkillTreeUI(ItemStack stack, World world, EntityPlayer player){
		AMCore.proxy.openSkillTreeUI(world, player);
	}

	public void spawnWhirlwind(ItemStack stack, World world, EntityPlayer player){
		if (!world.isRemote){
			EntityWhirlwind whirlwind = new EntityWhirlwind(world);
			whirlwind.setPosition(player.posX, player.posY, player.posZ);
			world.spawnEntityInWorld(whirlwind);
		}
	}

	public void spawnAirSled(ItemStack stack, World world, EntityPlayer player){
		if (!world.isRemote){
			EntityAirSled whirlwind = new EntityAirSled(world);
			whirlwind.setPosition(player.posX, player.posY, player.posZ);
			world.spawnEntityInWorld(whirlwind);
		}
	}

	public void spawnBroom(ItemStack stack, World world, EntityPlayer player){
		if (!world.isRemote){
			MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(world, player, true);
			if (mop != null && mop.typeOfHit == MovingObjectType.BLOCK){
				TileEntity te = world.getTileEntity(mop.blockX, mop.blockY, mop.blockZ);
				if (te instanceof IInventory){
					EntityBroom broom = new EntityBroom(world);
					broom.setPosition(player.posX, player.posY, player.posZ);
					broom.setChestLocation(new AMVector3(mop.blockX, mop.blockY, mop.blockZ));
					world.spawnEntityInWorld(broom);
				}
			}

		}
	}

	public void spawnShockwave(ItemStack stack, World world, EntityPlayer player){
		if (!world.isRemote){
			for (int i = 0; i < 4; ++i){
				EntityShockwave shockwave = new EntityShockwave(world);
				shockwave.setPosition(player.posX, player.posY, player.posZ);
				shockwave.setMoveSpeedAndAngle(0.5f, MathHelper.wrapAngleTo180_float(player.rotationYaw + (90 * i)));
				world.spawnEntityInWorld(shockwave);
			}
		}
	}

	public void spawnRainOfFire(ItemStack stack, World world, EntityPlayer player){
		if (!world.isRemote){
			EntitySpellEffect fire = new EntitySpellEffect(world);
			fire.setPosition(player.posX, player.posY, player.posZ);
			fire.setTicksToExist(300);
			fire.setRainOfFire(true);
			fire.setRadius(10);
			fire.SetCasterAndStack(player, stack);
			world.spawnEntityInWorld(fire);
		}
	}

	public void spawnBlizzard(ItemStack stack, World world, EntityPlayer player){
		if (!world.isRemote){
			EntitySpellEffect frost = new EntitySpellEffect(world);
			frost.setPosition(player.posX, player.posY, player.posZ);
			frost.setTicksToExist(300);
			frost.setBlizzard();
			frost.setRadius(10);
			frost.SetCasterAndStack(player, stack);
			world.spawnEntityInWorld(frost);
		}
	}

	public void spawnWall(ItemStack stack, World world, EntityPlayer player){
		if (!world.isRemote){
			EntitySpellEffect wall = new EntitySpellEffect(world);
			wall.setPosition(player.posX, player.posY, player.posZ);
			wall.setTicksToExist(300000);
			wall.setWall(player.rotationYaw);
			wall.setRadius(10);

			ItemStack spellStack = new ItemStack(ItemsCommonProxy.spell);
			SpellUtils.instance.addSpellStageToScroll(spellStack, "touch", new String[]{"FireDamage"}, new String[0]);

			wall.SetCasterAndStack(player, spellStack);
			world.spawnEntityInWorld(wall);
		}
	}
}
