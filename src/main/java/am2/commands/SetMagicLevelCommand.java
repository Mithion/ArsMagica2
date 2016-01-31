package am2.commands;

import am2.playerextensions.ExtendedProperties;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class SetMagicLevelCommand extends CommandBase{

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
		return "setmagiclevel";
	}

	@Override
	public String getCommandUsage(ICommandSender var1){
		return "/setmagiclevel [<player>] <level>";
	}

	@Override
	public List getCommandAliases(){
		ArrayList<String> aliases = new ArrayList<String>();
		aliases.add("setmagelevel");
		return aliases;
	}

	@Override
	public void processCommand(ICommandSender var1, String[] var2) throws CommandException{
		if (var2.length != 2 && var2.length != 1){
			throw new WrongUsageException(this.getCommandUsage(var1), new Object[0]);
		}
		EntityPlayer player = null;
		int targetLevel = 0;

		if (var2.length == 2){
			if (var2[0].equals("@a")){
				List<EntityPlayerMP> players = PlayerSelector.matchEntities(var1, var2[0], EntityPlayerMP.class);
				for (EntityPlayerMP p : players){
					doMagicLevelSet(var1, p, targetLevel);
				}
			}else{
				player = getPlayer(var1, var2[0]);
			}
			targetLevel = parseInt(var2[1], 0, ExtendedProperties.maxMagicLevel);
		}else{
			player = getCommandSenderAsPlayer(var1);
			targetLevel = parseInt(var2[0], 0, ExtendedProperties.maxMagicLevel);
		}
		doMagicLevelSet(var1, player, targetLevel);
	}

	private void doMagicLevelSet(ICommandSender sender, EntityPlayer player, int magicLevel){
		if (player == null) return;

		ExtendedProperties.For(player).setMagicLevelWithMana(magicLevel);
		ExtendedProperties.For(player).forceSync();

		notifyOperators(sender, this, "Setting " + player.getName() + "'s magic level to " + magicLevel, new Object[0]);
	}

	@Override
	public List<String> addTabCompletionOptions(ICommandSender var1, String[] var2, BlockPos pos){
		if (var2.length == 1){
			ArrayList<String> completions = new ArrayList<String>();
			EntityPlayer player = null;
			try{
				player = getCommandSenderAsPlayer(var1);
			}catch (PlayerNotFoundException e){}
			if (player == null) return completions;
			for (Object o : player.worldObj.playerEntities){
				EntityPlayer p = (EntityPlayer)o;
				completions.add(p.getName());
			}
			return completions;
		}
		return null;
	}

}
