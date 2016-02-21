package thehippomaster.AnimationExample.client;

import thehippomaster.AnimationAPI.IAnimatedEntity;
import thehippomaster.AnimationAPI.client.Animator;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelTest extends ModelBase {
	
	public ModelRenderer body, head;
	private Animator animator;
	
	public static final float PI = (float)Math.PI;
	
	public ModelTest() {
		body = new ModelRenderer(this, 32, 0);
		body.addBox(-3F, -10F, -3F, 6, 10, 6);
		body.setRotationPoint(0F, 24F, 0F);
		head = new ModelRenderer(this, 0, 0);
		head.addBox(-4F, -8F, -4F, 8, 8, 8);
		head.setRotationPoint(0F, -10F, 0F);
		body.addChild(head);
		
		animator = new Animator(this);
	}
	
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		animate((IAnimatedEntity)entity, f, f1, f2, f3, f4, f5);
		body.render(f5);
	}
	
	public void setAngles() {
		//reset the rotation point each render tick
		body.rotationPointY = 24F;
	}
	
	public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		animator.update(entity);
		setAngles();
		
		animator.setAnim(1);
		animator.startPhase(10);
			animator.rotate(head, -PI / 6F, 0F, 0F);
			animator.rotate(body, -PI / 6F, 0F, 0F);
		animator.endPhase();
		animator.startPhase(4);
			animator.rotate(body, PI / 2F, 0F, 0F);
			animator.move(body, 0F, -2F, 0F);
		animator.endPhase();
		animator.setStationaryPhase(6);
		animator.resetPhase(10);
		
		animator.setAnim(2);
		animator.startPhase(10);
			animator.rotate(head, 0F, -PI / 4F, 0F);
			animator.rotate(body, 0F, -PI / 8F, 0F);
		animator.endPhase();
		animator.startPhase(10);
			animator.rotate(head, 0F, PI / 4F, 0F);
			animator.rotate(body, 0F, PI / 8F, 0F);
		animator.endPhase();
		animator.resetPhase(10);
	}
}
