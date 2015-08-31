package am2.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import am2.playerextensions.SkillData;

public class Respec extends CommandBase{
	@Override
	public String getCommandName() {
		return "respec";
	}

	@Override
	public int getRequiredPermissionLevel()
    {
        return 3;
    }
	
	@Override
	public String getCommandUsage(ICommandSender var1) {
		return "/respec [<player>]";
	}
	
	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		if (astring.length != 1 && astring.length != 0){
			throw new WrongUsageException(this.getCommandUsage(icommandsender), new Object[0]);
		}
		EntityPlayer player = null;

		if (astring.length == 1){
			player = getPlayer(icommandsender, astring[0]);
		}else{
			player = getCommandSenderAsPlayer(icommandsender);
		}
		
		if (player == null) return;
		
		SkillData.For(player).respec();
	
		func_152373_a(icommandsender, this, "Respeced " + player.getCommandSenderName(), new Object[0]);
	}
}
