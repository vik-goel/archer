package game.entity.component;

import game.entity.Camera;
import game.entity.Entity;
import game.entity.friendly.Archer;
import game.entity.friendly.Bandit;
import game.entity.friendly.Knight;
import game.entity.friendly.Monk;
import game.entity.gui.Money;

import java.util.ArrayList;

import com.badlogic.gdx.audio.Sound;

public class MoneyCollider extends Component {

	private int value;
	private Sound collisionSound;
	
	public MoneyCollider(int value, Sound collisionSound) {
		this.value = value;
		this.collisionSound = collisionSound;
	}

	public void update(Camera camera, float dt) {
		super.update(camera, dt);
		
		ArrayList<Entity> collisions = parent.getManager().getEntitiesWithinArea(parent.getBounds());
		
		for (int i = 0; i < collisions.size(); i++) {
			if (collisions.get(i) instanceof Archer ||
				collisions.get(i) instanceof Bandit || 
				collisions.get(i) instanceof Knight || 
				collisions.get(i) instanceof Monk) {

				Money.add(value);
				collisionSound.play();
				parent.remove();
				return;
			}
		}
	}

}
