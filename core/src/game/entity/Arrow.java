package game.entity;

import game.entity.component.Projectile;
import game.entity.component.Render;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Arrow extends Entity {

	private static final float SPEED = 6;
	
	public Arrow(Vector2 pos, Vector2 target, float range) {
		super(new Rectangle(pos.x, pos.y, 32, 12));
		addComponent(new Render(new Sprite(new Texture("arrow.png"))));
		addComponent(new Projectile(target, SPEED, range));
	}

}
