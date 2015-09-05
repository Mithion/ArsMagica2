package am2.blocks.tileentities;

import am2.AMCore;
import am2.items.ItemsCommonProxy;
import am2.network.AMDataWriter;
import am2.network.AMNetHandler;
import am2.network.AMPacketIDs;
import am2.particles.AMParticle;
import am2.particles.ParticleFadeOut;
import am2.particles.ParticleMoveOnHeading;
import cpw.mods.fml.common.Loader;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityEnchantmentTable;

import java.util.ArrayList;
import java.util.Random;

public class TileEntityLectern extends TileEntityEnchantmentTable{
	private ItemStack stack;
	private Random rand;
	private ItemStack tooltipStack;
	private boolean needsBook;
	private boolean overPowered;
	public int particleAge;
	public int particleMaxAge = 150;
	private boolean increasing = true;

	public TileEntityLectern(){
		rand = new Random();
	}

	public void resetParticleAge(){
		particleAge = 0;
		increasing = true;
	}

	public ItemStack getTooltipStack(){
		return tooltipStack;
	}

	public void setTooltipStack(ItemStack stack){
		this.tooltipStack = stack;
	}

	@Override
	public void updateEntity(){
		if (worldObj.isRemote){
			updateBookRender();
			if (tooltipStack != null && field_145926_a % 2 == 0){
				AMParticle particle = (AMParticle)AMCore.proxy.particleManager.spawn(worldObj, "sparkle", xCoord + 0.5 + ((rand.nextDouble() * 0.2) - 0.1), yCoord + 1, zCoord + 0.5 + ((rand.nextDouble() * 0.2) - 0.1));
				if (particle != null){
					particle.AddParticleController(new ParticleMoveOnHeading(particle, rand.nextDouble() * 360, -45 - rand.nextInt(90), 0.05f, 1, false));
					particle.AddParticleController(new ParticleFadeOut(particle, 2, false).setFadeSpeed(0.05f).setKillParticleOnFinish(true));
					particle.setIgnoreMaxAge(true);
					if (getOverpowered()){
						particle.setRGBColorF(1.0f, 0.2f, 0.2f);
					}
				}
			}
		}
	}

	private void updateBookRender(){
		particleAge++;
		if (increasing){
			particleMaxAge += 2;
			if (particleMaxAge - particleAge > 120)
				increasing = false;
		}else{
			if (particleMaxAge - particleAge < 5)
				increasing = true;
		}

		this.field_145927_n = this.field_145930_m;
		this.field_145925_p = this.field_145928_o;

		this.field_145930_m += 0.1F;

		if (this.field_145930_m < 0.5F || rand.nextInt(40) == 0){
			float f1 = this.field_145932_k;

			do{
				this.field_145932_k += (float)(rand.nextInt(4) - rand.nextInt(4));
			}
			while (f1 == this.field_145932_k);
		}

		while (this.field_145928_o >= (float)Math.PI){
			this.field_145928_o -= ((float)Math.PI * 2F);
		}

		while (this.field_145928_o < -(float)Math.PI){
			this.field_145928_o += ((float)Math.PI * 2F);
		}

		while (this.field_145924_q >= (float)Math.PI){
			this.field_145924_q -= ((float)Math.PI * 2F);
		}

		while (this.field_145924_q < -(float)Math.PI){
			this.field_145924_q += ((float)Math.PI * 2F);
		}

		float f2;

		for (f2 = this.field_145924_q - this.field_145928_o; f2 >= (float)Math.PI; f2 -= ((float)Math.PI * 2F)){
			;
		}

		while (f2 < -(float)Math.PI){
			f2 += ((float)Math.PI * 2F);
		}

		this.field_145928_o += f2 * 0.4F;

		if (this.field_145930_m < 0.0F){
			this.field_145930_m = 0.0F;
		}

		if (this.field_145930_m > 1.0F){
			this.field_145930_m = 1.0F;
		}

		++this.field_145926_a;
		this.field_145931_j = this.field_145933_i;
		float f = (this.field_145932_k - this.field_145933_i) * 0.4F;
		float f3 = 0.2F;

		if (f < -f3){
			f = -f3;
		}

		if (f > f3){
			f = f3;
		}

		this.field_145929_l += (f - this.field_145929_l) * 0.9F;
		this.field_145933_i += this.field_145929_l;

		//TODO:
			/*this.bookSpreadPrev = this.bookSpread;
		this.bookRotationPrev = this.bookRotation2;
		this.bookSpread += 0.1F;

		if (this.bookSpread < 0.5F || rand.nextInt(40) == 0)
		{
			float f = this.field_70373_d;

			do
			{
				this.field_70373_d += rand.nextInt(4) - rand.nextInt(4);
			}
			while (f == this.field_70373_d);
		}

		float f1;

		for (f1 = this.bookRotation - this.bookRotation2; f1 >= (float)Math.PI; f1 -= ((float)Math.PI * 2F))
		{
			;
		}

		while (f1 < -(float)Math.PI)
		{
			f1 += ((float)Math.PI * 2F);
		}

		this.bookRotation2 += f1 * 0.4F;

		if (this.bookSpread < 0.0F)
		{
			this.bookSpread = 0.0F;
		}

		if (this.bookSpread > 1.0F)
		{
			this.bookSpread = 1.0F;
		}

		++this.tickCount;
		this.pageFlipPrev = this.pageFlip;
		float f2 = (this.field_70373_d - this.pageFlip) * 0.4F;
		float f3 = 0.02F;

		if (f2 < -f3)
		{
			f2 = -f3;
		}

		if (f2 > f3)
		{
			f2 = f3;
		}

		this.field_70374_e += (f2 - this.field_70374_e) * 1.1F;
		this.pageFlip += this.field_70374_e;*/
	}

	@Override
	public boolean canUpdate(){
		return worldObj != null && worldObj.isRemote;
	}

	public ItemStack getStack(){
		return stack;
	}

	public boolean setStack(ItemStack stack){
		if (stack == null || getValidItems().contains(stack.getItem())){
			this.stack = stack;
			if (!this.worldObj.isRemote){
				AMDataWriter writer = new AMDataWriter();
				writer.add(xCoord);
				writer.add(yCoord);
				writer.add(zCoord);
				if (stack == null){
					writer.add(false);
				}else{
					writer.add(true);
					writer.add(stack);
				}
				AMNetHandler.INSTANCE.sendPacketToAllClientsNear(worldObj.provider.dimensionId, xCoord, yCoord, zCoord, 32, AMPacketIDs.LECTERN_DATA, writer.generate());
			}
			return true;
		}
		return false;
	}

	public boolean hasStack(){
		return stack != null;
	}

	@Override
	public Packet getDescriptionPacket(){
		NBTTagCompound compound = new NBTTagCompound();
		writeToNBT(compound);
		S35PacketUpdateTileEntity packet = new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, worldObj.getBlockMetadata(xCoord, yCoord, zCoord), compound);
		return packet;
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt){
		this.readFromNBT(pkt.func_148857_g());
	}

	private ArrayList<Item> getValidItems(){
		ArrayList<Item> validItems = new ArrayList<Item>();

		validItems.add(Items.written_book);
		validItems.add(ItemsCommonProxy.arcaneCompendium);

		if (Loader.isModLoaded("Thaumcraft")){
			ItemStack item = thaumcraft.api.ItemApi.getItem("itemThaumonomicon", 0);
			if (item != null){
				validItems.add(item.getItem());
			}
		}

		return validItems;
	}

	@Override
	public void readFromNBT(NBTTagCompound comp){
		super.readFromNBT(comp);
		if (comp.hasKey("placedBook")){
			NBTTagCompound bewk = comp.getCompoundTag("placedBook");
			stack = ItemStack.loadItemStackFromNBT(bewk);
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound comp){
		super.writeToNBT(comp);
		if (stack != null){
			NBTTagCompound bewk = new NBTTagCompound();
			stack.writeToNBT(bewk);
			comp.setTag("placedBook", bewk);
		}
	}

	public void setNeedsBook(boolean b){
		this.needsBook = b;
	}

	public boolean getNeedsBook(){
		return this.needsBook;
	}

	public void setOverpowered(boolean b){
		this.overPowered = b;
	}

	public boolean getOverpowered(){
		return this.overPowered;
	}

}
