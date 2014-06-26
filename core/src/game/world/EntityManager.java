package game.world;

import game.entity.Camera;
import game.entity.Entity;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class EntityManager {

	private ArrayList<Entity> entities;
	private Map map;
	
	private SpriteBatch batch;
	
	public EntityManager(Map map) {
		this.map = map;
		
		entities = new ArrayList<Entity>();
		batch = new SpriteBatch();
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

	public void render(Camera camera) {
		camera.projectBatch(batch);
		batch.begin();
		
		for (int i = 0; i < entities.size(); i++)
			entities.get(i).render(camera, batch);
		
		batch.end();
	}

	public void addEntity(Entity e) {
		entities.add(e);
		e.setManager(this);
		e.setMap(map);
	}

	public ArrayList<Entity> getEntitiesWithinArea(Rectangle area) {
		ArrayList<Entity> result = new ArrayList<Entity>();
		
		for (int i = 0; i < entities.size(); i++)
			if (entities.get(i).getBounds().overlaps(area))
				result.add(entities.get(i));
		
		return result;
	}

}
