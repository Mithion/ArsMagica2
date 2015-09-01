package am2.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import am2.spell.SkillTreeManager;

public class ReloadSkillTree extends CommandBase{

	@Override
	public String getCommandName() {
		return "reloadskilltree";
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		SkillTreeManager.instance.init();
		func_152373_a(icommandsender, this, "Reloaded the skill trees.", new Object[0]);
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "/reloadskilltree";
	}

}
