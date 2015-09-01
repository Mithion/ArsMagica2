package am2.api.spell.enums;

import net.minecraft.item.Item;

public enum Affinity {
	NONE(0,
			0xFFFFFF,
			-1, //no direct opposite
			new int[] {}, //no major opposites
			new int[] {}, //no minor opposites
			new int[] {}),

			ARCANE(1,
					0xb935cd,
					9, //nature is direct opposite
					new int[] { 9, 4, 2, 7 }, //Major Opposites: life, earth, water, ice
					new int[] { 5, 10 }, //Minor Opposites: Air, Ender
					new int[] { 6, 3 }), //Adjacent Affinities: Lightning, Fire

					WATER(2,
							0x0b5cef,
							3, //fire is direct opposite
							new int[] { 7, 4, 1, 10 }, //Major Opposites: lightning, earth, arcane, ender
							new int[] { 5, 7 }, //Minor Opposites: Air, Ice
							new int[] { 9, 8 }), //Adjacent Affinities: Life, Nature

							FIRE(3,
									0xef260b,
									2, //water is direct opposite
									new int[] { 5, 7, 8, 9 }, //Major Opposites: air, ice, nature, life
									new int[] { 4, 6 }, //Minor Opposites: Earth, Lightning
									new int[] { 10, 1 }), //Adjacent Affinities: Ender, Arcane

									EARTH(4,
											0x61330b,
											5, //air is direct opposite
											new int[] { 2, 1, 9, 6 }, //Major Opposites: water, arcane, life, lightning
											new int[] { 8, 3 }, //Minor Opposites: Nature, Fire
											new int[] { 7, 10 }), //Adjacent Affinities: Ice, Ender

											AIR(5,
													0x777777,
													4, //earth is direct opposite
													new int[] { 8, 3, 7, 10 }, //Major Opposites: nature, fire, ice, ender
													new int[] { 2, 1 }, //Minor Opposites: Water, Arcane
													new int[] { 9, 6 }), //Adjacent Affinities: Life, Lightning

													LIGHTNING(6,
															0xdece19,
															7, //ice is direct opposite
															new int[] { 2, 10, 8, 4 }, //Major Opposites: water, ender, nature, earth
															new int[] { 9, 3 }, //Minor Opposites: Life, Fire
															new int[] { 5, 1 }), //Adjacent Affinities: Air, Arcane

															ICE(7,
																	0xd3e8fc,
																	6, //lightning is direct opposite
																	new int[] { 9, 3, 5, 1 }, //Major Opposites: life, fire, air, arcane
																	new int[] { 2, 10 }, //Minor Opposites: Water, Ender
																	new int[] { 8, 4 }), //Adjacent Affinities: Nature, Earth

																	NATURE(8,
																			0x228718,
																			6, //arcane is direct opposite
																			new int[] { 5, 10, 6, 3 }, //Major Opposites: air, ender, lightning, fire
																			new int[] { 9, 4 }, //Minor Opposites: Life, Earth
																			new int[] { 2, 7 }), //Adjacent Affinities: Water, Ice

																			LIFE(9,
																					0x34e122,
																					10, //ender is direct opposite
																					new int[] { 1, 7, 3, 4 }, //Major Opposites: arcane, ice, fire, earth
																					new int[] { 8, 6 }, //Minor Opposites: Nature, Lightning
																					new int[] { 2, 5 }), //Adjacent Affinities: Water, Air

																					ENDER(10,
																							0x3f043d,
																							9, //life is direct opposite
																							new int[] { 8, 6, 2, 5 }, //Major Opposites: nature, lightning, water, air
																							new int[] { 1, 7 }, //Minor Opposites: Arcane, Ice
																							new int[] { 4, 3 }); //Adjacent Affinities: Earth, Fire


	public int ID;
	private int directOpposingAffinity;
	private int[] majorOppositions;
	private int[] minorOppositions;
	private int[] adjacentAffinities;
	public Item representItem;
	public int representMeta;
	public final int color;

	private Affinity(int ID, int color, int directOppositeAffinity, int[] majorOppositions, int[] minorOppositions, int[] adjacentAffinities){
		this.ID = ID;
		this.directOpposingAffinity = directOppositeAffinity;
		this.majorOppositions = majorOppositions;
		this.minorOppositions = minorOppositions;
		this.adjacentAffinities = adjacentAffinities;
		this.color = color;
	}

	public Affinity getOpposingAffinity(){
		if (this.directOpposingAffinity > -1)
			return Affinity.values()[this.directOpposingAffinity];
		return Affinity.NONE;
	}

	public Affinity[] getMajorOpposingAffinities(){
		Affinity[] opposing = new Affinity[this.majorOppositions.length];
		int count = 0;
		for (int i : this.majorOppositions){
			opposing[count++] = Affinity.values()[i];
		}
		return opposing;
	}

	public Affinity[] getMinorOpposingAffinities(){
		Affinity[] opposing = new Affinity[this.minorOppositions.length];
		int count = 0;
		for (int i : this.minorOppositions){
			opposing[count++] = Affinity.values()[i];
		}
		return opposing;
	}

	public Affinity[] getAdjacentAffinities(){
		Affinity[] adjacent = new Affinity[this.adjacentAffinities.length];
		int count = 0;
		for (int i : this.adjacentAffinities){
			adjacent[count++] = Affinity.values()[i];
		}
		return adjacent;
	}

	public void setRepresentItem(Item representItem, int meta){
		if (this.representItem == null){
			this.representItem = representItem;
			this.representMeta = meta;
		}
	}

	public static Affinity getByID(int ID){
		for (Affinity affinity : Affinity.values()){
			if (affinity.ID == ID){
				return affinity;
			}
		}
		return Affinity.NONE;
	}

	public static Affinity[] getOrderedAffinities(){
		return new Affinity[]{
				AIR,
				LIGHTNING,
				ARCANE,
				FIRE,
				ENDER,
				EARTH,
				ICE,
				NATURE,
				WATER,
				LIFE
		};
	}

	public int getAffinityMask(){
		switch (this){
		case AIR:
			return 0x1;
		case ARCANE:
			return 0x2;
		case EARTH:
			return 0x4;
		case ENDER:
			return 0x8;
		case FIRE:
			return 0x10;
		case ICE:
			return 0x20;
		case LIFE:
			return 0x40;
		case LIGHTNING:
			return 0x80;
		case NATURE:
			return 0x100;
		case WATER:
			return 0x200;
		case NONE:
		default:
			return 0;
		}
	}
}
