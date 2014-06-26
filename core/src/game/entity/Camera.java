package game.entity;

import game.world.Tile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Camera extends Entity {

	private OrthographicCamera orthoCamera;
	private Vector2 oldPos;

	public Camera(Vector2 size) {
		super(new Rectangle(0, 0, size.x, size.y));

		oldPos = new Vector2(-size.x / 2, -size.y / 2);
		orthoCamera = new OrthographicCamera(bounds.width, bounds.height);
	}

	public void update(Camera camera) {
		super.update(camera);

		positionCameraInsideMap();
		updateCameraPosition();
	}

	private void positionCameraInsideMap() {
		if (bounds.x < 0)
			bounds.x = 0;

		else if (bounds.x + bounds.width > map.getWidth() * Tile.SIZE)
			bounds.x = map.getWidth()  * Tile.SIZE - bounds.width;

		if (bounds.y < 0)
			bounds.y = 0;

		else if (bounds.y + bounds.height > map.getHeight() * Tile.SIZE)
			bounds.y = map.getHeight()  * Tile.SIZE - bounds.height;
	}

	private void updateCameraPosition() {
		if (oldPos.x != bounds.x || oldPos.y != bounds.y) {
			orthoCamera.translate(bounds.x - oldPos.x, bounds.y - oldPos.y);
			orthoCamera.update();
			oldPos.set(bounds.x, bounds.y);
		}
	}

	public void projectBatch(SpriteBatch batch) {
		batch.setProjectionMatrix(orthoCamera.combined);
	}
	
	public Vector2 toWorldPos(Vector2 screenPos) {
		Vector3 screenPos3 = new Vector3(screenPos.x, screenPos.y, 0);
		Vector3 worldPos3 = orthoCamera.unproject(screenPos3);
		return new Vector2(worldPos3.x, worldPos3.y);
	}

	public Vector2 getMousePosInWorld() {
		Vector2 mousePos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
		return toWorldPos(mousePos);
	}
	
	public Vector2 toScreenPos(Vector2 worldPos) {
		Vector3 worldPos3 = new Vector3(worldPos.x, worldPos.y, 0);
		Vector3 screenPos3 = orthoCamera.project(worldPos3);
		return new Vector2(screenPos3.x, screenPos3.y);
	}
}
