package am2.entities.models;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;

@SideOnly(Side.CLIENT)
public class ModelFireElemental extends ModelBiped{
	public ModelFireElemental(){
		super();
		this.heldItemRight = 1;
	}
}
