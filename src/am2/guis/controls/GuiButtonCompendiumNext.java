package am2.guis.controls;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GuiButtonCompendiumNext extends GuiButton{
	/**
     * True for pointing right (next page), false for pointing left (previous page).
     */
    private final boolean nextPage;
    private final static int sourceWidth = 12;
    private final static int sourceHeight = 12;
    
    private static final ResourceLocation buttonImage = new ResourceLocation("arsmagica2", "textures/guis/ArcaneCompendiumGuiExtras.png");
    
    public GuiButtonCompendiumNext(int id, int xPos, int yPos, boolean isNextPage)
    {
        super(id, xPos, yPos, sourceWidth, sourceHeight, "");
        this.nextPage = isNextPage;
    }
    
    public void setDimensions(int width, int height){
    	this.width = width;
    	this.height = height;
    }

    /**
     * Draws this button to the screen.
     */
    @Override
	public void drawButton(Minecraft par1Minecraft, int par2, int par3)
    {
        if (this.visible)
        {
            boolean isMousedOver = par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            par1Minecraft.renderEngine.bindTexture(buttonImage);

            if (isMousedOver){
            	GL11.glColor4f(0.6f, 0.6f, 0.6f, 1.0f);
            }
            
            int u = 364;
            int v = 240;
            if (!this.nextPage)
            {
                u += 12;
            }

            GL11.glDisable(GL11.GL_LIGHTING);
            this.drawTexturedModalRect_Classic(this.xPosition, this.yPosition, u, v, 12, 12, 12, 14);
            GL11.glEnable(GL11.GL_LIGHTING);
        }
    }
    
    
    
    public void drawTexturedModalRect_Classic(int dst_x, int dst_y, int src_x, int src_y, int dst_width, int dst_height, int src_width, int src_height)
	{
		float var7 = 0.00390625F;
		float var8 = 0.00390625F;

		Tessellator var9 = Tessellator.instance;
		
		var9.startDrawingQuads();
		var9.addVertexWithUV(dst_x + 0, dst_y + dst_height, this.zLevel, (src_x + 0) * var7, (src_y + src_height) * var8);
		var9.addVertexWithUV(dst_x + dst_width, dst_y + dst_height, this.zLevel, (src_x + src_width) * var7, (src_y + src_height) * var8);
		var9.addVertexWithUV(dst_x + dst_width, dst_y + 0, this.zLevel, (src_x + src_width) * var7, (src_y + 0) * var8);
		var9.addVertexWithUV(dst_x + 0, dst_y + 0, this.zLevel, (src_x + 0) * var7, (src_y + 0) * var8);
		var9.draw();
	}
}
