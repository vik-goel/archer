package game.entity;

import game.entity.component.Render;
import game.world.Game;
import game.world.Squad;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Staircase extends Entity {

	private int levelNum;
	private Squad squad;
	private boolean consumedEntity = false;
	
	public Staircase(Vector2 pos, boolean down, int levelNum, Squad squad) {
		super(new Rectangle(pos.x, pos.y, 32, 32));

		this.levelNum = levelNum;
		this.squad = squad;
		
		String spritePath = "up staircase.png";
		
		if (down) 
			spritePath = "down staircase.png";
		
		addComponent(new Render(new Sprite(new Texture(spritePath))));
	}

	public void update(Camera camera, float dt) {
		super.update(camera, dt);
		
		if (squad.getEntities().isEmpty() && consumedEntity) {
			Game.setLevel(levelNum);
			return;
		}
		
		for (int i = 0; i < squad.getEntities().size(); i++) {
			if (squad.getEntities().get(i).getBounds().overlaps(getBounds())) {
				squad.getEntities().get(i).remove();
				consumedEntity = true;
			}
		}
	}

}
