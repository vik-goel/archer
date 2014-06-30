package game.entity;

import game.entity.friendly.Arrow;
import game.entity.friendly.EnergyBall;
import game.entity.friendly.Sword;
import game.entity.friendly.ThrowingStar;

import com.badlogic.gdx.math.Vector2;

public class AttackFactory {

	public static Entity getProjectile(AttackType type, Vector2 pos, Vector2 target, float range) {
		switch (type) {
		case ARROW:
			return new Arrow(pos, target, range);
		case THROWING_STAR:
			return new ThrowingStar(pos, target, range);
		case ENERGY_BALL:
			return new EnergyBall(pos, target, range);
		case SWORD:
			return new Sword(pos, target, range);
		}
		
		return null;
	}
	
}
