package am2.particles;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Iterator;

public class ParticleRenderer{

	public static String name = "am2-particle";
	private final ArrayList<EntityFX> particles;
	private final ArrayList<EntityFX> blocks;
	private final ArrayList<EntityFX> radiants;
	private final ArrayList<AMLineArc> arcs;

	private final ArrayList<EntityFX> deferredParticles;
	private final ArrayList<EntityFX> deferredBlocks;
	private final ArrayList<EntityFX> deferredRadiants;
	private final ArrayList<AMLineArc> deferredArcs;

	public ParticleRenderer(){
		MinecraftForge.EVENT_BUS.register(this);
		particles = new ArrayList<EntityFX>();
		radiants = new ArrayList<EntityFX>();
		arcs = new ArrayList<AMLineArc>();
		blocks = new ArrayList<EntityFX>();

		deferredParticles = new ArrayList<EntityFX>();
		deferredRadiants = new ArrayList<EntityFX>();
		deferredArcs = new ArrayList<AMLineArc>();
		deferredBlocks = new ArrayList<EntityFX>();
	}

	public void addEffect(EntityFX effect){
		if (effect instanceof AMParticle){
			addAMParticle((AMParticle)effect);
		}else if (effect instanceof AMLineArc){
			addArcEffect((AMLineArc)effect);
		}else{
			deferredParticles.add(effect);
		}
	}

	public void addAMParticle(AMParticle particle){
		if (particle.isRadiant())
			deferredRadiants.add(particle);
		else if (particle.isBlockTexture())
			deferredBlocks.add(particle);
		else
			deferredParticles.add(particle);
	}

	public void addArcEffect(AMLineArc arc){
		deferredArcs.add(arc);
	}

	@SubscribeEvent
	public void onRenderWorldLast(RenderWorldLastEvent event){
		render(event.partialTicks);
	}

	@SubscribeEvent
	public void onWorldUnload(WorldEvent.Unload event){
		particles.clear();
		radiants.clear();
		arcs.clear();
		blocks.clear();
		deferredParticles.clear();
		deferredRadiants.clear();
		deferredArcs.clear();
		deferredBlocks.clear();
	}

	@SubscribeEvent
	public void onTickEnd(TickEvent.ClientTickEvent event){
		if (event.phase == TickEvent.Phase.END){
			updateParticles();
		}
	}

	private void updateParticles(){
		Minecraft.getMinecraft().mcProfiler.startSection(name + "-update");

		particles.addAll(deferredParticles);
		deferredParticles.clear();

		radiants.addAll(deferredRadiants);
		deferredRadiants.clear();

		arcs.addAll(deferredArcs);
		deferredArcs.clear();

		blocks.addAll(deferredBlocks);
		deferredBlocks.clear();

		for (Iterator<EntityFX> it = particles.iterator(); it.hasNext(); ){
			EntityFX particle = it.next();

			particle.onUpdate();

			if (particle.isDead){
				it.remove();
			}
		}

		for (Iterator<EntityFX> it = radiants.iterator(); it.hasNext(); ){
			EntityFX particle = it.next();

			particle.onUpdate();

			if (particle.isDead){
				it.remove();
			}
		}

		for (Iterator<AMLineArc> it = arcs.iterator(); it.hasNext(); ){
			AMLineArc particle = it.next();

			particle.onUpdate();

			if (particle.isDead){
				it.remove();
			}
		}

		for (Iterator<EntityFX> it = blocks.iterator(); it.hasNext(); ){
			EntityFX particle = it.next();

			particle.onUpdate();

			if (particle.isDead){
				it.remove();
			}
		}

		Minecraft.getMinecraft().mcProfiler.endSection();
	}

	private void render(float partialTicks){
		Minecraft.getMinecraft().mcProfiler.startSection(name + "-render");

		EntityLivingBase player = Minecraft.getMinecraft().renderViewEntity;
		EntityFX.interpPosX = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
		EntityFX.interpPosY = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
		EntityFX.interpPosZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;

		// bind the texture
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);

		//============================================================================================
		// Standard Particles Using the Block Atlas
		//============================================================================================
		renderBlockParticles(partialTicks);

		// bind the texture
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);

		//============================================================================================
		// Standard Particles
		//============================================================================================
		renderStandardParticles(partialTicks);
		//============================================================================================
		// Radiant Particles
		//============================================================================================
		renderRadiants(partialTicks);
		//============================================================================================
		// Arc Particles
		//============================================================================================
		renderArcs(partialTicks);
		//============================================================================================
		// End
		//============================================================================================

		Minecraft.getMinecraft().mcProfiler.endSection();
	}

	private void renderStandardParticles(float partialTicks){
		float rotationX = ActiveRenderInfo.rotationX;
		float rotationZ = ActiveRenderInfo.rotationZ;
		float rotationYZ = ActiveRenderInfo.rotationYZ;
		float rotationXY = ActiveRenderInfo.rotationXY;
		float rotationXZ = ActiveRenderInfo.rotationXZ;

		// save the old gl state
		GL11.glPushAttrib(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		// gl states/settings for drawing
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDepthMask(false);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569F);
		GL11.glDisable(32826 /*GL_RESCALE_NORMAL_EXT*/);

		//GL11.glRotatef(180F - RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
		//GL11.glRotatef(-RenderManager.instance.playerViewX, 1.0F, 0.0F, 0.0F);

		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();

		for (EntityFX particle : particles){
			tessellator.setBrightness(particle.getBrightnessForRender(partialTicks));

			particle.renderParticle(tessellator, partialTicks, rotationX, rotationXZ, rotationZ, rotationYZ, rotationXY);
		}

		tessellator.draw();

		// restore previous gl state
		GL11.glPopAttrib();
	}

	private void renderBlockParticles(float partialTicks){
		float rotationX = ActiveRenderInfo.rotationX;
		float rotationZ = ActiveRenderInfo.rotationZ;
		float rotationYZ = ActiveRenderInfo.rotationYZ;
		float rotationXY = ActiveRenderInfo.rotationXY;
		float rotationXZ = ActiveRenderInfo.rotationXZ;

		// save the old gl state
		GL11.glPushAttrib(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		// gl states/settings for drawing
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDepthMask(false);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569F);
		GL11.glDisable(32826 /*GL_RESCALE_NORMAL_EXT*/);

		//GL11.glRotatef(180F - RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
		//GL11.glRotatef(-RenderManager.instance.playerViewX, 1.0F, 0.0F, 0.0F);

		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();

		for (EntityFX particle : blocks){
			tessellator.setBrightness(particle.getBrightnessForRender(partialTicks));

			particle.renderParticle(tessellator, partialTicks, rotationX, rotationXZ, rotationZ, rotationYZ, rotationXY);
		}

		tessellator.draw();

		// restore previous gl state
		GL11.glPopAttrib();
	}

	private void renderRadiants(float partialTicks){
		float rotationX = ActiveRenderInfo.rotationX;
		float rotationZ = ActiveRenderInfo.rotationZ;
		float rotationYZ = ActiveRenderInfo.rotationYZ;
		float rotationXY = ActiveRenderInfo.rotationXY;
		float rotationXZ = ActiveRenderInfo.rotationXZ;

		// save the old gl state
		GL11.glPushAttrib(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		// gl states/settings for drawing
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glDepthMask(false);
		Tessellator tessellator = Tessellator.instance;

		for (EntityFX particle : radiants){
			tessellator.setBrightness(particle.getBrightnessForRender(partialTicks));

			particle.renderParticle(tessellator, partialTicks, rotationX, rotationXZ, rotationZ, rotationYZ, rotationXY);
		}

		// restore previous gl state
		GL11.glPopAttrib();

		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
	}

	private void renderArcs(float partialTicks){
		float rotationX = ActiveRenderInfo.rotationX;
		float rotationZ = ActiveRenderInfo.rotationZ;
		float rotationYZ = ActiveRenderInfo.rotationYZ;
		float rotationXY = ActiveRenderInfo.rotationXY;
		float rotationXZ = ActiveRenderInfo.rotationXZ;

		Tessellator tessellator = Tessellator.instance;

		GL11.glDepthMask(false);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 1);
		GL11.glDisable(2884);//2884

		for (EntityFX particle : arcs){
			tessellator.setBrightness(15728864);

			particle.renderParticle(tessellator, partialTicks, rotationX, rotationXZ, rotationZ, rotationYZ, rotationXY);
		}

		GL11.glEnable(2884);
		GL11.glDisable(3042);
		GL11.glDepthMask(true);
	}
}
