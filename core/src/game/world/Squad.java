package game.world;

import game.entity.Archer;
import game.entity.Entity;
import game.entity.component.ClickAttack;
import game.entity.component.Clickable;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

public class Squad {

	private ArrayList<Entity> squad;

	public Squad(EntityManager manager) {
		squad = new ArrayList<Entity>();

		squad.add(new Archer(new Vector2(100, 100)));
		squad.add(new Archer(new Vector2(130, 80)));

		for (int i = 0; i < squad.size(); i++)
			manager.addEntity(squad.get(i));
	}

	public void update() {
		if (!PhaseManager.isPlayerPhase())
			return;

		boolean alreadySelected = false;

		for (int i = 0; i < squad.size(); i++) {
			Clickable clickable = squad.get(i).getComponent(Clickable.class);
			if (clickable != null && clickable.isSelected()) {
				if (alreadySelected)
					clickable.deselect();
				else
					alreadySelected = true;
			}
		}

		for (int i = 0; i < squad.size(); i++) {
			ClickAttack clickAttack = squad.get(i).getComponent(ClickAttack.class);
			if (clickAttack == null)
				continue;
			if (!clickAttack.hasSetAttack())
				return;
		}

		PhaseManager.changePhase();
	}

}
