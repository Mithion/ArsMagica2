package am2.guis;

import am2.LogHelper;
import am2.buffs.BuffList;
import am2.playerextensions.ExtendedProperties;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GLContext;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class AMGuiHelper{

	private final static RenderBlocks renderBlocks = new RenderBlocks();
	protected static RenderItem itemRenderer = new RenderItem();

	private AMGuiHelper(){
		Minecraft mc = Minecraft.getMinecraft();
	}

	public static final AMGuiHelper instance = new AMGuiHelper();

	private static final ResourceLocation rl_items = new ResourceLocation("textures/atlas/items.png");

	private long millis;
	private long lastmillis;
	private long accumulatedMillis;
	private static float zLevel = 300.0f;

	private static int fractalLineDetail = 2;

	// Buff flashing variables (for the UI)
	//=========================================
	public float slowFlashAlpha = 1.0f;
	public float fastFlashAlpha = 1.0f;
	private int flashCounter = 0;
	public final short flashDuration = 20;
	private int magicXPBarShowTimer = 0;
	private float magicXPBarAlpha = 0;
	public float playerRunesAlpha = 0;
	//=========================================
	//Blackout variables (for the UI)
	//=========================================
	private final short[] flashTimers = new short[5];
	private final int[] blackoutTimers = new int[5];
	private final int[] blackoutTimersMax = new int[5];
	//=========================================
	// Compendium Variables
	//=========================================
	private int slowUITicker = 0;
	private static int fastUITicker = 0;
	public boolean runCompendiumTicker = true;
	private final LinkedList<CompendiumBreadcrumb> compendiumBreadcrumbs = new LinkedList<CompendiumBreadcrumb>();
	//=========================================

	public EntityItem dummyItem;

	public void blackoutArmorPiece(int index, int duration){
		flashTimers[index] = flashDuration;
		blackoutTimers[index] = duration;
		blackoutTimersMax[index] = duration;
	}

	public void flashArmorPiece(int index){
		flashTimers[index] = flashDuration;
	}

	public void flashManaBar(){
		if (flashTimers[4] <= 1)
			flashTimers[4] = flashDuration;
	}

	public short getFlashTimer(int index){
		return flashTimers[index];
	}

	public int getBlackoutTimer(int index){
		return blackoutTimers[index];
	}

	public int getBlackoutTimerMax(int index){
		return blackoutTimersMax[index];
	}

	public int getSlowTicker(){
		return slowUITicker;
	}

	public int getFastTicker(){
		return fastUITicker;
	}

	public float getMagicXPBarAlpha(){
		return this.magicXPBarAlpha;
	}

	public void showMagicXPBar(){
		this.magicXPBarAlpha = 1.0f;
		this.magicXPBarShowTimer = 100;
	}

	public void tick(){

		if (dummyItem == null){
			dummyItem = new EntityItem(Minecraft.getMinecraft().theWorld);
		}else{
			dummyItem.age++;
			//dummyItem.rotationYaw += 0.1f;
		}

		for (int i = 0; i < this.flashTimers.length; ++i){
			if (this.flashTimers[i] > 0)
				this.flashTimers[i]--;
		}

		for (int i = 0; i < this.blackoutTimers.length; ++i){
			if (this.blackoutTimers[i] > 0){
				this.blackoutTimers[i]--;
				if (this.blackoutTimers[i] == 0){
					flashArmorPiece(i);
				}
			}else{
				this.blackoutTimersMax[i] = 0;
			}
		}

		flashCounter++;
		if (flashCounter > 20) flashCounter = 0;

		if (magicXPBarShowTimer > 0){
			magicXPBarShowTimer--;
			if (magicXPBarShowTimer < 20)
				magicXPBarAlpha -= 0.05f;
		}

		if (runCompendiumTicker){
			fastUITicker++;
			if (fastUITicker > 40){
				fastUITicker = 0;
				slowUITicker++;
			}
		}

		slowFlashAlpha = Math.abs((flashCounter / 20f) - 0.5f) * 2;
		fastFlashAlpha = Math.abs((flashCounter % 5 / 5f) - 0.5f) * 2;

		lastmillis = millis;
		millis = System.currentTimeMillis();
	}

	public void guiTick(){
		lastmillis = millis;
		millis = System.currentTimeMillis();
		accumulatedMillis += (millis - lastmillis);
		if (accumulatedMillis >= 50){
			tick();
			accumulatedMillis = 0;
		}
	}

	public void pushCompendiumBreadcrumb(String identifier, int page, int type, Object... refData){
		compendiumBreadcrumbs.add(new CompendiumBreadcrumb(identifier, refData, type, page));
	}

	public CompendiumBreadcrumb popCompendiumBreadcrumb(){
		if (compendiumBreadcrumbs.size() > 0)
			return compendiumBreadcrumbs.pollLast();
		return null;
	}

	public void clearCompendiumBreadcrumbs(){
		compendiumBreadcrumbs.clear();
	}

	public static void OpenBookGUI(ItemStack stack){
		// TODO Auto-generated method stub
	}

	public static void OpenCompendiumGui(ItemStack stack){
		CompendiumBreadcrumb breadcrumb = AMGuiHelper.instance.popCompendiumBreadcrumb();
		if (breadcrumb != null){
			if (breadcrumb.entryType == breadcrumb.TYPE_ENTRY){
				Minecraft.getMinecraft().displayGuiScreen(new GuiArcaneCompendium(breadcrumb));
			}else{
				Minecraft.getMinecraft().displayGuiScreen(new GuiCompendiumIndex(breadcrumb));
			}
		}else{
			Minecraft.getMinecraft().displayGuiScreen(new GuiCompendiumIndex());
		}
	}

	public static void DrawIconAtXY(IIcon IIcon, float x, float y, float zLevel, int w, int h, boolean semitransparent){

		if (IIcon == null)
			return;

		GL11.glMatrixMode(GL11.GL_TEXTURE);
		GL11.glPushMatrix();
		if (semitransparent){
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		}
		Minecraft.getMinecraft().renderEngine.bindTexture(rl_items);

		Tessellator tessellator = Tessellator.instance;

		boolean drawing = ReflectionHelper.getPrivateValue(Tessellator.class, tessellator, "field_78415_z", "isDrawing");
		if (drawing)
			tessellator.draw();

		tessellator.startDrawingQuads();

		tessellator.addVertexWithUV(x, y + h, zLevel, IIcon.getMinU(), IIcon.getMaxV());
		tessellator.addVertexWithUV(x + w, y + h, zLevel, IIcon.getMaxU(), IIcon.getMaxV());
		tessellator.addVertexWithUV(x + w, y, zLevel, IIcon.getMaxU(), IIcon.getMinV());
		tessellator.addVertexWithUV(x, y, zLevel, IIcon.getMinU(), IIcon.getMinV());

		tessellator.draw();

		if (semitransparent){
			GL11.glDisable(GL11.GL_BLEND);
		}
		GL11.glPopMatrix();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		if (drawing)
			tessellator.startDrawingQuads();
	}

	public static void DrawItemAtXY(ItemStack stack, float x, float y, float zLevel){
		DrawItemAtXY(stack, x, y, zLevel, 1.0f);
	}

	public static void DrawItemAtXY(ItemStack stack, float x, float y, float zLevel, float scale){
		if (stack == null)
			return;
		boolean success = false;

		GL11.glPushAttrib(GL11.GL_TEXTURE_BIT | GL11.GL_LIGHTING_BIT);

		RenderHelper.disableStandardItemLighting();

		if (scale != 1.0f){
			GL11.glPushMatrix();
			GL11.glScalef(scale, scale, 1);
			float invScale = scale - 0.045f;
			success = ForgeHooksClient.renderInventoryItem(renderBlocks, Minecraft.getMinecraft().renderEngine, stack, true, zLevel, x + (x * invScale), y + (y * invScale));

			if (!success){
				itemRenderer.renderItemIntoGUI(Minecraft.getMinecraft().fontRendererObj, Minecraft.getMinecraft().renderEngine, stack, (int)(x + (x * invScale)), (int)(y + (y * invScale)));
			}

			GL11.glPopMatrix();
		}else{
			success = ForgeHooksClient.renderInventoryItem(renderBlocks, Minecraft.getMinecraft().renderEngine, stack, true, zLevel, x, y);

			if (!success){
				itemRenderer.renderItemIntoGUI(Minecraft.getMinecraft().fontRendererObj, Minecraft.getMinecraft().renderEngine, stack, (int)x, (int)y);
			}
		}
		GL11.glPopAttrib();
	}

	public static void drawCompendiumText(String text, int x_start, int y_start, int max_width, int start_color, FontRenderer fontRendererObj){
		int cur_color = start_color;
		String[] words = text.split(" ");
		int lineLength = 0;
		int posX = x_start;
		int posY = y_start;

		for (String word : words){
			if (word.equals("")) continue;
			int linesBefore = 0;
			int linesAfter = 0;

			int wordLength = fontRendererObj.getStringWidth(word.replaceAll("#.", "") + " ");
			if (lineLength + wordLength > max_width){
				posY += fontRendererObj.FONT_HEIGHT;
				posX = x_start;
				lineLength = 0;
			}

			while (word.startsWith("\n")){
				linesBefore++;
				word = word.substring(1);
			}
			while (word.endsWith("\n")){
				linesAfter++;
				word = word.substring(0, word.length() - 1);
			}
			word = word.replace("\n", "");

			if (linesBefore > 0){
				posY += fontRendererObj.FONT_HEIGHT * linesBefore;
				posX = x_start;
				lineLength = 0;
			}

			cur_color = parseColorAndDraw(word, posX, posY, cur_color, fontRendererObj);

			posX += wordLength;
			lineLength += wordLength;

			if (linesAfter > 0){
				posY += fontRendererObj.FONT_HEIGHT * linesAfter;
				posX = x_start;
				lineLength = 0;
			}
		}
	}

	protected static void drawHoveringText(List par1List, int par2, int par3, FontRenderer font, int width, int height){
		if (!par1List.isEmpty()){
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			RenderHelper.disableStandardItemLighting();
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			int k = 0;
			Iterator iterator = par1List.iterator();

			while (iterator.hasNext()){
				String s = (String)iterator.next();
				int l = font.getStringWidth(s);

				if (l > k){
					k = l;
				}
			}

			int i1 = par2 + 12;
			int j1 = par3 - 12;
			int k1 = 8;

			if (par1List.size() > 1){
				k1 += 2 + (par1List.size() - 1) * 10;
			}

			if (i1 + k > width){
				i1 -= 28 + k;
			}

			if (j1 + k1 + 6 > height){
				j1 = height - k1 - 6;
			}

			int l1 = -267386864;
			drawGradientRect(i1 - 3, j1 - 4, i1 + k + 3, j1 - 3, l1, l1);
			drawGradientRect(i1 - 3, j1 + k1 + 3, i1 + k + 3, j1 + k1 + 4, l1, l1);
			drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 + k1 + 3, l1, l1);
			drawGradientRect(i1 - 4, j1 - 3, i1 - 3, j1 + k1 + 3, l1, l1);
			drawGradientRect(i1 + k + 3, j1 - 3, i1 + k + 4, j1 + k1 + 3, l1, l1);
			int i2 = 1347420415;
			int j2 = (i2 & 16711422) >> 1 | i2 & -16777216;
			drawGradientRect(i1 - 3, j1 - 3 + 1, i1 - 3 + 1, j1 + k1 + 3 - 1, i2, j2);
			drawGradientRect(i1 + k + 2, j1 - 3 + 1, i1 + k + 3, j1 + k1 + 3 - 1, i2, j2);
			drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 - 3 + 1, i2, i2);
			drawGradientRect(i1 - 3, j1 + k1 + 2, i1 + k + 3, j1 + k1 + 3, j2, j2);

			for (int k2 = 0; k2 < par1List.size(); ++k2){
				String s1 = (String)par1List.get(k2);
				font.drawStringWithShadow(s1, i1, j1, -1);

				if (k2 == 0){
					j1 += 2;
				}

				j1 += 10;
			}
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			RenderHelper.enableStandardItemLighting();
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		}
	}

	protected static void drawGradientRect(int par1, int par2, int par3, int par4, int par5, int par6){
		float f = (par5 >> 24 & 255) / 255.0F;
		float f1 = (par5 >> 16 & 255) / 255.0F;
		float f2 = (par5 >> 8 & 255) / 255.0F;
		float f3 = (par5 & 255) / 255.0F;
		float f4 = (par6 >> 24 & 255) / 255.0F;
		float f5 = (par6 >> 16 & 255) / 255.0F;
		float f6 = (par6 >> 8 & 255) / 255.0F;
		float f7 = (par6 & 255) / 255.0F;
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.setColorRGBA_F(f1, f2, f3, f);
		tessellator.addVertex(par3, par2, zLevel);
		tessellator.addVertex(par1, par2, zLevel);
		tessellator.setColorRGBA_F(f5, f6, f7, f4);
		tessellator.addVertex(par1, par4, zLevel);
		tessellator.addVertex(par3, par4, zLevel);
		tessellator.draw();
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public static void line2d(float src_x, float src_y, float dst_x, float dst_y, float zLevel, int color){
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glLineWidth(1f);
		GL11.glColor3f(((color & 0xFF0000) >> 16) / 255.0f, ((color & 0x00FF00) >> 8) / 255.0f, (color & 0x0000FF) / 255.0f);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex3f(src_x, src_y, zLevel);
		GL11.glVertex3f(dst_x, dst_y, zLevel);
		GL11.glEnd();
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public static void line2d(float src_x, float src_y, float dst_x, float dst_y, float zLevel, float weight, int color){
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glLineWidth(weight);
		GL11.glColor3f(((color & 0xFF0000) >> 16) / 255.0f, ((color & 0x00FF00) >> 8) / 255.0f, (color & 0x0000FF) / 255.0f);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex3f(src_x, src_y, zLevel);
		GL11.glVertex3f(dst_x, dst_y, zLevel);
		GL11.glEnd();
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public static void gradientline2d(float src_x, float src_y, float dst_x, float dst_y, float zLevel, int color1, int color2){
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glLineWidth(1f);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glColor3f((color1 & 0xFF0000) >> 16, (color1 & 0x00FF00) >> 8, color1 & 0x0000FF);
		GL11.glVertex3f(src_x, src_y, zLevel);
		GL11.glColor3f((color2 & 0xFF0000) >> 16, (color2 & 0x00FF00) >> 8, color2 & 0x0000FF);
		GL11.glVertex3f(dst_x, dst_y, zLevel);
		GL11.glEnd();
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public static void fractalLine2d(int src_x, int src_y, int dst_x, int dst_y, float zLevel, int color, float displace){
		fractalLine2d(src_x, src_y, dst_x, dst_y, zLevel, color, displace, fractalLineDetail);
	}

	public static void fractalLine2d(int src_x, int src_y, int dst_x, int dst_y, float zLevel, int color, float displace, float fractalDetail){
		if (displace < fractalDetail){
			line2d(src_x, src_y, dst_x, dst_y, zLevel, color);
		}else{
			Random rand = new Random();
			int mid_x = (dst_x + src_x) / 2;
			int mid_y = (dst_y + src_y) / 2;
			mid_x += (rand.nextFloat() - 0.5) * displace;
			mid_y += (rand.nextFloat() - 0.5) * displace;
			fractalLine2d(src_x, src_y, mid_x, mid_y, zLevel, color, displace / 2f, fractalDetail);
			fractalLine2d(dst_x, dst_y, mid_x, mid_y, zLevel, color, displace / 2f, fractalDetail);
		}
	}

	private static int parseColorAndDraw(String word, int posX, int posY, int cur_color, FontRenderer fontRendererObj){
		int index = word.indexOf("#");
		int color = cur_color;
		while (index > -1 && index < word.length() - 1){

			String toRender = word.substring(0, index);
			fontRendererObj.drawString(toRender, posX, posY, color);
			posX += fontRendererObj.getStringWidth(toRender);

			char nextChar = word.charAt(index + 1);
			switch (nextChar){
			case '0':
				color = 0x000000;
				break;
			case '1':
				color = 0x0000BF;
				break;
			case '2':
				color = 0x00BF00;
				break;
			case '3':
				color = 0x00BFBF;
				break;
			case '4':
				color = 0xBF0000;
				break;
			case '5':
				color = 0xBF00BF;
				break;
			case '6':
				color = 0xBFBF00;
				break;
			case '7':
				color = 0xBFBFBF;
				break;
			case '8':
				color = 0x404040;
				break;
			case '9':
				color = 0x4040FF;
				break;
			case 'a':
				color = 0x40FF40;
				break;
			case 'b':
				color = 0x40FFFF;
				break;
			case 'c':
				color = 0xFF4040;
				break;
			case 'd':
				color = 0xFF40FF;
				break;
			case 'e':
				color = 0xFFFF40;
				break;
			case 'f':
				color = 0xFFFFFF;
				break;
			}

			word = word.substring(index + 2);
			index = word.indexOf("#");
		}

		fontRendererObj.drawString(word, posX, posY, color);

		return color;
	}

	public static int createRenderTexture(){
		int colorTextureID = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, colorTextureID);                                    // Bind the colorbuffer texture
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);                // make it linear filterd
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, 512, 512, 0, GL11.GL_RGBA, GL11.GL_INT, (java.nio.ByteBuffer)null);    // Create the texture data

		return colorTextureID;
	}

	public static int createFBO(int textureID, int w, int h, boolean depthBuffer){
		boolean FBOEnabled = GLContext.getCapabilities().GL_EXT_framebuffer_object;
		if (!FBOEnabled)
			return -1;
		IntBuffer buffer = ByteBuffer.allocateDirect(1 * 4).order(ByteOrder.nativeOrder()).asIntBuffer(); // allocate a 1 int byte buffer
		EXTFramebufferObject.glGenFramebuffersEXT(buffer); // generate
		int myFBOId = buffer.get();
		EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, myFBOId);
		EXTFramebufferObject.glFramebufferTexture2DEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT, GL11.GL_TEXTURE_2D, textureID, 0);
		int framebuffer = EXTFramebufferObject.glCheckFramebufferStatusEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT);
		switch (framebuffer){
		case EXTFramebufferObject.GL_FRAMEBUFFER_COMPLETE_EXT:
			break;
		case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT:
			throw new RuntimeException("FrameBuffer: " + myFBOId + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT exception");
		case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT:
			throw new RuntimeException("FrameBuffer: " + myFBOId + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT exception");
		case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_EXT:
			throw new RuntimeException("FrameBuffer: " + myFBOId + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_EXT exception");
		case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT:
			throw new RuntimeException("FrameBuffer: " + myFBOId + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT exception");
		case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_FORMATS_EXT:
			throw new RuntimeException("FrameBuffer: " + myFBOId + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_FORMATS_EXT exception");
		case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT:
			throw new RuntimeException("FrameBuffer: " + myFBOId + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT exception");
		default:
			throw new RuntimeException("Unexpected reply from glCheckFramebufferStatusEXT: " + framebuffer);
		}

		return myFBOId;
	}

	public static boolean bindFBOTexture(int FBOId, int w, int h){
		try{
			EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, FBOId);
			GL11.glPushAttrib(GL11.GL_VIEWPORT_BIT);
			GL11.glViewport(0, 0, w, h);
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

			return true;
		}catch (Throwable t){
			return false;
		}
	}

	public static boolean unbindFBOTexture(){
		try{
			EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, 0);
			GL11.glPopAttrib();

			return true;
		}catch (Throwable t){
			return false;
		}
	}

	public static void flipView(float f){
		float flip = ExtendedProperties.For(Minecraft.getMinecraft().thePlayer).getFlipRotation();
		float lastFlip = ExtendedProperties.For(Minecraft.getMinecraft().thePlayer).getPrevFlipRotation();
		GL11.glRotatef(lastFlip + (flip - lastFlip) * f, 0, 0, 1);
	}

	public static void shiftView(float f){
		EntityPlayer entity = Minecraft.getMinecraft().thePlayer;
		int viewSet = Minecraft.getMinecraft().gameSettings.thirdPersonView;
		if (viewSet == 0){
			ExtendedProperties exProps = ExtendedProperties.For(entity);
			if (exProps.getShrinkPct() > 0f){
				float amt = exProps.getPrevShrinkPct() + (exProps.getShrinkPct() - exProps.getPrevShrinkPct()) * f;
				GL11.glTranslatef(0, 1 * amt, 0);
			}
		}

		float flip = ExtendedProperties.For(entity).getFlipRotation();
		float prevFlip = ExtendedProperties.For(entity).getPrevFlipRotation();
		if (flip > 0){
			float smoothedFlip = prevFlip + ((flip - prevFlip) * f);
			GL11.glTranslatef(0, (entity.height * (smoothedFlip / 180f)) - 0.1f, 0);
		}
	}

	public static void overrideKeyboardInput(){
		Minecraft mc = Minecraft.getMinecraft();
		if (mc.thePlayer != null && mc.theWorld != null && ExtendedProperties.For(mc.thePlayer).shouldReverseInput()){
			EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
			if (mc.gameSettings.keyBindLeft.getIsKeyPressed()){
				LogHelper.info("Override Left");
				player.movementInput.moveStrafe -= 2;
			}

			if (mc.gameSettings.keyBindRight.getIsKeyPressed()){
				LogHelper.info("Override Rights");
				player.movementInput.moveStrafe += 2;
			}

			if (mc.thePlayer.isPotionActive(BuffList.scrambleSynapses)){
				if (mc.gameSettings.keyBindForward.getIsKeyPressed()){
					player.movementInput.moveForward -= 2;
				}
				if (mc.gameSettings.keyBindBack.getIsKeyPressed()){
					player.movementInput.moveForward += 2;
				}
			}
		}
	}

	public static boolean overrideMouseInput(EntityRenderer renderer, float f, boolean b){
		Minecraft mc = Minecraft.getMinecraft();

		if (!mc.inGameHasFocus || mc.thePlayer == null || mc.theWorld == null)
			return true;

		ExtendedProperties props = ExtendedProperties.For(mc.thePlayer);

		if (!(mc.thePlayer.isPotionActive(BuffList.scrambleSynapses) ^ props.getIsFlipped())){
			return true;
		}

		mc.mouseHelper.mouseXYChange();
		float f1 = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
		float f2 = f1 * f1 * f1 * 8.0F;
		float f3 = (float)mc.mouseHelper.deltaX * f2;
		float f4 = (float)mc.mouseHelper.deltaY * f2;
		byte b0 = -1;

		if (mc.gameSettings.invertMouse){
			b0 = 1;
		}

		if (mc.gameSettings.smoothCamera){
			String[] scy = {"field_78496_H", "smoothCamYaw"};
			String[] scp = {"field_78521_m", "smoothCamPitch"};
			String[] scpt = {"field_78533_p", "smoothCamPartialTicks"};
			String[] scfx = {"field_78518_n", "smoothCamFilterX"};
			String[] scfy = {"field_78499_K", "smoothCamFilterY"};

			//renderer.smoothCamYaw += f3;
			ReflectionHelper.setPrivateValue(EntityRenderer.class, renderer, (Float)ReflectionHelper.getPrivateValue(EntityRenderer.class, renderer, scy) - f3, scy);
			//renderer.smoothCamPitch += f4;
			ReflectionHelper.setPrivateValue(EntityRenderer.class, renderer, (Float)ReflectionHelper.getPrivateValue(EntityRenderer.class, renderer, scp) - f4, scp);
			//float f5 = f - renderer.smoothCamPartialTicks;
			float f5 = f - (Float)ReflectionHelper.getPrivateValue(EntityRenderer.class, renderer, scpt);
			//renderer.smoothCamPartialTicks = f;
			ReflectionHelper.setPrivateValue(EntityRenderer.class, renderer, f, scpt);
			//f3 = renderer.smoothCamFilterX * f5;
			f3 = (Float)ReflectionHelper.getPrivateValue(EntityRenderer.class, renderer, scfx) * f5;
			//f4 = renderer.smoothCamFilterY * f5;
			f4 = (Float)ReflectionHelper.getPrivateValue(EntityRenderer.class, renderer, scfy) * f5;
			mc.thePlayer.setAngles(-f3, f4 * (float)b0);
		}else{
			mc.thePlayer.setAngles(-f3, f4 * (float)b0);
		}

		return false;
	}

	public class CompendiumBreadcrumb{
		public final String entryName;
		public final Object[] refData;
		public final int entryType;
		public final int page;

		public static final int TYPE_INDEX = 0;
		public static final int TYPE_ENTRY = 1;

		public CompendiumBreadcrumb(String entryName, Object[] refObject, int entryType, int page){
			this.entryName = entryName;
			this.entryType = entryType;
			this.refData = refObject;
			this.page = page;
		}
	}
}
