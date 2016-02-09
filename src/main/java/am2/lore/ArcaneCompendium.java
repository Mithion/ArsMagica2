package am2.lore;

import am2.AMCore;
import am2.LogHelper;
import am2.api.ILoreHelper;
import am2.api.spell.component.interfaces.ISpellComponent;
import am2.api.spell.component.interfaces.ISpellModifier;
import am2.api.spell.component.interfaces.ISpellShape;
import am2.items.ItemsCommonProxy;
import am2.playerextensions.ExtendedProperties;
import am2.proxy.tick.ClientTickHandler;
import am2.utility.WebRequestUtils;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.Language;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.*;

@SideOnly(Side.CLIENT)
public class ArcaneCompendium implements ILoreHelper{

	public static final ArcaneCompendium instance = new ArcaneCompendium();

	public static final String KEYWORD_NEWPAGE = "!p";
	public static final String KEYWORD_NEWLINE = "!l";
	public static final String KEYWORD_DOUBLENEWLINE = "!d";

	public static Achievement compendiumData = (new Achievement("am2_ach_data", "compendiumData", 0, 0, ItemsCommonProxy.arcaneCompendium, null));
	public static Achievement componentUnlock = (new Achievement("am2_ach_unlock", "componentUnlock", 0, 0, ItemsCommonProxy.spellParchment, null));

	private final TreeMap<String, CompendiumEntry> compendium;
	private final TreeMap<String, String> aliases;
	private final TreeMap<String, String> zeroItemTexts;

	private String saveFileLocation;
	private String updatesFolderLocation;
	private boolean hasLoaded = false;
	private boolean forcePackagedCompendium = false;

	private String languageCode = "";
	private String MCVersion = "";
	private String modVersion = "";
	private String compendiumVersion = "";

	private String latestModVersion = "";
	private String latestDownloadLink = "";
	private String latestPatchNotesLink = "";
	private boolean modUpdateAvailable = false;

	private ArcaneCompendium(){
		compendium = new TreeMap<String, CompendiumEntry>();
		aliases = new TreeMap<String, String>();
		zeroItemTexts = new TreeMap<String, String>();
	}

	public void setSaveLocation(String saveFileLocation){
		this.saveFileLocation = saveFileLocation + File.separatorChar + "compendiumunlocks";
		this.updatesFolderLocation = saveFileLocation + File.separatorChar + "compendiumUpdates";
		try{
			File file = new File(this.saveFileLocation);
			if (!file.exists())
				file.mkdirs();

			file = new File(updatesFolderLocation);
			if (!file.exists())
				file.mkdirs();
		}catch (Throwable t){
			LogHelper.error("Could not create save location!");
			t.printStackTrace();
		}
	}

	private String getWorldName(){
		if (ClientTickHandler.worldName == null)
			return null;
		return ClientTickHandler.worldName.replaceAll("[^\\x00-\\x7F]", "");
	}

	public void saveUnlockData(){
		if (!hasLoaded)
			return;

		if (saveFileLocation != null && getWorldName() != null){
			try{
				File file = new File(saveFileLocation + File.separatorChar + getWorldName() + ".txt");
				if (!file.exists())
					file.createNewFile();
				BufferedWriter writer = new BufferedWriter(new FileWriter(file));

				for (CompendiumEntry entry : compendium.values()){
					String id = entry.getID() + "|U";
					if (entry.isLocked)
						id += "L";
					if (entry.isNew)
						id += "N";
					writer.write(id + "\n");
				}

				writer.close();
			}catch (IOException e){
				LogHelper.error("Compendium unlock state failed to save!");
				e.printStackTrace();
			}

		}
	}

	public void loadUnlockData(){

		if (!AMCore.config.stagedCompendium()){
			for (CompendiumEntry entry : compendium.values()){
				entry.isLocked = false;
				entry.isNew = false;
			}
			return;
		}

		if (saveFileLocation != null && getWorldName() != null){
			try{
				hasLoaded = true;

				File file = new File(saveFileLocation + File.separatorChar + getWorldName() + ".txt");
				if (!file.exists()){
					LogHelper.info("Compendium unlock state not found to load.  Assuming it hasn't been created yet.");
					return;
				}
				BufferedReader reader = new BufferedReader(new FileReader(file));

				String s;
				while ((s = reader.readLine()) != null){
					String[] split = s.trim().replace("\n", "").replace("\r", "").split("\\|");
					if (split.length != 2)
						continue;
					CompendiumEntry entry = this.getEntryAbsolute(split[0]);
					if (entry == null) continue;

					entry.isLocked = split[1].contains("L");
					entry.isNew = split[1].contains("N");
				}

				reader.close();
			}catch (IOException e){
				LogHelper.error("Compendium unlock state failed to load!");
				e.printStackTrace();
			}
		}
	}

	public void addAlias(String alias, String baseItem){
		aliases.put(alias, baseItem);
	}

	public void init(Language lang){
		if (lang == null){
			LogHelper.error("Got a current language of NULL from Minecraft?!?  The compendium cannot load!");
			return;
		}
		MCVersion = (String)ReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), "field_110447_Z", "launchedVersion");
		languageCode = lang.getLanguageCode().trim();
		modVersion = AMCore.instance.getVersion();

		compendium.clear();
		aliases.clear();
		zeroItemTexts.clear();

		//check for mod updates
		if (AMCore.config.allowVersionChecks())
			checkForModUpdates();
		else
			LogHelper.info("Skipping version check due to config");

		//load the version of the compendium only
		loadDocumentVersion(lang);

		//check for compendium updates
		if (AMCore.config.allowCompendiumUpdates())
			updateCompendium(lang);
		else
			LogHelper.info("Skipping Compendium auto-update due to config");

		//get the compendium stream again, either from an updated version or the default packaged one
		InputStream stream = getCompendium(lang);
		if (stream == null){
			LogHelper.error("Unable to load the Arcane Compendium!");
			return;
		}
		//load the entire document
		loadDocument(stream);
		//cleanup
		try{
			stream.close();
		}catch (IOException e){
			e.printStackTrace();
		}
	}

	private boolean updateCompendium(Language lang){
		if (!AMCore.config.allowCompendiumUpdates())
			return false;

		try{
			String txt = WebRequestUtils.sendPost("http://arcanacraft.qorconcept.com/mc/CompendiumVersioning.txt", new HashMap<String, String>());
			String[] lines = txt.replace("\r\n", "\n").split("\n");
			for (String s : lines){
				if (s.startsWith("#"))
					continue;
				String[] sections = s.split("\\|");

				//check MC version and language
				if (!sections[1].trim().equalsIgnoreCase(languageCode) || !MCVersion.startsWith(sections[0].trim()))
					continue;
				if (versionCompare(sections[2], this.compendiumVersion) > 0){
					if (modVersion.startsWith(sections[3])){
						//if we're here, an update is needed!
						String compendiumFileName = "ArcaneCompendium_" + lang.getLanguageCode() + ".xml";
						String compendiumData = WebRequestUtils.sendPost("http://arcanacraft.qorconcept.com/mc/" + MCVersion + "/" + compendiumFileName, new HashMap<String, String>());

						//we have the data, save it to the local repository
						saveCompendiumData(compendiumData, compendiumFileName);
						LogHelper.info("Updated Compendium");
						return true;
					}
				}
			}
		}catch (Throwable t){
			LogHelper.warn("Unable to update the compendium!");
			t.printStackTrace();
		}
		return false;
	}

	private void checkForModUpdates(){
		try{
			LogHelper.info("Checking Version.  MC Version: %s", MCVersion);
			this.latestModVersion = this.modVersion;
			String txt = WebRequestUtils.sendPost("http://arcanacraft.qorconcept.com/mc/AM2Versioning.txt", new HashMap<String, String>());
			String[] lines = txt.replace("\r\n", "\n").split("\n");
			for (String s : lines){
				if (s.startsWith("#"))
					continue;
				String[] sections = s.split("\\|");
				if (!MCVersion.startsWith(sections[0].trim()))
					continue;
				if (versionCompare(sections[1], this.latestModVersion) > 0){
					LogHelper.info("An update is available.  Version %s is released, detected local version of %s.", this.modVersion, sections[1]);
					this.latestModVersion = sections[1];
					this.latestDownloadLink = sections.length >= 3 ? sections[2] : "";
					this.latestPatchNotesLink = sections.length >= 4 ? sections[3] : "";
					modUpdateAvailable = true;
				}else{
					LogHelper.info("You are running the latest version of AM2.  Latest Released Version: %s.  Your Version: %s.", this.latestModVersion, this.modVersion);
				}
			}

		}catch (Throwable t){
			t.printStackTrace();
		}
	}

	private void saveCompendiumData(String compendium, String fileName) throws Exception{
		if (compendium == null){
			throw new Exception("No compendium data received");
		}
		String path = updatesFolderLocation + File.separatorChar + MCVersion + File.separatorChar + modVersion;
		File dirPath = new File(path);
		if (!dirPath.exists())
			dirPath.mkdirs();
		File f = new File(path + File.separatorChar + fileName);
		FileUtils.writeStringToFile(f, compendium);
	}

	/**
	 * Gets the entry, with alias checks.
	 */
	public CompendiumEntry getEntry(String key){
		if (aliases.containsKey(key)){
			CompendiumEntry aliased = compendium.get(aliases.get(key));
			if (aliased.hasSubItems()){
				for (CompendiumEntry sub : aliased.getSubItems()){
					if (sub.getID().equals(key)){
						return sub;
					}
				}
			}
			return aliased;
		}
		return compendium.get(key);
	}

	/**
	 * Gets the entry without alias checks.
	 */
	public CompendiumEntry getEntryAbsolute(String key){
		return compendium.get(key);
	}

	public String getZeroItemText(String category){
		String s = zeroItemTexts.get(category);
		return s != null ? s : "";
	}

	private void loadDocument(InputStream stream){
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try{
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.parse(stream);

			Node parentNode = document.getChildNodes().item(0);

			NodeList categories = parentNode.getChildNodes();

			for (int i = 0; i < categories.getLength(); ++i){
				Node category = categories.item(i);

				if (category.getNodeName().equals("version")){
					this.compendiumVersion = category.getTextContent();
					continue;
				}

				if (category.getAttributes() != null){
					Node zeroItemText = category.getAttributes().getNamedItem("zeroItemText");
					if (zeroItemText != null){
						zeroItemTexts.put(category.getNodeName(), zeroItemText.getTextContent());
					}
				}

				NodeList entries = category.getChildNodes();
				for (int j = 0; j < entries.getLength(); ++j){
					Node entry = entries.item(j);
					CompendiumEntryType type = CompendiumEntryTypes.getForSection(category.getNodeName(), entry.getNodeName());
					if (type == null) continue;
					CompendiumEntry ce = type.createCompendiumEntry(entry);
					if (ce == null) continue;
					this.compendium.put(ce.getID(), ce);
				}
			}

		}catch (Throwable t){
			t.printStackTrace();
		}
	}

	private void loadDocumentVersion(Language lang){
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try{
			//get the packaged compendium version
			InputStream stream = getPackagedCompendium(lang);
			//standard XML parsing stuff
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.parse(stream);

			Node parentNode = document.getChildNodes().item(0);

			NodeList categories = parentNode.getChildNodes();

			//look for the version node
			for (int i = 0; i < categories.getLength(); ++i){
				Node category = categories.item(i);

				//got it!
				if (category.getNodeName().equals("version")){
					this.compendiumVersion = category.getTextContent();
					break;
				}
			}
			stream.close();

			//get the downloaded version (or re-get the packaged version if there is no downloaded one)
			stream = getCompendium(lang);
			//standard parsing
			db = dbf.newDocumentBuilder();
			document = db.parse(stream);

			parentNode = document.getChildNodes().item(0);

			categories = parentNode.getChildNodes();

			//find version node
			for (int i = 0; i < categories.getLength(); ++i){
				Node category = categories.item(i);

				//got it!
				if (category.getNodeName().equals("version")){
					//get this one's version
					String altVersion = category.getTextContent();
					//compare versions - is the packaged compendium actually newer?
					if (versionCompare(altVersion, this.compendiumVersion) > 0)
						this.compendiumVersion = altVersion; //nope
					else
						this.forcePackagedCompendium = true; //yep
					break;
				}
			}
			stream.close();
		}catch (Throwable t){
			t.printStackTrace();
		}
	}

	public Integer versionCompare(String str1, String str2){
		String[] vals1 = str1.split("\\.");
		String[] vals2 = str2.split("\\.");
		int i = 0;
		// set index to first non-equal ordinal or length of shortest version string
		while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i])){
			i++;
		}
		// compare first non-equal ordinal number
		if (i < vals1.length && i < vals2.length){
			int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
			return Integer.signum(diff);
		}
		// the strings are equal or one string is a substring of the other
		// e.g. "1.2.3" = "1.2.3" or "1.2.3" < "1.2.3.4"
		else{
			return Integer.signum(vals1.length - vals2.length);
		}
	}

	private String getUpdatedPath(){
		String compendiumFileName = "ArcaneCompendium_" + languageCode + ".xml";
		return updatesFolderLocation + File.separatorChar + MCVersion + File.separatorChar + AMCore.instance.getVersion() + File.separatorChar + compendiumFileName;
	}

	private InputStream getCompendium(Language lang){

		if (!forcePackagedCompendium){
			String path = getUpdatedPath();

			try{
				File f = new File(path);
				if (f.exists())
					return new FileInputStream(f);
			}catch (Throwable t){
				LogHelper.trace("An error occurred when trying to create an inputstream from the updated compendium.  Reverting to default.");
			}
			LogHelper.info("No updated compendium found.  Using packaged compendium.");
		}
		return getPackagedCompendium(lang);
	}

	private InputStream getPackagedCompendium(Language lang){
		ResourceLocation rLoc = new ResourceLocation("arsmagica2", String.format("docs/ArcaneCompendium_%s.xml", lang.getLanguageCode()));
		IResource resource = null;
		try{
			resource = Minecraft.getMinecraft().getResourceManager().getResource(rLoc);
		}catch (IOException e){
		}finally{
			if (resource == null){
				LogHelper.info("Unable to find localized compendium.  Defaulting to en_US");
				rLoc = new ResourceLocation("arsmagica2", "docs/ArcaneCompendium_en_US.xml");
				try{
					resource = Minecraft.getMinecraft().getResourceManager().getResource(rLoc);
				}catch (IOException e){
				}
			}
		}

		if (resource != null)
			return resource.getInputStream();

		throw new MissingResourceException("No packaged version of the compendium was found.  You may have a corrupted download.", "compendium", "Arcane Compendium");
	}

	public LinkedHashSet<CompendiumEntryType> getCategories(){
		LinkedHashSet<CompendiumEntryType> toReturn = new LinkedHashSet();
		for (CompendiumEntryType type : CompendiumEntryTypes.categoryList()){
			toReturn.add(type);
		}
		return toReturn;
	}

	public ArrayList<CompendiumEntry> getEntriesForCategory(String category){
		ArrayList<CompendiumEntry> toReturn = new ArrayList<CompendiumEntry>();

		for (CompendiumEntry entry : compendium.values()){
			if (entry.type.getCategoryName().equals(category)){
				toReturn.add(entry);
			}
		}

		Collections.sort(toReturn);

		return toReturn;
	}

	private void unlockEntry(CompendiumEntry entry, boolean unlockRelated){
		if (unlockRelated){
			for (CompendiumEntry e : entry.getRelatedItems()){
				unlockEntry(e, false);
			}

			for (CompendiumEntry e : entry.getSubItems()){
				unlockEntry(e, false);
			}
		}
		if (entry.isLocked()){
			entry.setIsLocked(false);
			if (ExtendedProperties.For(Minecraft.getMinecraft().thePlayer).getMagicLevel() > 0)
				Minecraft.getMinecraft().guiAchievement.displayAchievement(compendiumData);

			saveUnlockData();
		}
	}

	/**
	 * Unlocks a Compendium Entry and all of it's related entries
	 *
	 * @param key The key used to identify the entry to unlock
	 */
	public void unlockEntry(String key){
		boolean verbose = key.startsWith("cmd::");
		key = key.replace("cmd::", "");

		if (verbose && key.equals("all")){
			for (CompendiumEntry entry : compendium.values()){
				entry.setIsLocked(false);
			}
			Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("am2.tooltip.unlockSuccess")));
			return;
		}

		CompendiumEntry entry = getEntry(key);
		if (entry == null){
			if (key.contains("@")){
				entry = getEntry(key.split("@")[0]);
			}
			if (entry == null){
				LogHelper.warn("Attempted to unlock a compendium entry for a non-existant key: " + key);
				if (verbose){
					String message = String.format(StatCollector.translateToLocal("am2.tooltip.compEntryNotFound"), key);
					Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(message));
				}
				return;
			}
		}
		while (entry.getParent() != null)
			entry = entry.getParent();
		if (entry.isLocked){
			unlockEntry(entry, true);
			if (verbose){
				Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("am2.tooltip.unlockSuccess"));
			}
		}
	}

	public void unlockCategory(String category){
		for (CompendiumEntry entry : compendium.values()){
			if (entry.type.getCategoryName().equals(category)){
				unlockEntry(entry, false);
			}
		}
	}

	public void unlockRelatedItems(ItemStack stack){
		//initial error checking
		if (stack == null)
			return;

		//initialize variables (definitely don't miss the C days...)
		//initialize not at the top of the function.  Take that, old school!
		CompendiumEntry entry = null;
		String itemID = "";

		//ensure the unlocalized name has been set
		if (stack.getItem().getUnlocalizedName() == null)
			return;

		//construct a search ID based on what is passed in
		if (stack.getItem() instanceof Item){
			itemID = stack.getItem().getUnlocalizedName().replace("item.", "").replace("arsmagica2:", "");
		}else if (stack.getItem() instanceof ItemBlock){
			itemID = stack.getItem().getUnlocalizedName().replace("arsmagica2:", "").replace("tile.", "");
		}

		//append meta specific search if needed
		if (stack.getItemDamage() > -1)
			itemID += "@" + stack.getItemDamage();

		//search based on our constructed ID
		entry = getEntry(itemID);

		//did we find something?
		if (entry != null){
			//great!  ensure it's unlocked in the compendium
			unlockEntry(entry, true);
		}
	}

	public void setLockedState(boolean b){
		for (CompendiumEntry entry : this.compendium.values())
			entry.setIsLocked(b);
	}

	public boolean isCategory(String id){
		for (CompendiumEntryType type : this.getCategories())
			if (type.getCategoryName().equals(id))
				return true;
		return false;
	}

	public boolean isModUpdateAvailable(){
		return this.modUpdateAvailable;
	}

	public String getModDownloadLink(){
		return this.latestDownloadLink;
	}

	public String getPatchNotesLink(){
		return this.latestPatchNotesLink;
	}


	@Override
	public void AddCompenidumEntry(Object entryItem, String entryKey, String entryName, String entryDesc, String parent, boolean allowReplace, String... relatedKeys){
		if (entryItem == null){
			LogHelper.warn("Null entry item passed.  Cannot add Compendium Entry with key %s.", entryKey);
			return;
		}

		CompendiumEntry existingEntry = getEntry(entryKey);
		if (existingEntry != null && !allowReplace){
			LogHelper.warn("Compendium entry with key %s exists, and allowReplace is false.  The entry was not added.", entryKey);
			return;
		}

		CompendiumEntry parentEntry = parent == null ? null : getEntry(parent);
		if (parent != null && parentEntry == null){
			LogHelper.warn("The parent ID %s was not found.  Entry %s will be added with no parent.", parent, entryKey);
		}

		CompendiumEntry newEntry = null;

		if (entryItem instanceof Item){
			newEntry = new CompendiumEntryItem();
		}else if (entryItem instanceof Block){
			newEntry = new CompendiumEntryBlock();
		}else if (entryItem instanceof ISpellShape){
			newEntry = new CompendiumEntrySpellShape();
		}else if (entryItem instanceof ISpellComponent){
			newEntry = new CompendiumEntrySpellComponent();
		}else if (entryItem instanceof ISpellModifier){
			newEntry = new CompendiumEntrySpellModifier();
		}else if (entryItem instanceof Entity){
			newEntry = new CompendiumEntryMob();
		}

		newEntry.id = entryKey;
		newEntry.name = entryName;
		newEntry.description = entryDesc;
		newEntry.isLocked = true;
		newEntry.isNew = true;
		newEntry.parent = parentEntry;
		for (String s : relatedKeys)
			newEntry.relatedItems.add(s);

		if (parentEntry != null){
			parentEntry.subItems.add(newEntry);
			addAlias(newEntry.getID(), parentEntry.getID());
		}

		this.compendium.put(entryKey, newEntry);
		LogHelper.debug("Successfully added compendium entry %s", entryKey);
	}
}
