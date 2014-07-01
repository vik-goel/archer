package game.world.level;

import game.entity.MoneyBag;
import game.entity.Staircase;
import game.entity.enemy.Spawner;

import com.badlogic.gdx.math.Vector2;

public class TestLevel extends Level {

	public TestLevel(int levelNum) {
		super("test_map.tmx", levelNum);
		
		manager.addEntity(new Spawner(new Vector2(770, 500), squad));
		manager.addEntity(new Spawner(new Vector2(155, 825), squad));

		manager.addEntity(new MoneyBag(new Vector2(200, 200)));
		
		manager.addEntity(new Staircase(new Vector2(300, 300), true, 2, squad));
	}

}
