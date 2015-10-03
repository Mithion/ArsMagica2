package am2.commands;

import am2.network.AMNetHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;

public class UnlockCompendiumEntry extends CommandBase{

	@Override
	public String getCommandName(){
		return "unlockcompendiumentry";
	}

	@Override
	public String getCommandUsage(ICommandSender sender){
		return "/unlockcompendiumentry [player] <identifier|ALL>";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args){
		if (args.length < 1 || args.length > 2){
			throw new WrongUsageException(getCommandUsage(sender), new Object[0]);
		}

		EntityPlayerMP player;
		String identifier;
		if (args.length == 2){
			player = getPlayer(sender, args[0]);
			identifier = args[1];
		}else{
			player = getCommandSenderAsPlayer(sender);
			identifier = args[0];
		}

		if (player == null)
			throw new WrongUsageException("Player could not be found.");

		AMNetHandler.INSTANCE.sendCompendiumUnlockPacket(player, "cmd::" + identifier.toLowerCase(), false);
	}

}
