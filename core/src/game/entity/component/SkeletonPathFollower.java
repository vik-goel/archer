package game.entity.component;

import game.entity.Camera;
import game.entity.Entity;
import game.entity.movement.Collision;
import game.world.PhaseManager;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

public class SkeletonPathFollower extends Component {

	private ArrayList<Vector2> path;
	private Entity target;
	
	private int pathIndex;
	private float moveSpeed;

	public SkeletonPathFollower(float moveSpeed) {
		this.moveSpeed = moveSpeed;
	}

	public void setPath(ArrayList<Vector2> path, Entity target) {
		if (this.path == path)
			return;
		
		this.path = path;
		this.target = target;
		pathIndex = 0;
	}

	public void update(Camera camera, float dt) {
		if (!PhaseManager.isEnemyPhase())
			return;

		if (path == null || pathIndex >= path.size()) {
			moveTowardsTarget(dt);
			return;
		}
		
		followPath(dt);
	}

	private void moveTowardsTarget(float dt) {
		if (target == null)
			return;
		
		Vector2 targetCenter = new Vector2();
		target.getBounds().getCenter(targetCenter);
		
		Vector2 movement = targetCenter.sub(parent.getBounds().x, parent.getBounds().y);
		float length = movement.len();
		movement.nor().scl(Math.min(length, moveSpeed * dt));
		movement.scl(Collision.collision(parent, movement));
		
		parent.getBounds().x += movement.x;
		parent.getBounds().y += movement.y;
	}
	
	private void followPath(float dt) {
		Vector2 destination = path.get(pathIndex);
		Vector2 currentPos = new Vector2(parent.getBounds().x, parent.getBounds().y);

		Vector2 movement = new Vector2(destination).sub(currentPos);
		float length = movement.len();
		movement.nor().scl(Math.min(moveSpeed * dt, length));

		if (length < moveSpeed)
			pathIndex++;

		parent.getBounds().x += movement.x;
		parent.getBounds().y += movement.y;
	}

}
