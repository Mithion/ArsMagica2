package am2.lore;

import net.minecraft.util.IIcon;
import org.w3c.dom.Node;

public class CompendiumEntryType{

	private String categoryName;
	private String nodeName;
	private String categoryLabel;
	private int order;
	private IIcon representItem;
	private Class<? extends CompendiumEntry> compendiumClass;

	public CompendiumEntryType(String categoryName, String nodeName, String categoryLabel, int order, Class<? extends CompendiumEntry> compendiumClass){
		this.categoryName = categoryName;
		this.nodeName = nodeName;
		this.compendiumClass = compendiumClass;
		this.categoryLabel = categoryLabel;
		this.order = order;
	}

	public void setRepresentIcon(IIcon IIcon){
		this.representItem = IIcon;
	}

	public String getCategoryName(){
		return this.categoryName;
	}

	public String getNodeName(){
		return this.nodeName;
	}

	public CompendiumEntry createCompendiumEntry(Node node){
		try{
			CompendiumEntry ce = this.compendiumClass.newInstance();
			ce.parse(node);
			return ce;
		}catch (Throwable t){
			t.printStackTrace();
		}
		return null;
	}


	public String getCategoryLabel(){
		return this.categoryLabel;
	}

	public IIcon getRepresentItem(){
		return representItem;
	}


}
