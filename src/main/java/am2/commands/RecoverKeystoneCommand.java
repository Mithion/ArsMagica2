package am2.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import am2.playerextensions.ExtendedProperties;

public class RecoverKeystoneCommand extends CommandBase{

	@Override
	public String getCommandName() {
		return "recoverkeystone";
	}

	@Override
	public int getRequiredPermissionLevel()
    {
        return 2;
    }
	
	@Override
	public String getCommandUsage(ICommandSender var1) {
		return "/recoverkeystone [<player>]";
	}
	
	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		if (astring.length != 1 && astring.length != 0){
			throw new WrongUsageException(this.getCommandUsage(icommandsender), new Object[0]);
		}
		EntityPlayer player = null;

		if (astring.length == 2){
			player = getPlayer(icommandsender, astring[0]);
		}else{
			player = getCommandSenderAsPlayer(icommandsender);
		}
		
		if (player == null) return;
		
		ExtendedProperties.For(player).isRecoveringKeystone = true;		
	
		func_152373_a(icommandsender, this, player.getCommandSenderName() + " is recovering a Keystone combination.", new Object[0]);
	}

}
