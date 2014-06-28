package game.entity.component;

import game.entity.Camera;
import game.world.PhaseManager;

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

	private Clickable clickable;

	public ClickAttack(float radius, float fov, Color attackColor) {
		this.radius = radius;
		this.attackColor = attackColor;
		this.fov = fov;
	}

	public void init(Camera camera) {
		super.init(camera);

		clickable = parent.getComponent(Clickable.class);
	}

	public void update(Camera camera) {
		super.update(camera);

		if (!PhaseManager.isPlayerPhase())
			return;
		
		if (clickable.isClicked()) {
			attacking = false;
			setAttack = false;
		}
		
		if (!clickable.isSelected()) {
			if (!setAttack)
				selectingAngle = attacking = false;
			
			return;
		}

		if (Gdx.input.isButtonPressed(1) && Gdx.input.justTouched()) {
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

	public void render(Camera camera, SpriteBatch batch) {
		super.render(camera, batch);

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
