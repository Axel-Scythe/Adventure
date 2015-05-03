package gunner.game.levels.tiles;

import gunner.game.gfx.Colours;
import gunner.game.gfx.Screen;
import gunner.game.levels.Level;

public abstract class Tile {

	public static final Tile[] tiles = new Tile[256];
	public static final Tile VOID = new BasicSolidTile(0, 0, 0, Colours.get(
			000, -1, -1, -1), 0xff000000);
	public static final Tile GRASS = new BasicTile(1, 1, 0, Colours.get(-1,
			131, 141, -1), 0xff00ff00);
	public static final Tile WATER = new AnimatedTile(2, new int[][] {
			{ 0, 2 }, { 1, 2 }, { 2, 2 }, { 1, 2 } }, Colours.get(-1, 004, 115,
			-1), 0xff0000ff, 1000);
	
	protected byte id;
	protected boolean solid;
	protected boolean emmiter;
	protected boolean damage;
	private int levelColour;

	public Tile(int id, boolean isSolid, boolean isEmmiter, boolean isDamage,
			int levelColour) {
		this.id = (byte) id;
		if (tiles[id] != null)
			throw new RuntimeException("Same Tile Id on " + id);
		this.solid = isSolid;
		this.emmiter = isEmmiter;
		this.damage = isDamage;
		this.levelColour = levelColour;
		tiles[id] = this;
	}

	public byte getId() {
		return id;
	}

	public boolean isSolid() {
		return solid;
	}

	public boolean isEmmiter() {
		return emmiter;
	}

	public boolean isDamage() {
		return damage;
	}

	public int getLevelColour() {
		return levelColour;
	}

	public abstract void tick();

	public abstract void render(Screen screen, Level level, int x, int y);

}
