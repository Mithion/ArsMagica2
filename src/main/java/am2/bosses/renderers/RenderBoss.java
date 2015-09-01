package am2.bosses.renderers;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.util.ResourceLocation;
import am2.bosses.AM2Boss;

public abstract class RenderBoss extends RenderLiving {

	public RenderBoss(ModelBase model){
		super(model, 0.5f);
	}

	@Override
	protected void renderModel(EntityLivingBase par1EntityLivingBase, float par2, float par3, float par4, float par5, float par6, float par7) {
		AM2Boss boss = (AM2Boss)par1EntityLivingBase;
		if (boss.playerCanSee)
			BossStatus.setBossStatus((AM2Boss)par1EntityLivingBase, true);
		super.renderModel(par1EntityLivingBase, par2, par3, par4, par5, par6, par7);
	}

	@Override
	protected abstract ResourceLocation getEntityTexture(Entity entity);
}
