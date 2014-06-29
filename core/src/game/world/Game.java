package game.world;

import game.entity.Camera;
import game.entity.Spawner;
import box2dLight.RayHandler;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Game extends ApplicationAdapter {

	private Camera camera;
	private Map map;
	private EntityManager manager;
	private Squad squad;
	private boolean firstUpdate = true;

	private RayHandler handler;
	private FPSLogger fpsLogger;
	
	public void create() {
		World world = new World(new Vector2(), true);
		handler = new RayHandler(world);
		
		map = new Map("test_map_2.tmx", world);

		manager = new EntityManager(map);
		manager.addEntity(camera = new Camera(new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight())));

		squad = new Squad(manager, handler);
		PhaseManager.setSquad(squad);

		manager.addEntity(new Spawner(new Vector2(770, 500), squad));

		fpsLogger = new FPSLogger();
	}

	public void render() {
		updateWorld(Gdx.graphics.getDeltaTime() * 60);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		renderWorld();
		
		fpsLogger.log();
	}

	private void updateWorld(float dt) {
		manager.update(camera, dt);
		squad.update();
		PhaseManager.update();
		handler.update();
		
		if (firstUpdate) {
			firstUpdate = false;
			SpawnerManager.setSpawners();
		}
	}

	private void renderWorld() {
		map.render(camera);
		manager.renderLit(camera);
		
		handler.setCombinedMatrix(camera.getCombinedMatrix());
		handler.render();
		
		manager.renderUnlit(camera);
	}

}
