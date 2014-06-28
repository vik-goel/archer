package game.entity.component;

import game.entity.Camera;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.graphics.Color;

public class SightRange extends Component {


	private PointLight light;
	
	public SightRange(RayHandler handler, float sightRange) {
		light = new PointLight(handler, 5000, Color.WHITE, sightRange, 0, 0);
		light.setColor(0, 0, 0, 1);
	}

	public void update(Camera camera) {
		super.update(camera);
		light.setPosition(parent.getBounds().x + parent.getBounds().width / 2, parent.getBounds().y + parent.getBounds().height / 2);
	}
	
	
}
