package game.entity.component;

import game.entity.Camera;
import game.world.PhaseManager;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

public class SkeletonPathFollower extends Component {

	private ArrayList<Vector2> path;
	private int pathIndex;
	private float moveSpeed;

	public SkeletonPathFollower(float moveSpeed) {
		this.moveSpeed = moveSpeed;
	}

	public void setPath(ArrayList<Vector2> path) {
		if (this.path == path)
			return;
		
		this.path = path;
		pathIndex = 0;
	}

	public void update(Camera camera) {
		if (path == null || !PhaseManager.isEnemyPhase())
			return;

		if (pathIndex >= path.size()) {
			return;
		}

		Vector2 destination = path.get(pathIndex);
		Vector2 currentPos = new Vector2(parent.getBounds().x, parent.getBounds().y);

		Vector2 movement = new Vector2(destination).sub(currentPos);
		float length = movement.len();
		movement.nor().scl(Math.min(moveSpeed, length));

		if (length < moveSpeed)
			pathIndex++;

		parent.getBounds().x += movement.x;
		parent.getBounds().y += movement.y;
	}

}
