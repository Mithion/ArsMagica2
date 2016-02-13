package am2.proxy.gui;

import am2.api.blocks.IKeystoneLockable;
import am2.blocks.tileentities.*;
import am2.guis.*;
import am2.items.*;
import am2.playerextensions.RiftStorage;
import am2.utility.InventoryUtilities;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class ClientGuiManager extends ServerGuiManager{

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z){
		TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
		if (te == null && ID != ArsMagicaGuiIdList.GUI_SPELL_BOOK && ID != ArsMagicaGuiIdList.GUI_KEYSTONE && ID != ArsMagicaGuiIdList.GUI_ESSENCE_BAG && ID != ArsMagicaGuiIdList.GUI_RUNE_BAG && ID != ArsMagicaGuiIdList.GUI_RIFT && ID != ArsMagicaGuiIdList.GUI_SPELL_CUSTOMIZATION){
			return null;
		}
		switch (ID){
		case ArsMagicaGuiIdList.GUI_ESSENCE_REFINER:
			if (!(te instanceof TileEntityEssenceRefiner)){
				return null;
			}
			return new GuiEssenceRefiner(player.inventory, (TileEntityEssenceRefiner)te);
		case ArsMagicaGuiIdList.GUI_SPELL_BOOK:
			ItemStack bookStack = player.getCurrentEquippedItem();
			if (bookStack == null || bookStack.getItem() == null || !(bookStack.getItem() instanceof ItemSpellBook)){
				return null;
			}
			ItemSpellBook item = (ItemSpellBook)bookStack.getItem();
			return new GuiSpellBook(player.inventory, player.getCurrentEquippedItem(), item.ConvertToInventory(bookStack));
		case ArsMagicaGuiIdList.GUI_CALEFACTOR:
			if (!(te instanceof TileEntityCalefactor)){
				return null;
			}
			return new GuiCalefactor(player, (TileEntityCalefactor)te);
		case ArsMagicaGuiIdList.GUI_KEYSTONE_LOCKABLE:
			if (!(te instanceof IKeystoneLockable)){
				return null;
			}
			return new GuiKeystoneLockable(player.inventory, (IKeystoneLockable)te);
		case ArsMagicaGuiIdList.GUI_ASTRAL_BARRIER:
			if (!(te instanceof TileEntityAstralBarrier)){
				return null;
			}
			return new GuiAstralBarrier(player.inventory, (TileEntityAstralBarrier)te);
		case ArsMagicaGuiIdList.GUI_SEER_STONE:
			if (!(te instanceof TileEntitySeerStone)){
				return null;
			}
			return new GuiSeerStone(player.inventory, (TileEntitySeerStone)te);
		case ArsMagicaGuiIdList.GUI_KEYSTONE_CHEST:
			if (!(te instanceof TileEntityKeystoneChest)){
				return null;
			}
			return new GuiKeystoneChest(player.inventory, (TileEntityKeystoneChest)te);
		case ArsMagicaGuiIdList.GUI_KEYSTONE:
			ItemStack keystoneStack = player.getCurrentEquippedItem();
			if (keystoneStack == null || keystoneStack.getItem() == null || !(keystoneStack.getItem() instanceof ItemKeystone)){
				return null;
			}
			ItemKeystone keystone = (ItemKeystone)keystoneStack.getItem();

			int runeBagSlot = InventoryUtilities.getInventorySlotIndexFor(player.inventory, ItemsCommonProxy.runeBag);
			ItemStack runeBag = null;
			if (runeBagSlot > -1)
				runeBag = player.inventory.getStackInSlot(runeBagSlot);

			return new GuiKeystone(player.inventory, player.getCurrentEquippedItem(), runeBag, keystone.ConvertToInventory(keystoneStack), runeBag == null ? null : ItemsCommonProxy.runeBag.ConvertToInventory(runeBag), runeBagSlot);
		case ArsMagicaGuiIdList.GUI_ESSENCE_BAG:
			ItemStack bagStack = player.getCurrentEquippedItem();
			if (bagStack.getItem() == null || !(bagStack.getItem() instanceof ItemEssenceBag)){
				return null;
			}
			ItemEssenceBag bag = (ItemEssenceBag)bagStack.getItem();
			return new GuiEssenceBag(player.inventory, player.getCurrentEquippedItem(), bag.ConvertToInventory(bagStack));
		case ArsMagicaGuiIdList.GUI_RUNE_BAG:
			bagStack = player.getCurrentEquippedItem();
			if (bagStack.getItem() == null || !(bagStack.getItem() instanceof ItemRuneBag)){
				return null;
			}
			ItemRuneBag runebag = (ItemRuneBag)bagStack.getItem();
			return new GuiRuneBag(player.inventory, player.getCurrentEquippedItem(), runebag.ConvertToInventory(bagStack));
		case ArsMagicaGuiIdList.GUI_ARCANE_RECONSTRUCTOR:
			if (!(te instanceof TileEntityArcaneReconstructor)){
				return null;
			}
			return new GuiArcaneReconstructor(player.inventory, (TileEntityArcaneReconstructor)te);
		case ArsMagicaGuiIdList.GUI_SUMMONER:
			if (!(te instanceof TileEntitySummoner)){
				return null;
			}
			return new GuiSummoner(player.inventory, (TileEntitySummoner)te);
		case ArsMagicaGuiIdList.GUI_INSCRIPTION_TABLE:
			if (!(te instanceof TileEntityInscriptionTable)){
				return null;
			}
			return new GuiInscriptionTable(player.inventory, (TileEntityInscriptionTable)te);
		case ArsMagicaGuiIdList.GUI_MAGICIANS_WORKBENCH:
			if (!(te instanceof TileEntityMagiciansWorkbench)){
				return null;
			}
			return new GuiMagiciansWorkbench(player.inventory, (TileEntityMagiciansWorkbench)te);
		case ArsMagicaGuiIdList.GUI_RIFT:
			return new GuiRiftStorage(player.inventory, RiftStorage.For(player));
		case ArsMagicaGuiIdList.GUI_SPELL_CUSTOMIZATION:
			return new GuiSpellCustomization(player);
		case ArsMagicaGuiIdList.GUI_CRYSTAL_MARKER:
			if (!(te instanceof TileEntityCrystalMarker)){
				return null;
			}
			return new GuiCrystalMarker(player, (TileEntityCrystalMarker)te);

		case ArsMagicaGuiIdList.GUI_OBELISK:
			if (!(te instanceof TileEntityObelisk)){
				return null;
			}
			return new GuiObelisk((TileEntityObelisk)te, player);

		case ArsMagicaGuiIdList.GUI_FLICKER_HABITAT:
			if (!(te instanceof TileEntityFlickerHabitat)){
				return null;
			}
			return new GuiFlickerHabitat(player, (TileEntityFlickerHabitat)te);
		case ArsMagicaGuiIdList.GUI_ARMOR_INFUSION:
			if (!(te instanceof TileEntityArmorImbuer)){
				return null;
			}
			return new GuiArmorImbuer(player, (TileEntityArmorImbuer)te);
		case ArsMagicaGuiIdList.GUI_ARCANE_DECONSTRUCTOR:
			if (!(te instanceof TileEntityArcaneDeconstructor)){
				return null;
			}
			return new GuiArcaneDeconstructor(player.inventory, (TileEntityArcaneDeconstructor)te);
		case ArsMagicaGuiIdList.GUI_INERT_SPAWNER:
			if (!(te instanceof TileEntityInertSpawner)){
				return null;
			}
			return new GuiInertSpawner(player, (TileEntityInertSpawner)te);
		case ArsMagicaGuiIdList.GUI_SPELL_SEALED_DOOR:
			if (!(te instanceof TileEntitySpellSealedDoor)){
				return null;
			}
			return new GuiSpellSealedDoor(player.inventory, (TileEntitySpellSealedDoor)te);
		}
		return null;
	}

}
