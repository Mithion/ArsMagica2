package am2.commands;

import java.util.ArrayList;
import java.util.List;

import am2.AMCore;
import am2.network.AMDataWriter;
import am2.network.AMNetHandler;
import am2.network.AMPacketIDs;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;

public class DumpNBT extends CommandBase{

	@Override
	public String getCommandName() {
		return "nbtdump";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "/nbtdump";
	}
	
	@Override
	public List getCommandAliases() {
		ArrayList<String> aliases = new ArrayList<String>();
		aliases.add("dumpnbt");
		return aliases;
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		EntityPlayerMP player = getCommandSenderAsPlayer(icommandsender);
		if (player.getCurrentEquippedItem() == null)
		{
			player.addChatMessage(new ChatComponentText("This command dumps the NBT of your current equipped item...see the problem here?"));
			return;
		}
		if (!player.getCurrentEquippedItem().hasTagCompound()){
			player.addChatMessage(new ChatComponentText("No NBT exists on this item."));
			return;
		}
		
		AMNetHandler.INSTANCE.sendPacketToClientPlayer((EntityPlayerMP) player, AMPacketIDs.NBT_DUMP, new byte[0]);
	}
	
	@Override
	public boolean canCommandSenderUseCommand(ICommandSender par1iCommandSender) {
		return true;
	}

}
