package game.world.level;

public class LevelFactory {

	public static Level getLevel(int levelNum) {
		switch (levelNum) {
		case 1: 
			return new TestLevel(levelNum);
		case 2: 
			return new TestLevel(levelNum);
		}
		
		return null;
	}
	
}
