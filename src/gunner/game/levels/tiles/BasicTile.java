package gunner.game.levels.tiles;

import gunner.game.gfx.Screen;
import gunner.game.levels.Level;

public class BasicTile extends Tile {

	protected int tileID, tileColour;

	public BasicTile(int id, int x, int y, int tileColour, int levelColour) {
		super(id, false, false, false, levelColour);
		this.tileID = x + y * 32;
		this.tileColour = tileColour;
	}

	public void tick(){
		
	}
	
	public void render(Screen screen, Level level, int x, int y) {
		screen.render(x, y, tileID, tileColour, 0x00, 1);
	}

}
