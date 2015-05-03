package gunner.game.entities;

import gunner.game.InputHandler;
import gunner.game.gfx.Colours;
import gunner.game.gfx.Font;
import gunner.game.gfx.Screen;
import gunner.game.items.Items;
import gunner.game.levels.Level;

public class Player extends Mob {

	private InputHandler input;
	private int colour = Colours.get(-1, 111, 523, 543);
	private int scale = 1;
	protected boolean isSwimming = false;
	private boolean gameOver = false;
	private boolean movement = true;
	private boolean key = false;
	private boolean rope = false;
	private int tickCount = 0;
	private int hearts = 5;
	private int health = 10;
	private int damageCount = 0;

	public Player(Level level, int x, int y, InputHandler input) {
		super(level, "Player", x, y, 1);
		this.input = input;
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

	public void tick2() {
		if (hasTakenDamage(0, 0)) {
			damageCount++;
			if (damageCount == 5) {
				colour = Colours.get(-1, 500, 500, 500);
			}
			if (damageCount == 15) {
				colour = Colours.get(-1, 111, 523, 543);
			}
			if (damageCount == 25) {
				colour = Colours.get(-1, 500, 500, 500);
			}
			if (damageCount == 35) {
				health--;
			}
			if (damageCount > 35) {
				damageCount = 0;
			}
		}

		if (!hasTakenDamage(0, 0)) {
			colour = Colours.get(-1, 111, 523, 543);

		}

	}

	public void tick() {

		int xa = 0;
		int ya = 0;

		if (input.up.isPressed() && movement) {
			ya--;
		}
		if (input.down.isPressed() && movement) {
			ya++;
		}
		if (input.left.isPressed() && movement) {
			xa--;
		}
		if (input.right.isPressed() && movement) {
			xa++;
		}

		if (xa != 0 || ya != 0) {
			move(xa, ya);
			isMoving = true;
		} else {
			isMoving = false;
		}

		if (level.getTile((this.x >> 3), (this.y >> 3)).getId() == 3
				|| (level.getTile((this.x >> 3), (this.y >> 3)).getId() == 4)) {
			isSwimming = true;
		}

		else {
			isSwimming = false;
		}

		/*
		 * if(damageCount == 100){ health--; } if(damageCount > 100){
		 * damageCount = 0; } damageCount++;
		 */
		tickCount++;
	}

	public boolean hasTakenDamage(int xa, int ya) {
		int xMin = 0;
		int xMax = 7;
		int yMin = 3;
		int yMax = 7;

		for (int x = xMin; x < xMax; x++) {
			if (isDamageTile(xa, ya, x, yMin)) {
				return true;
			}
		}

		for (int x = xMin; x < xMax; x++) {
			if (isDamageTile(xa, ya, x, yMax)) {
				return true;
			}
		}

		for (int y = yMin; y < yMax; y++) {
			if (isDamageTile(xa, ya, xMin, y)) {
				return true;
			}
		}

		for (int y = yMin; y < yMax; y++) {
			if (isDamageTile(xa, ya, xMax, y)) {
				return true;
			}
		}

		return false;
	}

	public void render(Screen screen) {
		int xTile = 0;
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
		int xOffset = x - modifier / 2;
		int yOffset = y - modifier / 2 - 4;

		if (isSwimming) {
			int waterColour = 0;
			yOffset += 4;
			if (tickCount % 60 < 15) {

				waterColour = Colours.get(-1, -1, 225, -1);
			} else if (15 <= tickCount % 60 && tickCount % 60 < 30) {
				yOffset -= 1;
				waterColour = Colours.get(-1, 225, 115, -1);
			} else if (30 <= tickCount % 60 && tickCount % 60 < 45) {
				waterColour = Colours.get(-1, 115, -1, 225);
			} else {
				yOffset -= 1;
				waterColour = Colours.get(-1, 225, 115, -1);
			}
			screen.render(xOffset, yOffset + 3, 0 + 27 * 32, waterColour, 0x00,
					1);
			screen.render(xOffset + 8, yOffset + 3, 0 + 27 * 32, waterColour,
					0x01, 1);

		}

		screen.render(xOffset + (modifier * flipTop), yOffset, xTile + yTile
				* 32, colour, flipTop, scale);
		screen.render(xOffset + modifier - (modifier * flipTop), yOffset,
				(xTile + 1) + yTile * 32, colour, flipTop, scale);

		if (!isSwimming) {
			screen.render(xOffset + (modifier * flipBottom),
					yOffset + modifier, xTile + (yTile + 1) * 32, colour,

					flipBottom, scale);
			screen.render(xOffset + modifier - (modifier * flipBottom), yOffset
					+ modifier, (xTile + 1) + (yTile + 1) * 32, colour,
					flipBottom, scale);
		}
		if (level.getTile((this.x >> 3), (this.y >> 3)).getId() == 15
				&& input.event.isPressed()) {
			// Font.render("You got a key!", screen, (xOffset + modifier) - 57,
			// (yOffset + modifier) - 29, Colours.get(555, -1, -1, 000), 1);
			if (hearts == 3) {
				hearts = 4;
				health = 8;
			}
			if (hearts == 5 && Items.Key == 0) {

				Font.render("You got a key!", screen,
						(xOffset + modifier) - 61, (yOffset + modifier) - 29,
						Colours.get(000, -1, -1, 055), 1);
				key = true;
			}

			else {
				Font.render("The Chest is Empty", screen,
						(xOffset + modifier) - 61, (yOffset + modifier) - 29,
						Colours.get(000, -1, -1, 500), 1);
			}
		}
		if (key && !input.event.isPressed()) {
			Items.Key = 1;
		}
		if (level.getTile((this.x >> 3), (this.y >> 3)).getId() == 16
				&& input.event.isPressed()) {
			// Font.render("You got a key!", screen, (xOffset + modifier) -
			// 57,
			// (yOffset + modifier) - 29, Colours.get(555, -1, -1, 000), 1);
			if (hearts == 4) {
				hearts = 5;
				health = 10;
			}

			if (hearts == 5 && Items.Key == 1) {
				Font.render("You got some rope!", screen,
						(xOffset + modifier) - 61, (yOffset + modifier) - 29,
						Colours.get(000, -1, -1, 055), 1);
				rope = true;
			}

			if (rope && !input.event.isPressed()) {
				Items.Rope = 1;
			}

			else {
				Font.render("The Chest is Empty", screen,
						(xOffset + modifier) - 61, (yOffset + modifier) - 29,
						Colours.get(000, -1, -1, 500), 1);
			}
		}

		if (input.inventory.isPressed()) {
			Font.render("- inventory -", screen, (xOffset + modifier) - 53,
					yOffset + modifier - 40, Colours.get(-1, -1, -1, 555), 1);
			if (Items.Key == 1) {
				Font.render("Key", screen, (xOffset + modifier) - 13,
						(yOffset + modifier) - 32,
						Colours.get(-1, -1, -1, 550), 1);
			}
			if (Items.Rope == 1) {
				Font.render("Rope", screen, (xOffset + modifier) - 17,
						(yOffset + modifier) -24,
						Colours.get(-1, -1, -1, 210), 1);
			}
		}
		if (health == 6 && hearts == 3) {
			Font.render("~~~", screen, (xOffset + modifier) - 13,
					(yOffset + modifier) - 17, Colours.get(-1, 000, 500, 555),
					1);
		}
		if (health == 5 && hearts == 3) {
			Font.render("~~`", screen, (xOffset + modifier) - 13,
					(yOffset + modifier) - 17, Colours.get(-1, 000, 500, 555),
					1);
		}
		if (health == 4 && hearts == 3) {
			Font.render("~~|", screen, (xOffset + modifier) - 13,
					(yOffset + modifier) - 17, Colours.get(-1, 000, 500, 555),
					1);
		}
		if (health == 3 && hearts == 3) {
			Font.render("~`|", screen, (xOffset + modifier) - 13,
					(yOffset + modifier) - 17, Colours.get(-1, 000, 500, 555),
					1);
		}
		if (health == 2 && hearts == 3) {
			Font.render("~||", screen, (xOffset + modifier) - 13,
					(yOffset + modifier) - 17, Colours.get(-1, 000, 500, 555),
					1);
		}
		if (health == 1 && hearts == 3) {
			Font.render("`||", screen, (xOffset + modifier) - 13,
					(yOffset + modifier) - 17, Colours.get(-1, 000, 500, 555),
					1);
		}
		if (health <= 0 && hearts == 3) {
			Font.render("|||", screen, (xOffset + modifier) - 13,
					(yOffset + modifier) - 17, Colours.get(-1, 000, 500, 555),
					1);
			gameOver = true;
		}
		if (health == 8 && hearts == 4) {
			Font.render("~~~~", screen, (xOffset + modifier) - 17,
					(yOffset + modifier) - 17, Colours.get(-1, 000, 500, 555),
					1);
		}
		if (health == 7 && hearts == 4) {
			Font.render("~~~`", screen, (xOffset + modifier) - 17,
					(yOffset + modifier) - 17, Colours.get(-1, 000, 500, 555),
					1);
		}
		if (health == 6 && hearts == 4) {
			Font.render("~~~|", screen, (xOffset + modifier) - 17,
					(yOffset + modifier) - 17, Colours.get(-1, 000, 500, 555),
					1);
		}
		if (health == 5 && hearts == 4) {
			Font.render("~~`|", screen, (xOffset + modifier) - 17,
					(yOffset + modifier) - 17, Colours.get(-1, 000, 500, 555),
					1);
		}
		if (health == 4 && hearts == 4) {
			Font.render("~~||", screen, (xOffset + modifier) - 17,
					(yOffset + modifier) - 17, Colours.get(-1, 000, 500, 555),
					1);
		}
		if (health == 3 && hearts == 4) {
			Font.render("~`||", screen, (xOffset + modifier) - 17,
					(yOffset + modifier) - 17, Colours.get(-1, 000, 500, 555),
					1);
		}
		if (health == 2 && hearts == 4) {
			Font.render("~|||", screen, (xOffset + modifier) - 17,
					(yOffset + modifier) - 17, Colours.get(-1, 000, 500, 555),
					1);
		}
		if (health == 1 && hearts == 4) {
			Font.render("`|||", screen, (xOffset + modifier) - 17,
					(yOffset + modifier) - 17, Colours.get(-1, 000, 500, 555),
					1);
		}
		if (health <= 0 && hearts == 4) {
			Font.render("||||", screen, (xOffset + modifier) - 17,
					(yOffset + modifier) - 17, Colours.get(-1, 000, 500, 555),
					1);
			gameOver = true;
		}
		if (health == 10 && hearts == 5) {
			Font.render("~~~~~", screen, (xOffset + modifier) - 20,
					(yOffset + modifier) - 17, Colours.get(-1, 000, 500, 555),
					1);
		}
		if (health == 9 && hearts == 5) {
			Font.render("~~~~`", screen, (xOffset + modifier) - 20,
					(yOffset + modifier) - 17, Colours.get(-1, 000, 500, 555),
					1);
		}
		if (health == 8 && hearts == 5) {
			Font.render("~~~~|", screen, (xOffset + modifier) - 20,
					(yOffset + modifier) - 17, Colours.get(-1, 000, 500, 555),
					1);
		}
		if (health == 7 && hearts == 5) {
			Font.render("~~~`|", screen, (xOffset + modifier) - 20,
					(yOffset + modifier) - 17, Colours.get(-1, 000, 500, 555),
					1);
		}
		if (health == 6 && hearts == 5) {
			Font.render("~~~||", screen, (xOffset + modifier) - 20,
					(yOffset + modifier) - 17, Colours.get(-1, 000, 500, 555),
					1);
		}
		if (health == 5 && hearts == 5) {
			Font.render("~~`||", screen, (xOffset + modifier) - 20,
					(yOffset + modifier) - 17, Colours.get(-1, 000, 500, 555),
					1);
		}
		if (health == 4 && hearts == 5) {
			Font.render("~~|||", screen, (xOffset + modifier) - 20,
					(yOffset + modifier) - 17, Colours.get(-1, 000, 500, 555),
					1);
		}
		if (health == 3 && hearts == 5) {
			Font.render("~`|||", screen, (xOffset + modifier) - 20,
					(yOffset + modifier) - 17, Colours.get(-1, 000, 500, 555),
					1);
		}
		if (health == 2 && hearts == 5) {
			Font.render("~||||", screen, (xOffset + modifier) - 20,
					(yOffset + modifier) - 17, Colours.get(-1, 000, 500, 555),
					1);
		}
		if (health == 1 && hearts == 5) {
			Font.render("`||||", screen, (xOffset + modifier) - 20,
					(yOffset + modifier) - 17, Colours.get(-1, 000, 500, 555),
					1);
		}
		if (health <= 0 && hearts == 5) {
			Font.render("|||||", screen, (xOffset + modifier) - 20,
					(yOffset + modifier) - 17, Colours.get(-1, 000, 500, 555),
					1);
			gameOver = true;
		}
		if (gameOver) {
			Font.render("G", screen, (xOffset + modifier) - 61,
					(yOffset + modifier) - 8, Colours.get(000, -1, -1, 500), 2);
			Font.render("A", screen, (xOffset + modifier) - 45,
					(yOffset + modifier) - 8, Colours.get(000, -1, -1, 500), 2);
			Font.render("M", screen, (xOffset + modifier) - 29,
					(yOffset + modifier) - 8, Colours.get(000, -1, -1, 500), 2);
			Font.render("E", screen, (xOffset + modifier) - 14,
					(yOffset + modifier) - 8, Colours.get(000, -1, -1, 500), 2);
			Font.render("-", screen, (xOffset + modifier) + 2,
					(yOffset + modifier) - 8, Colours.get(000, -1, -1, 500), 2);
			Font.render("O", screen, (xOffset + modifier) + 18,
					(yOffset + modifier) - 8, Colours.get(000, -1, -1, 500), 2);
			Font.render("V", screen, (xOffset + modifier) + 34,
					(yOffset + modifier) - 8, Colours.get(000, -1, -1, 500), 2);
			Font.render("E", screen, (xOffset + modifier) + 50,
					(yOffset + modifier) - 8, Colours.get(000, -1, -1, 500), 2);
			Font.render("R", screen, (xOffset + modifier) + 66,
					(yOffset + modifier) - 8, Colours.get(000, -1, -1, 500), 2);
			movement = false;

		}
	}
}
