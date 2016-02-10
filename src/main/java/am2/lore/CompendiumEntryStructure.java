package am2.lore;

import am2.guis.GuiArcaneCompendium;
import am2.multiblock.IMultiblockStructureController;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Map;

public class CompendiumEntryStructure extends CompendiumEntry{

	private String controllerClass;

	public CompendiumEntryStructure(){
		super(CompendiumEntryTypes.instance.STRUCTURE);
	}

	@Override
	protected void parseEx(Node node){
		NodeList childNodes = node.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); ++i){
			Node child = childNodes.item(i);
			if (child.getNodeName().equals("controller")){
				this.controllerClass = child.getTextContent();
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected GuiArcaneCompendium getCompendiumGui(String searchID, int meta){
		if (this.controllerClass != null){
			Class tileEntityClass = (Class)((Map)ReflectionHelper.getPrivateValue(TileEntity.class, null, 1)).get(controllerClass);
			if (tileEntityClass != null && IMultiblockStructureController.class.isAssignableFrom(tileEntityClass)){
				try{
					TileEntity te = (TileEntity)tileEntityClass.newInstance();
					return new GuiArcaneCompendium(((IMultiblockStructureController)te).getDefinition(), te);
				}catch (InstantiationException e){
					e.printStackTrace();
				}catch (IllegalAccessException e){
					e.printStackTrace();
				}
			}
		}
		return new GuiArcaneCompendium(searchID);
	}

	@Override
	public ItemStack getRepresentItemStack(String searchID, int meta){
		return null;
	}

}
