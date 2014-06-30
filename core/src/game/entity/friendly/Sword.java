package game.entity.friendly;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import game.entity.Entity;
import game.entity.component.Projectile;
import game.entity.component.Render;

public class Sword extends Entity {

private static final Sound collisionSound = Gdx.audio.newSound(Gdx.files.internal("sword.wav"));
	
	public Sword(Vector2 pos, Vector2 target, float range) {
		super(new Rectangle(pos.x, pos.y, 32, 12));
		addComponent(new Render(new Sprite(new Texture("sword.png"))));
		addComponent(new Projectile(collisionSound, target, true, 3, range, 39, 20));
	}
	
}
