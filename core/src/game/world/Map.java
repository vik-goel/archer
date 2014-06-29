package game.world;

import game.entity.Camera;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Map {

	public static final float TILE_SIZE = 32;

	private TiledMap tiledMap;
	private OrthogonalTiledMapRenderer renderer;
	private TiledMapTileLayer floorLayer;
	private TiledMapTileLayer wallLayer;

	public Map(String tmxPath, World world) {
		tiledMap = new TmxMapLoader().load(tmxPath);
		renderer = new OrthogonalTiledMapRenderer(tiledMap);

		floorLayer = (TiledMapTileLayer) tiledMap.getLayers().get("floor");
		wallLayer = (TiledMapTileLayer) tiledMap.getLayers().get("walls");

		int width = getWidth();
		int height = getHeight();

		PolygonShape wallShape = new PolygonShape();
		wallShape.setAsBox(TILE_SIZE / 2, TILE_SIZE / 2);

		BodyDef wallDef = new BodyDef();

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (!isSolid(x, y))
					continue;

				float xOffs = 0, yOffs = 0;

				if (floorLayer.getCell(x + 1, y) == null)
					xOffs = TILE_SIZE;

				if (floorLayer.getCell(x, y + 1) == null)
					yOffs = TILE_SIZE;

				wallDef.position.set(x * TILE_SIZE + xOffs, y * TILE_SIZE + yOffs);

				Body wallBody = world.createBody(wallDef);
				wallBody.createFixture(wallShape, 0);
			}
		}
	}

	public void render(Camera camera) {
		camera.projectMap(renderer);
		renderer.render();
	}

	public boolean isSolid(int x, int y) {
		return floorLayer.getCell(x, y) == null;
	}

	public int getWidth() {
		return floorLayer.getWidth();
	}

	public int getHeight() {
		return floorLayer.getHeight();
	}

	public boolean outOfBounds(int x, int y) {
		return x < 0 || y < 0 || x >= getWidth() || y >= getHeight();
	}

	public TextureRegion getTextureRegion(int x, int y) {
		Cell cell = floorLayer.getCell(x, y);
		
		if (cell == null)
			cell = wallLayer.getCell(x, y);
		
		if (cell == null)
			return null;
		
		return cell.getTile().getTextureRegion();
	}
	
	public boolean isFloor(int x, int y) {
		return floorLayer.getCell(x, y) != null;
	}
	
	public boolean isWall(int x, int y) {
		return wallLayer.getCell(x, y) != null;
	}

}
