package am2.commands;

import am2.spell.SkillTreeManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class ReloadSkillTree extends CommandBase{

	@Override
	public String getCommandName(){
		return "reloadskilltree";
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring){
		SkillTreeManager.instance.init();
		notifyOperators(icommandsender, this, "Reloaded the skill trees.", new Object[0]);
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender){
		return "/reloadskilltree";
	}

}
