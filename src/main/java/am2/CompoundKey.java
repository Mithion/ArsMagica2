package am2;

import net.minecraft.item.Item;

public class CompoundKey{
	public Item item;
	public int meta;

	private Integer cachedHash = null;

	public CompoundKey(Item item, int meta){
		this.item = item;
		this.meta = meta;
	}

	public void setMeta(int meta){
		this.meta = meta;
		this.cachedHash = null;
	}

	@Override
	public boolean equals(Object other){
		if (other == null) return false;
		if (other == this) return true;
		if (!(other instanceof CompoundKey)) return false;
		CompoundKey right = (CompoundKey)other;
		return right.item == this.item && (right.meta == this.meta || right.meta == -1 || this.meta == -1);
	}

	@Override
	public int hashCode(){
		if (cachedHash == null){
			String hashString = String.format("%d@%d", Item.getIdFromItem(this.item), this.meta);
			cachedHash = hashString.hashCode();
		}
		return cachedHash;
	}
}