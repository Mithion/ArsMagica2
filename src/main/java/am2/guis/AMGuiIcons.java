package am2.guis;

import am2.texture.ResourceManager;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;


public class AMGuiIcons{
	public static boolean initialized = false;
	public static TextureAtlasSprite manaBar;
	public static TextureAtlasSprite manaLevel;

	public static TextureAtlasSprite fatigueIcon;
	public static TextureAtlasSprite fatigueBar;
	public static TextureAtlasSprite fatigueLevel;

	public static TextureAtlasSprite padlock;
	public static TextureAtlasSprite gatewayPortal;
	public static TextureAtlasSprite evilBook;
	public static TextureAtlasSprite selectedRunes;

	public static TextureAtlasSprite warning;
	public static TextureAtlasSprite checkmark;
	public static TextureAtlasSprite newEntry;

	public static TextureMap guiMap;

	public static AMGuiIcons instance = new AMGuiIcons();


	private AMGuiIcons(){

	}

	public void init(TextureMap IIconRegister){
		manaBar = ResourceManager.RegisterTexture("arsmagica2:gui_icons/Mana_Bar", IIconRegister);
		manaLevel = ResourceManager.RegisterTexture("arsmagica2:gui_icons/Mana_Level", IIconRegister);

		fatigueIcon = ResourceManager.RegisterTexture("arsmagica2:gui_icons/Fatigue_Icon", IIconRegister);
		fatigueBar = ResourceManager.RegisterTexture("arsmagica2:gui_icons/Fatigue_Bar", IIconRegister);
		fatigueLevel = ResourceManager.RegisterTexture("arsmagica2:gui_icons/Fatigue_Level", IIconRegister);

		padlock = ResourceManager.RegisterTexture("arsmagica2:gui_icons/padlock", IIconRegister);
		warning = ResourceManager.RegisterTexture("arsmagica2:gui_icons/update_available", IIconRegister);
		checkmark = ResourceManager.RegisterTexture("arsmagica2:gui_icons/up_to_date", IIconRegister);

		newEntry = ResourceManager.RegisterTexture("arsmagica2:gui_icons/new", IIconRegister);

		evilBook = ResourceManager.RegisterTexture("arsmagica2:evilBook", IIconRegister);

		gatewayPortal = ResourceManager.RegisterTexture("arsmagica2:gui_icons/gateway.png", IIconRegister);

		selectedRunes = ResourceManager.RegisterTexture("arsmagica2:gui_icons/rune_selected_aura.png", IIconRegister);
	}
}
