package am2.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import am2.api.spell.enums.Affinity;
import am2.blocks.tileentities.TileEntityGroundRuneSpell;
import am2.texture.ResourceManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockGroundRuneSpell extends BlockGroundRune{

	@SideOnly(Side.CLIENT)
	private IIcon[] icons;
	private String[] textureNames = {"rune_affinity_none", "rune_affinity_arcane", "rune_affinity_water", "rune_affinity_fire", "rune_affinity_earth", "rune_affinity_air", "rune_affinity_lightning", "rune_affinity_ice", "rune_affinity_plant", "rune_affinity_life", "rune_affinity_ender"};

	protected BlockGroundRuneSpell() {
		super();
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int i) {
		return new TileEntityGroundRuneSpell();
	}

	@SideOnly(Side.CLIENT)
	@Override
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        this.icons = new IIcon[textureNames.length];

        for (int i = 0; i < textureNames.length; ++i)
        {
            this.icons[i] = ResourceManager.RegisterTexture(textureNames[i], iconRegister);
        }
    }

	@Override
	public String GetRuneTexture() {
		return textureNames[0];
	}

	@Override
	public IIcon getIcon(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		if (l == 1 || l == 0){
			int meta = iblockaccess.getBlockMetadata(i, j, k);
			return icons[(meta & 0x0000FF)];
		}
		return null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int par1, int par2) {
		if (par1 == 1 || par1 == 0){
			return icons[(par2 & 0x0000FF)];
		}
		return null;
	}

	private int getTextureForAffinity(Affinity aff){
		int texIndex = aff.ordinal();
		return texIndex;
	}

	@Override
	protected boolean ActivateRune(World world, List<Entity> entitiesInRange, int x, int y, int z) {
		TileEntityGroundRuneSpell te = getTileEntity(world, x, y, z);
		if (te == null)
			return false;
		for (Entity e : entitiesInRange){
			if (e instanceof EntityLivingBase){
				te.applySpellEffect((EntityLivingBase) e);
				break;
			}
		}
		return true;
	}

	@Override
	protected boolean isPermanent(World world, int x, int y, int z, int metadata) {
		TileEntityGroundRuneSpell te = getTileEntity(world, x, y, z);
		if (te == null)
			return false;
		return te.getPermanent();
	}

	@Override
	protected int getNumTriggers(World world, int x, int y, int z, int metadata) {
		TileEntityGroundRuneSpell te = getTileEntity(world, x, y, z);
		if (te == null)
			return 1;
		return te.getNumTriggers();
	}

	@Override
	public void setNumTriggers(World world, int x, int y, int z, int meta, int numTriggers) {
		TileEntityGroundRuneSpell te = getTileEntity(world, x, y, z);
		if (te == null)
			return;
		te.setNumTriggers(numTriggers);
		if (numTriggers == -1)
			te.setPermanent(true);
	}

	private TileEntityGroundRuneSpell getTileEntity(World world, int x, int y, int z){
		TileEntity te = world.getTileEntity(x, y, z);
		if (te instanceof TileEntityGroundRuneSpell){
			return (TileEntityGroundRuneSpell)te;
		}
		return null;
	}

	public void setSpellStack(World world, int x, int y, int z, ItemStack effect){
		TileEntityGroundRuneSpell te = getTileEntity(world, x, y, z);
		if (te == null)
			return;
		te.setSpellStack(effect);
	}

	public void setPlacedBy(World world, int x, int y, int z, EntityLivingBase caster){
		TileEntityGroundRuneSpell te = getTileEntity(world, x, y, z);
		if (te == null)
			return;
		te.setPlacedBy(caster);
	}

    @Override
	public boolean placeAt(World world, int x, int y, int z, int meta){
    	if (!canPlaceBlockAt(world, x, y, z)) return false;
    	if (!world.isRemote)
    		world.setBlock(x, y, z, this, meta, 2);
    	return true;
    }
}
