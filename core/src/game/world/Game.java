package game.world;

import game.entity.Camera;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Game extends ApplicationAdapter {
	
	private Texture img;
	private SpriteBatch batch;
	
	private Camera camera;
	
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		
		camera = new Camera(new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
	}

	public void render() {
		updateWorld();
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
	
		camera.update(camera);
	}
	
	private void renderWorld() {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.projectBatch(batch);
		batch.begin();
		batch.draw(img, 50, 50);
		batch.end();
	}
	
	
}
