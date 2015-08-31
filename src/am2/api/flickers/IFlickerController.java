package am2.api.flickers;

import net.minecraft.tileentity.TileEntity;

public interface IFlickerController <T extends TileEntity> {
	public byte[] getMetadata(IFlickerFunctionality operator);
	public void setMetadata(IFlickerFunctionality operator, byte[] meta);
	public void removeMetadata(IFlickerFunctionality operator);
}
