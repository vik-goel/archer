package game.entity.component;

import game.entity.Camera;
import game.entity.movement.Collision;

import com.badlogic.gdx.math.Vector2;


public class Projectile extends Component {

	private Vector2 moveAmount;
	private Vector2 oldPos;
	private Vector2 target;
	private float range, speed;
	
	public Projectile(Vector2 target, float speed, float range) {
		this.target = target;
		this.speed = speed;
		this.range = range;
	}
	
	public void init(Camera camera) {
		super.init(camera);
		
		oldPos = new Vector2(parent.getBounds().x, parent.getBounds().y);
		
		moveAmount = new Vector2(target).sub(oldPos);
		moveAmount.nor().scl(speed);
		
		Render render = parent.getComponent(Render.class);
		
		if (render != null) {
			double dir = Math.atan2(moveAmount.y, moveAmount.x);
			render.setRotation((float)Math.toDegrees(dir));
		}
	}

	public void update(Camera camera) {
		super.update(camera);
		
		move();
		range();
	}

	private void move() {
		Vector2 collision = Collision.collision(parent, moveAmount);
		
		if (collision.x != 1 || collision.y != 1) {
			parent.remove();
			return;
		}
		
		parent.getBounds().x += moveAmount.x;
		parent.getBounds().y += moveAmount.y;
	}

	private void range() {
		Vector2 currentPos =  new Vector2(parent.getBounds().x, parent.getBounds().y);
		
		if (currentPos.dst(oldPos) >= range)
			parent.remove();
	}

}
