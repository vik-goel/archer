package game.world;

import game.entity.Camera;
import game.entity.Entity;
import game.entity.component.ClickAttack;
import game.entity.component.ClickMove;
import game.entity.component.Clickable;
import game.entity.component.Render;
import game.entity.friendly.Archer;
import game.entity.friendly.Bandit;
import game.entity.friendly.Knight;
import game.entity.friendly.Monk;
import game.entity.gui.Minimap;

import java.util.ArrayList;

import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

public class Squad {

	private ArrayList<Entity> squad;
	private boolean firstUpdate = true;
	private boolean tabPushed = false;
	private boolean notPlayerPhase = true;
	
	public Squad(EntityManager manager, RayHandler handler, Vector2 squadPos) {
		squad = new ArrayList<Entity>();
		
		final float deviance = 20;
		
		squad.add(new Monk(new Vector2(deviance, deviance).add(squadPos), handler));
		squad.add(new Bandit(new Vector2(deviance, -deviance).add(squadPos), handler));
		squad.add(new Knight(new Vector2(-deviance, -deviance).add(squadPos), handler));
		squad.add(new Archer(new Vector2(-deviance, deviance).add(squadPos), handler));
		
		for (int i = 0; i < squad.size(); i++)
			manager.addEntity(squad.get(i));
	}

	public void update(Camera camera) {
		if (firstUpdate) {
			firstUpdate = false;
			addToMinimap();
		}
		
		destroyReferencesToRemovedEntities();
		
		if (!PhaseManager.isPlayerPhase()) {
			notPlayerPhase = true;
			return;
		}
		
		if (notPlayerPhase) 
			selectAnyEntity(camera);
		
		notPlayerPhase = false;

		selectNewEntity(camera);
		ensureOnlyOneEntityIsSelected();
		tabOverToNewEntity(camera);

		if (shouldChangePhase())
			PhaseManager.changePhase();
	}

	private void selectAnyEntity(Camera camera) {
		for (int i = 0; i < squad.size(); i++) {
			Clickable clickable = squad.get(i).getComponent(Clickable.class);
		
			if (clickable != null) {
				clickable.select();
				center(camera, squad.get(i));
				return;
			}
		}
	}

	private void center(Camera camera, Entity entity) {
		Vector2 center = new Vector2();
		entity.getBounds().getCenter(center);
		camera.centerAround(center);
	}

	private void selectNewEntity(Camera camera) {
		for (int i = 0; i < squad.size(); i++) {
			Clickable clickable = squad.get(i).getComponent(Clickable.class);
			
			if (clickable == null)
				continue;
			
			if (clickable.wasSelected()) {
				ClickAttack attack = squad.get(i).getComponent(ClickAttack.class);
				
				if (attack != null && attack.hasSetAttack()) {
					for (int j = 0; j < squad.size(); j++) {
						ClickAttack otherAttack = squad.get(j).getComponent(ClickAttack.class);
						Clickable otherClickable = squad.get(j).getComponent(Clickable.class);
						
						if (otherAttack != null && !otherAttack.hasSetAttack() && otherClickable != null) {
							otherClickable.select();
							center(camera,squad.get(j));
							return;
						}
					}
				}
			}
		}
	}
	
	private void tabOverToNewEntity(Camera camera) {
		if (!Gdx.input.isKeyPressed(Input.Keys.TAB))
			tabPushed = false;
		
		if (tabPushed || !Gdx.input.isKeyPressed(Input.Keys.TAB))
			return;
		
		tabPushed = true;
		
		boolean setNewSelected = false;
		Clickable currentlySelected = null;
		
		int loopStart = 0;
		int loopEnd = squad.size();
		
		for (int i = 0; i < squad.size(); i++) {
			Clickable clickable = squad.get(i).getComponent(Clickable.class);
			
			if (clickable != null && clickable.isSelected()) {
				currentlySelected = clickable;
				loopStart = i;
				break;
			}
		}
		
		for (int i = loopStart; i <= loopEnd; i++) {
			if (i >= loopEnd && loopStart != 0) {
				loopEnd = loopStart - 1;
				loopStart = 0;
				i = -1;	
			}
			
			if (i < 0 || i >= squad.size())
				continue;
			
			Clickable clickable = squad.get(i).getComponent(Clickable.class);
			ClickAttack attack = squad.get(i).getComponent(ClickAttack.class);
			
			if (clickable == null || attack == null)
				 continue;
			
			if (!attack.hasSetAttack() && !clickable.isSelected()) {
				clickable.select();
				center(camera, clickable.getParent());
				setNewSelected = true;
				break;
			}
		}
		
		if (setNewSelected && currentlySelected != null)
			currentlySelected.deselect();
	}


	private void addToMinimap() {
		for (int i = 0; i < squad.size(); i++) {
			Render render = squad.get(i).getComponent(Render.class);
			
			if (render != null)
				Minimap.addObject(render);
		}
	}

	private void destroyReferencesToRemovedEntities() {
		for (int i = 0; i < squad.size(); i++) {
			if (squad.get(i).isRemoved()) {
				Clickable clickable = squad.get(i).getComponent(Clickable.class);
				
				if (clickable != null && clickable.isSelected()) {
					int nextSelect = i + 1;
					
					if (nextSelect >= squad.size())
						nextSelect = 0;
					
					if (nextSelect < squad.size()) {
						Clickable nextClickable = squad.get(nextSelect).getComponent(Clickable.class);
						
						if (nextClickable != null)
							nextClickable.select();
					}
				}
				
				squad.remove(i--);
			}
		}
	}

	private void ensureOnlyOneEntityIsSelected() {
		ArrayList<Clickable> selected = new ArrayList<Clickable>();

		for (int i = 0; i < squad.size(); i++) {
			Clickable clickable = squad.get(i).getComponent(Clickable.class);
			
			if (clickable != null && clickable.isSelected()) 
				selected.add(clickable);
		}
		
		if (selected.size() <= 1)
			return;
		
		for (int i = 0; i < selected.size(); i++) {
			ClickAttack attack = selected.get(i).getParent().getComponent(ClickAttack.class);
			
			if (attack != null && attack.hasSetAttack()) {
				selected.get(i).deselect();
				selected.remove(i--);
			}
		}
		
		for (int i = 1; i < selected.size(); i++) 
			selected.get(i).deselect();
	}
	
	private boolean shouldChangePhase() {
		if (squad.isEmpty())
			return false;
		
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
