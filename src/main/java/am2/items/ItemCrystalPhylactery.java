package am2.items;

import am2.utility.EntityUtilities;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityList.EntityEggInfo;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.List;

public class ItemCrystalPhylactery extends ArsMagicaItem{

	private final HashMap<String, Integer> spawnableEntities;

	public static final int META_EMPTY = 0;
	public static final int META_QUARTER = 1;
	public static final int META_HALF = 2;
	public static final int META_FULL = 3;


	public ItemCrystalPhylactery(){
		super();
		spawnableEntities = new HashMap<String, Integer>();
		setMaxDamage(0);
		setMaxStackSize(1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4){
		if (par1ItemStack.hasTagCompound()){
			String className = par1ItemStack.getTagCompound().getString("SpawnClassName");
			if (className != null){
				par3List.add(String.format(StatCollector.translateToLocal("am2.tooltip.phyEss"), StatCollector.translateToLocal("entity." + className + ".name")));
				Float f = par1ItemStack.getTagCompound().getFloat("PercentFilled");
				float pct = f == null ? 0 : f.floatValue();
				par3List.add(String.format(StatCollector.translateToLocal("am2.tooltip.pctFull"), pct));
			}else{
				par3List.add(StatCollector.translateToLocal("am2.tooltip.empty"));
			}
		}else{
			par3List.add(StatCollector.translateToLocal("am2.tooltip.empty"));
		}
	}

	public void addFill(ItemStack stack){
		if (stack.hasTagCompound()){
			String className = stack.getTagCompound().getString("SpawnClassName");
			if (className != null){
				Float f = stack.getTagCompound().getFloat("PercentFilled");
				float pct = f == null ? 0 : f.floatValue();
				pct += itemRand.nextFloat() * 5;
				if (pct > 100) pct = 100;
				stack.getTagCompound().setFloat("PercentFilled", pct);
				if (pct == 100)
					stack.setItemDamage(META_FULL);
				else if (pct > 50)
					stack.setItemDamage(META_HALF);
				else if (pct > 25)
					stack.setItemDamage(META_QUARTER);
				else
					stack.setItemDamage(META_EMPTY);

			}
		}
	}

	public void addFill(ItemStack stack, float amt){
		if (stack.hasTagCompound()){
			String className = stack.getTagCompound().getString("SpawnClassName");
			if (className != null){
				Float f = stack.getTagCompound().getFloat("PercentFilled");
				float pct = f == null ? 0 : f.floatValue();
				pct += amt;
				if (pct > 100) pct = 100;
				stack.getTagCompound().setFloat("PercentFilled", pct);
				if (pct == 100)
					stack.setItemDamage(META_FULL);
				else if (pct > 50)
					stack.setItemDamage(META_HALF);
				else if (pct > 25)
					stack.setItemDamage(META_QUARTER);
				else
					stack.setItemDamage(META_EMPTY);

			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack par1ItemStack){
		return par1ItemStack.getItemDamage() == META_FULL;
	}

	public void setSpawnClass(ItemStack stack, Class clazz){

		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());

		String s = (String)EntityList.classToStringMapping.get(clazz);
		if (s != null)
			stack.getTagCompound().setString("SpawnClassName", s);
	}

	public boolean canStore(ItemStack stack, EntityLiving entity){
		if (entity instanceof IBossDisplayData) return false;
		if (stack.getItemDamage() == META_FULL)
			return false;
		if (!stack.hasTagCompound())
			return true;

		String e = stack.getTagCompound().getString("SpawnClassName");
		String s = (String)EntityList.classToStringMapping.get(entity.getClass());

		return (e != null && s != null) && e.equals(s);
	}

	public boolean isFull(ItemStack stack){
		return stack.getItemDamage() == META_FULL;
	}

	public String getSpawnClass(ItemStack stack){
		if (!stack.hasTagCompound())
			return "";
		return stack.getTagCompound().getString("SpawnClassName");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack stack, int pass){
		if (pass == 0){
			int color = 0x0000FF;
			if (stack.hasTagCompound()){
				String className = stack.getTagCompound().getString("SpawnClassName");
				if (className != null){
					Integer storedColor = spawnableEntities.get(className);
					if (storedColor != null){
						color = storedColor.intValue();
					}
				}
			}
			return color;
		}
		return 0xFFFFFF;
	}

	public void getSpawnableEntities(World world){
		for (Object clazz : EntityList.classToStringMapping.keySet()){
			if (EntityCreature.class.isAssignableFrom((Class)clazz)){
				try{
					EntityCreature temp = (EntityCreature)((Class)clazz).getConstructor(World.class).newInstance(world);
					if (EntityUtilities.isAIEnabled(temp) && !(temp instanceof IBossDisplayData)){
						int color = 0;
						boolean found = false;
						//look for entity egg
						for (Object info : EntityList.entityEggs.values()){
							EntityEggInfo eei = (EntityEggInfo)info;
							Class spawnClass = EntityList.getClassFromID(eei.spawnedID);
							if (spawnClass == (Class)clazz){
								color = eei.primaryColor;
								found = true;
								break;
							}
						}
						if (!found){
							//no spawn egg...pick random color?
							color = world.rand.nextInt();
						}
						spawnableEntities.put((String)EntityList.classToStringMapping.get(clazz), color);
					}
				}catch (Throwable e){
					//e.printStackTrace();
				}
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List){
		par3List.add(new ItemStack(this));
		for (String s : spawnableEntities.keySet()){
			ItemStack stack = new ItemStack(this, 1, META_FULL);
			stack.getTagCompound().setString("SpawnClassName", s);
			stack.getTagCompound().setFloat("PercentFilled", 100);
			par3List.add(stack);
		}
	}
}
