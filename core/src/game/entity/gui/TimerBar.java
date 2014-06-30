package game.entity.gui;

import game.entity.Camera;
import game.entity.Entity;
import game.world.Timer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

public class TimerBar extends Entity {

	private static final Color BAR_COLOR = new Color(0f, 0f, 0f, 0f);
	private static final Color TIME_COLOR = new Color(194f / 255f, 59f / 255f, 34f / 255f, 1f);
	private static final Color OUTLINE_COLOR = new Color(207f / 255f, 207f / 255f, 196f / 255f, 1f);
	
	private static final float WIDTH = 0.9f;
	private static final float HEIGHT = 0.05f;
	private static final float YOFFS = 0.01f;
	private static final float TIME_SPACING = 5;
	private static final float OUTLINE_THICKNESS = 2;
	
	private static ShapeRenderer shapeRenderer;
	
	private Timer timer;
	private double phaseTime;
	
	public TimerBar(Timer timer, double phaseTime) {
		super(new Rectangle());
		
		this.timer = timer;
		this.phaseTime = phaseTime;
		
		shapeRenderer = new ShapeRenderer();
	}

	public void update(Camera camera, float dt) {
		super.update(camera, dt);
		
		bounds.width = camera.getBounds().width * WIDTH;
		bounds.x = (camera.getBounds().width - bounds.width) / 2;
		bounds.y = camera.getBounds().height * YOFFS;
		bounds.height = camera.getBounds().height * HEIGHT;
	}

	public void renderLate(Camera camera, SpriteBatch batch) {
		super.renderUnlit(camera, batch);
		
		if (!timer.isStarted())
			return;
		
		Gdx.gl.glEnable(GL20.GL_BLEND);
	    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		shapeRenderer.begin(ShapeType.Filled);
		
		shapeRenderer.setColor(BAR_COLOR);
		shapeRenderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
		
		Rectangle timeBounds = new Rectangle(bounds);

		timeBounds.x += TIME_SPACING / 2;
		timeBounds.y += TIME_SPACING / 2;
		timeBounds.width -= TIME_SPACING;
		timeBounds.height -= TIME_SPACING;
		
		shapeRenderer.setColor(TIME_COLOR);
		shapeRenderer.rect(timeBounds.x, timeBounds.y, timeBounds.width * (1 - (float)(timer.getRuntime() / phaseTime)), timeBounds.height);
		
		shapeRenderer.end();
		
		Gdx.gl20.glLineWidth(OUTLINE_THICKNESS);
		
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(OUTLINE_COLOR);
		shapeRenderer.rect(bounds.x + OUTLINE_THICKNESS / 2, bounds.y + OUTLINE_THICKNESS / 2, bounds.width - OUTLINE_THICKNESS, bounds.height - OUTLINE_THICKNESS);
		shapeRenderer.end();
		
		Gdx.gl.glDisable(GL20.GL_BLEND);
	}

}
