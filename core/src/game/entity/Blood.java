package game.entity;

import game.entity.component.Render;

import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Blood extends Entity {

	private static Texture texture = new Texture("blood.png");
	private static Random random = new Random();

	public Blood(Vector2 pos) {
		super(new Rectangle(pos.x, pos.y, 0, 0));

		Sprite sprite = getSprite();

		bounds.width = sprite.getWidth();
		bounds.height = sprite.getHeight();
		bounds.x -= sprite.getWidth() / 2;
		bounds.y -= sprite.getHeight() / 2;

		addComponent(new Render(sprite).setEarly(true));
	}

	private static Sprite getSprite() {
		switch (random.nextInt(12)) {
		case 0:
			return new Sprite(texture, 0, 0, 64, 96);
		case 1:
			return new Sprite(texture, 64, 0, 64, 96);
		case 2:
			return new Sprite(texture, 128, 0, 96, 96);
		case 3:
			return new Sprite(texture, 224, 0, 96, 96);
		case 4:
			return new Sprite(texture, 320, 0, 96, 96);
		case 5:
			return new Sprite(texture, 416, 0, 96, 96);
		case 6:
			return new Sprite(texture, 0, 96, 96, 60);
		case 7:
			return new Sprite(texture, 96, 96, 96, 60);
		case 8:
			return new Sprite(texture, 192, 96, 96, 60);
		case 9:
			return new Sprite(texture, 0, 96, 160, 60);
		case 10:
			return new Sprite(texture, 96, 96, 160, 60);
		case 11:
			return new Sprite(texture, 192, 96, 160, 60);
		}

		return null;
	}

	static {
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}

}
