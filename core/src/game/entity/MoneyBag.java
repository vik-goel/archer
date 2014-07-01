package game.entity;

import game.entity.component.MoneyCollider;
import game.entity.component.Render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class MoneyBag extends Entity {

	private static final Sound collisionSound = Gdx.audio.newSound(Gdx.files.internal("money.wav"));
	
	public MoneyBag(Vector2 pos) {
		super(new Rectangle(pos.x, pos.y, 32, 32));
		addComponent(new Render(new Sprite(new Texture("money bag.png"))));
		addComponent(new MoneyCollider(100, collisionSound));
	}

	
	
}
