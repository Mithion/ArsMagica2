package am2.blocks.tileentities;

import java.util.Random;

import am2.api.power.PowerTypes;

public class TileEntityEssenceConduit extends TileEntityAMPower {

	private float rotationX;
	private float rotationY;
	private float rotationZ;

	private boolean redstonePowered;

	private float rotationIncrementX;
	private float rotationIncrementY;
	private float rotationIncrementZ;

	public TileEntityEssenceConduit(){
		super(1);
		Random rand = new Random();
		rotationX = rand.nextInt(360);
		rotationY = rand.nextInt(360);
		rotationZ = rand.nextInt(360);

		redstonePowered = false;

		rotationIncrementX = rand.nextFloat() * 0.002f + 0.005f;
		rotationIncrementY = rand.nextFloat() * 0.002f + 0.005f;
		rotationIncrementZ = rand.nextFloat() * 0.002f + 0.005f;

	}

	@Override
	public void updateEntity(){
		if (worldObj != null && worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord)){
			redstonePowered = true;
		}else{
			redstonePowered = false;
		}
		super.updateEntity();
	}

	@Override
	public float particleOffset(int axis) {
		int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);

		if (axis == 0){
			switch(meta){
			case 6:
				return 0.8f;
			case 5:
				return 0.2f;
			default:
				return 0.5f;
			}
		}else if (axis == 1){
			switch(meta){
			case 1:
				return 0.2f;
			case 2:
				return 0.8f;
			default:
				return 0.5f;
			}
		}else if (axis == 2){
			switch(meta){
			case 4:
				return 0.8f;
			case 3:
				return 0.2f;
			default:
				return 0.5f;
			}
		}

		return 0.5f;
	}

	public float getRotationX(){
		return this.rotationX;
	}

	public float getRotationY(){
		return this.rotationY;
	}

	public float getRotationZ(){
		return this.rotationZ;
	}

	public void incrementRotations(){
		rotationX += rotationIncrementX;
		rotationY += rotationIncrementY;
		rotationZ += rotationIncrementZ;

		if (rotationX >= 360){
			rotationX = 0;
		}

		if (rotationY >= 360){
			rotationY = 0;
		}

		if (rotationZ >= 360){
			rotationZ = 0;
		}

		if (rotationX < 0){
			rotationX = 359;
		}

		if (rotationY < 0){
			rotationY = 359;
		}

		if (rotationZ < 0){
			rotationZ = 359;
		}
	}

	@Override
	public int getChargeRate() {
		return 1;
	}

	@Override
	public boolean canRequestPower() {
		return !this.redstonePowered;
	}

	@Override
	public boolean canProvidePower(PowerTypes type) {
		return false;
	}

	@Override
	public boolean canRelayPower(PowerTypes type) {
		return true;
	}
}
