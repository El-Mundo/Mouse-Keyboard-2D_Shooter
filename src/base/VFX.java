package base;

import java.awt.image.BufferedImage;

public abstract class VFX {
	Sprite sprite;
	int time;
	boolean alive;

	public VFX(BufferedImage[] imgArray, float x, float y, float scale) {
		this.sprite = Display.instance.newEffect(imgArray, x, y, scale);
		this.time = 0;
		this.alive = true;
	}
	public abstract void animate();

	public static class EnemyExplosionVFX extends VFX {
		
		public EnemyExplosionVFX(float scale, Sprite enemy) {
			super(Resource.EXPLOSION_IMGS, enemy.x, enemy.y, scale);
		}
		@Override
		public void animate() {
			time ++;
			if(time == 5) {
				time = 0;
				sprite.frame++;
				if(sprite.frame >= sprite.getFrameTotal()) alive = false;
			}
		}
	}
	
}
