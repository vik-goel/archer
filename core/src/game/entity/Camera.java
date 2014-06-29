package game.entity;

import game.entity.component.ClickMove;
import game.world.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Camera extends Entity {

	private static final float CENTER_ACCELERATION = 0.4f;
	private static final float MAX_CENTER_SPEED = ClickMove.MOVE_SPEED;
	
	private static boolean movementEnabled = true;

	private OrthographicCamera orthoCamera;
	private Vector2 oldPos;
	private Vector2 newPos;

	private float cameraSpeed = 4;
	private float centerSpeed = 0;

	public Camera(Vector2 size) {
		super(new Rectangle(0, 0, size.x, size.y));

		oldPos = new Vector2(-size.x / 2, -size.y / 2);
		orthoCamera = new OrthographicCamera(bounds.width, bounds.height);
	}

	public void update(Camera camera, float dt) {
		super.update(camera, dt);

		centerCamera(dt);
		moveCameraBasedOnInput(dt);
		positionCameraInsideMap();
		updateCameraPosition();
	}

	private void centerCamera(float dt) {
		if (newPos != null) {
			centerSpeed += CENTER_ACCELERATION * dt;
			
			if (centerSpeed > MAX_CENTER_SPEED)
				centerSpeed = MAX_CENTER_SPEED;

			Vector2 moveAmount = new Vector2(newPos).sub(bounds.x + bounds.width / 2, bounds.y + bounds.height / 2);
			float length = moveAmount.len();
			moveAmount.nor().scl(Math.min(length, dt * centerSpeed));

			if (length <= dt * centerSpeed) {
				newPos = null;
				centerSpeed = 0;
			}

			bounds.x += moveAmount.x;
			bounds.y += moveAmount.y;
		}
	}

	private void moveCameraBasedOnInput(float dt) {
		if (!movementEnabled)
			return;

		float speed = cameraSpeed * dt;

		if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W))
			bounds.y += speed;
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A))
			bounds.x -= speed;
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S))
			bounds.y -= speed;
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D))
			bounds.x += speed;
	}

	private void positionCameraInsideMap() {
		if (bounds.x < 0)
			bounds.x = 0;

		else if (bounds.x + bounds.width > map.getWidth() * Map.TILE_SIZE)
			bounds.x = map.getWidth() * Map.TILE_SIZE - bounds.width;

		if (bounds.y < 0)
			bounds.y = 0;

		else if (bounds.y + bounds.height > map.getHeight() * Map.TILE_SIZE)
			bounds.y = map.getHeight() * Map.TILE_SIZE - bounds.height;
	}

	private void updateCameraPosition() {
		if (oldPos.x != bounds.x || oldPos.y != bounds.y) {
			orthoCamera.translate(bounds.x - oldPos.x, bounds.y - oldPos.y);
			orthoCamera.update();
			oldPos.set(bounds.x, bounds.y);
		}
	}

	public void centerAround(Vector2 pos) {
		this.newPos = pos;
	}

	public void projectBatch(SpriteBatch batch) {
		batch.setProjectionMatrix(orthoCamera.combined);
	}

	public void projectMap(MapRenderer renderer) {
		renderer.setView(orthoCamera);
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

	public Matrix4 getCombinedMatrix() {
		return orthoCamera.combined;
	}

	public static void setMovementEnabled(boolean movementEnabled) {
		Camera.movementEnabled = movementEnabled;
	}
}
