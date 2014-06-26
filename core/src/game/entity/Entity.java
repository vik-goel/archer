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

	public void update(Camera camera) {
		for (int i = 0; i < components.size(); i++) {
			
			if (!components.get(i).isInitialized()) 
				components.get(i).init(camera);
			
			components.get(i).update(camera);
		}
	}

	public void render(Camera camera, SpriteBatch batch) {
		for (int i = 0; i < components.size(); i++) 
			if (components.get(i).isInitialized()) 
				components.get(i).render(camera, batch);
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

	public Component getComponent(Class<? extends Component> component) {
		for (int i = 0; i < components.size(); i++) {
			if (components.get(i).getClass().equals(component)) return components.get(i);
		}
		
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
