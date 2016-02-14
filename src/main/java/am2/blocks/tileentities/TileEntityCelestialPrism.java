package am2.blocks.tileentities;

import am2.AMCore;
import am2.api.blocks.MultiblockStructureDefinition;
import am2.api.blocks.MultiblockStructureDefinition.StructureGroup;
import am2.api.power.PowerTypes;
import am2.blocks.BlocksCommonProxy;
import am2.buffs.BuffEffectManaRegen;
import am2.buffs.BuffList;
import am2.multiblock.IMultiblockStructureController;
import am2.power.PowerNodeRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TileEntityCelestialPrism extends TileEntityObelisk implements IMultiblockStructureController{

	private StructureGroup moonstone;
	private int particleCounter = 0;

	private boolean onlyChargeAtNight = false;

	public TileEntityCelestialPrism(){
		super(2500);

		powerBase = 1.0f;

		structure = new MultiblockStructureDefinition("celestialprism_structure");

		StructureGroup glass = structure.createGroup("caps_glass", 2);
		StructureGroup gold = structure.createGroup("caps_gold", 2);
		StructureGroup diamond = structure.createGroup("caps_diamond", 2);
		moonstone = structure.createGroup("caps_moonstone", 2);

		pillars = structure.createGroup("pillars", 4);

		caps = new HashMap<StructureGroup, Float>();
		caps.put(glass, 1.1f);
		caps.put(gold, 1.4f);
		caps.put(diamond, 2f);
		caps.put(moonstone, 3f);

		structure.addAllowedBlock(0, 0, 0, BlocksCommonProxy.celestialPrism);

		structure.addAllowedBlock(pillars, -2, 0, -2, Blocks.quartz_block);
		structure.addAllowedBlock(pillars, -2, 1, -2, Blocks.quartz_block);

		structure.addAllowedBlock(glass, -2, 2, -2, Blocks.glass);
		structure.addAllowedBlock(gold, -2, 2, -2, Blocks.gold_block);
		structure.addAllowedBlock(diamond, -2, 2, -2, Blocks.diamond_block);
		structure.addAllowedBlock(moonstone, -2, 2, -2, BlocksCommonProxy.AMOres, BlocksCommonProxy.AMOres.META_MOONSTONE_BLOCK);

		structure.addAllowedBlock(pillars, 2, 0, -2, Blocks.quartz_block);
		structure.addAllowedBlock(pillars, 2, 1, -2, Blocks.quartz_block);

		structure.addAllowedBlock(glass, 2, 2, -2, Blocks.glass);
		structure.addAllowedBlock(gold, 2, 2, -2, Blocks.gold_block);
		structure.addAllowedBlock(diamond, 2, 2, -2, Blocks.diamond_block);
		structure.addAllowedBlock(moonstone, 2, 2, -2, BlocksCommonProxy.AMOres, BlocksCommonProxy.AMOres.META_MOONSTONE_BLOCK);

		structure.addAllowedBlock(pillars, -2, 0, 2, Blocks.quartz_block);
		structure.addAllowedBlock(pillars, -2, 1, 2, Blocks.quartz_block);

		structure.addAllowedBlock(glass, -2, 2, 2, Blocks.glass);
		structure.addAllowedBlock(gold, -2, 2, 2, Blocks.gold_block);
		structure.addAllowedBlock(diamond, -2, 2, 2, Blocks.diamond_block);
		structure.addAllowedBlock(moonstone, -2, 2, 2, BlocksCommonProxy.AMOres, BlocksCommonProxy.AMOres.META_MOONSTONE_BLOCK);

		structure.addAllowedBlock(pillars, 2, 0, 2, Blocks.quartz_block);
		structure.addAllowedBlock(pillars, 2, 1, 2, Blocks.quartz_block);

		structure.addAllowedBlock(glass, 2, 2, 2, Blocks.glass);
		structure.addAllowedBlock(gold, 2, 2, 2, Blocks.gold_block);
		structure.addAllowedBlock(diamond, 2, 2, 2, Blocks.diamond_block);
		structure.addAllowedBlock(moonstone, 2, 2, 2, BlocksCommonProxy.AMOres, BlocksCommonProxy.AMOres.META_MOONSTONE_BLOCK);

		wizardChalkCircle = addWizChalkGroupToStructure(structure, 1);
	}

	@Override
	protected void checkNearbyBlockState(){
		ArrayList<StructureGroup> groups = structure.getMatchedGroups(7, worldObj, pos);

		float capsLevel = 1;
		boolean pillarsFound = false;
		boolean wizChalkFound = false;

		for (StructureGroup group : groups){
			if (group == pillars)
				pillarsFound = true;
			else if (group == wizardChalkCircle)
				wizChalkFound = true;

			for (StructureGroup cap : caps.keySet()){
				if (group == cap){
					capsLevel = caps.get(cap);
					if (group == moonstone)
						onlyChargeAtNight = true;
					else
						onlyChargeAtNight = false;
					break;
				}
			}
		}

		powerMultiplier = 1;

		if (wizChalkFound)
			powerMultiplier = 1.25f;

		if (pillarsFound)
			powerMultiplier *= capsLevel;
	}

	private boolean isNight(){
		long ticks = worldObj.getWorldTime() % 24000;
		return ticks >= 12500 && ticks <= 23500;
	}

	@Override
	public void update(){

		if (surroundingCheckTicks++ % 100 == 0){
			checkNearbyBlockState();
			surroundingCheckTicks = 1;
			if (!worldObj.isRemote && PowerNodeRegistry.For(this.worldObj).checkPower(this, this.capacity * 0.1f)){
				List<EntityPlayer> nearbyPlayers = worldObj.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(pos.add(-2, 0, -2), pos.add(2, 3, 2)));
				for (EntityPlayer p : nearbyPlayers){
					if (p.isPotionActive(BuffList.manaRegen.id)) continue;
					p.addPotionEffect(new BuffEffectManaRegen(600, 1));
				}
			}
		}

		if (onlyChargeAtNight == isNight()){
			PowerNodeRegistry.For(this.worldObj).insertPower(this, PowerTypes.LIGHT, 0.25f * powerMultiplier);
			if (worldObj.isRemote){

				if (particleCounter++ % 180 == 0){
					particleCounter = 1;
					AMCore.proxy.particleManager.RibbonFromPointToPoint(worldObj, pos.getX() + worldObj.rand.nextFloat(), pos.getY() + (worldObj.rand.nextFloat() * 2), pos.getZ() + worldObj.rand.nextFloat(), pos.getX() + worldObj.rand.nextFloat(), pos.getY() + (worldObj.rand.nextFloat() * 2), pos.getZ() + worldObj.rand.nextFloat());
				}
			}
		}
		super.callSuperUpdate();
	}

	@Override
	public MultiblockStructureDefinition getDefinition(){
		return structure;
	}

	@Override
	public boolean canRequestPower(){
		return false;
	}

	@Override
	public boolean canProvidePower(PowerTypes type){
		return type == PowerTypes.LIGHT;
	}

	@Override
	public PowerTypes[] getValidPowerTypes(){
		return new PowerTypes[]{
				PowerTypes.LIGHT
		};
	}

	@Override
	public int getSizeInventory(){
		return 0;
	}
}
