package am2.entities.models;

import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.Entity;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

@SideOnly(Side.CLIENT)
public class ModelChestGolem extends ModelBase
{
  //fields
    ModelRenderer ChestLid;
    ModelRenderer ChestKnob;
    ModelRenderer ChestBody;
    ModelRenderer LeftEye;
    ModelRenderer RightEye;
  
  public ModelChestGolem()
  {
    textureWidth = 64;
    textureHeight = 32;
    
      ChestLid = new ModelRenderer(this, 8, 8);
      ChestLid.addBox(-7F, -5F, -14F, 14, 5, 14);
      ChestLid.setRotationPoint(0F, 14F, 7F);
      ChestLid.setTextureSize(64, 32);
      ChestLid.mirror = true;
      setRotation(ChestLid, 0F, 0F, 0F);
      ChestKnob = new ModelRenderer(this, 0, 25);
      ChestKnob.addBox(-1F, -2F, -15F, 2, 4, 1);
      ChestKnob.setRotationPoint(0F, 14F, 7F);
      ChestKnob.setTextureSize(64, 32);
      ChestKnob.mirror = true;
      setRotation(ChestKnob, 0F, 0F, 0F);
      ChestBody = new ModelRenderer(this, 8, 8);
      ChestBody.addBox(0F, 0F, 0F, 14, 10, 14);
      ChestBody.setRotationPoint(-7F, 14F, -7F);
      ChestBody.setTextureSize(64, 32);
      ChestBody.mirror = true;
      setRotation(ChestBody, 0F, 0F, 0F);
      LeftEye = new ModelRenderer(this, 0, 0);
      LeftEye.addBox(2F, -3F, -15F, 3, 2, 1);
      LeftEye.setRotationPoint(0F, 14F, 7F);
      LeftEye.setTextureSize(64, 32);
      LeftEye.mirror = true;
      setRotation(LeftEye, 0F, 0F, 0F);
      RightEye = new ModelRenderer(this, 0, 0);
      RightEye.addBox(-5F, -3F, -15F, 3, 2, 1);
      RightEye.setRotationPoint(0F, 14F, 7F);
      RightEye.setTextureSize(64, 32);
      RightEye.mirror = true;
      setRotation(RightEye, 0F, 0F, 0F);
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    ChestLid.render(f5);
    ChestKnob.render(f5);
    ChestBody.render(f5);
    LeftEye.render(f5);
    RightEye.render(f5);
  }
  
  private void setRotation(ModelRenderer model, float x, float y, float z)
  {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
}
