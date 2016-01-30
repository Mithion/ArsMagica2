package am2.lore;

import am2.AMCore;
import am2.blocks.BlocksCommonProxy;
import am2.guis.GuiArcaneCompendium;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.w3c.dom.Node;

public class CompendiumEntryBlock extends CompendiumEntry{

	public CompendiumEntryBlock(){
		super(CompendiumEntryTypes.instance.BLOCK);
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected GuiArcaneCompendium getCompendiumGui(String searchID, int meta){

		if (this.id.equals("stonebricksmooth@3")){
			return new GuiArcaneCompendium(this.id, Blocks.stonebrick, 3);
		}else if (searchID.equals("wakebloom")){
			return new GuiArcaneCompendium(searchID, Item.getItemFromBlock(BlocksCommonProxy.wakebloom), 0);
		}

		String[] idSplit = searchID.split(":");
		if (idSplit.length == 2){
			Item item = GameRegistry.findItem(idSplit[0], idSplit[1]);
			if (item != null){
				if (meta == -1)
					return new GuiArcaneCompendium(item);
				else
					return new GuiArcaneCompendium(searchID + "@" + meta, item, meta);
			}
		}else{
			for (Item item : AMCore.instance.proxy.items.getArsMagicaItems()){
				if (item.getUnlocalizedName() == null) continue;
				String itemID = item.getUnlocalizedName().replace("item.", "").replace("arsmagica2:", "");
				if (itemID.equals(searchID)){
					if (meta == -1)
						return new GuiArcaneCompendium(item);
					else
						return new GuiArcaneCompendium(searchID + "@" + meta, item, meta);
				}
			}
		}

		if (idSplit.length == 2){
			Block block = GameRegistry.findBlock(idSplit[0], idSplit[1]);
			if (block != null){
				if (meta == -1)
					return new GuiArcaneCompendium(block);
				else
					return new GuiArcaneCompendium(searchID + "@" + meta, block, meta);
			}
		}else{
			for (Block block : AMCore.instance.proxy.blocks.getArsMagicaBlocks()){
				if (block.getUnlocalizedName() == null) continue;
				String[] split = searchID.split("@");
				if (block.getUnlocalizedName().replace("arsmagica2:", "").replace("tile.", "").equals(split[0])){
					if (meta == -1)
						return new GuiArcaneCompendium(block);
					else
						return new GuiArcaneCompendium(this.id, block, meta);
				}
			}
		}

		return new GuiArcaneCompendium(searchID);
	}

	@Override
	protected void parseEx(Node node){
	}

	@Override
	public int compareTo(CompendiumEntry arg0){
		return 0;
	}

	@Override
	public ItemStack getRepresentItemStack(String searchID, int meta){
		if (this.id.equals("stonebricksmooth@3")){
			return new ItemStack(Blocks.stonebrick, 1, 3);
		}

		String[] idSplit = searchID.split(":");
		if (idSplit.length == 2){
			Item item = GameRegistry.findItem(idSplit[0], idSplit[1]);
			if (item != null){
				if (meta == -1)
					return new ItemStack(item);
				else
					return new ItemStack(item, 1, meta);
			}
		}else{
			for (Block block : AMCore.instance.proxy.blocks.getArsMagicaBlocks()){
				if (block.getUnlocalizedName() == null) continue;
				String[] split = searchID.split("@");
				if (block.getUnlocalizedName().replace("arsmagica2:", "").replace("tile.", "").equals(split[0])){
					if (meta == -1)
						return new ItemStack(block);
					else
						return new ItemStack(block, 1, meta);
				}
			}
		}

		if (idSplit.length == 2){
			Block block = GameRegistry.findBlock(idSplit[0], idSplit[1]);
			if (block != null){
				if (meta == -1)
					return new ItemStack(block);
				else
					return new ItemStack(block, 1, meta);
			}
		}else{
			for (Item item : AMCore.instance.proxy.items.getArsMagicaItems()){
				if (item.getUnlocalizedName() == null) continue;
				String itemID = item.getUnlocalizedName().replace("item.", "").replace("arsmagica2:", "");
				if (itemID.equals(searchID)){
					if (meta == -1)
						return new ItemStack(item);
					else
						return new ItemStack(item, 1, meta);
				}
			}
		}
		return null;
	}
}
