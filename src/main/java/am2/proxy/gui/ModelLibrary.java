package am2.proxy.gui;

import am2.bosses.models.ModelPlantGuardianSickle;
import am2.bosses.models.ModelWinterGuardianArm;
import am2.entities.EntityBroom;
import am2.entities.models.ModelBroom;
import am2.models.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;

@SideOnly(Side.CLIENT)
public class ModelLibrary{

	public static final ModelLibrary instance = new ModelLibrary();

	private ModelLibrary(){
		dummyBroom = new EntityBroom(Minecraft.getMinecraft().theWorld);
		sickle.setNoSpin();

		dummyArcaneSpellbook = new ModelArcaneGuardianSpellBook();
		winterGuardianArm = new ModelWinterGuardianArm();
		fireEars = new ModelFireGuardianEars();
		waterOrbs = new ModelWaterGuardianOrbs();
		earthArmor = new ModelEarthGuardianChest();
		airSled = new ModelAirGuardianHoverball();
		wardingCandle = new ModelCandle();
	}

	public final ModelPlantGuardianSickle sickle = new ModelPlantGuardianSickle();

	public final ModelBroom magicBroom = new ModelBroom();
	public final EntityBroom dummyBroom;
	public final ModelArcaneGuardianSpellBook dummyArcaneSpellbook;
	public final ModelWinterGuardianArm winterGuardianArm;

	public final ModelFireGuardianEars fireEars;
	public final ModelWaterGuardianOrbs waterOrbs;
	public final ModelEarthGuardianChest earthArmor;

	public final ModelAirGuardianHoverball airSled;

	public final ModelCandle wardingCandle;
}
