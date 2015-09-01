package am2.proxy;

import java.io.File;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;

@SideOnly(Side.CLIENT)
public class ShadowSkinHelper {
	public static final ResourceLocation locationStevePng = new ResourceLocation("textures/entity/steve.png");
	private ThreadDownloadImageData downloadImageSkin;	
	private ResourceLocation locationSkin;
	
	public ResourceLocation getLocationSkin()
	{
		return this.locationSkin;
	}
	
	public ThreadDownloadImageData getTextureSkin()
	{
		return this.downloadImageSkin;
	}

	public void setupCustomSkin(String mimicUser)
	{
		if (!mimicUser.isEmpty())
		{
			this.locationSkin = getLocationSkin(mimicUser);
			this.downloadImageSkin = getDownloadImageSkin(this.locationSkin, mimicUser);
		}
	}	

	public static ThreadDownloadImageData getDownloadImageSkin(ResourceLocation par0ResourceLocation, String par1Str)
	{
		return getDownloadImage(par0ResourceLocation, getSkinUrl(par1Str), locationStevePng, new ImageBufferDownload());
	}
	
	private static ThreadDownloadImageData getDownloadImage(ResourceLocation par0ResourceLocation, String par1Str, ResourceLocation par2ResourceLocation, IImageBuffer par3IImageBuffer)
	{
		TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
		Object object = texturemanager.getTexture(par0ResourceLocation);

		if (object == null)
		{
			object = new ThreadDownloadImageData((File)null, par1Str, par2ResourceLocation, par3IImageBuffer);
			texturemanager.loadTexture(par0ResourceLocation, (ITextureObject)object);
		}

		return (ThreadDownloadImageData)object;
	}

	public static String getSkinUrl(String par0Str)
	{
		return String.format("http://skins.minecraft.net/MinecraftSkins/%s.png", new Object[] {StringUtils.stripControlCodes(par0Str)});
	}

	public static ResourceLocation getLocationSkin(String par0Str)
	{
		return new ResourceLocation("skins/" + StringUtils.stripControlCodes(par0Str));
	}
}
