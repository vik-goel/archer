package game.entity.component;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.entity.Camera;
import game.entity.Entity;

public abstract class Component {

	protected Entity parent;
	private boolean initialized = false;

	public Entity getParent() {
		return parent;
	}
	
	public void setParent(Entity parent) {
		this.parent = parent;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void init(Camera camera) {
		initialized = true;
	}

	public void update(Camera camera, float dt) {
	}
	
	public void renderEarly(Camera camera, SpriteBatch batch) {
	}

	public void renderLit(Camera camera, SpriteBatch batch) {
	}
	
	public void renderUnlit(Camera camera, SpriteBatch batch) {
	}

}
