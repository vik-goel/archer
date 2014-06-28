package game.entity.component;

import game.entity.Camera;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Health extends Component {

	private static final ShapeRenderer SHAPE_RENDERER = new ShapeRenderer();
	private static final Color HEALTH_COLOR = new Color(1f, 0f, 0f, 1f);
	private static final Color BAR_COLOR = new Color(0.5f, 0.5f, 0.5f, 1f);
	
	private static final float BAR_HEIGHT = 6;
	private float barWidth;
	
	private float maxHealth, health;
	
	public Health(float maxHealth, float barWidth) {
		this.maxHealth = maxHealth;
		this.health = maxHealth;
		this.barWidth = barWidth;
	}
	
	public void renderLit(Camera camera, SpriteBatch batch) {
		super.renderLit(camera, batch);
		
		float x = parent.getBounds().x + (parent.getBounds().width - barWidth) / 2 - camera.getBounds().x;
		float y = parent.getBounds().y + parent.getBounds().height - camera.getBounds().y;
		
		SHAPE_RENDERER.begin(ShapeType.Filled);
		
		SHAPE_RENDERER.setColor(BAR_COLOR);
		SHAPE_RENDERER.rect(x, y, barWidth, BAR_HEIGHT);
		
		SHAPE_RENDERER.setColor(HEALTH_COLOR);
		SHAPE_RENDERER.rect(x, y, barWidth * (health / maxHealth), BAR_HEIGHT);
		
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
