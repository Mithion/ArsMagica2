package am2.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
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
	public void processCommand(ICommandSender icommandsender, String[] astring){
		EntityPlayer player = getCommandSenderAsPlayer(icommandsender);
		Random rand = new Random();

		if (player.getCommandSenderName().equals("Moridrex") || player.getCommandSenderName().equals("Mithion")){
			if (rand.nextInt(10) < 5 || !player.worldObj.canBlockSeeTheSky((int)player.posX, (int)player.posY, (int)player.posZ)){
				Explosion explosion = player.worldObj.newExplosion(null, player.posX, player.posY, player.posZ, 10, true, true);
				player.attackEntityFrom(DamageSource.setExplosionSource(explosion), 5000);
			}else{
				if (!player.worldObj.isRemote){
					for (int i = 0; i < 25; ++i){
						EntityCreeper creeper = new EntityCreeper(player.worldObj);
						creeper.setPosition(player.posX + rand.nextInt(4) - 2, player.posY + 20, player.posZ + rand.nextInt(4) - 2);
						player.worldObj.spawnEntityInWorld(creeper);
					}
				}
			}
		}else{
			player.addChatMessage(new ChatComponentText("You aren't Moridrex..."));
		}
	}

}
