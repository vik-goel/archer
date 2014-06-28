package game.entity.component;

import game.entity.Camera;
import game.entity.Skeleton;
import game.entity.SkeletonGroup;
import game.world.PhaseManager;
import game.world.Squad;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;

public class MobSpawner extends Component {

	private static final Random random = new Random();
	private static final float SPAWN_VARIANCE = 30;
	
	private Squad squad;
	
	boolean pastEnemyPhase = false;
	
	public MobSpawner(Squad squad) {
		this.squad = squad;
	}
	
	public void update(Camera camera) {
		super.update(camera);
		
		if (!PhaseManager.isEnemyPhase())
			pastEnemyPhase = false;
		
		if (PhaseManager.isEnemyPhase() && !pastEnemyPhase) {
			pastEnemyPhase = true;
			spawnSkeletons();
		}
	}

	private void spawnSkeletons() {
		SkeletonGroup group = new SkeletonGroup(new Vector2(parent.getBounds().x, parent.getBounds().y), squad);
		parent.getManager().addEntity(group);
		
		for (int i = 0; i < 2; i++) {
			float x = parent.getBounds().x + random.nextFloat() * SPAWN_VARIANCE - SPAWN_VARIANCE / 2;
			float y = parent.getBounds().y + random.nextFloat() * SPAWN_VARIANCE - SPAWN_VARIANCE / 2;
			
			Skeleton skeleton = new Skeleton(new Vector2(x, y), squad);
			
			group.addSkeleton(skeleton);
			parent.getManager().addEntity(skeleton);
		}
	}
	
}
