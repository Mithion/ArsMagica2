package am2.guis;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import am2.texture.ResourceManager;


public class AMGuiIcons {
	public static boolean initialized = false;
	public static IIcon manaBar;
	public static IIcon manaLevel;

	public static IIcon fatigueIcon;
	public static IIcon fatigueBar;
	public static IIcon fatigueLevel;

	public static IIcon padlock;
	public static IIcon gatewayPortal;
	public static IIcon evilBook;
	public static IIcon selectedRunes;

	public static IIcon warning;
	public static IIcon checkmark;
	public static IIcon newEntry;

	public static TextureMap guiMap;

	public static AMGuiIcons instance = new AMGuiIcons();


	private AMGuiIcons(){

	}

	public void init(IIconRegister IIconRegister){
		manaBar = ResourceManager.RegisterTexture("arsmagica2:gui_icons/Mana_Bar", IIconRegister);
		manaLevel = ResourceManager.RegisterTexture("arsmagica2:gui_icons/Mana_Level", IIconRegister);

		fatigueIcon = ResourceManager.RegisterTexture("arsmagica2:gui_icons/Fatigue_Icon", IIconRegister);
		fatigueBar = ResourceManager.RegisterTexture("arsmagica2:gui_icons/Fatigue_Bar", IIconRegister);
		fatigueLevel = ResourceManager.RegisterTexture("arsmagica2:gui_icons/Fatigue_Level", IIconRegister);

		padlock = ResourceManager.RegisterTexture("arsmagica2:gui_icons/padlock", IIconRegister);
		warning = ResourceManager.RegisterTexture("arsmagica2:gui_icons/update_available", IIconRegister);
		checkmark = ResourceManager.RegisterTexture("arsmagica2:gui_icons/up_to_date", IIconRegister);

		newEntry = ResourceManager.RegisterTexture("arsmagica2:gui_icons/new", IIconRegister);

		evilBook= ResourceManager.RegisterTexture("arsmagica2:evilBook", IIconRegister);

		gatewayPortal = ResourceManager.RegisterTexture("arsmagica2:gui_icons/gateway.png", IIconRegister);

		selectedRunes = ResourceManager.RegisterTexture("arsmagica2:gui_icons/rune_selected_aura.png", IIconRegister);
	}
}
