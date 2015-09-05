package am2.blocks.tileentities;

import am2.AMCore;
import am2.api.math.AMVector3;
import am2.items.ItemsCommonProxy;
import am2.network.AMDataWriter;
import am2.network.AMNetHandler;
import am2.network.AMPacketIDs;
import am2.particles.AMParticle;
import am2.particles.AMParticleIcons;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

import java.util.Random;

public class TileEntityParticleEmitter extends TileEntity{

	private int particleType;
	private int particleQuantity;
	private int spawnRate;
	private int particleBehaviour;
	private int particleColor;
	private float particleScale;
	private float particleAlpha;
	private boolean defaultColor;
	private boolean randomColor;
	private boolean show;
	private float speed;

	private boolean hasReceivedFullUpdate = false;
	private int fullUpdateDelayTicks;
	private int updateCounter = 0;

	private int spawnTicks = 0;
	private int showTicks = 0;
	boolean forceShow;

	private Random random = new Random();

	public TileEntityParticleEmitter(){
		particleType = 0;
		particleQuantity = 1;
		spawnRate = 5;
		particleBehaviour = 0;
		particleColor = 0;
		particleScale = 0.5f;
		particleAlpha = 1.0f;
		defaultColor = true;
		randomColor = false;
		show = true;
		forceShow = false;
		fullUpdateDelayTicks = random.nextInt(40);
	}

	@Override
	public void updateEntity(){
		if (worldObj.isRemote && spawnTicks++ >= spawnRate){
			for (int i = 0; i < particleQuantity; ++i)
				doSpawn();
			spawnTicks = 0;
		}

		if (!show && worldObj.isRemote && ((forceShow && showTicks++ > 100) || !forceShow)){
			showTicks = 0;
			forceShow = false;
			EntityPlayer localPlayer = AMCore.proxy.getLocalPlayer();
			if (localPlayer != null && localPlayer.inventory.getCurrentItem() != null && localPlayer.inventory.getCurrentItem().getItem() == ItemsCommonProxy.crystalWrench){
				AMVector3 myLoc = new AMVector3(xCoord, yCoord, zCoord);
				AMVector3 playerLoc = new AMVector3(localPlayer);
				if (myLoc.distanceSqTo(playerLoc) < 64D){
					forceShow = true;
				}
			}

			int oldMeta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);

			if (forceShow){
				this.worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, oldMeta & ~0x8, 2);
			}else{
				this.worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, oldMeta | 0x8, 2);
			}
		}
	}

	private void doSpawn(){
		if (!hasReceivedFullUpdate) return;
		double x = randomzieCoord(xCoord + 0.5);
		double y = randomzieCoord(yCoord + 0.5);
		double z = randomzieCoord(zCoord + 0.5);
		AMParticle particle = (AMParticle)AMCore.proxy.particleManager.spawn(worldObj, AMParticle.particleTypes[particleType], x, y, z);
		if (particle != null){
			particle.AddParticleController(AMCore.proxy.particleManager.createDefaultParticleController(particleBehaviour, particle, new AMVector3(x, y, z), speed, worldObj.getBlockMetadata(xCoord, yCoord, zCoord)));
			particle.setParticleAge(Math.min(Math.max(spawnRate, 10), 40));
			particle.setIgnoreMaxAge(false);
			particle.setParticleScale(particleScale);
			particle.SetParticleAlpha(particleAlpha);
			if (!defaultColor){
				if (!randomColor)
					particle.setRGBColorF(((particleColor >> 16) & 0xFF) / 255f, ((particleColor >> 8) & 0xFF) / 255f, (particleColor & 0xFF) / 255f);
				else
					particle.setRGBColorF(random.nextFloat(), random.nextFloat(), random.nextFloat());
			}
		}
	}

	private double randomzieCoord(double base){
		return base + random.nextDouble() - 0.5;
	}

	@Override
	public Packet getDescriptionPacket(){
		NBTTagCompound compound = new NBTTagCompound();
		this.writeToNBT(compound);
		S35PacketUpdateTileEntity packet = new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, worldObj.getBlockMetadata(xCoord, yCoord, zCoord), compound);
		return packet;
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt){
		this.readFromNBT(pkt.func_148857_g());
		applyParamConstraints();
		hasReceivedFullUpdate = true;
	}

	private void applyParamConstraints(){
		if (spawnRate < 1) spawnRate = 1;
		if (particleQuantity < 1) particleQuantity = 1;
		if (particleQuantity > 5) particleQuantity = 5;
		if (particleType < 0) particleType = 0;
		if (particleType > AMParticleIcons.instance.numParticles())
			particleType = AMParticleIcons.instance.numParticles() - 1;
		if (particleBehaviour < 0) particleBehaviour = 0;
		if (particleBehaviour > 6) particleBehaviour = 6;
		if (particleScale < 0) particleScale = 0;
		if (particleScale > 1) particleScale = 1;
		if (particleAlpha < 0) particleAlpha = 0;
		if (particleAlpha > 1) particleAlpha = 1;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound){
		super.readFromNBT(compound);
		readSettingsFromNBT(compound);
	}

	public void readSettingsFromNBT(NBTTagCompound compound){
		particleType = compound.getInteger("particleType");
		particleQuantity = compound.getInteger("particleQuantity");
		spawnRate = compound.getInteger("spawnRate");
		particleBehaviour = compound.getInteger("particleBehaviour");
		particleColor = compound.getInteger("particleColor");
		particleScale = compound.getFloat("particleScale");
		particleAlpha = compound.getFloat("particleAlpha");
		defaultColor = compound.getBoolean("defaultColor");
		randomColor = compound.getBoolean("randomColor");
		show = compound.getBoolean("show");
		speed = compound.getFloat("speed");
	}

	@Override
	public void writeToNBT(NBTTagCompound compound){
		super.writeToNBT(compound);
		writeSettingsToNBT(compound);
	}

	public void writeSettingsToNBT(NBTTagCompound compound){
		compound.setInteger("particleType", particleType);
		compound.setInteger("particleQuantity", particleQuantity);
		compound.setInteger("spawnRate", spawnRate);
		compound.setInteger("particleBehaviour", particleBehaviour);
		compound.setInteger("particleColor", particleColor);
		compound.setFloat("particleScale", particleScale);
		compound.setFloat("particleAlpha", particleAlpha);
		compound.setBoolean("defaultColor", defaultColor);
		compound.setBoolean("randomColor", randomColor);
		compound.setBoolean("show", show);
		compound.setFloat("speed", speed);
	}

	public void setParticleType(int particleType){
		this.particleType = particleType;
	}

	public void setParticleBehaviour(int particleBehaviour){
		this.particleBehaviour = particleBehaviour;
	}

	public void setColorDefault(boolean def){
		this.defaultColor = def;
	}

	public void setColorRandom(boolean rand){
		this.randomColor = rand;
	}

	public void setColor(int color){
		this.particleColor = color;
	}

	public void setScale(float scale){
		this.particleScale = scale;
	}

	public void setAlpha(float alpha){
		this.particleAlpha = alpha;
	}

	public void setShow(boolean show){
		this.show = show;
		if (worldObj.isRemote && show){
			forceShow = false;
			showTicks = 0;
			EntityPlayer localPlayer = AMCore.proxy.getLocalPlayer();
			if (localPlayer != null && localPlayer.inventory.getCurrentItem() != null && localPlayer.inventory.getCurrentItem().getItem() == ItemsCommonProxy.crystalWrench){
				AMVector3 myLoc = new AMVector3(xCoord, yCoord, zCoord);
				AMVector3 playerLoc = new AMVector3(localPlayer);
				if (myLoc.distanceSqTo(playerLoc) < 64D){
					forceShow = true;
				}
			}
		}

		int oldMeta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);

		worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, show ? oldMeta & ~0x8 : oldMeta | 0x8, 2);
	}

	public int getParticleType(){
		return this.particleType;
	}

	public int getParticleBehaviour(){
		return this.particleBehaviour;
	}

	public boolean getColorDefault(){
		return this.defaultColor;
	}

	public boolean getColorRandom(){
		return this.randomColor;
	}

	public int getColor(){
		return this.particleColor;
	}

	public float getScale(){
		return this.particleScale;
	}

	public float getAlpha(){
		return this.particleAlpha;
	}

	public boolean getShow(){
		return this.show;
	}

	public void setQuantity(int quantity){
		this.particleQuantity = quantity;
	}

	public int getQuantity(){
		return this.particleQuantity;
	}

	public void setDelay(int delay){
		this.spawnRate = delay;
		this.spawnTicks = 0;
	}

	public int getDelay(){
		return this.spawnRate;
	}

	public void setSpeed(float speed){
		this.speed = speed;
	}

	public float getSpeed(){
		return speed;
	}

	public void syncWithServer(){
		if (this.worldObj.isRemote){
			AMDataWriter writer = new AMDataWriter();
			writer.add(this.xCoord);
			writer.add(this.yCoord);
			writer.add(this.zCoord);
			NBTTagCompound compound = new NBTTagCompound();
			this.writeToNBT(compound);
			writer.add(compound);

			byte[] data = writer.generate();

			AMNetHandler.INSTANCE.sendPacketToServer(AMPacketIDs.DECO_BLOCK_UPDATE, data);
		}
	}
}
