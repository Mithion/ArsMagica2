package am2.lore;

import am2.guis.GuiArcaneCompendium;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public abstract class CompendiumEntry implements Comparable<CompendiumEntry>{
	protected CompendiumEntryType type;
	protected String name;
	protected String description;
	protected String id;
	protected int order;
	protected CompendiumEntry parent;
	protected boolean isLocked;
	protected boolean isNew;

	protected ArrayList<CompendiumEntry> subItems;
	protected ArrayList<String> relatedItems;

	public CompendiumEntry(CompendiumEntryType type){
		this.type = type;
		this.order = -1;
		subItems = new ArrayList<CompendiumEntry>();
		relatedItems = new ArrayList<String>();
		this.isLocked = true;
	}

	protected CompendiumEntry setParent(CompendiumEntry parent){
		this.parent = parent;
		return this;
	}

	public final String getID(){
		return id;
	}

	public final boolean isLocked(){
		return isLocked;
	}

	public final boolean isNew(){
		return isNew;
	}

	public final void setIsNew(boolean isNew){
		this.isNew = isNew;
	}

	public final void setIsLocked(boolean locked){
		this.isLocked = locked;
	}

	public final String getName(){
		return name;
	}

	public final String getName(String subItemID){
		for (CompendiumEntry entry : subItems){
			if (entry.getID().equals(subItemID)){
				return entry.getName();
			}
		}
		return name;
	}

	public final String getDescription(){
		if (this.description == null || this.description.equals("")){
			if (this.parent != null){
				return this.parent.getDescription();
			}
		}
		return description;
	}

	public final String getDescription(String subItemID){
		for (CompendiumEntry entry : subItems){
			if (entry.getID().equals(subItemID)){
				return description + " " + ArcaneCompendium.KEYWORD_NEWLINE + " " + ArcaneCompendium.KEYWORD_NEWLINE + " " + entry.getDescription();
			}
		}
		return getDescription();
	}

	public final String getParentSection(){
		return type.getCategoryName();
	}

	public final String getTagName(){
		return type.getNodeName();
	}

	public final CompendiumEntry getParent(){
		return this.parent;
	}

	protected abstract void parseEx(Node node);

	public void parse(Node node){
		Node idNode = node.getAttributes().getNamedItem("id");
		this.id = idNode != null ? idNode.getNodeValue() : "unknown";

		Node orderNode = node.getAttributes().getNamedItem("order");
		this.order = orderNode != null ? Integer.parseInt(orderNode.getNodeValue()) : -1;

		Node lockableNode = node.getAttributes().getNamedItem("unlocked");
		this.isLocked = lockableNode != null ? !Boolean.parseBoolean(lockableNode.getNodeValue()) : true;

		Node newNode = node.getAttributes().getNamedItem("new");
		this.isNew = newNode != null ? Boolean.parseBoolean(newNode.getNodeValue()) : true;

		NodeList childNodes = node.getChildNodes();

		for (int i = 0; i < childNodes.getLength(); ++i){
			Node childNode = childNodes.item(i);
			if (childNode.getNodeName() == "name"){
				this.name = childNode.getTextContent();
			}else if (childNode.getNodeName() == "desc"){
				this.description = childNode.getTextContent();
			}else if (childNode.getNodeName() == "relatedEntries"){
				String[] relatedItems = childNode.getTextContent().split(",");
				for (String s : relatedItems)
					this.relatedItems.add(s.trim());
			}else if (childNode.getNodeName().equals("subitem")){
				CompendiumEntry subItem = null;
				try{
					subItem = this.getClass().getConstructor().newInstance();
				}catch (Throwable t){
					t.printStackTrace();
					continue;
				}
				subItem.parse(childNode);
				subItem.setParent(this);
				this.subItems.add(subItem);
				ArcaneCompendium.instance.addAlias(subItem.getID(), this.getID());
			}
		}

		//perform any child-specific parsing
		parseEx(node);
	}

	public boolean hasSubItems(){
		return subItems.size() > 0;
	}

	public CompendiumEntry[] getSubItems(){
		return subItems.toArray(new CompendiumEntry[subItems.size()]);
	}

	public CompendiumEntry[] getRelatedItems(){
		ArrayList<CompendiumEntry> relations = new ArrayList<CompendiumEntry>();
		for (String s : this.relatedItems){
			CompendiumEntry e = ArcaneCompendium.instance.getEntry(s);
			if (e != null && e != this)
				relations.add(e);
		}

		return relations.toArray(new CompendiumEntry[relations.size()]);
	}

	public GuiArcaneCompendium getCompendiumGui(String searchID){
		int meta = -1;
		if (searchID.indexOf('@') > -1){
			String[] split = searchID.split("@");
			searchID = split[0];
			try{
				meta = Integer.parseInt(split[1]);
			}catch (Throwable t){
				t.printStackTrace();
			}
		}

		return getCompendiumGui(searchID, meta);
	}

	@SideOnly(Side.CLIENT)
	protected
	abstract GuiArcaneCompendium getCompendiumGui(String searchID, int meta);

	public abstract ItemStack getRepresentItemStack(String searchID, int meta);

	@Override
	public int compareTo(CompendiumEntry arg0){

		if (arg0 == null) return 1;

		if (this.order > -1 && arg0.order > -1){
			return this.order > arg0.order ? 1 : this.order < arg0.order ? -1 : 0;
		}

		if (this.name != null && arg0.name != null)
			return this.name.compareTo(arg0.name);
		if (this.id != null && arg0.id != null)
			return this.id.compareTo(arg0.id);

		return -1;
	}
}
