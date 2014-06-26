package game.entity;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Camera extends Entity {

	private OrthographicCamera orthoCamera;
	private Vector2 oldPos;
	
	public Camera(Vector2 size) {
		super(new Rectangle(0, 0, size.x, size.y));
		oldPos = new Vector2(size.x / 2, size.y / 2);
		orthoCamera = new OrthographicCamera(bounds.width, bounds.height);
	}
	
	public void update(Camera camera) {
		super.update(camera);
		
		if (oldPos.x != bounds.x || oldPos.y != bounds.y) {
			orthoCamera.translate(oldPos.sub(bounds.x, bounds.y));
			orthoCamera.update();
			oldPos.set(bounds.x, bounds.y);
		}
	}
	
	public void projectBatch(SpriteBatch batch) {
		batch.setProjectionMatrix(orthoCamera.combined);
	}
}
