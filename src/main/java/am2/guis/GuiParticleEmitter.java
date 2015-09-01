package am2.guis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.StatCollector;
import am2.blocks.tileentities.TileEntityParticleEmitter;
import am2.guis.controls.GuiButtonVariableDims;
import am2.guis.controls.GuiSlideControl;
import am2.particles.AMParticle;
import am2.particles.ParticleController;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiParticleEmitter extends GuiScreen {

	/** The title string that is displayed in the top-center of the screen. */
	protected String screenTitle = StatCollector.translateToLocal("am2.gui.particleEmitter");

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

	private GuiButtonVariableDims hideBlock;

	private final TileEntityParticleEmitter tile;

	private final GuiScreen parent;

	public GuiParticleEmitter(TileEntityParticleEmitter target)
	{
		this.mc = Minecraft.getMinecraft();
		this.parent = this.mc.currentScreen;
		this.fontRendererObj = Minecraft.getMinecraft().fontRenderer;
		ScaledResolution scaledresolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		this.width = scaledresolution.getScaledWidth();
		this.height = scaledresolution.getScaledHeight();
		tile = target;
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void initGui()
	{

		btnParticleType = new GuiButtonVariableDims(10, 50, 40, AMParticle.particleTypes[tile.getParticleType()]);
		btnParticleBehaviour = new GuiButtonVariableDims(11, 50, 60, ParticleController.AuraControllerOptions[tile.getParticleBehaviour()]);
		btnParticleColorMode = new GuiButtonVariableDims(12, 50, 80, tile.getColorDefault() ? StatCollector.translateToLocal("am2.gui.default") : tile.getColorRandom() ? StatCollector.translateToLocal("am2.gui.random") : StatCollector.translateToLocal("am2.gui.custom"));
		hideBlock = new GuiButtonVariableDims(19, 50, 100, tile.getShow() ? StatCollector.translateToLocal("am2.gui.visible") : StatCollector.translateToLocal("am2.gui.hidden"));

		btnParticleType.setDimensions(80, 20);
		btnParticleBehaviour.setDimensions(80, 20);
		btnParticleColorMode.setDimensions(80, 20);
		hideBlock.setDimensions(80, 20);

		sliParticleScale = new GuiSlideControl(14, width - 110, 40, 100, StatCollector.translateToLocal("am2.gui.scale"), tile.getScale() * 100, 1f, 200f);
		sliParticleAlpha = new GuiSlideControl(15, width - 110, 60, 100, StatCollector.translateToLocal("am2.gui.alpha"), tile.getAlpha() * 100, 1f, 100f);
		sliParticleRed = new GuiSlideControl(16, width - 110, 80, 100, StatCollector.translateToLocal("am2.gui.red"), (tile.getColor() >> 16) & 0xFF, 0f, 255f);
		sliParticleRed.setInteger(true);
		sliParticleGreen = new GuiSlideControl(17, width - 110, 100, 100, StatCollector.translateToLocal("am2.gui.green"), (tile.getColor() >> 8) & 0xFF, 0f, 255f);
		sliParticleGreen.setInteger(true);
		sliParticleBlue = new GuiSlideControl(18, width - 110, 120, 100, StatCollector.translateToLocal("am2.gui.blue"), tile.getColor() & 0xFF, 0f, 255f);
		sliParticleBlue.setInteger(true);

		sliParticleQuantity = new GuiSlideControl(20, width - 110, 140, 100, StatCollector.translateToLocal("am2.gui.qty"), tile.getQuantity(), 1, 5);
		sliParticleQuantity.setInteger(true);

		sliParticleDelay = new GuiSlideControl(21, width - 110, 160, 100, StatCollector.translateToLocal("am2.gui.delay"), tile.getDelay(), 1, 100);
		sliParticleDelay.setInteger(true);

		sliParticleSpeed = new GuiSlideControl(22, width - 110, 180, 100, StatCollector.translateToLocal("am2.gui.speed"), 1.0f, 0.25f, 10.0f);

		if (tile.getColorDefault() || tile.getColorRandom()){
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
		this.buttonList.add(hideBlock);
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

		if (!tile.getShow()){
			fontRendererObj.drawSplitString(StatCollector.translateToLocal("am2.gui.wrenchWarning"), 10, 125, 100, 0xff0000);
		}

		super.drawScreen(par1, par2, par3);
	}

	@Override
	protected void actionPerformed(GuiButton par1GuiButton) {

		int index = 0;
		boolean flag = false;

		activeButton = par1GuiButton;

		switch (par1GuiButton.id){
		case 10: //particle type
			index = tile.getParticleType();
			index++;
			if (index >= AMParticle.particleTypes.length) index = 0;

			tile.setParticleType(index);
			btnParticleType.displayString = AMParticle.particleTypes[index];
			break;
		case 11: //particle behaviour
			index = tile.getParticleBehaviour();
			index++;
			if (index >= ParticleController.AuraControllerOptions.length) index = 0;
			tile.setParticleBehaviour(index);
			btnParticleBehaviour.displayString = ParticleController.AuraControllerOptions[index];
			break;
		case 12: //default color
		case 13: //random color
			if (tile.getColorDefault()){
				tile.setColorDefault(false);
				tile.setColorRandom(true);
				sliParticleRed.enabled = false;
				sliParticleBlue.enabled = false;
				sliParticleGreen.enabled = false;
			}else if (tile.getColorRandom()){
				tile.setColorDefault(false);
				tile.setColorRandom(false);
				sliParticleRed.enabled = true;
				sliParticleBlue.enabled = true;
				sliParticleGreen.enabled = true;
			}else{
				tile.setColorDefault(true);
				tile.setColorRandom(false);
				sliParticleRed.enabled = false;
				sliParticleBlue.enabled = false;
				sliParticleGreen.enabled = false;
			}
			btnParticleColorMode.displayString = tile.getColorDefault() ? "Default" : tile.getColorRandom() ? StatCollector.translateToLocal("am2.gui.random") : StatCollector.translateToLocal("am2.gui.custom");
			break;
		case 14: //scale
			tile.setScale(((GuiSlideControl)par1GuiButton).getShiftedValue() / 100);
			break;
		case 15: //alpha
			tile.setAlpha(((GuiSlideControl)par1GuiButton).getShiftedValue() / 100);
			break;
		case 16: //red
		case 17: //green
		case 18: //blue
			int color = ((int)sliParticleRed.getShiftedValue() & 0xFF) << 16 | ((int)sliParticleGreen.getShiftedValue() & 0xFF) << 8 | (int)sliParticleBlue.getShiftedValue() & 0xFF;
			tile.setColor(color);
			break;
		case 19: //hide/show
			flag = tile.getShow();
			tile.setShow(!flag);
			hideBlock.displayString = !flag ? StatCollector.translateToLocal("am2.gui.visible") : StatCollector.translateToLocal("am2.gui.hidden");
			break;
		case 20: //quantity
			tile.setQuantity((int)sliParticleQuantity.getShiftedValue());
			break;
		case 21: //delay
			tile.setDelay((int)sliParticleDelay.getShiftedValue());
			break;
		case 22: //speed
			tile.setSpeed(sliParticleSpeed.getShiftedValue());
			break;
		case 200: //close
			tile.syncWithServer();
			this.mc.displayGuiScreen(this.parent);
			break;
		}
	}

	@Override
	public void onGuiClosed() {
		tile.syncWithServer();
		super.onGuiClosed();
	}

	@Override
	protected void mouseMovedOrUp(int par1, int par2, int par3)
	{
		if (activeButton != null && activeButton instanceof GuiSlideControl && par3 != 0){
			actionPerformed(activeButton);
		}else{
			super.mouseMovedOrUp(par1, par2, par3);
		}
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


