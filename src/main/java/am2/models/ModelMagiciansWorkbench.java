package am2.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelMagiciansWorkbench extends ModelBase{
	//fields
	ModelRenderer Leg4;
	ModelRenderer tabletop;
	ModelRenderer Leg1;
	ModelRenderer Leg2;
	ModelRenderer Cloth2;
	ModelRenderer Back2;
	ModelRenderer Back;
	ModelRenderer shelf;
	ModelRenderer Leg3;
	ModelRenderer Shelf2;
	ModelRenderer Cloth1;
	ModelRenderer Drawer;

	public float drawerOffset = 0;

	public ModelMagiciansWorkbench(){
		textureWidth = 64;
		textureHeight = 128;
		setTextureOffset("Drawer.Bottom", 4, 91);
		setTextureOffset("Drawer.Side1", 0, 104);
		setTextureOffset("Drawer.Side2", 0, 104);
		setTextureOffset("Drawer.Handle2", 36, 104);
		setTextureOffset("Drawer.Back", 41, 106);
		setTextureOffset("Drawer.Front", 25, 104);
		setTextureOffset("Drawer.Handle3", 41, 104);
		setTextureOffset("Drawer.Handle1", 36, 104);

		Leg4 = new ModelRenderer(this, 5, 20);
		Leg4.addBox(0F, 0F, 0F, 1, 10, 5);
		Leg4.setRotationPoint(7F, 14F, 3F);
		Leg4.setTextureSize(64, 128);
		Leg4.mirror = true;
		setRotation(Leg4, 0F, 0F, 0F);
		tabletop = new ModelRenderer(this, 0, 0);
		tabletop.addBox(-8F, 0F, -8F, 16, 1, 16);
		tabletop.setRotationPoint(0F, 13F, 0F);
		tabletop.setTextureSize(64, 128);
		tabletop.mirror = true;
		setRotation(tabletop, 0F, 0F, 0F);
		Leg1 = new ModelRenderer(this, 0, 20);
		Leg1.addBox(0F, 0F, 0F, 1, 10, 1);
		Leg1.setRotationPoint(7F, 14F, -8F);
		Leg1.setTextureSize(64, 128);
		Leg1.mirror = true;
		setRotation(Leg1, 0F, 0F, 0F);
		Leg2 = new ModelRenderer(this, 0, 20);
		Leg2.addBox(0F, 0F, 0F, 1, 10, 1);
		Leg2.setRotationPoint(-8F, 14F, -8F);
		Leg2.setTextureSize(64, 128);
		Leg2.mirror = true;
		setRotation(Leg2, 0F, 0F, 0F);
		Cloth2 = new ModelRenderer(this, 0, 76);
		Cloth2.addBox(0F, 0F, 0F, 0, 8, 6);
		Cloth2.setRotationPoint(8F, 14F, -3F);
		Cloth2.setTextureSize(64, 128);
		Cloth2.mirror = true;
		setRotation(Cloth2, 0F, 0F, 0F);
		Back2 = new ModelRenderer(this, 18, 32);
		Back2.addBox(0F, 0F, 0F, 16, 5, 1);
		Back2.setRotationPoint(-8F, 8F, 7F);
		Back2.setTextureSize(64, 128);
		Back2.mirror = true;
		setRotation(Back2, 0F, 0F, 0F);
		Back = new ModelRenderer(this, 18, 20);
		Back.addBox(0F, 0F, 0F, 14, 10, 1);
		Back.setRotationPoint(-7F, 14F, 7F);
		Back.setTextureSize(64, 128);
		Back.mirror = true;
		setRotation(Back, 0F, 0F, 0F);
		shelf = new ModelRenderer(this, 0, 39);
		shelf.addBox(-8F, 0F, -8F, 8, 1, 15);
		shelf.setRotationPoint(1F, 19F, 0F);
		shelf.setTextureSize(64, 128);
		shelf.mirror = true;
		setRotation(shelf, 0F, 0F, 0F);
		Leg3 = new ModelRenderer(this, 5, 20);
		Leg3.addBox(0F, 0F, 0F, 1, 10, 5);
		Leg3.setRotationPoint(-8F, 14F, 3F);
		Leg3.setTextureSize(64, 128);
		Leg3.mirror = true;
		setRotation(Leg3, 0F, 0F, 0F);
		Shelf2 = new ModelRenderer(this, 0, 56);
		Shelf2.addBox(0F, 0F, 0F, 1, 5, 14);
		Shelf2.setRotationPoint(0F, 14F, -8F);
		Shelf2.setTextureSize(64, 128);
		Shelf2.mirror = true;
		setRotation(Shelf2, 0F, 0F, 0F);
		Cloth1 = new ModelRenderer(this, 0, 76);
		Cloth1.addBox(0F, 0F, 0F, 0, 8, 6);
		Cloth1.setRotationPoint(-8F, 14F, -3F);
		Cloth1.setTextureSize(64, 128);
		Cloth1.mirror = true;
		setRotation(Cloth1, 0F, 0F, 0F);
		Drawer = new ModelRenderer(this, "Drawer");
		Drawer.setRotationPoint(4F, 17F, -8F);
		setRotation(Drawer, 0F, 0F, 0F);
		Drawer.mirror = true;
		Drawer.addBox("Bottom", -2F, 0F, 0F, 4, 0, 12);
		Drawer.addBox("Side1", -2F, -3F, 0F, 0, 3, 12);
		Drawer.addBox("Side2", 2F, -3F, 0F, 0, 3, 12);
		Drawer.addBox("Handle2", 0.5F, -2F, -1F, 1, 1, 1);
		Drawer.addBox("Back", -2F, -3F, 12F, 4, 3, 0);
		Drawer.addBox("Front", -2.5F, -3F, 0F, 5, 3, 0);
		Drawer.addBox("Handle3", -1.5F, -2F, -1F, 3, 1, 0);
		Drawer.addBox("Handle1", -1.5F, -2F, -1F, 1, 1, 1);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5){
		/*super.render(entity, f, f1, f2, f3, f4, f5);
		Leg4.render(f5);
		tabletop.render(f5);
		Leg1.render(f5);
		Leg2.render(f5);
		Cloth2.render(f5);
		Back2.render(f5);
		Back.render(f5);
		shelf.render(f5);
		Leg3.render(f5);
		Shelf2.render(f5);
		Cloth1.render(f5);
		Drawer.render(f5);*/
	}

	public void renderModel(int meta){
		float f5 = 0.0625F;
		Leg4.render(f5);
		tabletop.render(f5);
		Leg1.render(f5);
		Leg2.render(f5);
		Cloth2.render(f5);
		Back2.render(f5);
		Back.render(f5);
		shelf.render(f5);
		Leg3.render(f5);
		Shelf2.render(f5);
		Cloth1.render(f5);

		Drawer.offsetZ = -drawerOffset;
		Drawer.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z){
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

}
