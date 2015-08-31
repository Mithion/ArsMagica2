package am2.entities.renderers;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import am2.entities.EntityHecate;
import am2.entities.models.ModelHecate;
import am2.texture.ResourceManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderHecate extends RenderLiving {

	private ModelHecate model;
	private static final ResourceLocation rLoc = new ResourceLocation("arsmagica2", ResourceManager.getMobTexturePath("mobHecate.png"));


	private float animationTicks;

	public RenderHecate(ModelHecate hecate, float par2) {
		super(hecate, par2);
		model = hecate;
		animationTicks = 0;
	}

	@Override
	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
	{
		doRenderHecate((EntityHecate)par1Entity, par2, par4, par6, par8, par9);
	}

	private void doRenderHecate(EntityHecate hecate, double par2, double par4, double par6, float par8, float par9){
		setForwardRotation(hecate);
		setArmRotations(hecate);
		this.doRender(hecate, par2, par4, par6, par8, par9);
	}

	private void setForwardRotation(EntityHecate hecate){
		model.setMainRotationAngle(hecate.getForwardRotation());
	}

	private void setArmRotations(EntityHecate hecate){
		model.setLeftArmRotationOffset((float) hecate.getLeftArmOffset());
		model.setRightArmRotationOffset((float) hecate.getRightArmOffset());
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return rLoc;
	}

}
