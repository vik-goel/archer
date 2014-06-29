package game.entity.gui;

import game.entity.Camera;
import game.entity.Entity;
import game.entity.component.Clickable;
import game.entity.component.Render;
import game.world.Map;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

public class Minimap extends Entity {

	private static final float WIDTH = 0.9f;
	private static final float HEIGHT = 0.9f;
	private static final float SPACING = 25;

	private static final float ALPHA_ACCELERATION = 0.0125f;
	
	private static Minimap minimap;
	private static ShapeRenderer renderer = new ShapeRenderer();
	private static ArrayList<Render> renders = new ArrayList<Render>();

	private FrameBuffer frameBuffer;
	private boolean active = false;

	private boolean mDown;
	private float tileWidth, tileHeight;

	private boolean[][] discovered;

	private float alpha = 0f;
	private float alphaSpeed = 0f;

	public Minimap(Camera camera, int mapWidth, int mapHeight) {
		super(new Rectangle());

		minimap = this;

		discovered = new boolean[mapWidth][mapHeight];

		for (int i = 0; i < discovered.length; i++)
			for (int j = 0; j < discovered[0].length; j++)
				discovered[i][j] = false;

		bounds.width = camera.getBounds().width * WIDTH;
		bounds.height = camera.getBounds().height * HEIGHT;

		if (bounds.width > bounds.height)
			bounds.width = bounds.height;
		else if (bounds.height > bounds.width)
			bounds.height = bounds.width;

		tileWidth = (bounds.width - SPACING) / mapWidth;
		tileHeight = (bounds.height - SPACING) / mapHeight;

		bounds.x = (camera.getBounds().width - bounds.width) / 2;
		bounds.y = (camera.getBounds().height - bounds.height) / 2;

		frameBuffer = new FrameBuffer(Format.RGBA8888, (int) bounds.width, (int) bounds.height, false);

		initBuffer();
	}

	private void initBuffer() {
		frameBuffer.bind();

		renderer.begin(ShapeType.Filled);
		renderer.setColor(0f, 0f, 0f, 0f);
		renderer.rect(0, 0, bounds.width, bounds.height);
		renderer.end();

		Gdx.gl20.glLineWidth(SPACING);
		
		renderer.begin(ShapeType.Line);
		renderer.setColor(1f, 1f, 0f, 1f);
		renderer.rect(0, 0, bounds.width, bounds.height);
		renderer.end();

		FrameBuffer.unbind();
	}

	public void update(Camera camera, float dt) {
		super.update(camera, dt);

		activate();
		fadeAlpha();
		destroyRemovedReferences();
	}

	private void activate() {
		if (Gdx.input.isKeyPressed(Input.Keys.M) && !mDown) {
			active = !active;
			mDown = true;
			Clickable.setEnabled(false);
		} else if (!Gdx.input.isKeyPressed(Input.Keys.M)) {
			mDown = false;
			Clickable.setEnabled(true);
		}

		if (active)
			Camera.setMovementEnabled(false);
		else
			Camera.setMovementEnabled(true);
	}

	private void fadeAlpha() {
		if (active) {
			if (alphaSpeed < 0)
				alphaSpeed = 0;
			alphaSpeed += ALPHA_ACCELERATION;
		} else {
			if (alphaSpeed > 0)
				alphaSpeed = 0;
			alphaSpeed -= ALPHA_ACCELERATION;
		}
		
		alpha += alphaSpeed ;
		
		if (alpha > 1)
			alpha = 1;
		else if (alpha < 0)
			alpha = 0;
	}

	private void destroyRemovedReferences() {
		for (int i = 0; i < renders.size(); i++)
			if (renders.get(i).getParent().isRemoved())
				renders.remove(i--);
	}

	public void renderLate(Camera camera, SpriteBatch batch) {
		super.renderLate(camera, batch);

		if (alpha <= 0)
			return;

		renderBackground(camera);

		batch.setColor(1, 1, 1, alpha);
		batch.begin();
		
		renderTiles(camera, batch);
		renderObjects(camera, batch);

		batch.end();
		batch.setColor(Color.WHITE);
	}

	private void renderBackground(Camera camera) {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		renderer.begin(ShapeType.Filled);
		renderer.setColor(0f, 0f, 0f, 0.8f * alpha);
		renderer.rect(0, 0, camera.getBounds().width, camera.getBounds().height);
		renderer.end();

		Gdx.gl.glDisable(GL20.GL_BLEND);
	}

	private void renderTiles(Camera camera, SpriteBatch batch) {
		Texture texture = frameBuffer.getColorBufferTexture();
		batch.draw(texture, bounds.x + camera.getBounds().x, bounds.y + camera.getBounds().y);
	}

	private void renderObjects(Camera camera, SpriteBatch batch) {
		float xScale = tileWidth / Map.TILE_SIZE;
		float yScale = tileHeight / Map.TILE_SIZE;

		for (Render render : renders) {
			Sprite sprite = render.getSprite();

			Texture texture = sprite.getTexture();

			float xPos = render.getParent().getBounds().x * xScale + SPACING / 2f + bounds.x;
			float yPos = render.getParent().getBounds().y * yScale + SPACING / 2f + bounds.y;
			float width = render.getParent().getBounds().width * xScale;
			float height = render.getParent().getBounds().height * yScale;

			batch.draw(texture, xPos + camera.getBounds().x, yPos + camera.getBounds().y, width, height, sprite.getU(), sprite.getV2(), sprite.getU2(), sprite.getV());
		}
	}

	public static void discoverObject(int x, int y, TextureRegion region) {
		drawTile(x, y, region, null);
	}

	public static void discoverObject(int x, int y, Texture texture) {
		drawTile(x, y, null, texture);
	}

	private static void drawTile(int x, int y, TextureRegion region, Texture texture) {
		if (minimap.discovered[x][y])
			return;

		minimap.discovered[x][y] = true;

		float xPos = x * minimap.tileWidth + SPACING / 2f;
		float yPos = (minimap.discovered[0].length - y - 1) * minimap.tileHeight + SPACING / 2f;

		SpriteBatch batch = new SpriteBatch();

		minimap.frameBuffer.bind();

		batch.begin();

		if (region != null)
			batch.draw(region, xPos, yPos, minimap.tileWidth, minimap.tileHeight);
		else
			batch.draw(texture, xPos, yPos, minimap.tileWidth, minimap.tileHeight);

		batch.end();

		FrameBuffer.unbind();
	}

	public static void addObject(Render render) {
		if (!renders.contains(render))
			renders.add(render);
	}

}
