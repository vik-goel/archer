package game.entity.component;

import game.entity.Camera;
import game.world.PhaseManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

public class Clickable extends Component {

	private static final ShapeRenderer SHAPE_RENDERER = new ShapeRenderer();
	private static final Color SELECTED_COLOR = new Color(0f, 1f, 0f, 1f);
	private static boolean enabled = true;
	
	private boolean drawSelectedCircle = false;
	private float xOffs, yOffs, circleWidth, circleHeight;
	
	private boolean wasSelected = false;
	private boolean selected = false;
	private boolean clicked = false, wasClicked = false;
	
	public Clickable() {
	}
	
	public Clickable(float xOffs, float yOffs, float circleWidth, float circleHeight) {
		this.drawSelectedCircle = true;
		this.xOffs = xOffs;
		this.yOffs = yOffs;
		this.circleWidth = circleWidth;
		this.circleHeight = circleHeight;
	}

	public void update(Camera camera, float dt) {
		super.update(camera, dt);

		if (!PhaseManager.isPlayerPhase())
			return;
		
		if (!enabled) {
			wasSelected = selected = clicked = wasClicked = false;
			return;
		}
		
		wasSelected = false;
		wasClicked = clicked;
		clicked = false;
		
		if (!(Gdx.input.isButtonPressed(0) && Gdx.input.justTouched()))
			return;
		
		boolean wasSelected = selected;
		selected = false;

		Vector2 mousePos = camera.getMousePosInWorld();
		
		if (parent.getBounds().contains(mousePos)) {
			selected = true;
			clicked = true;
		} else {
			this.wasSelected = wasSelected;
		}
	}
	
	public void renderLit(Camera camera, SpriteBatch batch) {
		super.renderLit(camera, batch);
		
		if (!drawSelectedCircle || !selected)
			return;
		
		Vector2 parentCenter = new Vector2();
		parent.getBounds().getCenter(parentCenter);
		camera.toScreenPos(parentCenter);
		
		Gdx.gl20.glLineWidth(1);
		
		SHAPE_RENDERER.begin(ShapeType.Line);
		SHAPE_RENDERER.setColor(SELECTED_COLOR);
		SHAPE_RENDERER.ellipse(parentCenter.x + xOffs - camera.getBounds().x, parentCenter.y + yOffs - camera.getBounds().y, circleWidth, circleHeight);
		SHAPE_RENDERER.end();
	}

	public boolean wasSelected() {
		return wasSelected;
	}
	
	public boolean isSelected() {
		return selected;
	}

	public boolean isClicked() {
		return clicked;
	}
	
	public boolean wasClicked() {
		return wasClicked;
	}
	
	public void deselect() {
		if (selected)
			wasSelected = true;
		
		if (clicked)
			wasClicked = true;
		
		selected = clicked = false;
	}
	
	public void select() {
		selected = clicked = true;
		wasSelected = false;
		wasClicked = false;
	}
	
	public static void setEnabled(boolean enabled) {
		Clickable.enabled = enabled;
	}

}
