package game.entity.component;

import game.entity.Camera;
import game.world.PhaseManager;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class SquadSprite extends Component {

	private static final int SPRITE_SIZE = 64;
	
	private ClickAttack clickAttack;
	private Render render;
	private Sprite[] sprites;
	
	public SquadSprite(String texturePath) {
		sprites = new Sprite[4];
		
		Texture texture = new Texture(texturePath);
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		for (int i = 0; i < sprites.length; i++)
			sprites[i] = new Sprite(texture, 0, SPRITE_SIZE * i, SPRITE_SIZE, SPRITE_SIZE);
		
		render = new Render(sprites[2]);
	}

	public void init(Camera camera) {
		super.init(camera);
		
		clickAttack = parent.getComponent(ClickAttack.class);
		parent.addComponent(render);
	}

	public void update(Camera camera, float dt) {
		super.update(camera, dt);
		
		if (!PhaseManager.isPlayerPhase())
			return;
		
		if (clickAttack.isAttacking()) {
			float angle = clickAttack.getAttackAngle();
			
			if (angle <= 145 && angle > 45)
				render.setSprite(sprites[0]);
			else if (angle <= 45 && angle > -45)
				render.setSprite(sprites[3]);
			else if (angle <= -45 && angle > -145)
				render.setSprite(sprites[2]);
			else 
				render.setSprite(sprites[1]);
		}
	}
	
}
