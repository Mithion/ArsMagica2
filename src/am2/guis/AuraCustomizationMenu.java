package am2.guis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.StatCollector;
import am2.AMCore;
import am2.guis.controls.GuiButtonVariableDims;
import am2.guis.controls.GuiSlideControl;
import am2.network.AMDataWriter;
import am2.network.AMNetHandler;
import am2.network.AMPacketIDs;
import am2.particles.AMParticle;
import am2.particles.ParticleController;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class AuraCustomizationMenu extends GuiScreen {

    /** The title string that is displayed in the top-center of the screen. */
    protected String screenTitle = "Beta Particle Customization";

    private GuiButtonVariableDims btnParticleType;
	private GuiButtonVariableDims btnParticleBehaviour;
	private GuiButtonVariableDims btnParticleColorMode;
	private GuiSlideControl sliParticleRed;
	private GuiSlideControl sliParticleGreen;
	private GuiSlideControl sliParticleBlue;
	private GuiSlideControl sliParticleAlpha;
	private GuiSlideControl sliParticleScale;
	private GuiSlideControl sliParticleQuantity;
	private GuiSlideControl sliParticleDelay;
	private GuiSlideControl sliParticleSpeed;

    private GuiButton activeButton;

    private GuiScreen parent;

	public AuraCustomizationMenu()
	{
		this.mc = Minecraft.getMinecraft();
		this.parent = this.mc.currentScreen;
		this.fontRendererObj = Minecraft.getMinecraft().fontRenderer;
		ScaledResolution scaledresolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		this.width = scaledresolution.getScaledWidth();
		this.height = scaledresolution.getScaledHeight();
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void initGui()
	{
		btnParticleType = new GuiButtonVariableDims(10, 50, 40, AMParticle.particleTypes[AMCore.config.getAuraIndex()]);
		btnParticleBehaviour = new GuiButtonVariableDims(11, 50, 60, ParticleController.AuraControllerOptions[AMCore.config.getAuraBehaviour()]);
		btnParticleColorMode = new GuiButtonVariableDims(12, 50, 80, AMCore.config.getAuraColorDefault() ? StatCollector.translateToLocal("am2.gui.default") : AMCore.config.getAuraColorRandom() ? StatCollector.translateToLocal("am2.gui.random") : StatCollector.translateToLocal("am2.gui.custom"));

		btnParticleType.setDimensions(80, 20);
		btnParticleBehaviour.setDimensions(80, 20);
		btnParticleColorMode.setDimensions(80, 20);

		sliParticleScale = new GuiSlideControl(14, width - 110, 40, 100, StatCollector.translateToLocal("am2.gui.scale"), AMCore.config.getAuraScale() * 10, 1f, 200f);
		sliParticleAlpha = new GuiSlideControl(15, width - 110, 60, 100, StatCollector.translateToLocal("am2.gui.alpha"), AMCore.config.getAuraAlpha() * 100, 1f, 100f);
		sliParticleRed = new GuiSlideControl(16, width - 110, 80, 100, StatCollector.translateToLocal("am2.gui.red"), (AMCore.config.getAuraColor() >> 16) & 0xFF, 0f, 255f);
		sliParticleRed.setInteger(true);
		sliParticleGreen = new GuiSlideControl(17, width - 110, 100, 100, StatCollector.translateToLocal("am2.gui.green"), (AMCore.config.getAuraColor() >> 8) & 0xFF, 0f, 255f);
		sliParticleGreen.setInteger(true);
		sliParticleBlue = new GuiSlideControl(18, width - 110, 120, 100, StatCollector.translateToLocal("am2.gui.blue"), AMCore.config.getAuraColor() & 0xFF, 0f, 255f);
		sliParticleBlue.setInteger(true);

		sliParticleQuantity = new GuiSlideControl(20, width - 110, 140, 100, StatCollector.translateToLocal("am2.gui.qty"), AMCore.config.getAuraQuantity(), 1, 5);
		sliParticleQuantity.setInteger(true);

		sliParticleDelay = new GuiSlideControl(21, width - 110, 160, 100, StatCollector.translateToLocal("am2.gui.delay"), AMCore.config.getAuraDelay(), 1, 100);
		sliParticleDelay.setInteger(true);

		sliParticleSpeed = new GuiSlideControl(22, width - 110, 180, 100, StatCollector.translateToLocal("am2.gui.speed"), AMCore.config.getAuraSpeed(), 0.05f, 10.0f);

		if (AMCore.config.getAuraColorDefault() || AMCore.config.getAuraColorRandom()){
			sliParticleRed.enabled = false;
			sliParticleBlue.enabled = false;
			sliParticleGreen.enabled = false;
		}else{
			sliParticleRed.enabled = true;
			sliParticleBlue.enabled = true;
			sliParticleGreen.enabled = true;
		}


		this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height - 28, StatCollector.translateToLocal("am2.gui.done")));

		this.buttonList.add(btnParticleType);
		this.buttonList.add(btnParticleBehaviour);
		this.buttonList.add(btnParticleColorMode);
		this.buttonList.add(sliParticleScale);
		this.buttonList.add(sliParticleAlpha);
		this.buttonList.add(sliParticleRed);
		this.buttonList.add(sliParticleGreen);
		this.buttonList.add(sliParticleBlue);
		this.buttonList.add(sliParticleDelay);
		this.buttonList.add(sliParticleQuantity);
		this.buttonList.add(sliParticleSpeed);
	}

	@Override
	public void drawScreen(int par1, int par2, float par3)
    {
        this.drawDefaultBackground();
        drawCenteredString(fontRendererObj, screenTitle, width / 2, 4, 0xffffff);
		drawString(fontRendererObj, StatCollector.translateToLocal("am2.gui.type"), 10, 45, 0xffffff);
		drawString(fontRendererObj, StatCollector.translateToLocal("am2.gui.action"), 10, 65, 0xffffff);
		drawString(fontRendererObj, StatCollector.translateToLocal("am2.gui.color"), 10, 85, 0xffffff);
		drawString(fontRendererObj, StatCollector.translateToLocal("am2.gui.border"), 10, 105, 0xffffff);
        super.drawScreen(par1, par2, par3);
    }

	@Override
	protected void actionPerformed(GuiButton par1GuiButton) {

		int index = 0;
		boolean flag = false;

		activeButton = par1GuiButton;

		switch (par1GuiButton.id){
		case 10: //particle type
			index = AMCore.config.getAuraIndex();
			index++;
			if (index >= AMParticle.particleTypes.length) index = 0;

			AMCore.config.setAuraIndex(index);
			btnParticleType.displayString = AMParticle.particleTypes[index];
			break;
		case 11: //particle behaviour
			index = AMCore.config.getAuraBehaviour();
			index++;
			if (index >= ParticleController.AuraControllerOptions.length) index = 0;
			AMCore.config.setAuraBehaviour(index);
			btnParticleBehaviour.displayString = ParticleController.AuraControllerOptions[index];
			break;
		case 12: //default color
		case 13: //random color
			if (AMCore.config.getAuraColorDefault()){
				AMCore.config.setAuraColorDefault(false);
				AMCore.config.setAuraColorRandom(true);
				sliParticleRed.enabled = false;
				sliParticleBlue.enabled = false;
				sliParticleGreen.enabled = false;
			}else if (AMCore.config.getAuraColorRandom()){
				AMCore.config.setAuraColorDefault(false);
				AMCore.config.setAuraColorRandom(false);
				sliParticleRed.enabled = true;
				sliParticleBlue.enabled = true;
				sliParticleGreen.enabled = true;
			}else{
				AMCore.config.setAuraColorDefault(true);
				AMCore.config.setAuraColorRandom(false);
				sliParticleRed.enabled = false;
				sliParticleBlue.enabled = false;
				sliParticleGreen.enabled = false;
			}
			btnParticleColorMode.displayString = AMCore.config.getAuraColorDefault() ? "Default" : AMCore.config.getAuraColorRandom() ? StatCollector.translateToLocal("am2.gui.random") : StatCollector.translateToLocal("am2.gui.custom");
			break;
		case 14: //scale
			AMCore.config.setAuraScale(((GuiSlideControl)par1GuiButton).getShiftedValue() / 10f);
			break;
		case 15: //alpha
			AMCore.config.setAuraAlpha(((GuiSlideControl)par1GuiButton).getShiftedValue() / 100f);
			break;
		case 16: //red
		case 17: //green
		case 18: //blue
			int color = ((int)sliParticleRed.getShiftedValue() & 0xFF) << 16 | ((int)sliParticleGreen.getShiftedValue() & 0xFF) << 8 | (int)sliParticleBlue.getShiftedValue() & 0xFF;
			AMCore.config.setAuraColor(color);
			break;
		case 20: //quantity
			AMCore.config.setAuraQuantity((int)sliParticleQuantity.getShiftedValue());
			break;
		case 21: //delay
			AMCore.config.setAuraDelay((int)sliParticleDelay.getShiftedValue());
			break;
		case 22: //speed
			AMCore.config.setAuraSpeed(sliParticleSpeed.getShiftedValue());
			break;
		case 200: //close
			this.mc.displayGuiScreen(this.parent);
			break;
		}
	}

	@Override
	public void onGuiClosed() {

		AMDataWriter writer = new AMDataWriter();

		writer.add(AMCore.config.getAuraIndex());
		writer.add(AMCore.config.getAuraBehaviour());
		writer.add(AMCore.config.getAuraScale());
		writer.add(AMCore.config.getAuraAlpha());
		writer.add(AMCore.config.getAuraColorRandom());
		writer.add(AMCore.config.getAuraColorDefault());
		writer.add(AMCore.config.getAuraColor());
		writer.add(AMCore.config.getAuraDelay());
		writer.add(AMCore.config.getAuraQuantity());
		writer.add(AMCore.config.getAuraSpeed());

		byte[] data = writer.generate();

		AMNetHandler.INSTANCE.sendPacketToServer(AMPacketIDs.SYNC_BETA_PARTICLES, data);

		AMCore.config.save();

		super.onGuiClosed();
	}

	@Override
	protected void mouseMovedOrUp(int par1, int par2, int par3)
    {
		if (activeButton != null && activeButton instanceof GuiSlideControl){
			actionPerformed(activeButton);
		}

		super.mouseMovedOrUp(par1, par2, par3);
    }

	@Override
	protected void mouseClicked(int x, int y, int button) {
		GuiButton clickedBtn = getControlByXY(x, y);
		if (clickedBtn != null && button == 1){
			if (clickedBtn.id == 10){
			int index = AMCore.config.getAuraIndex();
			index--;
			if (index < 0) index = AMParticle.particleTypes.length-1;

			while (AMParticle.particleTypes[index].startsWith("lightning_bolt") && AMCore.proxy.playerTracker.getAAL(Minecraft.getMinecraft().thePlayer) < 3){
				index--;
				if (index < 0) index = AMParticle.particleTypes.length-1;
			}

			AMCore.config.setAuraIndex(index);
			btnParticleType.displayString = AMParticle.particleTypes[index];
			}else if (clickedBtn.id == 11){
				int index = AMCore.config.getAuraBehaviour();
				index--;
				if (index < 0) index = ParticleController.AuraControllerOptions.length - 1;
				AMCore.config.setAuraBehaviour(index);
				btnParticleBehaviour.displayString = ParticleController.AuraControllerOptions[index];
			}
		}
		super.mouseClicked(x, y, button);
	}

	private GuiButton getControlByXY(int x, int y){
		for (Object btn : this.buttonList){
			if (btn instanceof GuiButton){
				GuiButton button = (GuiButton)btn;
				if (button.mousePressed(mc, x, y))
					return button;
			}
		}
		return null;
	}
}


