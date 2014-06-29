package game.entity.component;

import game.entity.Camera;
import game.entity.Entity;
import game.entity.AttackFactory;
import game.entity.AttackType;
import game.entity.Skeleton;
import game.world.PhaseManager;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

public class ClickAttack extends Component {

	private static final ShapeRenderer SHAPE_RENDERER = new ShapeRenderer();

	private Color attackColor;

	private boolean setAttack = false;
	private boolean selectingAngle = false;
	private boolean attacking = false;
	private float radius;
	private float attackAngle = 0;
	private float fov;

	private AttackType type;
	private float attackDelay, attackDelayCounter = 0;
	
	private Clickable clickable;

	public ClickAttack(float radius, float fov, Color attackColor, AttackType type, float attackDelay) {
		this.radius = radius;
		this.attackColor = attackColor;
		this.fov = fov;
		this.type = type;
		this.attackDelay = attackDelay;
	}

	public void init(Camera camera) {
		super.init(camera);

		clickable = parent.getComponent(Clickable.class);
	}

	public void update(Camera camera, float dt) {
		super.update(camera, dt);

		attackDelayCounter += dt;
		
		if (PhaseManager.isEnemyPhase())
			attack();
		
		if (PhaseManager.isPlayerPhase())
			setAttack(camera);
	}
	
	private void attack() {
		if (!canAttack())
			return;
		
		Entity target = getTarget();
		
		if (target != null) {
			Vector2 pos = new Vector2(parent.getBounds().x + parent.getBounds().width / 2, parent.getBounds().y + parent.getBounds().height / 2);
			Vector2 targetPos = new Vector2(target.getBounds().x, target.getBounds().y);
			
			Entity attackEntity = AttackFactory.getProjectile(type, pos, targetPos, radius);
			
			if (attackEntity != null) {
				parent.getManager().addEntity(attackEntity);
				attackDelayCounter = 0;
			}
		}
	}

	private boolean canAttack() {
		return attackDelayCounter >= attackDelay;
	}

	private Entity getTarget() {
		Vector2 parentCenter = new Vector2();
		parent.getBounds().getCenter(parentCenter);
		
		ArrayList<Entity> inRange = getParent().getManager().getEntitiesWithinArc(parentCenter, radius, attackAngle, fov / 2);
		
		Entity target = null;
		double distanceToTarget = Double.MAX_VALUE;
		
		Vector2 parentPos = new Vector2(parent.getBounds().x, parent.getBounds().y);
		
		for (int i = 0; i < inRange.size(); i++) {
			if (!(inRange.get(i) instanceof Skeleton))
				continue;
			
			double distance = new Vector2(inRange.get(i).getBounds().x, inRange.get(i).getBounds().y).dst2(parentPos);
			
			if (distance < distanceToTarget) {
				distanceToTarget = distance;
				target = inRange.get(i);
			}
		}
		
		return target;
	}

	public void setAttack(Camera camera) {
		if (clickable.isClicked()) {
			attacking = false;
			setAttack = false;
		}
		
		if (!clickable.isSelected()) {
			if (!setAttack)
				selectingAngle = attacking = false;
			
			return;
		}

		if (!Gdx.input.isButtonPressed(0) && Gdx.input.isButtonPressed(1) && Gdx.input.justTouched()) {
			if (attacking && selectingAngle) {
				selectingAngle = false;
				setAttack = true;
				clickable.deselect();
			} else {
				attacking = true;
				selectingAngle = true;
			}
		}
		
		if (selectingAngle) { 
			Vector2 parentCenter = new Vector2();
			parent.getBounds().getCenter(parentCenter);
			
			Vector2 mouseToParent = camera.getMousePosInWorld().sub(parentCenter);
			attackAngle = (float) Math.toDegrees(Math.atan2(mouseToParent.y, mouseToParent.x));
		}
	}

	public void renderUnlit(Camera camera, SpriteBatch batch) {
		super.renderUnlit(camera, batch);

		if (!attacking)
			return;
		
		Gdx.gl.glEnable(GL20.GL_BLEND);
	    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

	    Vector2 parentCenter = new Vector2();
	    parent.getBounds().getCenter(parentCenter);
	    parentCenter = camera.toScreenPos(parentCenter);
	    
		SHAPE_RENDERER.begin(ShapeType.Filled);
		SHAPE_RENDERER.setColor(attackColor);
		SHAPE_RENDERER.arc(parentCenter.x, parentCenter.y, radius, attackAngle - fov / 2, fov, 25);
		SHAPE_RENDERER.end();
		
		Gdx.gl.glDisable(GL20.GL_BLEND);
	}
	
	public boolean isAttacking() {
		return attacking;
	}
	
	public float getAttackAngle() {
		return attackAngle;
	}
	
	public boolean hasSetAttack() {
		return setAttack;
	}
	
	public void reset() {
		setAttack = attacking = selectingAngle = false;
	}
}
