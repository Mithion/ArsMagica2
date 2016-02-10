package am2.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.world.Explosion;

import java.util.Random;

public class Explosions extends CommandBase{

	@Override
	public String getCommandName(){
		return "EXPLOSIONS!";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender){
		return "/EXPLOSIONS!";
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) throws CommandException{
		EntityPlayer player = getCommandSenderAsPlayer(icommandsender);

		if (player.getName().equals("Moridrex") || player.getName().equals("Mithion")){
			if (player.worldObj.rand.nextInt(10) < 5 || !player.worldObj.canBlockSeeSky(new BlockPos(player))){
				Explosion explosion = player.worldObj.newExplosion(null, player.posX, player.posY, player.posZ, 10, true, true);
				player.attackEntityFrom(DamageSource.setExplosionSource(explosion), 5000);
			}else{
				if (!player.worldObj.isRemote){
					for (int i = 0; i < 25; ++i){
						EntityCreeper creeper = new EntityCreeper(player.worldObj);
						creeper.setPosition(player.posX + player.worldObj.rand.nextInt(4) - 2, player.posY + 20, player.posZ + player.worldObj.rand.nextInt(4) - 2);
						player.worldObj.spawnEntityInWorld(creeper);
					}
				}
			}
		}else{
			player.addChatMessage(new ChatComponentText("You aren't Moridrex..."));
		}
	}

}
