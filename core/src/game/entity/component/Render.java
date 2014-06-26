package game.entity.component;

import game.entity.Camera;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Render extends Component {

	private Sprite sprite;
	
	public Render(Sprite sprite) {
		this.sprite = sprite;
	}

	public void update(Camera camera) {
		super.update(camera);
		sprite.setPosition(parent.getBounds().x, parent.getBounds().y);
		sprite.setSize(parent.getBounds().width, parent.getBounds().height);
	}

	public void render(Camera camera, SpriteBatch batch) {
		super.render(camera, batch);
		
		batch.begin();
		sprite.draw(batch);
		batch.end();
	}
	
	
	
}
