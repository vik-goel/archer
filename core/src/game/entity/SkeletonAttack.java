package game.entity;

import game.entity.component.Component;
import game.entity.component.Health;
import game.world.Squad;

import com.badlogic.gdx.math.Vector2;

public class SkeletonAttack extends Component {

	private Squad squad;
	
	private float attackRadius;
	private float damage;
	private float attackDelay, attackDelayCounter = 0;
	
	public SkeletonAttack(Squad squad, float attackRadius, float damage, int attackDelay) {
		this.squad = squad;
		this.attackRadius = attackRadius;
		this.damage = damage;
		this.attackDelay = attackDelay;
	}

	public void update(Camera camera, float dt) {
		super.update(camera, dt);
		
		attackDelayCounter += dt;
		
		if (canAttack())
			attack();
	}

	private boolean canAttack() {
		return attackDelayCounter >= attackDelay;
	}

	private void attack() {
		Vector2 parentCenter = new Vector2();
		parent.getBounds().getCenter(parentCenter);
		
		Vector2 eCenter = new Vector2();
		
		for (Entity e : squad.getEntities()) {
			e.getBounds().getCenter(eCenter);
			
			if (parentCenter.dst(eCenter) <= attackRadius) {
				Health health = e.getComponent(Health.class);
				
				if (health == null)
					continue;
				
				health.damage(damage);
				attackDelayCounter = 0;
				return;
			}
		}
	}

}
