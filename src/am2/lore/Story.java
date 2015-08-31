package am2.lore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import am2.AMCore;
import am2.items.ItemsCommonProxy;
import am2.utility.InventoryUtilities;
import cpw.mods.fml.common.FMLLog;


public class Story {
	private final String resourceName;
	private short parts;
	private String title;
	private String author;
	private int[] colors;

	private final ArrayList<ArrayList<NBTTagString>> partPages;

	public int getStoryPassColor(int pass){
		if (colors.length <= pass){
			return colors[colors.length - 1];
		}
		return colors[pass];
	}
	
	public short getNumParts(){
		return parts;		
	}

	public ArrayList<NBTTagString> getStoryPart(int index){
		if (index < 0 || index >= parts){
			return new ArrayList<NBTTagString>();
		}
		return partPages.get(index);
	}

	public String getTitle(){
		return this.title;
	}

	public String getAuthor(){
		return this.author;
	}

	public Story(String resourceName) throws IOException{
		this.resourceName = resourceName;
		partPages = new ArrayList<ArrayList<NBTTagString>>();
		parseFile();

		if (title == null || author == null){
			throw new IOException("Invalid file - needs #TITLE and #AUTHOR directives at the beginning!");
		}
	}

	private void parseFile(){
		String fileText = readResource();
		String[] split = fileText.split("<<__>>");
		parts = (short)split.length;
		for (String s : split){
			if (s.equals("") || s.equals("\n")) continue;
			if (s.startsWith("\n")) s = s.replaceFirst("\n", "");
			s = s.replace("\r\n", "\n");
			partPages.add(splitStoryPartIntoPages(s));
		}
	}

	private InputStream getResourceAsStream(String resourceName){
		return AMCore.class.getResourceAsStream(resourceName);
	}

	private String readResource(){
		InputStream stream = getResourceAsStream(resourceName);
		if (stream == null){
			FMLLog.info("Ars Magica >> Missing Resource '" + resourceName + "'");
			return "";
		}

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		try{		
			br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
			String line;
			while ((line = br.readLine()) != null){
				if (line.startsWith("#TITLE")){
					this.title = line.replace("#TITLE", "").trim();
					continue;
				}else if (line.startsWith("#AUTHOR")){
					this.author = line.replace("#AUTHOR", "").trim();
					continue;
				}else if (line.startsWith("#COLORS")){
					String colorString = line.replace("#COLORS", "").trim();
					String[] colorStrings = colorString.split(" ");
					this.colors = new int[4];
					for (int i = 0; i < 4; ++i){
						if (i < colorStrings.length){
							try{
								this.colors[i] = Integer.parseInt(colorStrings[i], 16);
							}catch(Throwable t){
								this.colors[i] = 0xFFFFFF;
							}
						}else{
							this.colors[i] = 0xFFFFFF;
						}
					}
					continue;
				}
				sb.append(line + "\n");
			}
			br.close();
			stream.close();	
		}catch(Throwable t){
			FMLLog.severe("Error reading JRN File Data!");
			return "";
		}

		if (this.colors == null){
			this.colors = new int[] { 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF };
		}
		
		return sb.toString();
	}

	public void WritePartToNBT(NBTTagCompound compound, int part){
		ArrayList<NBTTagString> storyData = getStoryPart(part);
		if (storyData.equals("")) return;

		//part
		compound.setInteger("story_part", part);

		//title
		NBTTagString title = new NBTTagString(this.title);
		compound.setTag("title", title);

		//author
		NBTTagString author = new NBTTagString(this.author);
		compound.setTag("author", author);

		//pages
		NBTTagList pages = new NBTTagList();
		for (NBTTagString page : storyData){
			pages.appendTag(page);
		}
		compound.setTag("pages", pages);
	}
	
	public static void WritePartToNBT(NBTTagCompound compound, ArrayList<NBTTagString> storyData){
		//pages
		NBTTagList pages = new NBTTagList();
		for (NBTTagString page : storyData){
			pages.appendTag(page);
		}
		compound.setTag("pages", pages);
	}
	
	public static ItemStack finalizeStory(ItemStack stack, String title, String author){
		if (stack.stackTagCompound == null) return stack;
		stack.stackTagCompound.setTag("title", new NBTTagString(title));
		stack.stackTagCompound.setTag("author", new NBTTagString(author));
		stack = InventoryUtilities.replaceItem(stack, Items.written_book);
		return stack;
	}

	public void WriteMultiplePartsToNBT(NBTTagCompound compound, List<Short> parts){
		//title
		NBTTagString title = new NBTTagString(this.title);
		compound.setTag("title", title);

		//author
		NBTTagString author = new NBTTagString(this.author);
		compound.setTag("author", author);
		
		Collections.sort(parts);
		
		NBTTagList pages = new NBTTagList();
		for (Short i : parts){
			ArrayList<NBTTagString> storyData = getStoryPart(i);
			if (storyData.equals("")) continue;
			for (NBTTagString page : storyData){
				pages.appendTag(page);
			}
		}
		compound.setTag("pages", pages);
	}
	
	public static ArrayList<NBTTagString> splitStoryPartIntoPages(String storyPart){
		ArrayList<NBTTagString> parts = new ArrayList<NBTTagString>();
		String[] words = storyPart.split(" ");
		String currentPage = "";


		for (String word : words){			
			//special commands
			if (word.contains("<newpage>")){
				int idx = word.indexOf("<newpage>");
				String preNewPage = word.substring(0, idx);
				String postNewPage = word.substring(idx + "<newpage>".length());
				while (preNewPage.endsWith("\n")) preNewPage = preNewPage.substring(0, preNewPage.lastIndexOf('\n'));
				if (getStringOverallLength(currentPage + preNewPage) > 256){
					parts.add(new NBTTagString(currentPage));
					currentPage = preNewPage.trim();									
				}else{
					currentPage += " " + preNewPage.trim();					
				}				
				parts.add(new NBTTagString(currentPage));
				while (postNewPage.startsWith("\n")) postNewPage = postNewPage.replaceFirst("\n", "");
				currentPage = postNewPage.trim();
				continue;
			}

			//length limit
			if (getStringOverallLength(currentPage + word) > 256){
				parts.add(new NBTTagString(currentPage));
				currentPage = word;
				if (getStringOverallLength(currentPage) > 256){
					currentPage = currentPage.substring(0, getStringSplitIndex(currentPage, 255));
					parts.add(new NBTTagString(currentPage));
					currentPage = "";
				}
				continue;
			}
			//
			if (currentPage.equals("")){
				currentPage = word.trim();
			}else{
				currentPage += " " + word;
			}
		}
		parts.add(new NBTTagString(currentPage));

		return parts;
	}	

	private static int getStringOverallLength(String s){
		int length = 0;
		for (int i = 0; i < s.length(); ++i){
			char c = s.charAt(i);
			if (c == '\n'){				
				length += length % 19;				
			}else{
				length++;
			}
		}
		return length;
	}
	
	private static int getStringSplitIndex(String s, int splitpoint){
		int length = 0;
		int index = 0;
		for (int i = 0; i < s.length(); ++i){
			char c = s.charAt(i);
			if (c == '\n'){				
				length += length % 19;				
			}else{
				length++;
			}
			if (length > splitpoint){
				return index;
			}else{
				index++;
			}
		}
		return index-1;
	}
}
