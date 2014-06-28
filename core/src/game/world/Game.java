package game.world;

import game.entity.Camera;
import game.entity.Spawner;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;

public class Game extends ApplicationAdapter {

	private Camera camera;
	private Map map;
	private EntityManager manager;
	private Squad squad;
	private boolean firstUpdate = true;

	public void create() {
		map = new Map("test_map_2.tmx");

		manager = new EntityManager(map);
		manager.addEntity(camera = new Camera(new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight())));

		squad = new Squad(manager);
		PhaseManager.setSquad(squad);

		manager.addEntity(new Spawner(new Vector2(770, 500), squad));
	}

	public void render() {
		updateWorld();

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		renderWorld();
	}

	private void updateWorld() {
		manager.update(camera);
		squad.update();
		PhaseManager.update();

		if (firstUpdate) {
			firstUpdate = false;
			SpawnerManager.setSpawners();
		}
	}

	private void renderWorld() {
		map.render(camera);
		manager.render(camera);
	}

}
