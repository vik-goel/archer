package game.world;

import game.entity.Camera;
import game.entity.Entity;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

public class EntityManager implements Disposable {

	private ArrayList<Entity> entities;
	private Map map;

	private SpriteBatch batch;

	public EntityManager(Map map) {
		this.map = map;

		entities = new ArrayList<Entity>();
		batch = new SpriteBatch();
	}

	public void update(Camera camera, float dt) {
		for (int i = 0; i < entities.size(); i++) {

			if (entities.get(i).isRemoved()) {
				entities.remove(i--);
				continue;
			}

			entities.get(i).update(camera, dt);
		}
	}

	public void renderLit(Camera camera) {
		renderEarly(camera);

		camera.projectBatch(batch);

		for (int i = 0; i < entities.size(); i++)
			entities.get(i).renderLit(camera, batch);
	}

	private void renderEarly(Camera camera) {
		camera.projectBatch(batch);

		for (int i = 0; i < entities.size(); i++)
			entities.get(i).renderEarly(camera, batch);
	}

	public void renderUnlit(Camera camera) {
		camera.projectBatch(batch);

		for (int i = 0; i < entities.size(); i++)
			entities.get(i).renderUnlit(camera, batch);
		
		renderLate(camera);
	}

	private void renderLate(Camera camera) {
		camera.projectBatch(batch);

		for (int i = 0; i < entities.size(); i++)
			entities.get(i).renderLate(camera, batch);
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

	public ArrayList<Entity> getEntitiesWithinArc(Vector2 pos, float radius, float angle, float angleSpread) {
		ArrayList<Entity> result = new ArrayList<Entity>();

		for (int i = 0; i < entities.size(); i++) {
			Vector2 ePos = new Vector2();
			entities.get(i).getBounds().getCenter(ePos);

			if (ePos.dst(pos) > radius)
				continue;

			Vector2 direction = ePos.sub(pos);

			double angleToEntity = Math.toDegrees(Math.atan2(direction.y, direction.x));

			if (angleToEntity >= angle - angleSpread && angleToEntity <= angle + angleSpread)
				result.add(entities.get(i));
		}

		return result;
	}

	public void dispose() {
		for (int i = 0; i < entities.size(); i++)
			entities.get(i).dispose();
	}

}
