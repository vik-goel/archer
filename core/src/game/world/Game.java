package game.world;

import game.world.level.Level;
import game.world.level.LevelFactory;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;

public class Game extends ApplicationAdapter {

	private static Level level;
	private FPSLogger fpsLogger;
	
	public void create() {
		fpsLogger = new FPSLogger();
		
		Music music = Gdx.audio.newMusic(Gdx.files.internal("adagio.ogg"));
		music.setLooping(true);
		music.play();
		
		setLevel(1);
	}

	public void render() {
		level.update(Gdx.graphics.getDeltaTime() * 60);
		PhaseManager.update();
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		level.render();
		
		fpsLogger.log();
	}
	
	public static void setLevel(int levelNum) {
		if (level != null)
			level.dispose();
		
		level = LevelFactory.getLevel(levelNum);
	}

}
