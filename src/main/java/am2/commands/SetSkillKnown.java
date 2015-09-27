package am2.commands;

import am2.api.spell.component.interfaces.ISkillTreeEntry;
import am2.playerextensions.SkillData;
import am2.spell.SkillManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;

public class SetSkillKnown extends CommandBase{
	@Override
	public int compareTo(Object arg0){
		return 0;
	}

	@Override
	public String getCommandName(){
		return "setskillknown";
	}

	@Override
	public int getRequiredPermissionLevel(){
		return 2;
	}

	@Override
	public String getCommandUsage(ICommandSender var1){
		return "/setskillknown [<player>] <skill>";
	}

	@Override
	public void processCommand(ICommandSender var1, String[] var2){
		if (var2.length != 2 && var2.length != 1){
			throw new WrongUsageException(this.getCommandUsage(var1), new Object[0]);
		}
		EntityPlayer player = null;
		String skill = "";

		if (var2.length == 2){
			player = getPlayer(var1, var2[0]);
			skill = var2[1];
		}else{
			player = getCommandSenderAsPlayer(var1);
			skill = var2[0];
		}

		if (player == null) return;

		ISkillTreeEntry entry = SkillManager.instance.getSkill(skill);
		SkillData.For(player).learn(entry);

		notifyOperators(var1, this, "Unlocking " + skill + " for " + player.getCommandSenderName(), new Object[0]);
	}

	@Override
	public List addTabCompletionOptions(ICommandSender var1, String[] var2){
		ArrayList<String> completions = new ArrayList<String>();
		if (var2.length == 1){
			for (String s : SkillManager.instance.getAllSkillNames()){
				if (s.replace(' ', '_').toLowerCase().startsWith(var2[0].toLowerCase())){
					completions.add(s.replace(' ', '_'));
				}
			}
		}else if (var2.length == 0){
			EntityPlayer player = getCommandSenderAsPlayer(var1);
			for (Object o : player.worldObj.playerEntities){
				EntityPlayer p = (EntityPlayer)o;
				completions.add(p.getCommandSenderName());
			}
		}
		return completions;
	}
}
