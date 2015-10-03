package am2.commands;

import am2.AMCore;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;

public class RegisterTeamHostilityCommand extends CommandBase{

	@Override
	public String getCommandName(){
		return "registerteamhostility";
	}

	@Override
	public int getRequiredPermissionLevel(){
		return 2;
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring){
		if (astring.length != 2){
			throw new WrongUsageException(this.getCommandUsage(icommandsender), new Object[0]);
		}

		AMCore.instance.proxy.teamHostility.put(astring[0], astring[1]);
		AMCore.instance.proxy.teamHostility.put(astring[1], astring[0]);

		notifyOperators(icommandsender, this, String.format("Teams %s and %s are now marked as hostile to each other.", astring[0], astring[1]), new Object[0]);
	}

	@Override
	public String getCommandUsage(ICommandSender var1){
		return "Sets 2 team names to be hostile to each other.  Used in summon AI targeting.  Usage: '/RegisterTeamHostility <team1> <team2>";
	}

}
