package am2.items;

import am2.guis.AMGuiHelper;
import am2.lore.Story;
import am2.lore.StoryManager;
import am2.texture.ResourceManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEditableBook;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.List;

public class ItemLostJournal extends ItemEditableBook{

	@SideOnly(Side.CLIENT)
	private IIcon[] icons;
	@SideOnly(Side.CLIENT)
	private String[] textureFiles;

	public ItemLostJournal(){
		super();
		setMaxDamage(0);
		setMaxStackSize(1);
	}

	public ItemLostJournal setUnlocalizedAndTextureName(String name){
		this.setUnlocalizedName(name);
		setTextureName(name);
		return this;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack par1ItemStack){
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister){
		textureFiles = new String[]{"lost_journal_cover", "lost_journal_pages", "lost_journal_ribbon", "lost_journal_clasp"};
		icons = new IIcon[textureFiles.length];

		for (int i = 0; i < textureFiles.length; ++i){
			icons[i] = ResourceManager.RegisterTexture(textureFiles[i], par1IconRegister);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack par1ItemStack, int renderPass){
		Story s = this.getStory(par1ItemStack);
		if (s == null){
			return super.getColorFromItemStack(par1ItemStack, renderPass);
		}
		return s.getStoryPassColor(renderPass);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamageForRenderPass(int damage, int renderPass){
		switch (renderPass){
		case 0:
			return icons[0];
		case 1:
			return icons[1];
		case 2:
			return icons[2];
		default:
			return icons[3];
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses(){
		return true;
	}

	@Override
	public int getRenderPasses(int metadata){
		return 4;
	}

	public String getItemStackDisplayName(ItemStack par1ItemStack){
		String title = super.getItemStackDisplayName(par1ItemStack);
		if (par1ItemStack.hasTagCompound()){
			NBTTagCompound compound = par1ItemStack.getTagCompound();
			int part = compound.getInteger("story_part");
			String nbtTitle = compound.getString("title");

			title = nbtTitle + " Volume " + part;
		}

		return title;
	}

	public Story getStory(ItemStack stack){
		if (stack.hasTagCompound()){
			NBTTagCompound compound = stack.getTagCompound();
			String nbtTitle = compound.getString("title");
			Story s = StoryManager.INSTANCE.getByTitle(nbtTitle);
			return s;
		}
		return null;
	}

	public short getStoryPart(ItemStack stack){
		if (stack.hasTagCompound()){
			NBTTagCompound compound = stack.getTagCompound();
			int part = compound.getInteger("story_part");
			return (short)part;
		}
		return -1;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer){
		if (par2World.isRemote){
			AMGuiHelper.OpenBookGUI(par1ItemStack);
		}
		return par1ItemStack;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs par2CreativeTabs, List par3List){

		super.getSubItems(item, par2CreativeTabs, par3List);

		int sCount = 0;
		for (Story s : StoryManager.INSTANCE.allStories()){
			int meta = sCount << 16;
			for (short i = 0; i < s.getNumParts(); ++i){
				meta = sCount + i;
				ItemStack stack = new ItemStack(item, 1, meta);
				stack.stackTagCompound = new NBTTagCompound();
				s.WritePartToNBT(stack.stackTagCompound, i);
				stack.stackTagCompound.setString("title", s.getTitle());
				par3List.add(stack);
			}
		}
	}
}
