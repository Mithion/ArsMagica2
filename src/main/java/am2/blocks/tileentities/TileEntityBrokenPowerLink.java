package am2.blocks.tileentities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityBrokenPowerLink extends TileEntity{

	private String highlightText = "";

	@Override
	public boolean canUpdate(){
		return false;
	}

	public void setHighlightText(String text){
		this.highlightText = text;
	}

	public String getHighlightText(){
		return this.highlightText;
	}

	@Override
	public void writeToNBT(NBTTagCompound par1nbtTagCompound){
		super.writeToNBT(par1nbtTagCompound);
		par1nbtTagCompound.setString("highlightText", highlightText);
	}

	@Override
	public void readFromNBT(NBTTagCompound par1nbtTagCompound){
		super.readFromNBT(par1nbtTagCompound);
		this.highlightText = par1nbtTagCompound.getString("highlightText");
	}
}
