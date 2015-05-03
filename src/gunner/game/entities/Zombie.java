package gunner.game.entities;

import gunner.game.gfx.Colours;
import gunner.game.gfx.Screen;
import gunner.game.levels.Level;

public class Zombie extends Mob {

	private int colour = Colours.get(-1, 111, 145, 120);
	private int scale = 1;
	protected boolean isSwimming = false;
	private int tickCount = 0;
	public Zombie(Level level, int x, int y) {
		super(level, "Zombie", x, y, 1);
	}

	public boolean hasCollided(int xa, int ya) {

		int xMin = 0;
		int xMax = 7;
		int yMin = 3;
		int yMax = 7;

		for (int x = xMin; x < xMax; x++) {
			if (isSolidTile(xa, ya, x, yMin)) {
				return true;
			}
		}

		for (int x = xMin; x < xMax; x++) {
			if (isSolidTile(xa, ya, x, yMax)) {
				return true;
			}
		}

		for (int y = yMin; y < yMax; y++) {
			if (isSolidTile(xa, ya, xMin, y)) {
				return true;
			}
		}

		for (int y = yMin; y < yMax; y++) {
			if (isSolidTile(xa, ya, xMax, y)) {
				return true;
			}
		}

		return false;
	}

	public void tick() {

		int xa = 0;
		int ya = 0;

		/*
		if (tickCount == 20) {
			y++;
			y++;
			y++;
			y++;
			y++;
			y++;
			y++;
			y++;
			// /System.out.println("Zombie Down");
		}
		if (tickCount == 40) {
			x++;
			x++;
			x++;
			x++;

		}
		if (tickCount == 45) {
			x++;
			x++;
			x++;
			x++;
			x++;
		}
		if (tickCount == 50) {
			x++;
			x++;
			x++;
			x++;
			// System.out.println("Zombie Left");
		}
		if (tickCount == 55) {

			x++;
			x++;
			x++;
			x++;
			x++;
		}
		if (tickCount == 60) {
			y--;
			y--;
			y--;
			y--;
			y--;
			y--;
			y--;
			y--;
			// System.out.println("Zombie Up");
		}
		if (tickCount == 65) {
			x--;
			x--;
			x--;
			x--;
		}
		if (tickCount == 70) {
			x--;
			x--;
			x--;
			x--;
			x--;
		}
		if (tickCount == 75) {
			x--;
			x--;
			x--;
			x--;
			// System.out.println("Zombie Right");
		}
		if (tickCount == 80) {
			x--;
			x--;
			x--;
			x--;
			x--;
		}
		if (tickCount == 81) {
			tickCount = 0;
			// System.out.println("Tick Reset");
		}
		*/
		if (xa != 0 || ya != 0) {
			move(xa, ya);
			isMoving = true;
		} else {
			isMoving = false;
		}

		if (level.getTile((this.x >> 3), (this.y >> 3)).getId() == 3 || (level.getTile((this.x >> 3), (this.y >> 3)).getId() == 4)) {
			isSwimming = true;
		}
		
		else{
			isSwimming = false;
		}
		//tickCount++;

	}

	public void render(Screen screen) {
		int xTile = 9;
		int yTile = 28;
		int walkingSpeed = 4;
		int flipTop = (numSteps >> walkingSpeed) & 1;
		int flipBottom = (numSteps >> walkingSpeed) & 1;

		if (movingDir == 1) {
			xTile += 2;
		}

		else if (movingDir > 1) {
			xTile += 4 + ((numSteps >> walkingSpeed) & 1) * 2;
			flipTop = (movingDir - 1) % 2;
		}

		int modifier = 8 * scale;
		int xOffsetZ = x - modifier / 2;
		int yOffsetZ = y - modifier / 2 - 4;

		if (isSwimming) {
			int waterColour = 0;
			yOffsetZ += 4;
			if (tickCount % 60 < 15) {

				waterColour = Colours.get(-1, -1, 225, -1);
			} else if (15 <= tickCount % 60 && tickCount % 60 < 30) {
				yOffsetZ -= 1;
				waterColour = Colours.get(-1, 225, 115, -1);
			} else if (30 <= tickCount % 60 && tickCount % 60 < 45) {
				waterColour = Colours.get(-1, 115, -1, 225);
			} else {
				yOffsetZ -= 1;
				waterColour = Colours.get(-1, 225, 115, -1);
			}
			screen.render(xOffsetZ, yOffsetZ + 3, 0 + 27 * 32, waterColour,
					0x00, 1);
			screen.render(xOffsetZ + 8, yOffsetZ + 3, 0 + 27 * 32, waterColour,
					0x01, 1);

		}

		screen.render(xOffsetZ + (modifier * flipTop), yOffsetZ, xTile + yTile
				* 32, colour, flipTop, scale);
		screen.render(xOffsetZ + modifier - (modifier * flipTop), yOffsetZ,
				(xTile + 1) + yTile * 32, colour, flipTop, scale);

		if (!isSwimming) {
			screen.render(xOffsetZ + (modifier * flipBottom), yOffsetZ
					+ modifier, xTile + (yTile + 1) * 32, colour, flipBottom,
					scale);
			screen.render(xOffsetZ + modifier - (modifier * flipBottom),
					yOffsetZ + modifier, (xTile + 1) + (yTile + 1) * 32,
					colour, flipBottom, scale);
		}
	}

	@Override
	public boolean hasTakenDamage(int xa, int ya) {
		// TODO Auto-generated method stub
		return false;
	}
}