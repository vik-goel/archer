package game.entity;

import com.badlogic.gdx.math.Vector2;

public class AttackFactory {

	public static Entity getProjectile(AttackType type, Vector2 pos, Vector2 target, float range) {
		switch (type) {
		case ARROW:
			return new Arrow(pos, target, range);
		}
		
		return null;
	}
	
}
