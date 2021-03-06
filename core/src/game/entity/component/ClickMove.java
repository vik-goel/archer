package game.entity.component;

import game.entity.Camera;
import game.entity.movement.Collision;
import game.world.PhaseManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

public class ClickMove extends Component {

	private static final ShapeRenderer SHAPE_RENDERER = new ShapeRenderer();
	private static final Color RADIUS_COLOR = new Color(0f, 0f, 1f, 1f);
	
	public static final int MOVE_SPEED = 6;

	private Clickable clickable;
	private boolean movingEnabled = false;
	private boolean canMove = true;

	private float moveRadius;
	private Vector2 moveRadiusCenter;

	public ClickMove(float moveRadius) {
		this.moveRadius = moveRadius;
		moveRadiusCenter = new Vector2();
	}

	public void init(Camera camera) {
		super.init(camera);

		clickable = parent.getComponent(Clickable.class);
		resetMoveRadiusCenter();
	}

	public void update(Camera camera, float dt) {
		super.update(camera, dt);

		if (!PhaseManager.isPlayerPhase())
			return;

		if (clickable.isClicked()) {
			movingEnabled = true;
			canMove = true;
		}

		if (!Gdx.input.isButtonPressed(0) || !clickable.isSelected())
			movingEnabled = false;
		
		if (Gdx.input.isButtonPressed(1) && Gdx.input.justTouched())
			canMove = !canMove;

		if (movingEnabled && canMove) {
			Vector2 mousePos = camera.getMousePosInWorld();

			Vector2 parentCenter = new Vector2();
			parent.getBounds().getCenter(parentCenter);

			Vector2 moveAmount = mousePos.sub(parentCenter);
			float length = moveAmount.len();
			int speed = (int) Math.min(length, MOVE_SPEED);
			moveAmount.nor();
			
			for (int i = 0; i < speed; i++) {
				Vector2 amt = new Vector2(moveAmount).scl(Collision.collision(parent, moveAmount));
				
				Vector2 newCenterPos = parentCenter.add(amt);

				if (newCenterPos.dst(moveRadiusCenter) <= moveRadius) {
					parent.getBounds().setPosition(parent.getBounds().x + amt.x, parent.getBounds().y + amt.y);
					camera.centerAround(newCenterPos);
				}
			}
		}
	}

	public void renderUnlit(Camera camera, SpriteBatch batch) {
		super.renderUnlit(camera, batch);

		if (!clickable.isSelected())
			return;

		Vector2 circleCenter = camera.toScreenPos(new Vector2(moveRadiusCenter));

		Gdx.gl20.glLineWidth(3);

		SHAPE_RENDERER.begin(ShapeType.Line);
		SHAPE_RENDERER.setColor(RADIUS_COLOR);
		SHAPE_RENDERER.circle(circleCenter.x, circleCenter.y, moveRadius, 85);
		SHAPE_RENDERER.end();
	}

	public void resetMoveRadiusCenter() {
		parent.getBounds().getCenter(moveRadiusCenter);
	}

}
