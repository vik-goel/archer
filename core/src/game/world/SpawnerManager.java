package game.world;

import game.entity.Spawner;
import game.entity.component.MobSpawner;

import java.util.ArrayList;
import java.util.Random;

public class SpawnerManager {

	private static final Random random = new Random();
	
	private static final int MIN_ACTIVE_SPAWNERS = 1;
	private static final int MAX_ACTIVE_SPAWNERS = 3;
	
	private static final int MIN_ENTITIES = 3;
	private static final int MAX_ENTITIES = 20;
	
	private static ArrayList<MobSpawner> spawners = new ArrayList<MobSpawner>();
	
	public static void setSpawners() {
		for (MobSpawner spawner : spawners)
			spawner.deactivate();
		
		int numSpawners = random.nextInt(MAX_ACTIVE_SPAWNERS - MIN_ACTIVE_SPAWNERS) + MIN_ACTIVE_SPAWNERS;
	
		if (numSpawners > spawners.size())
			numSpawners = spawners.size();
		
		ArrayList<Integer> usedSpawners = new ArrayList<Integer>();
		
		for (int i = 0; i < numSpawners; i++) {
			int numEntities = random.nextInt(MAX_ENTITIES - MIN_ENTITIES) + MIN_ENTITIES;
			
			int spawnerIndex = -1;
			
			while (spawnerIndex == -1 || usedSpawners.contains(spawnerIndex)) 
				spawnerIndex = random.nextInt(spawners.size());
			
			spawners.get(spawnerIndex).activate(numEntities);
			
			usedSpawners.add(i);
		}
	}
	
	public static void addSpawner(MobSpawner spawner) {
		spawners.add(spawner);
	}
	
	public static Spawner spawnerAt(float x, float y) {
		for (int i = 0; i < spawners.size(); i++) 
			if (spawners.get(i).getParent().getBounds().contains(x, y)) 
				return (Spawner) spawners.get(i).getParent();
		
		return null;
	}
	
}
