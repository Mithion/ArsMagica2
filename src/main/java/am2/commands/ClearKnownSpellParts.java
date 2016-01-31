package am2.commands;

import am2.playerextensions.SkillData;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;

public class ClearKnownSpellParts extends CommandBase{
	@Override
	public String getCommandName(){
		return "clearknownspellparts";
	}

	@Override
	public int getRequiredPermissionLevel(){
		return 2;
	}

	@Override
	public String getCommandUsage(ICommandSender var1){
		return "/clearknownspellparts [<player>]";
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) throws CommandException{
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

		SkillData.For(player).clearAllKnowledge();

		notifyOperators(icommandsender, this, "Cleared " + player.getName() + "'s known spell shapes, components, and modifiers.", new Object[0]);
	}
}
