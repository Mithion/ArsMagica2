package am2.blocks.tileentities;

import java.util.ArrayList;
import java.util.Iterator;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import am2.AMCore;
import am2.api.math.AMVector3;
import am2.blocks.BlocksCommonProxy;
import am2.particles.AMParticle;
import am2.particles.ParticleFadeOut;
import am2.particles.ParticleFloatUpward;
import am2.utility.InventoryUtilities;

public class TileEntityCrystalMarkerSpellExport extends TileEntityCrystalMarker{
	static final int RESCAN_INTERVAL = 600;
	static final int UPDATE_INTERVAL = 100;
	
	ArrayList<AMVector3> craftingAltarCache;
	int updateCounter = 0;
	
	public TileEntityCrystalMarkerSpellExport(){
		this(0);
	}
	
	public TileEntityCrystalMarkerSpellExport(int type){
		super(type);
		craftingAltarCache = new ArrayList<AMVector3>();
	}
	
	@Override
	public boolean canUpdate() {
		return true;
	}
	
	@Override
	public void updateEntity() {
		super.updateEntity();
		if (this.updateCounter % RESCAN_INTERVAL == 0){
			scanForCraftingAltars();
		}
		
		if (this.updateCounter % UPDATE_INTERVAL == 0){			
			if (updateFilter() && worldObj.isRemote){
				spawnParticles();
			}
		}			
		this.updateCounter++;
	}
	
	private void spawnParticles(){
		for (int i = 0; i < 15; ++i){
			AMParticle effect = (AMParticle) AMCore.proxy.particleManager.spawn(worldObj, "sparkle2", xCoord, yCoord, zCoord);
			if (effect != null){
				effect.AddParticleController(new ParticleFloatUpward(effect, 0, worldObj.rand.nextFloat() * 0.1f, 1, false));
				effect.AddParticleController(new ParticleFadeOut(effect, 2, false).setFadeSpeed(0.035f).setKillParticleOnFinish(true));
				effect.addRandomOffset(0.2, 0.2, 0.2);
				effect.setRGBColorF(0, 0.5f, 1.0f);
				effect.setIgnoreMaxAge(true);
			}
		}
	}
	
	private void scanForCraftingAltars(){
		craftingAltarCache.clear();
		for (int i = -10; i <= 10; ++i){
			for (int j = -10; j <= 10; ++j){
				for (int k = -10; k <= 10; ++k){
					if (i == 0 && j == 0 && k == 0)
						continue;
					
					Block block = this.worldObj.getBlock(this.xCoord + i, this.yCoord + j, this.zCoord + k);
					if (block == BlocksCommonProxy.craftingAltar){
						craftingAltarCache.add(new AMVector3(xCoord + i, yCoord + j, zCoord + k));
					}
				}
			}
		}
	}

	private boolean updateFilter(){
		ArrayList<ItemStack> filter = new ArrayList<ItemStack>();
		Iterator it = this.craftingAltarCache.iterator();
		boolean changed = false;
		while (it.hasNext()){
			TileEntityCraftingAltar altar = getCATE((AMVector3) it.next());
			if (altar == null){
				it.remove();
				continue;
			}			
			if (altar.isCrafting()){
				filter.add(altar.getNextPlannedItem());
				changed = true;
			}
		}
		
		this.filterItems = filter.toArray(new ItemStack[filter.size()]);
		return changed;
	}
	
	private TileEntityCraftingAltar getCATE(AMVector3 vec){
		TileEntity te = this.worldObj.getTileEntity((int)vec.x, (int)vec.y, (int)vec.z);
		if (te != null && te instanceof TileEntityCraftingAltar)
			return (TileEntityCraftingAltar)te;
		
		return null;
	}
}
