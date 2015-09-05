package am2.particles.ribbon;

import am2.AMCore;
import am2.api.math.AMVector3;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public class Quad3D{
	AMVector3 p0, p1, p2, p3;
	AMVector3 avg;
	private IIcon icon;
	private static final Random rand = new Random();

	AMVector3 normal;

	Quad3D(AMVector3 p0, AMVector3 p1, AMVector3 p2, AMVector3 p3, IIcon icon){
		this.p0 = p0;
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;

		avg = new AMVector3((p0.x + p1.x + p2.x + p3.x) / 4, (p0.y + p1.y + p2.y + p3.y) / 4D, (p0.z + p1.z + p2.z + p3.z) / 4D);

		this.icon = icon;

		calcNormal(p0, p1, p2);
	}

	void calcNormal(AMVector3 v1, AMVector3 v2, AMVector3 v3){
		float Qx, Qy, Qz, Px, Py, Pz;

		Qx = v2.x - v1.x;
		Qy = v2.y - v1.y;
		Qz = v2.z - v1.z;

		Px = v3.x - v1.x;
		Py = v3.y - v1.y;
		Pz = v3.z - v1.z;

		normal = new AMVector3(Py * Qz - Pz * Qy, Pz * Qx - Px * Qz, Px * Qy - Py * Qx);
	}

	void draw(){
		//noStroke();
		//GL11.glBegin(GL11.GL_QUADS);
		Tessellator t = Tessellator.instance;
		t.startDrawingQuads();
		t.setBrightness(0xF00F0);
		t.addVertexWithUV(p0.x, p0.y, p0.z, icon.getMinU(), icon.getMinV());
		t.addVertexWithUV(p1.x, p1.y, p1.z, icon.getMaxU(), icon.getMinV());
		t.addVertexWithUV(p2.x, p2.y, p2.z, icon.getMaxU(), icon.getMaxV());
		t.addVertexWithUV(p3.x, p3.y, p3.z, icon.getMinU(), icon.getMaxV());
		t.draw();
		if (AMCore.config.FullGFX()){
			double off = 0.005;

			t.startDrawingQuads();
			t.setBrightness(0xF00F0);
			GL11.glColor4f(0, 0.5f, 1.0f, 0.6f);
			t.addVertexWithUV(p0.x + off, p0.y + off, p0.z + off, icon.getMinU(), icon.getMinV());
			t.addVertexWithUV(p1.x + off, p1.y + off, p1.z + off, icon.getMaxU(), icon.getMinV());
			t.addVertexWithUV(p2.x + off, p2.y + off, p2.z + off, icon.getMaxU(), icon.getMaxV());
			t.addVertexWithUV(p3.x + off, p3.y + off, p3.z + off, icon.getMinU(), icon.getMaxV());
			t.draw();

			GL11.glColor4f(1, 1, 1, 0.6f);
		}
		double mul = 0.0025;
		double halfMul = 0.00125;

		AMVector3 vecOffset = new AMVector3(rand.nextDouble() * mul - halfMul, rand.nextDouble() * mul - halfMul, rand.nextDouble() * mul - halfMul);
		p0.add(vecOffset);
		p1.add(vecOffset);
		p2.add(vecOffset);
		p3.add(vecOffset);
	}
}