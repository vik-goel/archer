package game.entity.enemy;

import game.entity.Entity;
import game.entity.component.MobSpawner;
import game.entity.component.Render;
import game.world.Squad;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Spawner extends Entity {

	public Spawner(Vector2 pos, Squad squad) {
		super(new Rectangle(pos.x, pos.y, 50, 50));
		addComponent(new Render(null));
		addComponent(new MobSpawner(squad));
	}
	
	

}
