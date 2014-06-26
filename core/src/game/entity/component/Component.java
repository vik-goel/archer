package game.entity.component;

import game.entity.Camera;
import game.entity.Entity;

import java.awt.Graphics;

public abstract class Component {

	private Entity parent;
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

	public void update(Camera camera) {
	}

	public void render(Graphics g, Camera camera) {
	}

}
