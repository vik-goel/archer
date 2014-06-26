package game.entity.component;

import game.entity.Camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class ClickMove extends Component {

	private Clickable clickable;
	private boolean movingEnabled = false;
	private float moveSpeed;
	
	public ClickMove(float moveSpeed) {
		this.moveSpeed = moveSpeed;
	}
	
	public void init(Camera camera) {
		super.init(camera);
		
		clickable = parent.getComponent(Clickable.class);
		
		if (clickable == null) {
			clickable = new Clickable();
			parent.addComponent(clickable);
		}
	}

	public void update(Camera camera) {
		super.update(camera);
		
		if (!Gdx.input.isButtonPressed(0)) movingEnabled = false;
		if (clickable.wasSelected()) movingEnabled = true;
	
		if (movingEnabled && !clickable.isSelected()) {
			Vector2 parentCenter = new Vector2();
			parent.getBounds().getCenter(parentCenter);
			
			Vector2 moveAmount = camera.getMousePosInWorld().sub(parentCenter);
			moveAmount.nor().scl(moveSpeed);
			
			parent.getBounds().setPosition(parent.getBounds().x + moveAmount.x, parent.getBounds().y + moveAmount.y);
		}
	}

	
}
