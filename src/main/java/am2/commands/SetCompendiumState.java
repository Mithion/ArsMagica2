package am2.commands;

import am2.lore.ArcaneCompendium;
import net.minecraft.command.*;

public class SetCompendiumState extends CommandBase{

	@Override
	public int compareTo(ICommand arg0){
		return 0;
	}

	@Override
	public int getRequiredPermissionLevel(){
		return 2;
	}

	@Override
	public String getCommandName(){
		return "setcompendiumstate";
	}

	@Override
	public String getCommandUsage(ICommandSender var1){
		return "/setcompendiumstate {LOCKED/UNLOCKED}";
	}

	@Override
	public void processCommand(ICommandSender var1, String[] var2) throws CommandException{
		if (var2.length != 1){
			throw new WrongUsageException(this.getCommandUsage(var1), new Object[0]);
		}

		if (var2[1].toLowerCase().equals("locked"))
			ArcaneCompendium.instance.setLockedState(true);
		else if (var2[1].toLowerCase().equals("unlocked"))
			ArcaneCompendium.instance.setLockedState(false);
		else
			throw new WrongUsageException(this.getCommandUsage(var1), new Object[0]);
	}
}
