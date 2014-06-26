package game.world;

import game.entity.Camera;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Map {

	private Texture tileset;
	private SpriteBatch batch;
	
	private Tile[][] tiles;
	
	public Map(int width, int height) {
		tileset = new Texture("ground.png");
		batch = new SpriteBatch();
		
		tiles = new Tile[width][height];
		
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {
				Sprite sprite = new Sprite(tileset);
				sprite.setSize(Tile.SIZE, Tile.SIZE);
				sprite.setPosition(i * Tile.SIZE, j * Tile.SIZE);
				
				tiles[i][j] = new Tile(sprite);
			}
		}
	}
	
	public void render(Camera camera) {
		camera.projectBatch(batch);
		batch.begin();
		
		for (int i = 0; i < tiles.length; i++) 
			for (int j = 0; j < tiles[0].length; j++) 
				tiles[i][j].render(batch);
		
		batch.end();
	}
	
	public int getWidth() {
		return tiles.length;
	}
	
	public int getHeight() {
		return tiles[0].length;
	}
}
