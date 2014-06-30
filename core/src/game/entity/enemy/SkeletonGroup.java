package game.entity.enemy;

import game.entity.Camera;
import game.entity.Entity;
import game.entity.component.SkeletonPathFollower;
import game.entity.movement.Pathfinder;
import game.world.PhaseManager;
import game.world.Squad;

import java.util.ArrayList;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class SkeletonGroup extends Entity {

	private Squad squad;
	private Entity target;
	private ArrayList<Entity> group;
	private ArrayList<Vector2> path;
	
	private boolean wasEnemyPhase = false;
	
	public SkeletonGroup(Vector2 spawnerPos, Squad squad) {
		super(new Rectangle(spawnerPos.x, spawnerPos.y, 0, 0));
		this.squad = squad;
		group = new ArrayList<Entity>();
	}

	public void update(Camera camera, float dt) {
		super.update(camera, dt);
		
		if (!PhaseManager.isEnemyPhase()) {
			wasEnemyPhase = false;
			target = null;
		}
		
		if (target != null && target.isRemoved())
			target = null;
		
		if ((!wasEnemyPhase || target == null) && PhaseManager.isEnemyPhase()) {
			wasEnemyPhase = true;
			
			if (!group.isEmpty()) {
				bounds.x = group.get(0).getBounds().x;
				bounds.y = group.get(0).getBounds().y;
			} else {
				remove();
				return;
			}
			
			ArrayList<Vector2> shortestPath = null;
			
			for (int i = 0; i < squad.getEntities().size(); i++) {
				ArrayList<Vector2> path = Pathfinder.aStarSearch(this, squad.getEntities().get(i));
				
				if (path != null && (shortestPath == null || path.size() < shortestPath.size())) {
					shortestPath = path;
					target = squad.getEntities().get(i);
				}
			}
			
			path = shortestPath;
		}
		
		for (Entity e: group) {
			SkeletonPathFollower pathFollower = e.getComponent(SkeletonPathFollower.class);
			
			if (pathFollower == null) 
				continue;
			
			pathFollower.setPath(path, target);
		}
	}
	
	public void addSkeleton(Entity e) {
		group.add(e);
	}

}
