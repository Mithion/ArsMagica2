package am2.commands;

import am2.playerextensions.ExtendedProperties;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class FillManaBarCommand extends CommandBase{

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
		return "fillmanabar";
	}

	@Override
	public String getCommandUsage(ICommandSender var1){
		return "/fillmanabar [<player>]";
	}

	@Override
	public void processCommand(ICommandSender var1, String[] var2) throws CommandException{
		if (var2.length != 1 && var2.length != 0){
			throw new WrongUsageException(this.getCommandUsage(var1), new Object[0]);
		}
		EntityPlayer player = null;

		if (var2.length == 1){
			if (var2[1].equals("@a")){
				List<EntityPlayerMP> players = PlayerSelector.matchEntities(var1, var2[1], EntityPlayerMP.class);
				for (EntityPlayerMP p : players){
					doRefillMana(var1, p);
				}
			}else{
				player = getPlayer(var1, var2[1]);
			}
		}else{
			player = getCommandSenderAsPlayer(var1);
		}
		doRefillMana(var1, player);
	}

	private void doRefillMana(ICommandSender sender, EntityPlayer player){
		if (player == null) return;

		ExtendedProperties.For(player).setCurrentMana(ExtendedProperties.For(player).getMaxMana());
		ExtendedProperties.For(player).forceSync();

		notifyOperators(sender, this, "Filling " + player.getName() + "'s mana.", new Object[0]);
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
