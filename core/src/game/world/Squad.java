package game.world;

import game.entity.Archer;
import game.entity.Entity;
import game.entity.component.ClickAttack;
import game.entity.component.ClickMove;
import game.entity.component.Clickable;
import game.entity.component.Render;
import game.entity.gui.Minimap;

import java.util.ArrayList;

import box2dLight.RayHandler;

import com.badlogic.gdx.math.Vector2;

public class Squad {

	private ArrayList<Entity> squad;
	private boolean firstUpdate = true;

	public Squad(EntityManager manager, RayHandler handler) {
		squad = new ArrayList<Entity>();

		squad.add(new Archer(new Vector2(100, 100), handler));
		squad.add(new Archer(new Vector2(130, 80), handler));

		for (int i = 0; i < squad.size(); i++)
			manager.addEntity(squad.get(i));
	}

	public void update() {
		if (firstUpdate) {
			firstUpdate = false;
			addToMinimap();
		}
		
		destroyReferencesToRemovedEntities();
		
		if (!PhaseManager.isPlayerPhase())
			return;

		ensureOnlyOneEntityIsSelected();

		if (shouldChangePhase())
			PhaseManager.changePhase();
	}

	private void addToMinimap() {
		for (int i = 0; i < squad.size(); i++) {
			Render render = squad.get(i).getComponent(Render.class);
			
			if (render != null)
				Minimap.addObject(render);
		}
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
