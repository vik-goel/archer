package game.world;

import game.entity.Camera;
import game.entity.Skeleton;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;

public class Game extends ApplicationAdapter {
	
	private Camera camera;
	private Map map;
	private EntityManager manager;
	private Squad squad;
	
	public void create () {
		map = new Map("test_map_2.tmx");
		
		manager = new EntityManager(map);
		
		manager.addEntity(camera = new Camera(new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight())));
		manager.addEntity(new Skeleton(new Vector2(500, 160)));
		
		squad = new Squad(manager);
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
	}
	
	private void renderWorld() {
		map.render(camera);
		manager.render(camera);
	}
	
	
}
