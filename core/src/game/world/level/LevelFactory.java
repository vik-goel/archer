package game.world.level;

public class LevelFactory {

	public static Level getLevel(int levelNum) {
		switch (levelNum) {
		case 1: 
			return new TestLevel(levelNum);
		case 2: 
			return new TestLevel2(levelNum);
		}
		
		throw new IllegalArgumentException("Error: Level " + levelNum + " does not exist!");
	}
	
}
