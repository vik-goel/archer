package game.entity.gui;

import game.entity.Camera;
import game.entity.Entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Money extends Entity {

	private static final BitmapFont FONT = new BitmapFont(Gdx.files.internal("fonts/handwritten.fnt"), Gdx.files.internal("fonts/handwritten.png"), false);
	
	private static int money = 0;
	
	public Money(Camera camera) {
		super(new Rectangle(camera.getBounds().width - 120, camera.getBounds().height - 20, 110, 10));
	}
	
	public void renderLate(Camera camera, SpriteBatch batch) {
		batch.begin();
		FONT.setColor(1.0f, 1.0f, 0.0f, 1.0f);
		FONT.draw(batch, "$" + money, bounds.x + camera.getBounds().x, bounds.y + camera.getBounds().y);
		batch.end();
	}
	
	public static void add(int amt) {
		money += amt;
	}
	
	public static void spend(int amt) {
		money -= amt;
	}
	
	public boolean canAfford(int cost) {
		return money >= cost;
	}
}
