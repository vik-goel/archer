package game.entity.component;

import game.entity.Camera;
import game.entity.movement.Collision;

import com.badlogic.gdx.math.Vector2;

public class Knockback extends Component {

	private static final float DECCELERATION = 0.5f;

	private Vector2 dir;
	private float speed;

	public Knockback(Vector2 dir, float initialSpeed) {
		this.dir = dir.nor();
		speed = initialSpeed;
	}

	public void update(Camera camera, float dt) {
		super.update(camera, dt);

		if (speed <= 0) {
			parent.removeComponent(this);
			return;
		}

		for (int i = 0; i < speed * dt; i++) {
			Vector2 moveAmount = new Vector2(dir);
			Vector2 collision = Collision.collision(parent, moveAmount);
			
			if (collision.x != 1 || collision.y != 1)
				speed = 0;
			
			moveAmount.scl(collision);
			
			parent.getBounds().x += moveAmount.x;
			parent.getBounds().y += moveAmount.y;
			
			if (speed == 0)
				break;
		}

		speed -= DECCELERATION * dt;
	}

}
