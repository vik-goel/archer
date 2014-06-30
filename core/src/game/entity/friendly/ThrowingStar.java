package game.entity.friendly;

import game.entity.Entity;
import game.entity.component.Projectile;
import game.entity.component.Render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class ThrowingStar extends Entity {

	private static final Sound collisionSound = Gdx.audio.newSound(Gdx.files.internal("throwing star.wav"));
	
	public ThrowingStar(Vector2 pos, Vector2 target, float range) {
		super(new Rectangle(pos.x, pos.y, 24, 24));
		addComponent(new Render(new Sprite(new Texture("throwing star.png"))));
		addComponent(new Projectile(collisionSound, target, true, 10, range, 35, 15));
	}
	
}
