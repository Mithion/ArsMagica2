package am2.commands;

import am2.api.spell.enums.SkillPointTypes;
import am2.playerextensions.SkillData;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerSelector;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.ArrayList;
import java.util.List;

public class GiveSkillPoints extends CommandBase{
	@Override
	public int compareTo(Object arg0){
		return 0;
	}

	@Override
	public int getRequiredPermissionLevel(){
		return 2;
	}

	@Override
	public String getCommandName(){
		return "giveskillpoints";
	}

	@Override
	public String getCommandUsage(ICommandSender var1){
		return "/giveskillpoints <amount_blue> <amount_green> <amount_red> [<player>]";
	}

	@Override
	public void processCommand(ICommandSender var1, String[] var2){
		if (var2.length != 4 && var2.length != 3){
			throw new WrongUsageException(this.getCommandUsage(var1), new Object[0]);
		}
		EntityPlayer player = null;
		int target_blue = parseIntBounded(var1, var2[0], 0, 50);
		int target_green = parseIntBounded(var1, var2[1], 0, 50);
		int target_red = parseIntBounded(var1, var2[2], 0, 50);

		if (var2.length == 4){
			if (var2[3].equals("@a")){
				EntityPlayerMP[] players = PlayerSelector.matchPlayers(var1, var2[3]);
				if (players != null){
					for (EntityPlayerMP p : players){
						doGiveSkillPoints(var1, p, target_blue, target_green, target_red);
					}
				}
			}else{
				player = getPlayer(var1, var2[3]);
			}
		}else{
			player = getCommandSenderAsPlayer(var1);
		}
		doGiveSkillPoints(var1, player, target_blue, target_green, target_red);
	}

	private void doGiveSkillPoints(ICommandSender sender, EntityPlayer player, int amount_blue, int amount_green, int amount_red){
		if (player == null) return;

		SkillData.For(player).setSpellPoints(SkillData.For(player).getSpellPoints(SkillPointTypes.BLUE) + amount_blue, SkillData.For(player).getSpellPoints(SkillPointTypes.GREEN) + amount_green, SkillData.For(player).getSpellPoints(SkillPointTypes.RED) + amount_red);
		SkillData.For(player).forceSync();

		func_152373_a(sender, this, String.format("Giving %s %d(B), %d(G), %d(R) skill points.", player.getName(), amount_blue, amount_green, amount_red), new Object[0]);
	}

	@Override
	public List addTabCompletionOptions(ICommandSender var1, String[] var2){
		if (var2.length == 1){
			ArrayList<String> completions = new ArrayList<String>();
			EntityPlayer player = getCommandSenderAsPlayer(var1);
			for (Object o : player.worldObj.playerEntities){
				EntityPlayer p = (EntityPlayer)o;
				completions.add(p.getName());
			}
			return completions;
		}
		return null;
	}
}
