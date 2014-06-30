package game.entity;

import game.entity.movement.Collision;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class BloodParticle extends Entity {

	private static final Random random = new Random();
	private static final ShapeRenderer shapeRenderer = new ShapeRenderer();
	
	private static final Color[] colors = new Color[]{new Color(0x8A0707FF), 
													  new Color(0x660000FF),
													  new Color(0xDC143CFF),
													  new Color(0x8B0000FF),
													  new Color(0x800000FF),
													  new Color(0xCC1100FF)};
	
	private static final float POS_VARIANCE = 30;
	private static final float MIN_WIDTH = 1;
	private static final float MAX_WIDTH = 8;
	private static final float HEIGHT_VARIANCE = 3;
	
	private static final float MIN_LIFE = 25;
	private static final float MAX_LIFE = 50;
	
	private static final double DIR_VARIANCE = Math.PI / 2;
	private static final float MIN_SPEED = 0.5f;
	private static final float MAX_SPEED = 2;
	
	private static final float ACCELERATION = 0.15f;
	private static final float GRAVITY = 0.05f;
	
	private Color color;
	private float life;
	private Vector2 dir;
	private float speed;
	
	public BloodParticle(Vector2 pos, Vector2 bulletDir) {
		super(new Rectangle());
		
		bounds.x = pos.x + random.nextFloat() * POS_VARIANCE * 2 - POS_VARIANCE;
		bounds.y = pos.y + random.nextFloat() * POS_VARIANCE * 2 - POS_VARIANCE;
		
		bounds.width = random.nextFloat() * (MAX_WIDTH - MIN_WIDTH) + MIN_WIDTH;
		bounds.height = bounds.width + random.nextFloat() * HEIGHT_VARIANCE * 2 - HEIGHT_VARIANCE;
		
		color = colors[random.nextInt(colors.length)];
		
		life = random.nextFloat() * (MAX_LIFE - MIN_LIFE) + MIN_LIFE;
		
		double bulletAngle = Math.atan2(bulletDir.y, bulletDir.x) + random.nextDouble() * DIR_VARIANCE * 2 - DIR_VARIANCE;
		dir = new Vector2((float)Math.cos(bulletAngle), (float)Math.sin(bulletAngle));
		
		speed = random.nextFloat() * (MAX_SPEED - MIN_SPEED) + MIN_SPEED;
	}
	
	public void update(Camera camera, float dt) {
		super.update(camera, dt);
		
		life -= dt;
		
		if (life < 0)
			remove();
		
		speed += ACCELERATION;
		dir.y -= GRAVITY;
		
		Vector2 moveAmount = new Vector2(dir).scl(dt * speed);
		Vector2 collision = Collision.collision(this, moveAmount);
		
		if (collision.x != 1 || collision.y != 1)
			remove();
		
		bounds.x += moveAmount.x;
		bounds.y += moveAmount.y;
	}

	public void renderLit(Camera camera, SpriteBatch batch) {
		super.renderLit(camera, batch);
		
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(color);
		shapeRenderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
		shapeRenderer.end();
	}

}
