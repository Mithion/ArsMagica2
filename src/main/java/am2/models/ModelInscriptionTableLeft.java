package am2.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelInscriptionTableLeft extends ModelBase{
	//fields
	ModelRenderer LFoot2;
	ModelRenderer LFoot1;
	ModelRenderer LFoot3;
	ModelRenderer LLeg2;
	ModelRenderer LLeg1;
	ModelRenderer LLeg3;
	ModelRenderer Tabletop;
	ModelRenderer Runner2;
	ModelRenderer Runner1;
	ModelRenderer Tablecloth2;
	ModelRenderer Tablecloth1;
	ModelRenderer Candle;
	ModelRenderer Book2;
	ModelRenderer Book3;
	ModelRenderer Book1;

	public ModelInscriptionTableLeft(){
		textureWidth = 128;
		textureHeight = 64;

		LFoot2 = new ModelRenderer(this, 55, 19);
		LFoot2.addBox(-1F, -3F, -2F, 2, 5, 4);
		LFoot2.setRotationPoint(-4F, 22F, 6F);
		LFoot2.setTextureSize(128, 64);
		LFoot2.mirror = true;
		setRotation(LFoot2, 0F, 0F, 0F);
		LFoot1 = new ModelRenderer(this, 42, 19);
		LFoot1.addBox(-1F, -3F, -2F, 2, 5, 4);
		LFoot1.setRotationPoint(-4F, 22F, -6F);
		LFoot1.setTextureSize(128, 64);
		LFoot1.mirror = true;
		setRotation(LFoot1, 0F, 0F, 0F);
		LFoot3 = new ModelRenderer(this, 42, 29);
		LFoot3.addBox(0F, 0F, 0F, 1, 2, 8);
		LFoot3.setRotationPoint(-5F, 19F, -4F);
		LFoot3.setTextureSize(128, 64);
		LFoot3.mirror = true;
		setRotation(LFoot3, 0F, 0F, 0F);
		LLeg2 = new ModelRenderer(this, 79, 19);
		LLeg2.addBox(0F, 0F, 0F, 2, 3, 6);
		LLeg2.setRotationPoint(-6F, 10F, -3F);
		LLeg2.setTextureSize(128, 64);
		LLeg2.mirror = true;
		setRotation(LLeg2, 0F, 0F, 0F);
		LLeg1 = new ModelRenderer(this, 68, 19);
		LLeg1.addBox(0F, 0F, 0F, 1, 6, 4);
		LLeg1.setRotationPoint(-5F, 13F, -2F);
		LLeg1.setTextureSize(128, 64);
		LLeg1.mirror = true;
		setRotation(LLeg1, 0F, 0F, 0F);
		LLeg3 = new ModelRenderer(this, 43, 45);
		LLeg3.addBox(0F, 0F, 0F, 1, 2, 16);
		LLeg3.setRotationPoint(-8F, 10F, -8F);
		LLeg3.setTextureSize(128, 64);
		LLeg3.mirror = true;
		setRotation(LLeg3, 0F, 0F, 0F);
		Tabletop = new ModelRenderer(this, 0, 0);
		Tabletop.addBox(0F, 0F, 0F, 16, 2, 16);
		Tabletop.setRotationPoint(-8F, 8F, -8F);
		Tabletop.setTextureSize(128, 64);
		Tabletop.mirror = true;
		setRotation(Tabletop, 0F, 0F, 0F);
		Runner2 = new ModelRenderer(this, 42, 41);
		Runner2.addBox(0F, 0F, 0F, 15, 1, 1);
		Runner2.setRotationPoint(-7F, 10F, 7F);
		Runner2.setTextureSize(128, 64);
		Runner2.mirror = true;
		setRotation(Runner2, 0F, 0F, 0F);
		Runner1 = new ModelRenderer(this, 43, 41);
		Runner1.addBox(0F, 0F, 0F, 15, 1, 1);
		Runner1.setRotationPoint(-7F, 10F, -8F);
		Runner1.setTextureSize(128, 64);
		Runner1.mirror = true;
		setRotation(Runner1, 0F, 0F, 0F);
		Tablecloth2 = new ModelRenderer(this, 0, 46);
		Tablecloth2.addBox(0F, 0F, 0F, 6, 9, 0);
		Tablecloth2.setRotationPoint(2F, 11F, 8F);
		Tablecloth2.setTextureSize(128, 64);
		Tablecloth2.mirror = true;
		setRotation(Tablecloth2, 0F, 0F, 0F);
		Tablecloth1 = new ModelRenderer(this, 0, 46);
		Tablecloth1.addBox(0F, 0F, 0F, 6, 9, 0);
		Tablecloth1.setRotationPoint(2F, 11F, -8F);
		Tablecloth1.setTextureSize(128, 64);
		Tablecloth1.mirror = true;
		setRotation(Tablecloth1, 0F, 0F, 0F);
		Candle = new ModelRenderer(this, 0, 56);
		Candle.addBox(0F, 0F, 0F, 1, 4, 1);
		Candle.setRotationPoint(4F, 4F, 5F);
		Candle.setTextureSize(128, 64);
		Candle.mirror = true;
		setRotation(Candle, 0F, 0F, 0F);
		Book2 = new ModelRenderer(this, 0, 28);
		Book2.addBox(0F, 0F, 0F, 4, 2, 6);
		Book2.setRotationPoint(-1F, 6F, -3F);
		Book2.setTextureSize(128, 64);
		Book2.mirror = true;
		setRotation(Book2, 0F, -0.2379431F, 0F);
		Book3 = new ModelRenderer(this, 0, 19);
		Book3.addBox(0F, 0F, 0F, 4, 2, 6);
		Book3.setRotationPoint(-4F, 6F, -7F);
		Book3.setTextureSize(128, 64);
		Book3.mirror = true;
		setRotation(Book3, 0.5056291F, 0F, 0F);
		Book1 = new ModelRenderer(this, 0, 37);
		Book1.addBox(0F, 0F, 0F, 4, 2, 6);
		Book1.setRotationPoint(-7F, 6F, -3F);
		Book1.setTextureSize(128, 64);
		Book1.mirror = true;
		setRotation(Book1, 0F, 0F, 0F);
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5){
		super.render(entity, f, f1, f2, f3, f4, f5);
		LFoot2.render(f5);
		LFoot1.render(f5);
		LFoot3.render(f5);
		LLeg2.render(f5);
		LLeg1.render(f5);
		LLeg3.render(f5);
		Tabletop.render(f5);
		Runner2.render(f5);
		Runner1.render(f5);
		Tablecloth2.render(f5);
		Tablecloth1.render(f5);
		Candle.render(f5);
		Book2.render(f5);
		Book3.render(f5);
		Book1.render(f5);
	}

	public void renderModel(float f5, int state){
		LFoot2.render(f5);
		LFoot1.render(f5);
		LFoot3.render(f5);
		LLeg2.render(f5);
		LLeg1.render(f5);
		LLeg3.render(f5);
		Tabletop.render(f5);
		Runner2.render(f5);
		Runner1.render(f5);
		if (state >= 3){
			Candle.render(f5);
		}
		if (state >= 2){
			Tablecloth2.render(f5);
			Tablecloth1.render(f5);
		}
		if (state >= 1){
			Book3.render(f5);
			Book1.render(f5);
			Book2.render(f5);
		}
	}

	private void setRotation(ModelRenderer model, float x, float y, float z){
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

}
