package gunner.game.levels.tiles;

public class DamageTile extends BasicTile {

	private int[][] anamationTileCoords;
	private int currentAnamationIndex;
	private long lastIterationTime;
	private int anamationSwitchDelay;
	
	public DamageTile(int id, int[][] anamationCoords, int tileColour,
			int levelColour, int anamationSwitchDelay) {
		super(id, anamationCoords[0][0], anamationCoords[0][1], tileColour,
				levelColour);
		this.damage = true;
		this.anamationTileCoords = anamationCoords;
		this.currentAnamationIndex = 0;
		this.lastIterationTime = System.currentTimeMillis();
		this.anamationSwitchDelay = anamationSwitchDelay;
	}

	public void tick() {
		if ((System.currentTimeMillis() - lastIterationTime) >= (anamationSwitchDelay)) {
			lastIterationTime = System.currentTimeMillis();
			currentAnamationIndex = (currentAnamationIndex + 1)% anamationTileCoords.length;
			this.tileID = (anamationTileCoords[currentAnamationIndex][0] + (anamationTileCoords[currentAnamationIndex][1] * 32));
		}
	}

}
