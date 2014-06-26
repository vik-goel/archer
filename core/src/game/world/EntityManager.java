package game.world;

import game.entity.Camera;
import game.entity.Entity;

import java.awt.Graphics;
import java.util.ArrayList;

import com.badlogic.gdx.math.Rectangle;

public class EntityManager {

	private ArrayList<Entity> entities;

	public EntityManager() {
		entities = new ArrayList<Entity>();
	}

	public void update(Camera camera) {
		for (int i = 0; i < entities.size(); i++) {

			if (entities.get(i).isRemoved()) {
				entities.remove(i--);
				continue;
			}

			entities.get(i).update(camera);
		}
	}

	public void render(Graphics g, Camera camera) {
		for (int i = 0; i < entities.size(); i++)
			entities.get(i).render(g, camera);
	}

	public void addEntity(Entity e) {
		entities.add(e);
	}

	public ArrayList<Entity> getEntitiesWithinArea(Rectangle area) {
		ArrayList<Entity> result = new ArrayList<Entity>();
		
		for (int i = 0; i < entities.size(); i++)
			if (entities.get(i).getBounds().overlaps(area))
				result.add(entities.get(i));
		
		return result;
	}

}
