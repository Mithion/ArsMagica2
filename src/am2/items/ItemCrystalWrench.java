package am2.items;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import am2.AMCore;
import am2.api.math.AMVector3;
import am2.api.power.IPowerNode;
import am2.blocks.tileentities.TileEntityCrystalMarker;
import am2.blocks.tileentities.TileEntityFlickerHabitat;
import am2.particles.AMParticle;
import am2.particles.ParticleFadeOut;
import am2.particles.ParticleMoveOnHeading;
import am2.power.PowerNodeRegistry;
import am2.texture.ResourceManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemCrystalWrench extends ArsMagicaRotatedItem {

	@SideOnly(Side.CLIENT)
	private IIcon wrenchStoredIcon;
	
	@SideOnly(Side.CLIENT)
	private IIcon wrenchDisconnectIcon;

	private static String KEY_PAIRLOC = "PAIRLOC";
	private static String HAB_PAIRLOC = "HABLOC";
	private static String KEEP_BINDING = "KEEPBINDING";
	private static String MODE = "WRENCHMODE";
	
	private static final int MODE_PAIR = 0;
	private static final int MODE_DISCONNECT = 1;

	public ItemCrystalWrench() {
		super();
	}

	@Override
	public void registerIcons(IIconRegister par1IconRegister) {
		this.itemIcon = ResourceManager.RegisterTexture("crystal_wrench", par1IconRegister);
		wrenchStoredIcon = ResourceManager.RegisterTexture("crystal_wrench_stored", par1IconRegister);
		wrenchDisconnectIcon = ResourceManager.RegisterTexture("crystal_wrench_disconnect", par1IconRegister);
	}

	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		TileEntity te = world.getTileEntity(x, y, z);

		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		
		int cMode = getMode(stack);		
		if (te != null && !(te instanceof IPowerNode) && cMode == MODE_DISCONNECT){
			player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("am2.tooltip.wrongWrenchMode")));
			return false;
		}

		if (te != null && te instanceof IPowerNode){
			if (cMode == MODE_DISCONNECT){
				doDisconnect((IPowerNode) te, world, x+hitX, y+hitY, z+hitZ, player);
				return false;
			}
			
			if (stack.stackTagCompound.hasKey(KEY_PAIRLOC)){
				doPairNodes(world, x, y, z, stack, player, hitX, hitY, hitZ, te);
			}else{
				storePairLocation(world, te, stack, player, x+hitX, y+hitY, z+hitZ);
			}
		} else if(te != null && te instanceof TileEntityCrystalMarker && stack.stackTagCompound != null && stack.stackTagCompound.hasKey(HAB_PAIRLOC)){
			handleCMPair(stack, world, player, te, x+hitX, y+hitY, z+hitZ);
		} else if(player.isSneaking()){
			handleModeChanges(stack);
			
		}
		return false;
	}
	
	private void handleCMPair(ItemStack stack, World world, EntityPlayer player, TileEntity te, double hitX, double hitY, double hitZ){
		AMVector3 habLocation = AMVector3.readFromNBT(stack.stackTagCompound.getCompoundTag(HAB_PAIRLOC));
		if(world.isRemote){
			spawnLinkParticles(world, hitX, hitY, hitZ);
		} else {
			TileEntityCrystalMarker tecm = (TileEntityCrystalMarker)te;

			tecm.linkToHabitat(habLocation, player);

			if(!stack.stackTagCompound.hasKey(KEEP_BINDING))
				stack.stackTagCompound.removeTag(HAB_PAIRLOC);
		}
	}
	
	private void storePairLocation(World world, TileEntity te, ItemStack stack, EntityPlayer player, double hitX, double hitY, double hitZ){
		AMVector3 destination = new AMVector3(te);
		if (!world.isRemote){
			if(te instanceof TileEntityFlickerHabitat){
				NBTTagCompound habLoc = new NBTTagCompound();
				destination.writeToNBT(habLoc);
				stack.stackTagCompound.setTag(HAB_PAIRLOC, habLoc);
			} else {
				NBTTagCompound pairLoc = new NBTTagCompound();
				destination.writeToNBT(pairLoc);
				stack.stackTagCompound.setTag(KEY_PAIRLOC, pairLoc);
			}

			if(player.isSneaking()){
				stack.stackTagCompound.setBoolean(KEEP_BINDING, true);
			}
		}else{
			spawnLinkParticles(world, hitX, hitY, hitZ);
		}
	}
	
	private void doPairNodes(World world, int x, int y, int z, ItemStack stack, EntityPlayer player, double hitX, double hitY, double hitZ, TileEntity te){
		AMVector3 source = AMVector3.readFromNBT(stack.stackTagCompound.getCompoundTag(KEY_PAIRLOC));
		TileEntity sourceTE = world.getTileEntity((int)source.x, (int)source.y, (int)source.z);
		if (sourceTE != null && sourceTE instanceof IPowerNode && !world.isRemote){
			player.addChatMessage(new ChatComponentText(PowerNodeRegistry.For(world).tryPairNodes((IPowerNode)sourceTE, (IPowerNode)te)));
		}else if (world.isRemote){
			spawnLinkParticles(world, x + hitX, y + hitY, z + hitZ);
		}
		if(!stack.stackTagCompound.hasKey(KEEP_BINDING))
			stack.stackTagCompound.removeTag(KEY_PAIRLOC);
	}
	
	private void doDisconnect(IPowerNode node, World world, double hitX, double hitY, double hitZ, EntityPlayer player){
		PowerNodeRegistry.For(world).tryDisconnectAllNodes(node);
		if (world.isRemote){
			spawnLinkParticles(player.worldObj, hitX, hitY, hitZ, true);
		}else{
			player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("am2.tooltip.disconnectPower")));
		}
	}
	
	private void handleModeChanges(ItemStack stack){
		if (stack.stackTagCompound.hasKey(KEEP_BINDING)){
			stack.stackTagCompound.removeTag(KEEP_BINDING);

			if(stack.stackTagCompound.hasKey(KEY_PAIRLOC))
				stack.stackTagCompound.removeTag(KEY_PAIRLOC);

			if(stack.stackTagCompound.hasKey(HAB_PAIRLOC))
				stack.stackTagCompound.removeTag(HAB_PAIRLOC);
		}else{				
			if (getMode(stack) == MODE_PAIR)
				stack.stackTagCompound.setInteger(MODE, MODE_DISCONNECT);
			else
				stack.stackTagCompound.setInteger(MODE, MODE_PAIR);
				
		}
	}
	
	private int getMode(ItemStack stack){
		if (stack.stackTagCompound.hasKey(MODE)){
			return stack.stackTagCompound.getInteger(MODE);
		}
		return 0;
	}

	private void spawnLinkParticles(World world, double hitX, double hitY, double hitZ){
		spawnLinkParticles(world, hitX, hitY, hitZ, false);
	}
	
	private void spawnLinkParticles(World world, double hitX, double hitY, double hitZ, boolean disconnect){
		for (int i = 0; i < 10; ++i){
			AMParticle particle = (AMParticle) AMCore.proxy.particleManager.spawn(world, "none_hand", hitX, hitY, hitZ);
			if (particle != null){
				if (disconnect){
					particle.setRGBColorF(1, 0, 0);
					particle.addRandomOffset(0.5f, 0.5f, 0.5f);
				}
				particle.setMaxAge(10);
				particle.setParticleScale(0.1f);
				particle.AddParticleController(new ParticleMoveOnHeading(particle, world.rand.nextInt(360), world.rand.nextInt(360), world.rand.nextDouble() * 0.2, 1, false));
				particle.AddParticleController(new ParticleFadeOut(particle, 1, false).setFadeSpeed(0.1f));
			}
		}
	}

	@Override
	public boolean getShareTag() {
		return true;
	}

	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		return GetWrenchIcon(stack, pass);
	}

	@Override
	public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
		return GetWrenchIcon(stack, renderPass);
	}

	@Override
	public IIcon getIconIndex(ItemStack par1ItemStack) {
		return GetWrenchIcon(par1ItemStack, 0);
	}

	private IIcon GetWrenchIcon(ItemStack stack, int pass){
		if(stack.stackTagCompound != null && pass == 0){
			if (stack.stackTagCompound.hasKey(KEEP_BINDING))
				return wrenchStoredIcon;
			else if (stack.stackTagCompound.hasKey(MODE) && stack.stackTagCompound.getInteger(MODE) == MODE_DISCONNECT)
				return wrenchDisconnectIcon;
			else
				return this.itemIcon;
		} else {
			return this.itemIcon;
		}
	}

	@Override
	public int getItemStackLimit() {
		return 1;
	}

}
