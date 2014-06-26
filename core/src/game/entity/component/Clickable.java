package game.entity.component;

import game.entity.Camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

public class Clickable extends Component {

	private static final ShapeRenderer SHAPE_RENDERER = new ShapeRenderer();
	
	private Color selectedColor;
	private boolean drawSelectedCircle = false;
	private float xOffs, yOffs, circleWidth, circleHeight;
	
	private boolean wasSelected = false;
	private boolean selected = false;
	private boolean clicked = false;
	
	public Clickable() {
	}
	
	public Clickable(Color selectedColor, float xOffs, float yOffs, float circleWidth, float circleHeight) {
		this.drawSelectedCircle = true;
		this.selectedColor = selectedColor;
		this.xOffs = xOffs;
		this.yOffs = yOffs;
		this.circleWidth = circleWidth;
		this.circleHeight = circleHeight;
	}

	public void update(Camera camera) {
		super.update(camera);

		wasSelected = false;
		clicked = false;
		
		if (!(Gdx.input.isButtonPressed(0) && Gdx.input.justTouched()))
			return;
		
		boolean wasSelected = selected;
		selected = false;

		Vector2 mousePos = camera.getMousePosInWorld();
		
		if (parent.getBounds().contains(mousePos)) {
			selected = true;
			clicked = !wasSelected;
		} else {
			this.wasSelected = wasSelected;
		}
	}
	
	public void render(Camera camera, SpriteBatch batch) {
		super.render(camera, batch);
		
		if (!drawSelectedCircle || !selected)
			return;
		
		Vector2 parentCenter = new Vector2();
		parent.getBounds().getCenter(parentCenter);
		camera.toScreenPos(parentCenter);
		
		Gdx.gl20.glLineWidth(1);
		
		SHAPE_RENDERER.begin(ShapeType.Line);
		SHAPE_RENDERER.setColor(selectedColor);
		SHAPE_RENDERER.ellipse(parentCenter.x + xOffs, parentCenter.y + yOffs, circleWidth, circleHeight);
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
	
	public void deselect() {
		if (selected)
			wasSelected = true;
		
		selected = clicked = false;
	}

}
