package game.entity.component;

import game.entity.Camera;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Render extends Component {

	private Sprite sprite;
	private boolean early = false;
	
	public Render(Sprite sprite) {
		this.sprite = sprite;
	}

	public void update(Camera camera, float dt) {
		super.update(camera, dt);
		sprite.setPosition(parent.getBounds().x, parent.getBounds().y);
		sprite.setSize(parent.getBounds().width, parent.getBounds().height);
	}

	public void renderEarly(Camera camera, SpriteBatch batch) {
		super.renderEarly(camera, batch);
		
		if (early)
			render(batch);
	}

	public void renderLit(Camera camera, SpriteBatch batch) {
		super.renderLit(camera, batch);
		
		if (!early)
			render(batch);
	}
	
	private void render(SpriteBatch batch) {
		batch.begin();
		sprite.draw(batch);
		batch.end();
	}
	
	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}
	
	public Sprite getSprite() {
		return sprite;
	}

	public void setRotation(float degrees) {
		sprite.setRotation(degrees);
	}

	public Render setEarly(boolean early) {
		this.early = early;
		
		return this;
	}
	
}
