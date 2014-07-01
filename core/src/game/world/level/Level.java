package game.world.level;

import game.entity.Camera;
import game.entity.component.SightRange;
import game.entity.gui.LevelTransition;
import game.entity.gui.Minimap;
import game.entity.gui.Money;
import game.entity.gui.TimerBar;
import game.world.EntityManager;
import game.world.Map;
import game.world.PhaseManager;
import game.world.SpawnerManager;
import game.world.Squad;
import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Level {

	protected Camera camera;
	protected Map map;
	protected EntityManager manager;
	protected Squad squad;
	protected RayHandler handler;
	protected World world;
	
	private boolean firstUpdate = true;
	
	public Level(String mapPath, int levelNum, Vector2 squadPos) {
		world = new World(new Vector2(), true);
		handler = new RayHandler(world);
		
		map = new Map(mapPath, world);
		
		manager = new EntityManager(map);
		manager.addEntity(camera = new Camera(new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight())));
		
		manager.addEntity(new Money(camera));
		manager.addEntity(new TimerBar(PhaseManager.getTimer(), PhaseManager.ENEMY_PHASE_TIME));
		manager.addEntity(new Minimap(camera, map.getWidth(), map.getHeight()));
		
		manager.addEntity(new LevelTransition(camera, levelNum));
		
		squad = new Squad(manager, handler, squadPos);
		PhaseManager.setSquad(squad);
		
		map.addEntities(manager, levelNum, squad);
	}
	
	public void update(float dt) {
		manager.update(camera, dt);
		squad.update(camera);
		handler.update();
		
		if (firstUpdate) {
			firstUpdate = false;
			PhaseManager.setPlayerPhase();
			SpawnerManager.setSpawners();
		}
	}
	
	public void render() {
		map.render(camera);
		manager.renderLit(camera);
		
		handler.setCombinedMatrix(camera.getCombinedMatrix());
		handler.render();
		
		manager.renderUnlit(camera);
	}
	
	public void dispose() {
		map.dispose();
		manager.dispose();
		handler.dispose();
		world.dispose();
		SightRange.resetSeen();
	}
	
}
