package game.entity.component;

import game.entity.Camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class Clickable extends Component {

	private boolean wasSelected = false;
	private boolean selected = false;
	private boolean clicked = false;

	public void update(Camera camera) {
		super.update(camera);

		wasSelected = false;
		clicked = false;
		
		if (!Gdx.input.isButtonPressed(0))
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

	public boolean wasSelected() {
		return wasSelected;
	}
	
	public boolean isSelected() {
		return selected;
	}

	public boolean isClicked() {
		return clicked;
	}

}
