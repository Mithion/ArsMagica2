package am2;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;

import org.lwjgl.input.Keyboard;

import am2.api.spell.enums.Affinity;
import am2.guis.AuraCustomizationMenu;
import am2.items.ItemSpellBook;
import am2.items.ItemsCommonProxy;
import am2.network.AMDataWriter;
import am2.network.AMNetHandler;
import am2.network.AMPacketIDs;
import am2.playerextensions.AffinityData;
import am2.spell.SpellUtils;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class AMKeyBindings{

	private static KeyBinding ShapeGroupKey = new KeyBinding("key.ShapeGroups", Keyboard.KEY_C, "key.am2.category");
	private static KeyBinding SpellBookNextSpellKey = new KeyBinding("key.SpellBookNext", Keyboard.KEY_X, "key.am2.category");
	private static KeyBinding SpellBookPrevSpellKey = new KeyBinding("key.SpellBookPrev", Keyboard.KEY_Z, "key.am2.category");
	private static KeyBinding AuraCustomizationKey = new KeyBinding("key.AuraCustomization", Keyboard.KEY_B, "key.am2.category");
	private static KeyBinding ManaToggleKey = new KeyBinding("key.ToggleManaDisplay", Keyboard.KEY_O, "key.am2.category");
	private static KeyBinding AffinityActivationKey = new KeyBinding("key.ActivateAffinityAbility", Keyboard.KEY_X, "key.am2.category");

	public AMKeyBindings() {
		ClientRegistry.registerKeyBinding(ShapeGroupKey);
		ClientRegistry.registerKeyBinding(SpellBookNextSpellKey);
		ClientRegistry.registerKeyBinding(SpellBookPrevSpellKey);
		ClientRegistry.registerKeyBinding(AuraCustomizationKey);
		ClientRegistry.registerKeyBinding(ManaToggleKey);
		ClientRegistry.registerKeyBinding(AffinityActivationKey);
	}

	@SubscribeEvent
	public void onKeyInput(KeyInputEvent event){
		EntityPlayer clientPlayer = FMLClientHandler.instance().getClient().thePlayer;

		if (Minecraft.getMinecraft().currentScreen != null){
			if (Minecraft.getMinecraft().currentScreen instanceof GuiInventory){
				if (ManaToggleKey.isPressed()){
					boolean curDisplayFlag = AMCore.config.displayManaInInventory();
					AMCore.config.setDisplayManaInInventory(!curDisplayFlag);
				}
			}
			return;
		}

		if (ShapeGroupKey.isPressed()){
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			ItemStack curItem = player.inventory.getStackInSlot(player.inventory.currentItem);
			if (curItem == null || (curItem.getItem() != ItemsCommonProxy.spell && curItem.getItem() != ItemsCommonProxy.spellBook && curItem.getItem() != ItemsCommonProxy.arcaneSpellbook)){
				return;
			}
			int shapeGroup;
			if (curItem.getItem() == ItemsCommonProxy.spell){
				shapeGroup = SpellUtils.instance.cycleShapeGroup(curItem);
			}else{
				ItemStack spellStack = ((ItemSpellBook)curItem.getItem()).GetActiveItemStack(curItem);
				shapeGroup = SpellUtils.instance.cycleShapeGroup(spellStack);
				((ItemSpellBook)curItem.getItem()).replaceAciveItemStack(curItem, spellStack);
			}

			AMNetHandler.INSTANCE.sendShapeGroupChangePacket(shapeGroup, clientPlayer.getEntityId());

		}else if (this.SpellBookNextSpellKey.isPressed()){
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			ItemStack curItem = player.inventory.getStackInSlot(player.inventory.currentItem);
			if (curItem == null){
				return;
			}
			if (curItem.getItem() == ItemsCommonProxy.spellBook || curItem.getItem() == ItemsCommonProxy.arcaneSpellbook){
				//send packet to server
				AMNetHandler.INSTANCE.sendSpellbookSlotChange(player, player.inventory.currentItem, ItemSpellBook.ID_NEXT_SPELL);
			}
		}else if (this.SpellBookPrevSpellKey.isPressed()){
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			ItemStack curItem = player.inventory.getStackInSlot(player.inventory.currentItem);
			if (curItem == null){
				return;
			}
			if (curItem.getItem() == ItemsCommonProxy.spellBook || curItem.getItem() == ItemsCommonProxy.arcaneSpellbook){
				//send packet to server
				AMNetHandler.INSTANCE.sendSpellbookSlotChange(player, player.inventory.currentItem, ItemSpellBook.ID_PREV_SPELL);
			}
		}else if (AuraCustomizationKey.isPressed()){
			if (AMCore.proxy.playerTracker.hasAA(clientPlayer)){
				Minecraft.getMinecraft().displayGuiScreen(new AuraCustomizationMenu());
			}
		}else if (AffinityActivationKey.isPressed()){
			if (AffinityData.For(clientPlayer).isAbilityReady()){
				//send packet to the server to process the ability
				AMNetHandler.INSTANCE.sendAffinityActivate();
				//activate the ability on the client
				AffinityData.For(clientPlayer).onAffinityAbility();
			}
		}
	}

}
