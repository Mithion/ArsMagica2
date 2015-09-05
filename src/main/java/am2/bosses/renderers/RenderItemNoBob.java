package am2.bosses.renderers;

import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;

public class RenderItemNoBob extends RenderItem{

	public RenderItemNoBob(){
		this.renderManager = RenderManager.instance;
	}

	@Override
	public boolean shouldBob(){
		return false;
	}
}
