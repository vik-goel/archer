package game.world;

public class Timer {

	private static final double NANO_TO_SECOND = 1000000000.0;
	
	private long startTime, endTime;
	private boolean started = false;
	
	public void start() {
		if (started) 
			throw new RuntimeException("Error: This timer has already been started!");
		
		startTime = System.nanoTime();
		started = true;
	}
	
	public void stop() {
		if (!started) 
			throw new RuntimeException("Error: This timer has not been started!");
		
		endTime = System.nanoTime();
		started = false;
	}
	
	public long getRuntimeNano() {
		if (started) 
			return System.nanoTime() - startTime;
		return endTime - startTime;
	}
	
	public double getRuntime() {
		return getRuntimeNano() / NANO_TO_SECOND;
	}
}
