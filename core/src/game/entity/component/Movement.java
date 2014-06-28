package game.entity.component;

import game.entity.Entity;
import game.world.Map;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Movement {

	public static Vector2 collision(Entity parent, Vector2 amt) {
		if (amt.x != 0 && amt.y != 0) 
			return new Vector2(collision(parent, new Vector2(amt.x, 0)).x, collision(parent, new Vector2(0, amt.y)).y);
		
		Rectangle bounds = parent.getCollisionBounds();
		
		Vector2 newPosBL = new Vector2(bounds.x + amt.x, bounds.y + amt.y);
		
		if (tileCollision(parent.getMap(), newPosBL)) 
			return new Vector2();
		
		Vector2 newPosBR = new Vector2(newPosBL);
		newPosBL.x += bounds.width;
		
		if (tileCollision(parent.getMap(), newPosBR)) 
			return new Vector2();
		
		Vector2 newPosTL = new Vector2(newPosBL);
		newPosTL.y += bounds.height;
		
		if (tileCollision(parent.getMap(), newPosTL)) 
			return new Vector2();
		
		Vector2 newPosTR = new Vector2(newPosBR);
		newPosTR.y += bounds.height;
		
		if (tileCollision(parent.getMap(), newPosTR)) 
			return new Vector2();
		
		return new Vector2(1, 1);
	}

	private static boolean tileCollision(Map map, Vector2 pos) {
		int x = (int) (pos.x / Map.TILE_SIZE);
		int y = (int) (pos.y / Map.TILE_SIZE);
		
		return map.isSolid(x, y);
	}
	
}
