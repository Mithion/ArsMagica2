package am2.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import am2.render3d.OBJModel;
import am2.texture.ResourceManager;

public class ModelEssenceConduit extends ModelBase
{
	//fields
	ModelRenderer Base;
	ModelRenderer Pedestal;
	ModelRenderer Crystal1;
	ModelRenderer Border3;
	ModelRenderer Border4;
	private IModelCustom crystalmodel;

	public ModelEssenceConduit()
	{
		textureWidth = 64;
		textureHeight = 32;

		Base = new ModelRenderer(this, 0, 0);
		Base.addBox(0F, 0F, 0F, 12, 2, 12);
		Base.setRotationPoint(-6F, 23F, -6F);
		Base.setTextureSize(64, 32);
		Base.mirror = true;
		setRotation(Base, 0F, 0F, 0F);
		Pedestal = new ModelRenderer(this, 0, 18);
		Pedestal.addBox(0F, 0F, 0F, 8, 5, 8);
		Pedestal.setRotationPoint(-4F, 18F, -4F);
		Pedestal.setTextureSize(64, 32);
		Pedestal.mirror = true;
		setRotation(Pedestal, 0F, 0F, 0F);
		Crystal1 = new ModelRenderer(this, 38, 24);
		Crystal1.addBox(-2F, -2F, -2F, 4, 4, 4);
		Crystal1.setRotationPoint(0F, 14F, 0F);
		Crystal1.setTextureSize(64, 32);
		Crystal1.mirror = true;
		setRotation(Crystal1, 0.7991336F, 1.147683F, 0.5240009F);
		Border3 = new ModelRenderer(this, 56, 7);
		Border3.addBox(0F, 0F, 0F, 1, 7, 1);
		Border3.setRotationPoint(-4F, 11F, 3F);
		Border3.setTextureSize(64, 32);
		Border3.mirror = true;
		setRotation(Border3, 0F, 0F, 0F);
		Border4 = new ModelRenderer(this, 49, 1);
		Border4.addBox(0F, 0F, 0F, 1, 7, 1);
		Border4.setRotationPoint(3F, 11F, -4F);
		Border4.setTextureSize(64, 32);
		Border4.mirror = true;
		setRotation(Border4, 0F, 0F, 0F);

		ResourceLocation path = ResourceManager.getOBJFilePath("crystal.obj");
		crystalmodel = AdvancedModelLoader.loadModel(path);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		Base.render(f5);
		Pedestal.render(f5);
		Crystal1.render(f5);
		Border3.render(f5);
		Border4.render(f5);
	}

	public void renderModel(float rotationX, float rotationY, float rotationZ, float defecitPercent, float f5)
	{
		Base.render(f5);
		Pedestal.render(f5);

		Crystal1.rotateAngleX = rotationX;
		Crystal1.rotateAngleY = rotationY;
		Crystal1.rotateAngleZ = rotationZ;

		Border3.render(f5);
		Border4.render(f5);

		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glColor3f(0.906f* defecitPercent, 1 - 0.894f * defecitPercent, 1 - 1f * defecitPercent);
		GL11.glTranslatef(0f, 1f, 0f);
		GL11.glScalef(5f, -5f, 5f);
		GL11.glRotatef(rotationZ, 0, 1, 0);
		try{
		crystalmodel.renderAll();
		}catch(Throwable t){}
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();

		/* GL11.glPushMatrix();
    GL11.glColor3f(0.906f* defecitPercent, 1 - 0.894f * defecitPercent, 1 - 1f * defecitPercent);
    Crystal1.render(f5);
    GL11.glPopMatrix();*/
	}

	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

}
