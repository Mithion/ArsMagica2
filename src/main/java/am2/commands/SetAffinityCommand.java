package am2.commands;

import am2.api.spell.enums.Affinity;
import am2.playerextensions.AffinityData;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.List;

public class SetAffinityCommand extends CommandBase{
	@Override
	public int compareTo(ICommand arg0){
		return 0;
	}

	@Override
	public String getCommandName(){
		return "setaffinity";
	}

	@Override
	public int getRequiredPermissionLevel(){
		return 2;
	}

	@Override
	public String getCommandUsage(ICommandSender var1){
		return "/setaffinity [<player>] <name> <depth>";
	}

	private int tryParseInt(String input){
		try{
			return Integer.parseInt(input);
		}catch (Throwable t){
			return 0;
		}
	}

	@Override
	public void processCommand(ICommandSender var1, String[] var2) throws CommandException{
		if (var2.length != 3 && var2.length != 2){
			throw new WrongUsageException(this.getCommandUsage(var1), new Object[0]);
		}
		EntityPlayer player = null;
		String affinity = "";
		float depth = 0f;

		if (var2.length == 3){
			player = getPlayer(var1, var2[0]);
			affinity = var2[1].toUpperCase();
			depth = tryParseInt(var2[2]);
		}else{
			player = getCommandSenderAsPlayer(var1);
			affinity = var2[0].toUpperCase();
			depth = tryParseInt(var2[1]);
		}

		if (player == null) return;

		Affinity enumAffinity;

		try{
			enumAffinity = Affinity.valueOf(affinity);
		}catch (Throwable t){
			player.addChatMessage(new ChatComponentText("Unknown affinity name specified."));
			return;
		}

		AffinityData.For(player).setAffinityAndDepth(enumAffinity, depth);
		AffinityData.For(player).forceSync();

		notifyOperators(var1, this, "Setting " + player.getName() + "'s " + affinity + " affinity level to " + depth, new Object[0]);

	}

	private String[] AllAffinities(){
		return new String[]{
				Affinity.AIR.toString(),
				Affinity.ARCANE.toString(),
				Affinity.EARTH.toString(),
				Affinity.ENDER.toString(),
				Affinity.FIRE.toString(),
				Affinity.ICE.toString(),
				Affinity.LIFE.toString(),
				Affinity.LIGHTNING.toString(),
				Affinity.NONE.toString(),
				Affinity.NATURE.toString(),
				Affinity.WATER.toString()
		};
	}

	@Override
	public List<String> addTabCompletionOptions(ICommandSender var1, String[] var2, BlockPos pos){
		ArrayList<String> completions = new ArrayList<String>();
		if (var2.length == 1){
			for (String s : AllAffinities()){
				if (s.replace(' ', '_').toLowerCase().startsWith(var2[0].toLowerCase())){
					completions.add(s.replace(' ', '_'));
				}
			}
		}else if (var2.length == 0){
			EntityPlayer player = null;
			try{
				player = getCommandSenderAsPlayer(var1);
			}catch (PlayerNotFoundException e){}
			if (player == null) return completions;
			for (Object o : player.worldObj.playerEntities){
				EntityPlayer p = (EntityPlayer)o;
				completions.add(p.getName());
			}
		}
		return completions;
	}
}
