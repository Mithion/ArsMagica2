package am2.entities.models;

import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.model.ModelBiped;

@SideOnly(Side.CLIENT)
public class ModelFireElemental extends ModelBiped {
	public ModelFireElemental(){
		super();
		this.heldItemRight = 1;
	}
}
