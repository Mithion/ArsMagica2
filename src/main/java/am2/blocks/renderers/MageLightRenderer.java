package am2.blocks.renderers;

import java.util.Random;

import am2.AMCore;
import am2.texture.ResourceManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

import org.lwjgl.opengl.GL11;


public class MageLightRenderer extends TileEntitySpecialRenderer {

	private RenderManager renderManager; 
	
	public MageLightRenderer() {
		renderManager = RenderManager.instance;
	}

	@Override
	public void renderTileEntityAt(TileEntity var1, double var2, double var4,
			double var6, float var8) {
		
	}

}
