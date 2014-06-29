package game.entity.component;

import game.entity.Camera;
import game.entity.movement.Collision;

import com.badlogic.gdx.math.Vector2;

public class Knockback extends Component {

	private static final float ACCELERATION = 1.5f;

	private Vector2 dir;
	private float amt;
	private float speed = 0;

	public Knockback(Vector2 dir, float amt) {
		this.dir = dir;
		this.amt = amt;
	}

	public void update(Camera camera, float dt) {
		super.update(camera, dt);

		if (amt <= 0) {
			parent.removeComponent(this);
			return;
		}
		
		speed += ACCELERATION;

		Vector2 moveAmount = new Vector2(dir).scl(speed);
		moveAmount.scl(Collision.collision(parent, moveAmount));

		parent.getBounds().x += moveAmount.x;
		parent.getBounds().y += moveAmount.y;

		amt -= speed;
	}

}
