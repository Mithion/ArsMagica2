package am2.models;

import net.minecraft.entity.Entity;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelInscriptionTableRight extends ModelBase
{
  //fields
    ModelRenderer RFoot1;
    ModelRenderer RFoot3;
    ModelRenderer RFoot2;
    ModelRenderer RLeg1;
    ModelRenderer RLeg2;
    ModelRenderer RLeg3;
    ModelRenderer Tabletop;
    ModelRenderer Runner2;
    ModelRenderer Runner1;
    ModelRenderer Tablecloth2;
    ModelRenderer Tablecloth1;
    ModelRenderer Candle2;
    ModelRenderer Book4;
    ModelRenderer Book5;
    ModelRenderer Book6;
  
  public ModelInscriptionTableRight()
  {
    textureWidth = 128;
    textureHeight = 64;
    
      RFoot1 = new ModelRenderer(this, 42, 19);
      RFoot1.addBox(-1F, -3F, -2F, 2, 5, 4);
      RFoot1.setRotationPoint(5F, 22F, -6F);
      RFoot1.setTextureSize(128, 64);
      RFoot1.mirror = true;
      setRotation(RFoot1, 0F, 0F, 0F);
      RFoot3 = new ModelRenderer(this, 42, 29);
      RFoot3.addBox(0F, 0F, 0F, 1, 2, 8);
      RFoot3.setRotationPoint(5F, 19F, -4F);
      RFoot3.setTextureSize(128, 64);
      RFoot3.mirror = true;
      setRotation(RFoot3, 0F, 0F, 0F);
      RFoot2 = new ModelRenderer(this, 55, 19);
      RFoot2.addBox(-1F, -3F, -2F, 2, 5, 4);
      RFoot2.setRotationPoint(5F, 22F, 6F);
      RFoot2.setTextureSize(128, 64);
      RFoot2.mirror = true;
      setRotation(RFoot2, 0F, 0F, 0F);
      RLeg1 = new ModelRenderer(this, 68, 19);
      RLeg1.addBox(0F, 0F, 0F, 1, 6, 4);
      RLeg1.setRotationPoint(5F, 13F, -2F);
      RLeg1.setTextureSize(128, 64);
      RLeg1.mirror = true;
      setRotation(RLeg1, 0F, 0F, 0F);
      RLeg2 = new ModelRenderer(this, 79, 19);
      RLeg2.addBox(0F, 0F, 0F, 2, 3, 6);
      RLeg2.setRotationPoint(4F, 10F, -3F);
      RLeg2.setTextureSize(128, 64);
      RLeg2.mirror = true;
      setRotation(RLeg2, 0F, 0F, 0F);
      RLeg3 = new ModelRenderer(this, 43, 45);
      RLeg3.addBox(0F, 0F, 0F, 1, 2, 16);
      RLeg3.setRotationPoint(7F, 10F, -8F);
      RLeg3.setTextureSize(128, 64);
      RLeg3.mirror = true;
      setRotation(RLeg3, 0F, 0F, 0F);
      Tabletop = new ModelRenderer(this, 64, 0);
      Tabletop.addBox(0F, 0F, 0F, 16, 2, 16);
      Tabletop.setRotationPoint(-8F, 8F, -8F);
      Tabletop.setTextureSize(128, 64);
      Tabletop.mirror = true;
      setRotation(Tabletop, 0F, 0F, 0F);
      Runner2 = new ModelRenderer(this, 58, 43);
      Runner2.addBox(0F, 0F, 0F, 15, 1, 1);
      Runner2.setRotationPoint(-8F, 10F, 7F);
      Runner2.setTextureSize(128, 64);
      Runner2.mirror = true;
      setRotation(Runner2, 0F, 0F, 0F);
      Runner1 = new ModelRenderer(this, 58, 41);
      Runner1.addBox(0F, 0F, 0F, 15, 1, 1);
      Runner1.setRotationPoint(-8F, 10F, -8F);
      Runner1.setTextureSize(128, 64);
      Runner1.mirror = true;
      setRotation(Runner1, 0F, 0F, 0F);
      Tablecloth2 = new ModelRenderer(this, 6, 46);
      Tablecloth2.addBox(0F, 0F, 0F, 6, 9, 0);
      Tablecloth2.setRotationPoint(-8F, 11F, 8F);
      Tablecloth2.setTextureSize(128, 64);
      Tablecloth2.mirror = true;
      setRotation(Tablecloth2, 0F, 0F, 0F);
      Tablecloth1 = new ModelRenderer(this, 6, 46);
      Tablecloth1.addBox(0F, 0F, 0F, 6, 9, 0);
      Tablecloth1.setRotationPoint(-8F, 11F, -8F);
      Tablecloth1.setTextureSize(128, 64);
      Tablecloth1.mirror = true;
      setRotation(Tablecloth1, 0F, 0F, 0F);
      Candle2 = new ModelRenderer(this, 0, 57);
      Candle2.addBox(0F, 0F, 0F, 1, 3, 1);
      Candle2.setRotationPoint(3F, 5F, -2F);
      Candle2.setTextureSize(128, 64);
      Candle2.mirror = true;
      setRotation(Candle2, 0F, 0F, 0F);
      Book4 = new ModelRenderer(this, 21, 37);
      Book4.addBox(0F, 0F, 0F, 4, 2, 6);
      Book4.setRotationPoint(-3F, 6F, 1F);
      Book4.setTextureSize(128, 64);
      Book4.mirror = true;
      setRotation(Book4, 0F, -0.4461433F, 0F);
      Book5 = new ModelRenderer(this, 21, 19);
      Book5.addBox(0F, 0F, 0F, 4, 2, 6);
      Book5.setRotationPoint(1F, 6F, 1F);
      Book5.setTextureSize(128, 64);
      Book5.mirror = true;
      setRotation(Book5, 0F, 0.1487144F, 0F);
      Book6 = new ModelRenderer(this, 21, 28);
      Book6.addBox(0F, 0F, 0F, 4, 2, 6);
      Book6.setRotationPoint(2F, 4F, 1F);
      Book6.setTextureSize(128, 64);
      Book6.mirror = true;
      setRotation(Book6, 0F, -1.249201F, 0F);
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    RFoot1.render(f5);
    RFoot3.render(f5);
    RFoot2.render(f5);
    RLeg1.render(f5);
    RLeg2.render(f5);
    RLeg3.render(f5);
    Tabletop.render(f5);
    Runner2.render(f5);
    Runner1.render(f5);
    Tablecloth2.render(f5);
    Tablecloth1.render(f5);
    Candle2.render(f5);
    Book4.render(f5);
    Book5.render(f5);
    Book6.render(f5);
  }
  
  public void renderMode(float f5, int state){
	  RFoot1.render(f5);
	    RFoot3.render(f5);
	    RFoot2.render(f5);
	    RLeg1.render(f5);
	    RLeg2.render(f5);
	    RLeg3.render(f5);
	    Tabletop.render(f5);
	    Runner2.render(f5);
	    Runner1.render(f5);
	    
	    if (state >= 3){
	    	Candle2.render(f5);		    
		    Book5.render(f5);
		    Book6.render(f5);
	    }
	    if (state >= 2){	    	
		    Tablecloth2.render(f5);
		    Tablecloth1.render(f5);
		    Book4.render(f5);
		    Book5.render(f5);
	    }	
  }
  
  private void setRotation(ModelRenderer model, float x, float y, float z)
  {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }

}
