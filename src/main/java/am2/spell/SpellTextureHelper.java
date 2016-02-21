package am2.spell;

import am2.AMCore;
import am2.LogHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@SideOnly(Side.CLIENT)
public class SpellTextureHelper{
	public static final SpellTextureHelper instance = new SpellTextureHelper();

	private static final String iconsPath = "/assets/arsmagica2/textures/items/spells/icons/";
	private static final String iconsPrefix = "/spells/icons/";

	private SpellTextureHelper(){
	}

	public static List<String> getResourceListing() throws IOException, URISyntaxException{
		CodeSource src = AMCore.class.getProtectionDomain().getCodeSource();
		ArrayList<String> toReturn = new ArrayList<String>();
		if (src != null){
			URL jar = src.getLocation();
			if (jar.getProtocol() == "jar"){
				String path = jar.toString().replace("jar:", "").replace("file:", "").replace("!/am2/AMCore.class", "").replace('/', File.separatorChar);
				path = URLDecoder.decode(path, "UTF-8");
				LogHelper.debug(path);
				JarFile jarFile = new JarFile(path);
				Enumeration<JarEntry> entries = jarFile.entries();
				while (entries.hasMoreElements()){
					JarEntry entry = entries.nextElement();
					if (entry.getName().startsWith("assets/arsmagica2/textures/items/spells/icons/")){
						String name = entry.getName().replace("assets/arsmagica2/textures/items/spells/icons/", "");
						if (name.equals("")) continue;
						toReturn.add("spells/icons/" + name.replace(".png", ""));
					}
				}
				jarFile.close();
			}else if (jar.getProtocol() == "file"){
				String path = jar.toURI().toString().replace("/am2/AMCore.class", iconsPath).replace("file:/", "").replace("%20", " ").replace("/", "\\");
				File file = new File(path);
				if (file.exists() && file.isDirectory()){
					for (File sub : file.listFiles()){
						toReturn.add(iconsPrefix + sub.getName().replace(".png", ""));
					}
				}
			}
			return toReturn;
		}else{
			return toReturn;
		}
	}
}
