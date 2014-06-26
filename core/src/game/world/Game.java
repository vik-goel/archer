package game.world;

import game.entity.Camera;
import game.entity.Archer;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;

public class Game extends ApplicationAdapter {
	
	private Camera camera;
	private Map map;
	private EntityManager manager;
	
	public void create () {
		map = new Map(100, 100);
		
		manager = new EntityManager(map);
		manager.addEntity(camera = new Camera(new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight())));
		manager.addEntity(new Archer(new Vector2(100, 100)));
	}

	public void render() {
		updateWorld();
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		renderWorld();
	}
	
	private void updateWorld() {
		float cameraSpeed = 4;
		
		if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W))
			camera.getBounds().y += cameraSpeed;
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A))
			camera.getBounds().x -= cameraSpeed;
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S))
			camera.getBounds().y -= cameraSpeed;
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D))
			camera.getBounds().x += cameraSpeed;
	
		manager.update(camera);
	}
	
	private void renderWorld() {
		map.render(camera);
		manager.render(camera);
	}
	
	
}
