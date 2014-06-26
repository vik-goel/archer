package game.world;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Tile {
	
	public static final int SIZE = 64;

	private Sprite sprite;
	
	public Tile(Sprite sprite) {
		this.sprite = sprite;
	}

	public void render(SpriteBatch batch) {
		sprite.draw(batch);
	}
	
}
