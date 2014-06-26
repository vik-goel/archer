package game.entity;

import game.entity.component.Render;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class TestEntity extends Entity {

	public TestEntity(Vector2 pos) {
		super(new Rectangle(pos.x, pos.y, 100, 100));
		addComponent(new Render(new Sprite(new Texture("badlogic.jpg"))));
	}

}
