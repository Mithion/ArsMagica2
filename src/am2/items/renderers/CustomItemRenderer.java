package am2.items.renderers;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import am2.api.spell.enums.Affinity;
import am2.bosses.models.ModelPlantGuardianSickle;
import am2.items.ItemsCommonProxy;
import am2.proxy.gui.ModelLibrary;
import am2.texture.ResourceManager;

public class CustomItemRenderer implements IItemRenderer{

	private Minecraft mc;

	private static final ResourceLocation sickleLocation = new ResourceLocation("arsmagica2", ResourceManager.getMobTexturePath("bosses/plant_guardian.png"));
	private static final ResourceLocation arcaneBookLocation = new ResourceLocation("arsmagica2", ResourceManager.getMobTexturePath("bosses/arcane_guardian.png"));
	private static final ResourceLocation winterArmLocation = new ResourceLocation("arsmagica2", ResourceManager.getMobTexturePath("bosses/ice_guardian.png"));
	private static final ResourceLocation airLocation = new ResourceLocation("arsmagica2", ResourceManager.getMobTexturePath("bosses/air_guardian.png"));
	private static final ResourceLocation waterLocation = new ResourceLocation("arsmagica2", ResourceManager.getMobTexturePath("bosses/water_guardian.png"));
	private static final ResourceLocation fireLocation = new ResourceLocation("arsmagica2", ResourceManager.getMobTexturePath("bosses/fire_guardian.png"));
	private static final ResourceLocation earthLocation = new ResourceLocation("arsmagica2", ResourceManager.getMobTexturePath("bosses/earth_guardian.png"));
	private static final ResourceLocation broomLocation = new ResourceLocation("arsmagica2", ResourceManager.getMobTexturePath("broom.png"));
	private static final ResourceLocation candleLocation = new ResourceLocation("arsmagica2", ResourceManager.getCustomBlockTexturePath("candle.png"));

	protected ModelPlantGuardianSickle modelSickle;

	public static final CustomItemRenderer instance = new CustomItemRenderer();

	private CustomItemRenderer(){
		mc = Minecraft.getMinecraft();
		modelSickle = new ModelPlantGuardianSickle();
		modelSickle.setNoSpin();
	}

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return type == ItemRenderType.ENTITY || type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON || type == ItemRenderType.INVENTORY;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		bindTextureByItem(item);

		GL11.glPushMatrix();

		setupItemRender(type, item);

		renderModelByItem(item);

		GL11.glPopMatrix();

		if (item.getItem() == ItemsCommonProxy.wardingCandle && (type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON)){
			renderCandleFlame(type, item, data);
		}
	}

	private void renderCandleFlame(ItemRenderType type, ItemStack item, Object...data){

		if (item.hasTagCompound()){
			GL11.glColor3f(item.stackTagCompound.getFloat("flame_red"),
					item.stackTagCompound.getFloat("flame_green"),
					item.stackTagCompound.getFloat("flame_blue"));
		}
		GL11.glPushMatrix();
		if (type == ItemRenderType.EQUIPPED){
			GL11.glTranslatef(0.2f, 1f, 0f);
			GL11.glScalef(0.5f, 0.5f, 0.5f);
		}else if (type == ItemRenderType.EQUIPPED_FIRST_PERSON){
			GL11.glTranslatef(-1.5f, 1.85f, 0.9f);
			GL11.glScalef(0.15f, 0.15f, 0.15f);
		}
		SpellScrollRenderer.instance.renderEffect(Affinity.FIRE, false, data);
		GL11.glPopMatrix();
	}

	private void setupItemRender(ItemRenderType type, ItemStack stack){
		float scale = 1.0F;
		switch (type) {
		case ENTITY:
			if (stack.getItem() == ItemsCommonProxy.magicBroom){
				GL11.glRotatef(90F, 1, 0, 0);
				GL11.glTranslatef(0, 0, -3f);
			}else if (stack.getItem() == ItemsCommonProxy.arcaneSpellbook){
				GL11.glTranslatef(0, 0.6f, 0);
			}else if (stack.getItem() == ItemsCommonProxy.earthGuardianArmor){
				GL11.glRotatef(90, 1, 0, 0);
				GL11.glTranslatef(0, 0, -3f);
			}else if (stack.getItem() == ItemsCommonProxy.fireEars){
				GL11.glRotatef(90, 1, 0, 0);
				GL11.glTranslatef(0, 0, -1f);
			}else if (stack.getItem() == ItemsCommonProxy.waterGuardianOrbs){
				GL11.glRotatef(90, 1, 0, 0);
				GL11.glTranslatef(0, 0, -2f);
			}else if (stack.getItem() == ItemsCommonProxy.airGuardianLower){
				GL11.glRotatef(90, 1, 0, 0);
				GL11.glTranslatef(0, 0, -2f);
			}else if (stack.getItem() == ItemsCommonProxy.wardingCandle){
				GL11.glRotatef(90, 1, 0, 0);
				GL11.glTranslatef(0, 0, -3f);
			}
			scale = 1.6f;
			break;
		case EQUIPPED_FIRST_PERSON:
			if (stack.getItem() == ItemsCommonProxy.magicBroom){
				GL11.glRotatef(135F, 0, 1, 0);
				GL11.glRotatef(-90F, 1, 0, 0);
				GL11.glTranslatef(-0.5f, 1f,0f);
			}else if (stack.getItem() == ItemsCommonProxy.arcaneSpellbook){
				GL11.glRotatef(270F, 0.0F, 1.0F, 0.0F);
				GL11.glTranslatef(0.5f, 1f, -0.5f);
			}else if (stack.getItem() == ItemsCommonProxy.winterGuardianArm){
				GL11.glRotatef(60, 1, -0.6f, 0);
				GL11.glTranslatef(1f, 0.5f, -1);
			}else if (stack.getItem() == ItemsCommonProxy.winterGuardianArm){
				GL11.glRotatef(180, 1, 0, 0);
				GL11.glTranslatef(0, 0, -1);
			}else if (stack.getItem() == ItemsCommonProxy.earthGuardianArmor){
				GL11.glRotatef(90, 1, 0, 0);
				GL11.glTranslatef(0f, 0.7f, -2f);
			}else if (stack.getItem() == ItemsCommonProxy.fireEars){
				GL11.glRotatef(90, 0, 1, 0);
				GL11.glTranslatef(-0.8f, 1.2f, -1f);
			}else if (stack.getItem() == ItemsCommonProxy.waterGuardianOrbs){
				GL11.glRotatef(45, 0, 1, 0);
				GL11.glTranslatef(0f, 0.8f, -0.5f);
			}else if (stack.getItem() == ItemsCommonProxy.airGuardianLower){
				GL11.glTranslatef(0, 0.8f, -0.8f);
			}else if (stack.getItem() == ItemsCommonProxy.wardingCandle){
				GL11.glRotatef(90F, 1, 0, 0);
				GL11.glTranslatef(-1f, 0.75f, -3.5f);
			}else{
				GL11.glRotatef(135F, 0, 1, 0);
				GL11.glTranslatef(-0.55f, -0.35f,-1f);
			}
			break;
		case EQUIPPED:
			scale = 1.6f;
			GL11.glRotatef(-135F, 0, 1, 0);
			if (stack.getItem() == ItemsCommonProxy.magicBroom){
				GL11.glRotatef(20F, 1, 0, 0);
				GL11.glTranslatef(-0.3f, 0f,-1.5f);
			}else if (stack.getItem() == ItemsCommonProxy.arcaneSpellbook){
				GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(120F, 1.0F, 0.0F, 0f);
				GL11.glTranslatef(-0.05f, 0.1f, -1f);
			}else if (stack.getItem() == ItemsCommonProxy.winterGuardianArm){
				GL11.glRotatef(180, 1, 0, 0);
				GL11.glTranslatef(0, 0, -1);
			}else if (stack.getItem() == ItemsCommonProxy.earthGuardianArmor){
				GL11.glRotatef(60F, 1, 0, 0);
				GL11.glTranslatef(-1f, -1f,-1.2f);
			}else if (stack.getItem() == ItemsCommonProxy.fireEars){
				GL11.glTranslatef(0, 0.15f, -2f);
			}else if (stack.getItem() == ItemsCommonProxy.waterGuardianOrbs){
				GL11.glRotatef(45, 0, 0, 1);
				GL11.glTranslatef(-0.7f, -0.7f, -3f);
			}else if (stack.getItem() == ItemsCommonProxy.airGuardianLower){
				GL11.glTranslatef(0, 0.2f, -2.5f);
			}else if (stack.getItem() == ItemsCommonProxy.wardingCandle){
				GL11.glRotatef(90F, 1, 0, 0);
				GL11.glTranslatef(-0.3f, -0.75f, -3.5f);
			}else{
				GL11.glRotatef(60F, 1, 0, 0);
				GL11.glTranslatef(-0.55f, -0.75f,-1f);
			}
			break;
		case INVENTORY:
			scale = 0.5f;
			GL11.glTranslatef(0.25f, 0, 0);
			if (stack.getItem() == ItemsCommonProxy.magicBroom){
				GL11.glRotatef(50F, 1, 0, 0);
				GL11.glTranslatef(-0.5F, 0F, -1.5F);
				scale = 1.0f;
			}else if (stack.getItem() == ItemsCommonProxy.winterGuardianArm){
				scale = 1.0f;
				GL11.glTranslatef(0.5f, 0.5f, 0);
			}else if (stack.getItem() == ItemsCommonProxy.arcaneSpellbook){
				GL11.glRotatef(240f, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(45f, 1.0F, 0.0F, 0.0F);
				GL11.glTranslatef(0.2f, 0.1f, 0);
			}else if (stack.getItem() == ItemsCommonProxy.earthGuardianArmor){
				GL11.glRotatef(90, 1, 0, 0);
				scale=1.0f;
				GL11.glTranslatef(0.2f, 0.8f, -1.6f);
			}else if (stack.getItem() == ItemsCommonProxy.fireEars){
				GL11.glRotatef(90, 1, 0, 0.7f);
				scale=1.7f;
				GL11.glTranslatef(0.2f, 0.2f, -0.8f);
			}else if (stack.getItem() == ItemsCommonProxy.waterGuardianOrbs){
				GL11.glRotatef(90, 0, 1, 1);
				scale=1.2f;
				GL11.glTranslatef(-0.4f, 0.3f, -0.4f);
			}else if (stack.getItem() == ItemsCommonProxy.airGuardianLower){
				scale=2.4f;
				GL11.glTranslatef(0f, 0.45f, -1.75f);
			}else if (stack.getItem() == ItemsCommonProxy.wardingCandle){
				scale = 2.4f;
				GL11.glRotatef(90, 1, 0, 0);
				GL11.glTranslatef(-0.5f, 0, -4);
			}
		default:
			break;
		}

		if (stack.getItem() == ItemsCommonProxy.arcaneSpellbook){
			scale = 3.5f;
		}else if (stack.getItem() == ItemsCommonProxy.winterGuardianArm){

		}else{
			GL11.glRotatef(90F, 1.0F, 0.0F, 0.0F);
			GL11.glTranslatef(0.3f, 0.75f, 0);
		}
		GL11.glScalef(scale, scale, scale);
	}

	private void renderSickle(ItemRenderType type, ItemStack item, Object... data){
		switch (type){
		case ENTITY:
			renderModelByItem(item);
			break;
		case EQUIPPED:
			renderModelByItem(item);
			break;
		case EQUIPPED_FIRST_PERSON:
			renderModelByItem(item);
			break;
		case FIRST_PERSON_MAP:
			return;
		case INVENTORY:
			renderModelByItem(item);
		default:
			break;
		}
	}

	private void renderModelByItem(ItemStack stack){
		if (stack == null)
			return;

		if (stack.getItem() == ItemsCommonProxy.scythe)
			ModelLibrary.instance.sickle.render(null, 0, 0, 0, 0, 0, 0.0625F);
		else if (stack.getItem() == ItemsCommonProxy.magicBroom)
			ModelLibrary.instance.magicBroom.render(ModelLibrary.instance.dummyBroom, 0, 0, 0, 0, 0, 0.0625F);
		else if (stack.getItem() == ItemsCommonProxy.arcaneSpellbook)
			ModelLibrary.instance.dummyArcaneSpellbook.render(null, 0, 0, 0, 0, 0, 0.0625F);
		else if (stack.getItem() == ItemsCommonProxy.winterGuardianArm)
			ModelLibrary.instance.winterGuardianArm.render(null, 0, 0, 0, 0, 0, 0.0625F);
		else if (stack.getItem() == ItemsCommonProxy.airGuardianLower)
			ModelLibrary.instance.airSled.render(null, 0, 0, 0, 0, 0, 0.0625F);
		else if (stack.getItem() == ItemsCommonProxy.waterGuardianOrbs)
			ModelLibrary.instance.waterOrbs.render(null, 0, 0, 0, 0, 0, 0.0625F);
		else if (stack.getItem() == ItemsCommonProxy.fireEars)
			ModelLibrary.instance.fireEars.render(null, 0, 0, 0, 0, 0, 0.0625F);
		else if (stack.getItem() == ItemsCommonProxy.earthGuardianArmor)
			ModelLibrary.instance.earthArmor.render(null, 0, 0, 0, 0, 0, 0.0625F);
		else if (stack.getItem() == ItemsCommonProxy.wardingCandle){
			ModelLibrary.instance.wardingCandle.render(0.0625f);
		}
	}

	private void bindTextureByItem(ItemStack stack){
		if (stack == null)
			return;

		if (stack.getItem() == ItemsCommonProxy.scythe)
			Minecraft.getMinecraft().renderEngine.bindTexture(sickleLocation);
		else if (stack.getItem() == ItemsCommonProxy.magicBroom)
			Minecraft.getMinecraft().renderEngine.bindTexture(broomLocation);
		else if (stack.getItem() == ItemsCommonProxy.arcaneSpellbook)
			Minecraft.getMinecraft().renderEngine.bindTexture(arcaneBookLocation);
		else if (stack.getItem() == ItemsCommonProxy.winterGuardianArm)
			Minecraft.getMinecraft().renderEngine.bindTexture(winterArmLocation);
		else if (stack.getItem() == ItemsCommonProxy.airGuardianLower)
			Minecraft.getMinecraft().renderEngine.bindTexture(airLocation);
		else if (stack.getItem() == ItemsCommonProxy.waterGuardianOrbs)
			Minecraft.getMinecraft().renderEngine.bindTexture(waterLocation);
		else if (stack.getItem() == ItemsCommonProxy.fireEars)
			Minecraft.getMinecraft().renderEngine.bindTexture(fireLocation);
		else if (stack.getItem() == ItemsCommonProxy.earthGuardianArmor)
			Minecraft.getMinecraft().renderEngine.bindTexture(earthLocation);
		else if (stack.getItem() == ItemsCommonProxy.wardingCandle)
			Minecraft.getMinecraft().renderEngine.bindTexture(candleLocation);
	}

}
