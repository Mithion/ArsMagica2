package am2.blocks.tileentities.flickers;

import am2.api.flickers.IFlickerController;
import am2.api.flickers.IFlickerFunctionality;
import am2.api.power.PowerTypes;
import am2.api.spell.enums.Affinity;
import am2.blocks.tileentities.TileEntityAMPower;
import am2.blocks.tileentities.TileEntityFlickerHabitat;
import am2.items.ItemsCommonProxy;
import am2.power.PowerNodeRegistry;
import com.sun.corba.se.impl.logging.POASystemException;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.util.Constants;

import java.util.HashMap;

public class TileEntityFlickerControllerBase extends TileEntityAMPower implements IFlickerController{
	private HashMap<Integer, byte[]> sigilMetadata;
	private IFlickerFunctionality operator;
	private int tickCounter;
	Affinity[] nearbyList = new Affinity[6];
	private boolean lastOpWasPowered = false;
	private boolean firstOp = true;

	public TileEntityFlickerControllerBase(){
		super(500);
		sigilMetadata = new HashMap<Integer, byte[]>();
	}

	protected void setOperator(IFlickerFunctionality operator){
		if (this.operator != null){
			this.operator.RemoveOperator(worldObj, this, PowerNodeRegistry.For(worldObj).checkPower(this, this.operator.PowerPerOperation()), nearbyList);
		}
		this.operator = operator;
		tickCounter = 0;
	}

	public void updateOperator(ItemStack stack){
		if (stack == null || stack.getItem() != ItemsCommonProxy.flickerFocus)
			return;
		operator = FlickerOperatorRegistry.instance.getOperatorForMask(stack.getItemDamage());
	}

	public void scanForNearbyUpgrades(){
		for (EnumFacing direction : EnumFacing.VALUES){
			TileEntity te = worldObj.getTileEntity(pos.add(pos.getX() + direction.getFrontOffsetX(), pos.getY() + direction.getFrontOffsetY(), pos.getZ() + direction.getFrontOffsetZ()));
			if (te != null && te instanceof TileEntityFlickerHabitat){
				nearbyList[direction.ordinal()] = ((TileEntityFlickerHabitat)te).getSelectedAffinity();
			}
		}
	}

	public void notifyOfNearbyUpgradeChange(TileEntity neighbor){
		if (neighbor instanceof TileEntityFlickerHabitat){
			EnumFacing direction = getNeighboringFacing(neighbor);
			if (direction != null){
				nearbyList[direction.ordinal()] = ((TileEntityFlickerHabitat)neighbor).getSelectedAffinity();
			}
		}
	}

	private EnumFacing getNeighboringFacing(TileEntity neighbor){
		if (neighbor.getPos().equals(pos.south()))
			return EnumFacing.SOUTH;
		else if (neighbor.getPos().equals(pos.north()))
			return EnumFacing.NORTH;
		else if (neighbor.getPos().equals(pos.east()))
			return EnumFacing.EAST;
		else if (neighbor.getPos().equals(pos.west()))
			return EnumFacing.WEST;
		else if (neighbor.getPos().equals(pos.up()))
			return EnumFacing.UP;
		else if (neighbor.getPos().equals(pos.down()))
			return EnumFacing.DOWN;

		return null;
	}

	@Override
	public void update(){
		//handle any power update ticks
		super.update();

		//if redstone powered, increment the tick counter (so that operator time still continues), but do nothing else.
		//this allows a redstone signal to effectively turn off any flicker habitat.
		if (worldObj.isBlockIndirectlyGettingPowered(pos) > 1){
			tickCounter++;
			return;
		}

		//tick operator, if it exists
		if (operator != null){
			boolean powered = PowerNodeRegistry.For(worldObj).checkPower(this, operator.PowerPerOperation());

			//check which neighbors are not receiving power
			//this allows individual upgrades to be turned off by providing them with a redstone signal.
			Affinity unpoweredNeighbors[] = getUnpoweredNeighbors();

			if (tickCounter++ >= operator.TimeBetweenOperation(powered, unpoweredNeighbors)){
				tickCounter = 0;
				if ((powered && operator.RequiresPower()) || !operator.RequiresPower()){
					if (firstOp){
						scanForNearbyUpgrades();
						firstOp = false;
					}
					boolean success = operator.DoOperation(worldObj, this, powered, unpoweredNeighbors);
					if (success || operator.RequiresPower())
						PowerNodeRegistry.For(worldObj).consumePower(this, PowerNodeRegistry.For(worldObj).getHighestPowerType(this), operator.PowerPerOperation());
					lastOpWasPowered = true;
				}else if (lastOpWasPowered && operator.RequiresPower() && !powered){
					operator.RemoveOperator(worldObj, this, powered, unpoweredNeighbors);
					lastOpWasPowered = false;
				}
			}
		}
	}

	private Affinity[] getUnpoweredNeighbors(){
		Affinity[] aff = new Affinity[EnumFacing.values().length];
		for (int i = 0; i < nearbyList.length; ++i){
			EnumFacing dir = EnumFacing.values()[i];
			if (nearbyList[i] == null || worldObj.isBlockIndirectlyGettingPowered(pos.offset(dir)) > 1){
				aff[i] = null;
			}else{
				aff[i] = nearbyList[i];
			}
		}
		return aff;
	}

	private Integer getFlagForOperator(IFlickerFunctionality operator){
		return FlickerOperatorRegistry.instance.getMaskForOperator(operator);
	}

	public void setMetadata(IFlickerFunctionality operator, byte[] meta){
		sigilMetadata.put(getFlagForOperator(operator), meta);
	}

	public byte[] getMetadata(IFlickerFunctionality operator){
		byte[] arr = sigilMetadata.get(getFlagForOperator(operator));
		return arr != null ? arr : new byte[0];
	}

	public void removeMetadata(IFlickerFunctionality operator){
		sigilMetadata.remove(getFlagForOperator(operator));
	}

	@Override
	public void writeToNBT(NBTTagCompound par1nbtTagCompound){
		super.writeToNBT(par1nbtTagCompound);

		NBTTagList sigilMetaStore = new NBTTagList();
		for (Integer i : sigilMetadata.keySet()){
			NBTTagCompound sigilMetaEntry = new NBTTagCompound();
			sigilMetaEntry.setInteger("sigil_mask", i);
			sigilMetaEntry.setByteArray("sigil_meta", sigilMetadata.get(i));
			sigilMetaStore.appendTag(sigilMetaEntry);
		}

		par1nbtTagCompound.setTag("sigil_metadata_collection", sigilMetaStore);
	}

	@Override
	public void readFromNBT(NBTTagCompound par1nbtTagCompound){
		super.readFromNBT(par1nbtTagCompound);

		sigilMetadata = new HashMap<Integer, byte[]>();

		NBTTagList sigilMetaStore = par1nbtTagCompound.getTagList("sigil_metadata_collection", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < sigilMetaStore.tagCount(); ++i){
			NBTTagCompound sigilMetaEntry = (NBTTagCompound)sigilMetaStore.getCompoundTagAt(i);
			Integer mask = sigilMetaEntry.getInteger("sigil_mask");
			byte[] meta = sigilMetaEntry.getByteArray("sigil_meta");
			sigilMetadata.put(mask, meta);
		}
	}

	@Override
	public boolean canProvidePower(PowerTypes type){
		return false;
	}

	@Override
	public boolean canRelayPower(PowerTypes type){
		return false;
	}

	@Override
	public boolean canRequestPower(){
		return true;
	}

	@Override
	public boolean isSource(){
		return false;
	}

	@Override
	public int getChargeRate(){
		return 100;
	}

	@Override
	public PowerTypes[] getValidPowerTypes(){
		return PowerTypes.all();
	}

	@Override
	public float particleOffset(EnumFacing.Axis axis){
		return 0.5f;
	}



	public Affinity[] getNearbyUpgrades(){
		return this.getUnpoweredNeighbors();
	}
}
