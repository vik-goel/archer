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
	private TiledMapTileLayer wallLayer;
	
	public Map(String tmxPath) {
		tiledMap = new TmxMapLoader().load(tmxPath);
		renderer = new OrthogonalTiledMapRenderer(tiledMap, 2);
		
		wallLayer = (TiledMapTileLayer) tiledMap.getLayers().get("walls");
	}
	
	public void render(Camera camera) {
		camera.projectMap(renderer);
		renderer.render();
	}
	
	public boolean isSolid(int x, int y) {
		return wallLayer.getCell(x, y) != null;
	}
	
	public int getWidth() {
		return wallLayer.getWidth();
	}
	
	public int getHeight()  {
		return wallLayer.getHeight();
	}
	
}
