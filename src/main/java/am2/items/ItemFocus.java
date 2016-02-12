package am2.items;

public abstract class ItemFocus extends ArsMagicaItem{

	protected ItemFocus(){
		super();
	}

	public abstract Object[] getRecipeItems();

	public abstract String getInGameName();
}
