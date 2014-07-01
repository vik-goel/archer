package game.entity.gui;

import game.entity.Camera;
import game.entity.Entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

public class LevelTransition extends Entity {

	private static final BitmapFont FONT = new BitmapFont(Gdx.files.internal("fonts/handwritten.fnt"), Gdx.files.internal("fonts/handwritten.png"), false);
	private static final ShapeRenderer renderer = new ShapeRenderer();

	private static final float ACCELERATION = 0.0005f;
	
	private String level;
	private float alpha = 1;
	private float alphaSpeed = 0;
	
	public LevelTransition(Camera camera, int levelNum) {
		super(new Rectangle(0, 0, camera.getBounds().width, camera.getBounds().height));
		
		level = "Level " + levelNum;
	}
	public void update(Camera camera, float dt) {
		super.update(camera, dt);
		
		if (alpha < 0) {
			remove();
			return;
		}
		
		alphaSpeed += ACCELERATION;
		alpha -= alphaSpeed;
	}

	public void renderLate(Camera camera, SpriteBatch batch) {
		super.renderLate(camera, batch);
		
		Gdx.gl.glEnable(GL20.GL_BLEND);
	    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		renderer.begin(ShapeType.Filled);
		renderer.setColor(0, 0, 0, alpha);
		renderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
		renderer.end();
		
		TextBounds textBounds =  FONT.getBounds(level);
		
		float width = textBounds.width;
		float height = textBounds.height;
		
		float x = (bounds.width - width) / 2 + camera.getBounds().x;
		float y = (bounds.height - height) / 2 + camera.getBounds().y;
		
		batch.begin();
		batch.setColor(1.0f, 1.0f, 1.0f, alpha);
		FONT.draw(batch, level, x, y);
		batch.end();
		batch.setColor(1, 1, 1, 1);
		
		Gdx.gl.glDisable(GL20.GL_BLEND);
	}
	
}
