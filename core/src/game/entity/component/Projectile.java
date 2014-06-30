package game.entity.component;

import game.entity.Blood;
import game.entity.BloodParticle;
import game.entity.Camera;
import game.entity.Entity;
import game.entity.enemy.Skeleton;
import game.entity.movement.Collision;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;

public class Projectile extends Component {

	private static final Random random = new Random();
	private static final int MIN_BLOOD_PARTICLES = 3;
	private static final int MAX_BLOOD_PARTICLES = 15;

	private Vector2 moveAmount;
	private Vector2 oldPos;
	private Vector2 target;

	private Sound collisionSound;
	private boolean friendlyProjectile;
	private float range, speed, damage;
	private int knockback;

	public Projectile(Sound collisionSound, Vector2 target, boolean friendlyProjectile, float speed, float range, float damage, int knockback) {
		this.collisionSound = collisionSound;
		this.target = target;
		this.friendlyProjectile = friendlyProjectile;
		this.speed = speed;
		this.range = range;
		this.damage = damage;
		this.knockback = knockback;
	}

	public void init(Camera camera) {
		super.init(camera);

		oldPos = new Vector2(parent.getBounds().x, parent.getBounds().y);

		moveAmount = new Vector2(target).sub(oldPos);
		moveAmount.nor().scl(speed);

		Render render = parent.getComponent(Render.class);

		if (render != null) {
			double dir = Math.atan2(moveAmount.y, moveAmount.x);
			render.setRotation((float) Math.toDegrees(dir));
		}
	}

	public void update(Camera camera, float dt) {
		super.update(camera, dt);

		move(dt);
		range();
		collisions(camera);
	}

	private void move(float dt) {
		Vector2 amt = new Vector2(moveAmount).scl(dt);
		Vector2 collision = Collision.collision(parent, amt);

		if (collision.x != 1 || collision.y != 1) {
			parent.remove();
			return;
		}

		parent.getBounds().x += amt.x;
		parent.getBounds().y += amt.y;
	}

	private void range() {
		Vector2 currentPos = new Vector2(parent.getBounds().x, parent.getBounds().y);

		if (currentPos.dst(oldPos) >= range)
			parent.remove();
	}

	private void collisions(Camera camera) {
		ArrayList<Entity> collisions = parent.getManager().getEntitiesWithinArea(parent.getBounds());

		for (int i = 0; i < collisions.size(); i++) {
			if (friendlyProjectile) {
				if (friendlyCollide(collisions.get(i), camera)) {
					parent.remove();
					break;
				}
			} else {
				if (enemyCollide(collisions.get(i))) {
					parent.remove();
					break;
				}
			}
		}
	}

	private boolean friendlyCollide(Entity e, Camera camera) {
		if (e instanceof Skeleton) {
			collision(e, camera);
			return true;
		}

		return false;
	}

	private boolean enemyCollide(Entity e) {
		return false;
	}

	private void collision(Entity e, Camera camera) {
		Health health = e.getComponent(Health.class);

		if (health != null)
			health.damage(damage);

		moveAmount.nor();

		e.addComponent(new Knockback(moveAmount, knockback));

		Vector2 pos = new Vector2();
		e.getBounds().getCenter(pos);

		if (e.isRemoved()) {
			parent.getManager().addEntity(new Blood(pos));

			if (random.nextDouble() < 0.3)
				camera.shake(2f, 4f, 2);
		}

		int numParticles = random.nextInt(MAX_BLOOD_PARTICLES - MIN_BLOOD_PARTICLES) + MIN_BLOOD_PARTICLES;

		for (int i = 0; i < numParticles; i++)
			parent.getManager().addEntity(new BloodParticle(pos, moveAmount));

		collisionSound.play();
	}

}
