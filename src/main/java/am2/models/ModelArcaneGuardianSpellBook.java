package am2.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelArcaneGuardianSpellBook extends ModelBase{
	//fields
	ModelRenderer Book;

	public ModelArcaneGuardianSpellBook(){
		textureWidth = 128;
		textureHeight = 128;

		Book = new ModelRenderer(this, 91, 120);
		Book.addBox(-2F, -3F, -1F, 4, 6, 2);
		Book.setRotationPoint(0F, 0F, 0F);
		Book.setTextureSize(128, 128);
		Book.mirror = true;
		setRotation(Book, 0F, 0F, 0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5){
		super.render(entity, f, f1, f2, f3, f4, f5);
		Book.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z){
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

}
