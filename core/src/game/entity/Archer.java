package game.entity;

import game.entity.component.ClickAttack;
import game.entity.component.ClickMove;
import game.entity.component.Clickable;
import game.entity.component.Render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Archer extends Entity {

	public Archer(Vector2 pos) {
		super(new Rectangle(pos.x, pos.y, 64, 64));
		
		addComponent(new Clickable(new Color(0f, 1f, 0f, 1f), -17, -40, 32, 16));
		
		Texture texture = new Texture("archer.png");
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		addComponent(new Render(new Sprite(texture, 0, 128, 64, 64)));
		
		addComponent(new ClickMove(8, 300));
		addComponent(new ClickAttack(200, 120, new Color(1f, 0f, 0f, 0.5f)));
	}

}
