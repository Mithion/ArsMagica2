package am2.spell.components;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;
import am2.AMCore;
import am2.api.ArsMagicaApi;
import am2.api.spell.component.interfaces.ISpellComponent;
import am2.api.spell.enums.Affinity;
import am2.items.ItemsCommonProxy;
import am2.particles.AMParticle;
import am2.utility.DummyEntityPlayer;

public class Plant implements ISpellComponent {

	@Override
	public boolean applyEffectBlock(ItemStack stack, World world, int blockx, int blocky, int blockz, int blockFace, double impactX, double impactY, double impactZ, EntityLivingBase caster) {
		Block soil = world.getBlock(blockx, blocky, blockz);
		IInventory inventory = DummyEntityPlayer.fromEntityLiving(caster).inventory;
		HashMap<Integer, ItemStack> seeds = GetAllSeedsInInventory(inventory);
		int currentSlot = 0;
		if (soil != Blocks.air && seeds.size() > 0)
		{
			currentSlot = seeds.keySet().iterator().next();
			ItemStack seedStack = seeds.get(currentSlot);	
			
			IPlantable seed = (IPlantable) seedStack.getItem();
			
			if (soil != null && soil.canSustainPlant(world, blockx, blocky, blockz, ForgeDirection.UP, seed) && world.isAirBlock(blockx, blocky + 1, blockz))
			{				
				world.setBlock(blockx, blocky + 1, blockz, seed.getPlant(world, blockx, blocky, blockz));
				
				seedStack.stackSize--;
				if (seedStack.stackSize <= 0){
					inventory.setInventorySlotContents(currentSlot, null);
					seeds.remove(currentSlot);
					if (seeds.size() == 0) return true;
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean applyEffectEntity(ItemStack stack, World world, EntityLivingBase caster, Entity target) {
		return false;
	}

	@Override
	public float manaCost(EntityLivingBase caster) {
		return 80;
	}

	@Override
	public float burnout(EntityLivingBase caster) {
		return ArsMagicaApi.getBurnoutFromMana(manaCost(caster));
	}

	@Override
	public ItemStack[] reagents(EntityLivingBase caster) {
		return null;
	}

	@Override
	public void spawnParticles(World world, double x, double y, double z, EntityLivingBase caster, Entity target, Random rand, int colorModifier) {		
		for (int i = 0; i < 15; ++i){
			AMParticle particle = (AMParticle) AMCore.proxy.particleManager.spawn(world, "plant", x, y + 1, z);
			if (particle != null){
				particle.addRandomOffset(1, 1, 1);
				particle.addVelocity(rand.nextDouble() * 0.2 - 0.1, 0.2f, rand.nextDouble() * 0.2 - 0.1);
				particle.setDontRequireControllers();
				particle.setAffectedByGravity();
				particle.setMaxAge(20);
				particle.setParticleScale(0.1f);
				if (colorModifier > -1){
					particle.setRGBColorF(((colorModifier >> 16) & 0xFF) / 255.0f, ((colorModifier >> 8) & 0xFF) / 255.0f, (colorModifier& 0xFF) / 255.0f);
				}
			}
		}
	}

	@Override
	public EnumSet<Affinity> getAffinity() {
		return EnumSet.of(Affinity.NATURE);
	}
	
	private HashMap<Integer, ItemStack> GetAllSeedsInInventory(IInventory inventory) {
		HashMap<Integer, ItemStack> seeds = new HashMap<Integer, ItemStack>();
		for (int i = 0; i < inventory.getSizeInventory(); ++i){
			ItemStack slotStack = inventory.getStackInSlot(i);
			if (slotStack == null) continue;
			Item item = slotStack.getItem();
			if (!(item instanceof IPlantable)) continue;
			seeds.put(i, slotStack);
		}		

		return seeds;
	}
	
	@Override
	public int getID() {
		return 41;
	}

	@Override
	public Object[] getRecipeItems() {
		return new Object[]{
			new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_GREEN),
			Items.wheat_seeds,
			new ItemStack(Blocks.sapling, 1, Short.MAX_VALUE),
			Items.wheat_seeds
		};
	}
	
	@Override
	public float getAffinityShift(Affinity affinity) {
		return 0.01f;
	}
}
