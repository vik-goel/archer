package game.entity.component;

import game.entity.Camera;
import game.entity.enemy.Spawner;
import game.entity.gui.Minimap;
import game.world.Map;
import game.world.SpawnerManager;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SightRange extends Component {

	private static int[][] seen;
	
	private PointLight light;
	private int discoverRange;
	private int pastTileX, pastTileY;
	
	public SightRange(RayHandler handler, float sightRange, int discoverRange) {
		this.discoverRange = discoverRange;
		
		light = new PointLight(handler, 200, Color.WHITE, sightRange, 0, 0);
		light.setColor(0, 0, 0, 1);
		pastTileX = pastTileY = -1;
	}

	
	public void init(Camera camera) {
		super.init(camera);
		
		if (seen == null) {
			seen = new int[parent.getMap().getWidth()][parent.getMap().getHeight()];
			
			for (int i = 0; i < seen.length; i++)
				for (int j = 0; j < seen[0].length; j++)
					seen[i][j] = -1;
		}
	}

	public void update(Camera camera, float dt) {
		super.update(camera, dt);
		
		light.setPosition(parent.getBounds().x + parent.getBounds().width / 2, parent.getBounds().y + parent.getBounds().height / 2);
		
		int tileX = (int) Math.floor(parent.getBounds().x / Map.TILE_SIZE);
		int tileY = (int) Math.floor(parent.getBounds().y / Map.TILE_SIZE);
		
		if (parent.getMap().isSolid(tileX, tileY)) {
			tileX = (int) Math.ceil(parent.getBounds().x / Map.TILE_SIZE);
			tileY = (int) Math.ceil(parent.getBounds().y / Map.TILE_SIZE);
		}
		
		if (tileX != pastTileX || tileY != pastTileY) {
			pastTileX = tileX;
			pastTileY = tileY;
			
			discover(tileX, tileY, discoverRange);
		}
	}

	private void discover(int x, int y, int range) {
		if (range <= 0)
			return;
		
		if (parent.getMap().outOfBounds(x, y))
			return;
		
		if (seen[x][y] >= range)
			return;
		
		seen[x][y] = range;
		
		TextureRegion region = parent.getMap().getTextureRegion(x, y);
		
		if (region != null)
			Minimap.discoverObject(x, y, region);
		
		if (parent.getMap().isSolid(x, y))
			range = Math.min(2, range);
		
		Spawner spawner = SpawnerManager.spawnerAt(x * Map.TILE_SIZE, y * Map.TILE_SIZE);
		
		if (spawner != null) {
			Render render = spawner.getComponent(Render.class);
			
			if (render != null) 
				Minimap.addObject(render);
		}
		
		discover(x + 1, y, range - 1);
		discover(x - 1, y, range - 1);
		discover(x, y + 1, range - 1);
		discover(x, y - 1, range - 1);
	}
	
	
}
