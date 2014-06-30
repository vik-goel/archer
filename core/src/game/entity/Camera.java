package game.entity;

import game.world.Map;

import java.util.Random;

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

	private static final float CENTER_ACCELERATION = 0.05f;
	private static final float MAX_CENTER_SPEED = 4;
	private static final float MIN_MOVE = 0.5f;

	private static final float SHAKE_ACCELERATION = 0.6f;
	private static final float VERTICAL_SHAKE_BIAS = 1;
	
	private static boolean movementEnabled = true;
	private static Random random = new Random();

	private OrthographicCamera orthoCamera;
	private Vector2 oldPos;
	private Vector2 newPos;

	private float cameraSpeed = 4;
	private float centerSpeed = 0;
	
	private boolean horMove, vertMove;
	
	private Vector2 shakeDir = new Vector2();
	private Vector2 shakeEffect = new Vector2();
	private int shakeCount;
	private float shakeMagnitude, minMagnitude, maxMagnitude, shakeSpeed;
	private boolean shakeOut;

	public Camera(Vector2 size) {
		super(new Rectangle(0, 0, size.x, size.y));

		oldPos = new Vector2(-size.x / 2, -size.y / 2);
		orthoCamera = new OrthographicCamera(bounds.width, bounds.height);
	}

	public void update(Camera camera, float dt) {
		super.update(camera, dt);

		horMove = vertMove = false;
		
		shakePosition(dt);
		moveCameraBasedOnInput(dt);
		centerCamera(dt);
		positionCameraInsideMap();
		updateCameraPosition();
	}

	//TODO: Use dt in this calculation
	private void shakePosition(float dt) {
		if (shakeCount < 0)
			return;
		
		if (shakeOut) {
			if (shakeSpeed >= shakeMagnitude) {
				shakeOut = false;
				shakeSpeed = 0;
			} else 
				shakeSpeed += SHAKE_ACCELERATION;
		}
		
		if (!shakeOut) {
			if (shakeSpeed <= -shakeMagnitude) {
				initShake();
				return;
			}
			
			shakeSpeed -= SHAKE_ACCELERATION;
		}
		
		//System.out.println(shakeSpeed + ", " + shakeMagnitude);
		
		Vector2 shakeAmt = new Vector2(shakeDir).scl(shakeSpeed);
		shakeEffect.add(shakeAmt);
		
		bounds.x += shakeAmt.x;
		bounds.y += shakeAmt.y;
	}

	private void centerCamera(float dt) {
		if (horMove || vertMove)
			newPos = null;
		
		if (newPos != null) {
			centerSpeed += CENTER_ACCELERATION * dt;

			if (centerSpeed > MAX_CENTER_SPEED)
				centerSpeed = MAX_CENTER_SPEED;

			Vector2 moveAmount = new Vector2(newPos).sub(bounds.x + bounds.width / 2, bounds.y + bounds.height / 2);
			float length = moveAmount.len();
			moveAmount.nor().scl(Math.min(length, dt * centerSpeed));
			
			if (length <= dt * centerSpeed) 
				newPos = null;

			bounds.x += moveAmount.x;
			bounds.y += moveAmount.y;
		} else {
			centerSpeed = 0;
		}
	}

	private void moveCameraBasedOnInput(float dt) {
		if (!movementEnabled)
			return;
		
		float speed = cameraSpeed * dt;

		if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
			bounds.y += speed;
			vertMove = true;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
			bounds.x -= speed;
			horMove = true;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
			bounds.y -= speed;
			vertMove = !vertMove;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
			bounds.x += speed;
			horMove = !horMove;
		}
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
		if (new Vector2(oldPos).sub(bounds.x, bounds.y).len() > MIN_MOVE) {
			orthoCamera.translate(bounds.x - oldPos.x, bounds.y - oldPos.y);
			orthoCamera.update();
			oldPos.set(bounds.x, bounds.y);
		} else {
			newPos = null;
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
	
	public void shake(float minMagnitude, float maxMagnitude, int numShakes) {
		if (shakeCount > 0)
			return;
		
		shakeCount = numShakes;
		
		this.minMagnitude = minMagnitude;
		this.maxMagnitude = maxMagnitude;
		
		initShake();
	}
	
	private void initShake() {
		shakeCount--;
		
		bounds.x -= shakeEffect.x;
		bounds.y -= shakeEffect.y;
		
		shakeOut = true;
		shakeSpeed = 0;
		
		shakeMagnitude = minMagnitude + random.nextFloat() * (maxMagnitude - minMagnitude);
		
		shakeDir.set(random.nextFloat() - 0.5f, random.nextFloat() - 0.5f + VERTICAL_SHAKE_BIAS).nor();
		shakeEffect.set(0, 0);
	}
}
