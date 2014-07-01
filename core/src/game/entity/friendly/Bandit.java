package game.entity.friendly;

import game.entity.AttackType;
import game.entity.Entity;
import game.entity.component.ClickAttack;
import game.entity.component.ClickMove;
import game.entity.component.Clickable;
import game.entity.component.Health;
import game.entity.component.SightRange;
import game.entity.component.SquadSprite;
import box2dLight.RayHandler;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;


public class Bandit extends Entity {

	public Bandit(Vector2 pos, RayHandler handler) {
		super(new Rectangle(pos.x, pos.y, 64, 64));
		
		addComponent(new ClickMove(Friendly.MOVE_RADIUS));
		addComponent(new Clickable(-17, -38, 32, 16));
		addComponent(new SquadSprite("bandit.png"));
		addComponent(new ClickAttack(140, 225, new Color(1f, 0.2f, 0.1f, 0.15f), AttackType.THROWING_STAR, 25));
		addComponent(new Health(Friendly.MAX_HEALTH, 30, -8));
		addComponent(new SightRange(handler, Friendly.SIGHT_RANGE, Friendly.DISCOVER_RANGE));
	}

	public Rectangle getCollisionBounds() {
		final float xIncrease = 5;
		final float xReduce = 40;
		final float heightReduce = 35;
		final float yIncrease = 7;
		
		return new Rectangle(bounds.x + xReduce / 2 - xIncrease, bounds.y - yIncrease, bounds.width - xReduce, bounds.height - heightReduce);
	}
	
}
