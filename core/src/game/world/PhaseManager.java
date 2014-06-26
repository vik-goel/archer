package game.world;

public class PhaseManager {

	private static boolean playerPhase = true;
	
	public static boolean isPlayerPhase() {
		return playerPhase;
	}
	
	public static boolean isEnemyPhase() {
		return !playerPhase;
	}
	
	public static void changePhase() {
		playerPhase = !playerPhase;
	}
	
}
