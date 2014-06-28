package game.entity;

import game.entity.component.Component;
import game.world.EntityManager;
import game.world.Map;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class Entity {

	private ArrayList<Component> components;
	private boolean removed = false;
	
	protected Rectangle bounds;
	protected EntityManager manager;
	protected Map map;
	
	public Entity(Rectangle bounds) {
		components = new ArrayList<Component>();
		this.bounds = bounds;
	}

	public void update(Camera camera, float dt) {
		for (int i = 0; i < components.size(); i++) {
			
			if (!components.get(i).isInitialized()) 
				components.get(i).init(camera);
			
			components.get(i).update(camera, dt);
		}
	}

	public void renderLit(Camera camera, SpriteBatch batch) {
		for (int i = 0; i < components.size(); i++) 
			if (components.get(i).isInitialized()) 
				components.get(i).renderLit(camera, batch);
	}
	
	public void renderUnlit(Camera camera, SpriteBatch batch) {
		for (int i = 0; i < components.size(); i++) 
			if (components.get(i).isInitialized()) 
				components.get(i).renderUnlit(camera, batch);
	}

	public void addComponent(Component component) {
		components.add(component);
		component.setParent(this);
	}

	public boolean isRemoved() {
		return removed;
	}

	public void remove() {
		removed = true;
	}
	
	public Rectangle getBounds() {
		return bounds;
	}
	
	public Rectangle getCollisionBounds() {
		return bounds;
	}

	@SuppressWarnings("unchecked")
	public <T> T getComponent(Class<T> component) {
		for (int i = 0; i < components.size(); i++) 
			if (components.get(i).getClass().equals(component)) 
				return (T) components.get(i);
		
		return null;
	}

	public EntityManager getManager() {
		return manager;
	}

	public void setManager(EntityManager manager) {
		this.manager = manager;
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

}
