package am2.commands;

import am2.api.spell.enums.SkillPointTypes;
import am2.playerextensions.SkillData;
import am2.spell.SkillManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;

public class UnlockAugmentedCastingCommand extends CommandBase{

	@Override
	public String getCommandName(){
		return "unlockaugmentedcasting";
	}

	@Override
	public int getRequiredPermissionLevel(){
		return 2;
	}

	@Override
	public String getCommandUsage(ICommandSender var1){
		return "/unlockaugmentedcasting [<player>]";
	}

	@Override
	public void processCommand(ICommandSender var1, String[] var2){
		if (var2.length > 1){
			throw new WrongUsageException(this.getCommandUsage(var1), new Object[0]);
		}

		EntityPlayer player;

		if (var2.length == 1){
			player = getPlayer(var1, var2[0]);
		}else{
			player = getCommandSenderAsPlayer(var1);
		}

		if (player == null) return;

		SkillData.For(player).incrementSpellPoints(SkillPointTypes.RED);
		SkillData.For(player).learn(SkillManager.instance.getSkill("AugmentedCasting").getID() + SkillManager.TALENT_OFFSET, 3);
		SkillData.For(player).forceSync();

		func_152373_a(var1, this, "Unlocking augmented casting for " + player.getName(), new Object[0]);
	}
}
