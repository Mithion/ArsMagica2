package am2.items;

import am2.bosses.*;
import am2.entities.*;
import am2.guis.AMGuiIcons;
import am2.particles.AMParticleIcons;
import am2.spell.SpellTextureHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class AMSpawnEgg extends ArsMagicaItem{

	private final int numClasses = 23;
	private final ArrayList<ColorPair> colorPairs;

	public AMSpawnEgg(){
		super();
		colorPairs = new ArrayList<AMSpawnEgg.ColorPair>();

		colorPairs.add(new ColorPair(0x1abfb5, 0x368580)); //mana creeper
		colorPairs.add(new ColorPair(0x166822, 0x683d16)); //dryad
		colorPairs.add(new ColorPair(0x222222, 0x444444)); //hecate
		colorPairs.add(new ColorPair(0xe7ca83, 0x6b2096)); //mage villager
		colorPairs.add(new ColorPair(0xFFFFFF, 0x6b2096)); //Mana Elemental
		colorPairs.add(new ColorPair(0x6080c9, 0x282096)); //Water Elemental
		colorPairs.add(new ColorPair(0xDDDDDD, 0x7b1a7c)); //Light Mage
		colorPairs.add(new ColorPair(0x222222, 0x7b1a7c)); //Dark Mage
		colorPairs.add(new ColorPair(0, 0)); //Hell Cow
		colorPairs.add(new ColorPair(0x5a5a5a, 0x42220c)); //Earth Elemental
		colorPairs.add(new ColorPair(0xd42603, 0xdb8f23)); //Fire Elemental
		colorPairs.add(new ColorPair(0x0, 0xdb8f23)); //Darkling
		colorPairs.add(new ColorPair(0xFFFFFF, 0xF0F0F0)); //Flicker

		colorPairs.add(new ColorPair(0x2f9821, 0xc9bc2f)); //Nature Guardian
		colorPairs.add(new ColorPair(0x7f3280, 0xc9bc2f)); //Arcane Guardian
		colorPairs.add(new ColorPair(0x999999, 0xc9bc2f)); //Earth Guardian
		colorPairs.add(new ColorPair(0x324fac, 0xc9bc2f)); //Water Guardian
		colorPairs.add(new ColorPair(0x36d5d7, 0xc9bc2f)); //Ice Guardian
		colorPairs.add(new ColorPair(0xc1e1dd, 0xc9bc2f)); //Air Guardian
		colorPairs.add(new ColorPair(0xcb5420, 0xc9bc2f)); //Fire Guardian
		colorPairs.add(new ColorPair(0x12e780, 0xc9bc2f)); //Life Guardian
		colorPairs.add(new ColorPair(0x006677, 0xc9bc2f)); //Lightning Guardian
		colorPairs.add(new ColorPair(0x000000, 0xc9bc2f)); //Ender Guardian

		setHasSubtypes(true);
	}

	@Override
	public boolean hasEffect(ItemStack stack, int pass){
		return pass == 0 && stack.getItemDamage() > 8;
	}

	@Override
	public boolean requiresMultipleRenderPasses(){
		return true;
	}

	@Override
	public int getRenderPasses(int metadata){
		return 2;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister){
		AMGuiIcons.instance.init(iconRegister);
		AMParticleIcons.instance.init(iconRegister);
		SpellTextureHelper.instance.loadAllIcons(iconRegister);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack){
		String s = ("" + StatCollector.translateToLocal(Items.spawn_egg.getUnlocalizedName() + ".name")).trim();
		String s1 = getSpawnStringFromMeta(stack.getItemDamage());

		if (s1 != null){
			s = s + " " + s1;
		}

		return s;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack stack, int pass){
		return colorPairs.get(stack.getItemDamage()).colors[pass];
	}

	private static Class getSpawnClassFromMeta(int meta){
		switch (meta){
		case 0:
			return EntityManaCreeper.class;
		case 1:
			return EntityDryad.class;
		case 2:
			return EntityHecate.class;
		case 3:
			return EntityMageVillager.class;
		case 4:
			return EntityManaElemental.class;
		case 5:
			return EntityWaterElemental.class;
		case 6:
			return EntityLightMage.class;
		case 7:
			return EntityDarkMage.class;
		case 8:
			return EntityHellCow.class;
		case 9:
			return EntityFlicker.class;
		case 10:
			return EntityEarthElemental.class;
		case 11:
			return EntityFireElemental.class;
		case 12:
			return EntityDarkling.class;
		case 13:
			return EntityNatureGuardian.class;
		case 14:
			return EntityArcaneGuardian.class;
		case 15:
			return EntityEarthGuardian.class;
		case 16:
			return EntityWaterGuardian.class;
		case 17:
			return EntityWinterGuardian.class;
		case 18:
			return EntityAirGuardian.class;
		case 19:
			return EntityFireGuardian.class;
		case 20:
			return EntityLifeGuardian.class;
		case 21:
			return EntityLightningGuardian.class;
		case 22:
			return EntityEnderGuardian.class;
		}

		return null;
	}

	private String getSpawnStringFromMeta(int meta){
		switch (meta){
		case 0:
			return StatCollector.translateToLocal("entity.arsmagica2.MobManaCreeper.name");
		case 1:
			return StatCollector.translateToLocal("entity.arsmagica2.MobDryad.name");
		case 2:
			return StatCollector.translateToLocal("entity.arsmagica2.MobHecate.name");
		case 3:
			return StatCollector.translateToLocal("entity.arsmagica2.MobMageVillager.name");
		case 4:
			return StatCollector.translateToLocal("entity.arsmagica2.MobManaElemental.name");
		case 5:
			return StatCollector.translateToLocal("entity.arsmagica2.MobWaterElemental.name");
		case 6:
			return StatCollector.translateToLocal("entity.arsmagica2.MobLightMage.name");
		case 7:
			return StatCollector.translateToLocal("entity.arsmagica2.MobDarkMage.name");
		case 8:
			return StatCollector.translateToLocal("entity.arsmagica2.HellCow.name");
		case 9:
			return StatCollector.translateToLocal("entity.arsmagica2.Flicker.name");
		case 10:
			return StatCollector.translateToLocal("entity.arsmagica2.EarthElemental.name");
		case 11:
			return StatCollector.translateToLocal("entity.arsmagica2.MobFireElemental.name");
		case 12:
			return StatCollector.translateToLocal("entity.arsmagica2.MobDarkling.name");
		case 13:
			return StatCollector.translateToLocal("entity.arsmagica2.BossNatureGuardian.name");
		case 14:
			return StatCollector.translateToLocal("entity.arsmagica2.BossArcaneGuardian.name");
		case 15:
			return StatCollector.translateToLocal("entity.arsmagica2.BossEarthGuardian.name");
		case 16:
			return StatCollector.translateToLocal("entity.arsmagica2.BossWaterGuardian.name");
		case 17:
			return StatCollector.translateToLocal("entity.arsmagica2.BossWinterGuardian.name");
		case 18:
			return StatCollector.translateToLocal("entity.arsmagica2.BossAirGuardian.name");
		case 19:
			return StatCollector.translateToLocal("entity.arsmagica2.BossFireGuardian.name");
		case 20:
			return StatCollector.translateToLocal("entity.arsmagica2.BossLifeGuardian.name");
		case 21:
			return StatCollector.translateToLocal("entity.arsmagica2.BossLightningGuardian.name");
		case 22:
			return StatCollector.translateToLocal("entity.arsmagica2.BossEnderGuardian.name");
		}

		return null;
	}

	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10){
		if (par3World.isRemote){
			return true;
		}else{

			Block block = par3World.getBlock(par4, par5, par6);
			par4 += Facing.offsetsXForSide[par7];
			par5 += Facing.offsetsYForSide[par7];
			par6 += Facing.offsetsZForSide[par7];
			double d0 = 0.0D;

			if (par7 == 1 && block != null && block.getRenderType() == 11){
				d0 = 0.5D;
			}

			Entity entity = spawnCreature(par3World, par1ItemStack.getItemDamage(), par4 + 0.5D, par5 + d0, par6 + 0.5D);

			if (entity != null){
				if (entity instanceof EntityLiving && par1ItemStack.hasDisplayName()){
					((EntityLiving)entity).setCustomNameTag(par1ItemStack.getDisplayName());
				}

				if (!par2EntityPlayer.capabilities.isCreativeMode){
					--par1ItemStack.stackSize;
				}
			}

			return true;
		}
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer){
		MovingObjectPosition mop = ItemsCommonProxy.spell.getMovingObjectPosition(par3EntityPlayer, par2World, 8.0f, true, false);

		if (mop != null && mop.typeOfHit == MovingObjectType.ENTITY && !par2World.isRemote){

			Entity entity = spawnCreature(par2World, par1ItemStack.getItemDamage(), mop.entityHit.posX, mop.entityHit.posY, mop.entityHit.posZ);

			if (entity != null){
				if (entity instanceof EntityLiving && par1ItemStack.hasDisplayName()){
					((EntityLiving)entity).setCustomNameTag(par1ItemStack.getDisplayName());
				}

				if (!par3EntityPlayer.capabilities.isCreativeMode){
					--par1ItemStack.stackSize;
				}

				if (mop.entityHit instanceof EntityHecate && getSpawnClassFromMeta(par1ItemStack.getItemDamage()) == EntityHecate.class){
					((EntityHecate)entity).setChild(true);
				}
			}
		}

		return par1ItemStack;
	}

	public static Entity spawnCreature(World par0World, int par1, double par2, double par4, double par6){
		Class entityClass = getSpawnClassFromMeta(par1);
		if (entityClass == null){
			return null;
		}else{
			Entity entity = null;
			try{
				Constructor c = entityClass.getConstructor(World.class);
				entity = (Entity)c.newInstance(par0World);
			}catch (InstantiationException e){
				e.printStackTrace();
			}catch (IllegalAccessException e){
				e.printStackTrace();
			}catch (NoSuchMethodException e){
				e.printStackTrace();
			}catch (SecurityException e){
				e.printStackTrace();
			}catch (IllegalArgumentException e){
				e.printStackTrace();
			}catch (InvocationTargetException e){
				e.printStackTrace();
			}

			if (entity != null && entity instanceof EntityLiving){
				EntityLiving entityliving = (EntityLiving)entity;
				entity.setLocationAndAngles(par2, par4, par6, MathHelper.wrapAngleTo180_float(par0World.rand.nextFloat() * 360.0F), 0.0F);
				entityliving.rotationYawHead = entityliving.rotationYaw;
				entityliving.renderYawOffset = entityliving.rotationYaw;
				par0World.spawnEntityInWorld(entity);
				entityliving.playLivingSound();
			}

			return entity;
		}
	}

	@Override
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List){
		for (int i = 0; i < numClasses; ++i){
			par3List.add(new ItemStack(par1, 1, i));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int par1){
		return Items.spawn_egg.getIconFromDamage(par1);
	}

	@Override
	public IIcon getIcon(ItemStack stack, int pass){
		return Items.spawn_egg.getIcon(stack, pass);
	}

	@Override
	public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining){
		return Items.spawn_egg.getIcon(stack, renderPass, player, usingItem, useRemaining);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamageForRenderPass(int par1, int par2){
		return Items.spawn_egg.getIconFromDamageForRenderPass(par1, par2);
	}

	class ColorPair{
		public int[] colors;

		public ColorPair(int primary, int secondary){
			colors = new int[2];
			colors[0] = primary;
			colors[1] = secondary;
		}
	}
}
