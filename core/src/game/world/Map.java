package game.world;

import game.entity.Camera;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class Map {
	
	public static final float TILE_SIZE = 32;

	private TiledMap tiledMap;
	private OrthogonalTiledMapRenderer renderer;
	private TiledMapTileLayer floorLayer;
	
	public Map(String tmxPath) {
		tiledMap = new TmxMapLoader().load(tmxPath);
		renderer = new OrthogonalTiledMapRenderer(tiledMap);
		
		floorLayer = (TiledMapTileLayer) tiledMap.getLayers().get("floor");
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
	
	public int getHeight()  {
		return floorLayer.getHeight();
	}

	public boolean outOfBounds(int x, int y) {
		return x < 0 || y < 0 || x >= getWidth() || y >= getHeight();
	}
	
}
