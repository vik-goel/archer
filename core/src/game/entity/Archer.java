package game.entity;

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

public class Archer extends Entity {

	public Archer(Vector2 pos, RayHandler handler) {
		super(new Rectangle(pos.x, pos.y, 64, 64));
		
		addComponent(new ClickMove(400));
		addComponent(new Clickable(-17, -38, 32, 16));
		addComponent(new SquadSprite("archer.png"));
		addComponent(new ClickAttack(200, 100, new Color(1f, 0f, 1f, 0.25f), AttackType.ARROW, 10));
		addComponent(new Health(100, 30, -8));
		addComponent(new SightRange(handler, 450, 10));
	}

	public Rectangle getCollisionBounds() {
		final float xIncrease = 5;
		final float xReduce = 40;
		final float heightReduce = 35;
		final float yIncrease = 7;
		
		return new Rectangle(bounds.x + xReduce / 2 - xIncrease, bounds.y - yIncrease, bounds.width - xReduce, bounds.height - heightReduce);
	}
	
}
