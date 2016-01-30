package am2.blocks;

import am2.blocks.renderers.*;
import am2.blocks.tileentities.*;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class BlocksClientProxy extends BlocksCommonProxy{
	public static TileEntityEssenceConduit essenceConduitInventoryRender;
	public static TileEntityObelisk obeliskInventoryRender;
	public static TileEntityCelestialPrism celestialPrismInventoryRender;
	public static TileEntityBlackAurem blackAuremInventoryRender;

	public static TileEntityCalefactor calefactorInventoryRenderer;
	public static TileEntityKeystoneRecepticle keystoneRecepticleInventoryRenderer;
	public static TileEntityAstralBarrier astralBarrierInventoryRenderer;
	public static TileEntitySeerStone seerStoneInventoryRenderer;
	public static TileEntityKeystoneChest keystoneChestInventoryRenderer;
	public static TileEntityLectern blockLecternInventoryRenderer;
	public static TileEntityArcaneReconstructor blockReconstructorInventoryRenderer;
	public static TileEntityOcculus occulusInventoryRenderer;
	public static TileEntityInscriptionTable inscriptionTableInventoryRenderer;
	public static TileEntitySummoner summonerInventoryRenderer;
	public static TileEntityMagiciansWorkbench magiciansWorkbenchInventoryRenderer;
	public static TileEntityEverstone everstoneInventoryRenderer;
	public static TileEntityCrystalMarker crystalMarkerInventoryRenderer;
	public static TileEntityCandle candleInventoryRenderer;
	public static TileEntityFlickerHabitat elementalAttunerInventoryRenderer;
	public static TileEntityOtherworldAura otherworldAuraInventoryRenderer;

	public BlocksClientProxy(){
		blockRenderID = RenderingRegistry.getNextAvailableRenderId();
		commonBlockRenderID = RenderingRegistry.getNextAvailableRenderId();

		essenceConduitInventoryRender = new TileEntityEssenceConduit();
		calefactorInventoryRenderer = new TileEntityCalefactor();
		keystoneRecepticleInventoryRenderer = new TileEntityKeystoneRecepticle();
		astralBarrierInventoryRenderer = new TileEntityAstralBarrier();
		seerStoneInventoryRenderer = new TileEntitySeerStone();
		keystoneChestInventoryRenderer = new TileEntityKeystoneChest();
		blockLecternInventoryRenderer = new TileEntityLectern();
		blockReconstructorInventoryRenderer = new TileEntityArcaneReconstructor();
		occulusInventoryRenderer = new TileEntityOcculus();
		obeliskInventoryRender = new TileEntityObelisk();
		celestialPrismInventoryRender = new TileEntityCelestialPrism();
		blackAuremInventoryRender = new TileEntityBlackAurem();
		inscriptionTableInventoryRenderer = new TileEntityInscriptionTable();
		summonerInventoryRenderer = new TileEntitySummoner();
		magiciansWorkbenchInventoryRenderer = new TileEntityMagiciansWorkbench();
		everstoneInventoryRenderer = new TileEntityEverstone();
		crystalMarkerInventoryRenderer = new TileEntityCrystalMarker();
		candleInventoryRenderer = new TileEntityCandle();
		elementalAttunerInventoryRenderer = new TileEntityFlickerHabitat();
		otherworldAuraInventoryRenderer = new TileEntityOtherworldAura();
	}

	@Override
	public void registerRenderInformation(){
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEssenceConduit.class, new EssenceConduitRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityObelisk.class, new EssenceGeneratorRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCalefactor.class, new CalefactorRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityKeystoneRecepticle.class, new KeystoneReceptacleRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAstralBarrier.class, new AstralBarrierRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySeerStone.class, new SeerStoneRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityKeystoneChest.class, new KeystoneChestRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLectern.class, new LecternRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityArcaneReconstructor.class, new ArcaneReconstructorRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityOcculus.class, new OcculusRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityInscriptionTable.class, new InscriptionTableRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySummoner.class, new SummonerRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMagiciansWorkbench.class, new MagiciansWorkbenchRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEverstone.class, new RenderEverstone());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCrystalMarker.class, new CrystalMarkerRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCrystalMarkerSpellExport.class, new CrystalMarkerRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCandle.class, new CandleRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFlickerHabitat.class, new FlickerHabitatRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityOtherworldAura.class, new RenderOtherworldAura());
	}
}
