package game.entity.component;

import game.entity.Camera;
import game.entity.enemy.Skeleton;
import game.entity.enemy.SkeletonGroup;
import game.world.PhaseManager;
import game.world.SpawnerManager;
import game.world.Squad;

import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class MobSpawner extends Component {

	private static final Random random = new Random();
	private static final float SPAWN_VARIANCE = 30;

	private static final Sprite ACTIVE = new Sprite(new Texture("spawner_active.png"));
	private static final Sprite INACTIVE = new Sprite(new Texture("spawner_inactive.png"));

	private static final BitmapFont FONT = new BitmapFont();
	
	private Squad squad;

	private boolean pastEnemyPhase = false;
	private int numEntities = 0;
	private SkeletonGroup group;
	private float spawnDelayCounter = 0;
	private int spawnDelay = 0;

	public MobSpawner(Squad squad) {
		this.squad = squad;
		SpawnerManager.addSpawner(this);
	}

	public void update(Camera camera, float dt) {
		super.update(camera, dt);

		spawnDelayCounter += dt;
		
		if (!PhaseManager.isEnemyPhase()) {
			pastEnemyPhase = false;
			group = null;
			return;
		}

		if (PhaseManager.isEnemyPhase() && !pastEnemyPhase) {
			pastEnemyPhase = true;
			createGroup();
		}

		spawnSkeletons();
	}

	private void createGroup() {
		if (numEntities == 0)
			return;

		group = new SkeletonGroup(new Vector2(parent.getBounds().x, parent.getBounds().y), squad);
		parent.getManager().addEntity(group);
	}

	private void spawnSkeletons() {
		if (numEntities <= 0 || group == null)
			return;

		if (spawnDelayCounter < spawnDelay)
			return;
		
		float x = parent.getBounds().x + random.nextFloat() * SPAWN_VARIANCE - SPAWN_VARIANCE / 2;
		float y = parent.getBounds().y + random.nextFloat() * SPAWN_VARIANCE - SPAWN_VARIANCE / 2;

		Skeleton skeleton = new Skeleton(new Vector2(x, y), squad);

		group.addSkeleton(skeleton);
		parent.getManager().addEntity(skeleton);
		
		numEntities--;
		spawnDelayCounter = 0;
		spawnDelay = random.nextInt(10);
	}

	public void renderLit(Camera camera, SpriteBatch batch) {
		super.renderLit(camera, batch);
		
		if (numEntities <= 0)
			return;
		
		float x = parent.getBounds().x + parent.getBounds().width - 5;
		float y = parent.getBounds().y + 5;
		
		batch.begin();
		
		FONT.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		FONT.draw(batch, "" + numEntities, x, y);
		
		batch.end();
	}

	public void deactivate() {
		numEntities = 0;

		Render render = parent.getComponent(Render.class);

		if (render != null)
			render.setSprite(INACTIVE);
	}

	public void activate(int numEntities) {
		this.numEntities = numEntities;

		Render render = parent.getComponent(Render.class);

		if (render != null)
			render.setSprite(ACTIVE);
	}

}
