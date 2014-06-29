package game.entity.component;

import game.entity.Camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Health extends Component {

	private static final ShapeRenderer SHAPE_RENDERER = new ShapeRenderer();
	
	private static final Color GOOD_HEALTH_COLOR = new Color(0f, 1f, 0f, 1f);
	private static final Color MEDIUM_HEALTH_COLOR = new Color(1f, 1f, 0f, 1f);
	private static final Color BAD_HEALTH_COLOR = new Color(1f, 0f, 0f, 1f);
	
	private static final Color BAR_COLOR = new Color(0.5f, 0.5f, 0.5f, 1f);
	private static final Color OUTLINE_COLOR = new Color(0f, 0f, 0f, 1f);
	
	private static final float BAR_HEIGHT = 6;
	private static final float HEALTH_HEIGHT_SPACING = 2;
	private static final float HEALTH_WIDTH_SPACING = 2;
	private static final float OUTLINE_SIZE = 1;
	
	private static final float MEDIUM_THRESHOLD = 0.5f;
	private static final float BAD_THRESHOLD = 0.2f;
	
	private float barWidth;
	private float maxHealth, health;
	private float yOffs;
	
	public Health(float maxHealth, float barWidth, float yOffs) {
		this.maxHealth = maxHealth;
		this.health = maxHealth;
		this.barWidth = barWidth;
		this.yOffs = yOffs;
	}
	
	public void renderLit(Camera camera, SpriteBatch batch) {
		super.renderLit(camera, batch);
		
		float x = parent.getBounds().x + (parent.getBounds().width - barWidth) / 2 - camera.getBounds().x;
		float y = parent.getBounds().y + parent.getBounds().height - camera.getBounds().y + yOffs;
		
		SHAPE_RENDERER.begin(ShapeType.Filled);
		
		SHAPE_RENDERER.setColor(BAR_COLOR);
		SHAPE_RENDERER.rect(x, y, barWidth, BAR_HEIGHT);
		
		SHAPE_RENDERER.setColor(GOOD_HEALTH_COLOR);
		
		if (health / maxHealth <= MEDIUM_THRESHOLD)
			SHAPE_RENDERER.setColor(MEDIUM_HEALTH_COLOR);
		
		if (health / maxHealth <= BAD_THRESHOLD)
			SHAPE_RENDERER.setColor(BAD_HEALTH_COLOR);
		
		SHAPE_RENDERER.rect(x + HEALTH_WIDTH_SPACING / 2, y + HEALTH_HEIGHT_SPACING / 2, (barWidth - HEALTH_WIDTH_SPACING) * (health / maxHealth), BAR_HEIGHT  - HEALTH_HEIGHT_SPACING);
		
		SHAPE_RENDERER.end();
		
		Gdx.gl20.glLineWidth(OUTLINE_SIZE);
		
		SHAPE_RENDERER.begin(ShapeType.Line);
		SHAPE_RENDERER.setColor(OUTLINE_COLOR);
		SHAPE_RENDERER.rect(x + OUTLINE_SIZE / 2, y + OUTLINE_SIZE / 2, barWidth - OUTLINE_SIZE, BAR_HEIGHT - OUTLINE_SIZE);
		
		SHAPE_RENDERER.end();
	}

	public void damage(float amt) {
		setHealth(health - amt);
	}
	
	public void heal(float amt) {
		setHealth(health + amt);
	}
	
	private void setHealth(float health) {
		this.health = health;
		
		if (health <= 0)
			parent.remove();
		else if (health > maxHealth)
			health = maxHealth;
	}
	
}
