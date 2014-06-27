package game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import game.world.Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.width = 800;
		config.height = 600;
		config.title = "Archer";
		config.resizable = false;
		config.samples = 4;
		config.backgroundFPS = -1;
		config.vSyncEnabled = true;
		config.useGL30 = false;
		config.allowSoftwareMode = true;
		config.x = -1;
		config.y = -1;
		
		new LwjglApplication(new Game(), config);
	}
}
