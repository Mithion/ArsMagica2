package am2.commands;

import am2.AMCore;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.ArrayList;
import java.util.List;

public class ConfigureAMUICommand extends CommandBase{

	@Override
	public String getCommandName(){
		return "amuicfg";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender){
		return "/amuicfg";
	}

	@Override
	public int getRequiredPermissionLevel(){
		return 0;
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender par1iCommandSender){
		return true;
	}

	@Override
	public List getCommandAliases(){
		ArrayList<String> aliases = new ArrayList<String>();
		aliases.add("AMUICFG");
		return aliases;
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring){
		EntityPlayerMP player = getCommandSenderAsPlayer(icommandsender);
		AMCore.proxy.guiManager.showUICustomizationScreen(player);
	}

}
