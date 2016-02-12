package am2.items.renderers;

import am2.api.spell.ItemSpellBase;
import am2.api.spell.enums.Affinity;
import am2.items.ItemSpellBook;
import am2.particles.AMParticleIcons;
import am2.spell.SpellUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.HashMap;
import java.util.Map;

public class SpellScrollRenderer implements IItemRenderer{

	private static RenderItem renderItem = new RenderItem();
	private final ModelBiped modelBipedMain;
	private final Minecraft mc;
	private final Map<Affinity, IIcon> icons;
	private boolean setupIcons = false;
	private static final ResourceLocation rLoc = new ResourceLocation("textures/atlas/items.png");

	public static SpellScrollRenderer instance = new SpellScrollRenderer();

	private SpellScrollRenderer(){
		modelBipedMain = new ModelBiped(0.0F);
		mc = Minecraft.getMinecraft();
		icons = new HashMap<Affinity, IIcon>();
	}

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type){
		if (type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON){
			if (Minecraft.getMinecraft().inGameHasFocus){
				if (item.getItem() instanceof ItemSpellBase || (item.getItem() instanceof ItemSpellBook && ((ItemSpellBook)item.getItem()).GetActiveScroll(item) != null)){
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper){
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data){

		if (mc.thePlayer.isPotionActive(Potion.invisibility.id)) return;

		ItemStack scrollStack = null;
		if (item.getItem() instanceof ItemSpellBase){
			scrollStack = item;
		}else if (item.getItem() instanceof ItemSpellBook){
			scrollStack = ((ItemSpellBook)item.getItem()).getActiveScrollInventory(item)[((ItemSpellBook)item.getItem()).GetActiveSlot(item)];
		}


		if (scrollStack == null) return;

		Affinity affinity = SpellUtils.instance.mainAffinityFor(scrollStack);

		renderEffect(affinity, true, data);
	}

	public void renderEffect(Affinity affinity, boolean includeArm, Object... data){

		if (!setupIcons){
			setupAffinityIcons();
			setupIcons = true;
		}

		RenderBlocks renderer = (RenderBlocks)data[0];

		EntityLivingBase entity = (EntityLivingBase)data[1];
		GL11.glPushMatrix();
		GL11.glEnable(32826 /*GL_RESCALE_NORMAL_EXT*/);
		GL11.glEnable(3042);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		float scale = 3f;
		if (entity == mc.thePlayer && Minecraft.getMinecraft().gameSettings.thirdPersonView == 0){

			GL11.glPushMatrix();

			if (((EntityPlayer)entity).getItemInUseCount() > 0){
				GL11.glRotatef(120, 1, 0, 1);
				GL11.glRotatef(-10, 0, 1, 0);
				GL11.glTranslatef(2f, 0f, 0.8f);
			}else{
				GL11.glTranslatef(3f, -1.2f, -2.5f);
				GL11.glScalef(scale, scale, scale);
				GL11.glRotatef(-45, 0, 1, 0);
			}
			RenderHelper.disableStandardItemLighting();
			GL11.glTranslatef(0.6f, 0.0f, -0.4f);
			RenderByAffinity(affinity);
			RenderHelper.enableStandardItemLighting();
			GL11.glPopMatrix();

			if (includeArm){
				if (entity instanceof EntityPlayer && ((EntityPlayer)entity).getItemInUseCount() > 0){
					GL11.glRotatef(-130, 0, 1, 0);
					GL11.glTranslatef(-1f, 0.2f, -2.5f);
				}

				GL11.glScalef(scale, scale, scale);
				GL11.glTranslatef(0f, 0.6f, 1.1f);

				Minecraft.getMinecraft().renderEngine.bindTexture(mc.thePlayer.getLocationSkin());
				renderFirstPersonArm(mc.thePlayer);
			}
		}else{
			if (entity instanceof EntityPlayer && ((EntityPlayer)entity).getItemInUseCount() > 0){
				GL11.glTranslatef(0.0f, 0.0f, 1.0f);
			}else{
				GL11.glTranslatef(0.0f, 0.0f, 1.6f);
			}
			scale = 1.5f;
			GL11.glScalef(scale, scale, scale);
			GL11.glRotatef(45, 0, 1, 0);
			RenderByAffinity(affinity);
		}

		GL11.glDisable(32826 /*GL_RESCALE_NORMAL_EXT*/);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(3042);

		GL11.glPopMatrix();
	}

	private void setupAffinityIcons(){
		icons.put(Affinity.AIR, AMParticleIcons.instance.getIconByName("air_hand"));
		icons.put(Affinity.ARCANE, AMParticleIcons.instance.getIconByName("arcane_hand"));
		icons.put(Affinity.EARTH, AMParticleIcons.instance.getIconByName("earth_hand"));
		icons.put(Affinity.ENDER, AMParticleIcons.instance.getIconByName("ender_hand"));
		icons.put(Affinity.FIRE, AMParticleIcons.instance.getIconByName("fire_hand"));
		icons.put(Affinity.ICE, AMParticleIcons.instance.getIconByName("ice_hand"));
		icons.put(Affinity.LIFE, AMParticleIcons.instance.getIconByName("life_hand"));
		icons.put(Affinity.LIGHTNING, AMParticleIcons.instance.getIconByName("lightning_hand"));
		icons.put(Affinity.NATURE, AMParticleIcons.instance.getIconByName("nature_hand"));
		icons.put(Affinity.NONE, AMParticleIcons.instance.getIconByName("none_hand"));
		icons.put(Affinity.WATER, AMParticleIcons.instance.getIconByName("water_hand"));
	}

	public void RenderByAffinity(Affinity affinity){

		Minecraft.getMinecraft().renderEngine.bindTexture(rLoc);

		IIcon icon = icons.get(affinity);
		if (icon == null) return;

		float TLX = icon.getMinU();
		float BRX = icon.getMaxU();
		float TLY = icon.getMinV();
		float BRY = icon.getMaxV();

		doRender(TLX, TLY, BRX, BRY);
	}


	private void doRender(float TLX, float TLY, float BRX, float BRY){
		//ItemRenderer.renderItemIn2D(Tessellator.instance, TLX, TLY, BRX, BRY, 1, 1, 0.0625F);
		Tessellator t = Tessellator.getInstance();
		t.getWorldRenderer().startDrawingQuads();
		t.getWorldRenderer().setNormal(0.0F, 0.0F, 1.0F);
		t.getWorldRenderer().addVertexWithUV(0.0D, 0.0D, 0.0D, TLX, BRY);
		t.getWorldRenderer().addVertexWithUV(1.0D, 0.0D, 0.0D, BRX, BRY);
		t.getWorldRenderer().addVertexWithUV(1.0D, 1.0D, 0.0D, BRX, TLY);
		t.getWorldRenderer().addVertexWithUV(0.0D, 1.0D, 0.0D, TLX, TLY);
		t.draw();
	}

	private void renderFirstPersonArm(EntityClientPlayerMP player){

		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		float par1 = 0.5f;

		float f1 = 1.0F;
		EntityPlayerSP entityclientplayermp = this.mc.thePlayer;
		float f2 = entityclientplayermp.prevRotationPitch + (entityclientplayermp.rotationPitch - entityclientplayermp.prevRotationPitch) * par1;
		GL11.glPushMatrix();
		GL11.glRotatef(f2, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(entityclientplayermp.prevRotationYaw + (entityclientplayermp.rotationYaw - entityclientplayermp.prevRotationYaw) * par1, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glPopMatrix();
		EntityPlayerSP entityplayersp = entityclientplayermp;
		float f3 = entityplayersp.prevRenderArmPitch + (entityplayersp.renderArmPitch - entityplayersp.prevRenderArmPitch) * par1;
		float f4 = entityplayersp.prevRenderArmYaw + (entityplayersp.renderArmYaw - entityplayersp.prevRenderArmYaw) * par1;
		GL11.glRotatef((entityclientplayermp.rotationPitch - f3) * 0.1F, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef((entityclientplayermp.rotationYaw - f4) * 0.1F, 0.0F, 1.0F, 0.0F);
		float f5 = this.mc.theWorld.getLightBrightness(MathHelper.floor_double(entityclientplayermp.posX), MathHelper.floor_double(entityclientplayermp.posY), MathHelper.floor_double(entityclientplayermp.posZ));
		f5 = 1.0F;
		int i = this.mc.theWorld.getLightBrightnessForSkyBlocks(MathHelper.floor_double(entityclientplayermp.posX), MathHelper.floor_double(entityclientplayermp.posY), MathHelper.floor_double(entityclientplayermp.posZ), 0);
		int j = i % 65536;
		int k = i / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j / 1.0F, k / 1.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		float f6;
		float f7;
		float f8;

		GL11.glPushMatrix();
		float f12 = 0.8F;
		f7 = entityclientplayermp.getSwingProgress(par1);
		f8 = MathHelper.sin(f7 * (float)Math.PI);
		f6 = MathHelper.sin(MathHelper.sqrt_float(f7) * (float)Math.PI);
		GL11.glTranslatef(-f6 * 0.3F, MathHelper.sin(MathHelper.sqrt_float(f7) * (float)Math.PI * 2.0F) * 0.4F, -f8 * 0.4F);
		GL11.glTranslatef(0.8F * f12, -0.75F * f12 - (1.0F - f1) * 0.6F, -0.9F * f12);
		GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		f7 = entityclientplayermp.getSwingProgress(par1);
		f8 = MathHelper.sin(f7 * f7 * (float)Math.PI);
		f6 = MathHelper.sin(MathHelper.sqrt_float(f7) * (float)Math.PI);
		GL11.glRotatef(f6 * 70.0F, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-f8 * 20.0F, 0.0F, 0.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(entityclientplayermp.getLocationSkin());
		GL11.glTranslatef(-1.0F, 3.6F, 3.5F);
		GL11.glRotatef(120.0F, 0.0F, 0.0F, 1.0F);
		GL11.glRotatef(200.0F, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
		GL11.glScalef(1.0F, 1.0F, 1.0F);
		GL11.glTranslatef(5.6F, 0.0F, 0.0F);

		float f = 1.0F;
		GL11.glColor3f(f, f, f);
		this.modelBipedMain.onGround = 0.0F;
		this.modelBipedMain.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, player);
		this.modelBipedMain.bipedRightArm.render(0.0625F);

		GL11.glPopMatrix();

	}
}
