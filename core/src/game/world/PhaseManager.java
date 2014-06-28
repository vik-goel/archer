package game.world;

public class PhaseManager {

	private static final double ENEMY_PHASE_TIME = 10;

	private static boolean playerPhase = true;
	private static Timer timer = new Timer();
	private static Squad squad;

	public static boolean isPlayerPhase() {
		return playerPhase;
	}

	public static boolean isEnemyPhase() {
		return !playerPhase;
	}

	public static void changePhase() {
		playerPhase = !playerPhase;

		if (isEnemyPhase()) 
			timer.start();

		if (isPlayerPhase()) {
			squad.reset();
			SpawnerManager.setSpawners();
		}
	}

	public static void update() {
		if (isEnemyPhase()) {
			if (timer.getRuntime() >= ENEMY_PHASE_TIME) {
				timer.stop();
				changePhase();
			}
		}
	}

	public static void setSquad(Squad squad) {
		PhaseManager.squad = squad;
	}
}
