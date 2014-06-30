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

public class EnergyBall extends Entity {

	private static final Sound collisionSound = Gdx.audio.newSound(Gdx.files.internal("energy ball.wav"));
	
	public EnergyBall(Vector2 pos, Vector2 target, float range) {
		super(new Rectangle(pos.x, pos.y, 48, 48));
		addComponent(new Render(new Sprite(new Texture("blue energy ball.png"))));
		addComponent(new Projectile(collisionSound, target, true, 8, range, 80, 5));
	}
	
}
