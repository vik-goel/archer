package game.world;

import game.entity.Archer;
import game.entity.Entity;
import game.entity.component.ClickAttack;
import game.entity.component.ClickMove;
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
		destroyReferencesToRemovedEntities();
		
		if (!PhaseManager.isPlayerPhase())
			return;

		ensureOnlyOneEntityIsSelected();

		if (shouldChangePhase())
			PhaseManager.changePhase();
	}

	private void destroyReferencesToRemovedEntities() {
		for (int i = 0; i < squad.size(); i++) 
			if (squad.get(i).isRemoved())
				squad.remove(i--);
	}

	private void ensureOnlyOneEntityIsSelected() {
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
	}
	
	private boolean shouldChangePhase() {
		for (int i = 0; i < squad.size(); i++) {
			ClickAttack clickAttack = squad.get(i).getComponent(ClickAttack.class);
			if (clickAttack == null)
				continue;
			if (!clickAttack.hasSetAttack())
				return false;
		}
		
		return true;
	}
	
	public void reset() {
		for (int i = 0; i < squad.size(); i++) {
			ClickAttack clickAttack = squad.get(i).getComponent(ClickAttack.class);
			if (clickAttack == null) continue;
			clickAttack.reset();
			
			ClickMove clickMove = squad.get(i).getComponent(ClickMove.class);
			if (clickMove == null) continue;
			clickMove.resetMoveRadiusCenter();
		}
	}

	public ArrayList<Entity> getEntities() {
		return squad;
	}

}
