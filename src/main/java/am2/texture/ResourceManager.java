package am2.texture;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;


public class ResourceManager{

	public static String getOverrideItemTexturePath(){
		return "textures/items/";
	}

	public static String getOverrideBlockTexturePath(){
		return "textures/blocks";
	}

	public static String getOverrideTexturesPath(){
		return "textures";
	}

	public static String GetGuiTexturePath(String textureFileName){
		return String.format("%s/guis/%s", getOverrideTexturesPath(), textureFileName);
	}

	public static String getCustomBlockTexturePath(String textureFileName){
		return String.format("%s/custom/%s", getOverrideBlockTexturePath(), textureFileName);
	}

	public static ResourceLocation getOBJFilePath(String objFileName){
		return new ResourceLocation("arsmagica2", String.format("obj/%s", objFileName));
	}

	public static String GetFXTexturePath(String textureFileName){
		return String.format("%s/items/particles/%s", getOverrideTexturesPath(), textureFileName);
	}

	public static String GetSpellIconTexturePath(String textureFileName){
		return String.format("%s/spell_icons/%s", getOverrideTexturesPath(), textureFileName);
	}

	public static String getMobTexturePath(String textureFileName){
		return String.format("%s/mobs/%s", getOverrideTexturesPath(), textureFileName);
	}

	public static TextureAtlasSprite RegisterTexture(String textureFile, TextureMap IIconRegister){
		if (!textureFile.contains(":")){
			textureFile = "arsmagica2:" + textureFile;
		}
		if (textureFile.endsWith(".png")){
			textureFile = textureFile.substring(0, textureFile.lastIndexOf(".png"));
		}
		return IIconRegister.registerSprite(new ResourceLocation(textureFile));
	}
}
