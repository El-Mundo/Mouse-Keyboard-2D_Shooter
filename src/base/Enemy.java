package base;

import java.awt.image.BufferedImage;

public abstract class Enemy {
	Sprite sprite;
	float vx, vy, x, y;
	int time;
	int animateInterval;
	boolean alive;
	int colX, colY, colW, colH;
	boolean invincible;
	
	enum EnemyTypes  {
		Bat, Bullet
	};

	public Enemy(BufferedImage[] img, float x, float y, float scale, int colX, int colY, int colW, int colH) {
		this.vx = this.vy = 0;
		this.x = x;
		this.y = y;
		this.sprite = Display.instance.newSprite(img, x, y, scale);
		this.time = 0;
		this.animateInterval = 0;
		this.alive = true;
		this.colX = colX;
		this.colY = colY;
		this.colH = colH;
		this.colW = colW;
		this.invincible = false;
	}
	public Enemy(BufferedImage[] img, float x, float y, float scale, int colX, int colY, int colW, int colH, int animateInterval) {
		this(img, x, y, scale, colX, colY, colW, colH);
		this.animateInterval = animateInterval;
	}
	
	public abstract void update();
	
	public void checkCollision() {
		int thisColX1 = (int)x+colX, thisColX2 = (int)x+colX+colW, thisColY1 = (int)y+colY, thisColY2 = (int)y+colY+colH;
		if(invincible) {
			for(Danmaku d : Game.instance.danmaku) {
				if(d.x + d.colX < thisColX2 && d.x + d.colX + d.colW > thisColX1) {
					if(d.y + d.colY < thisColY2 && d.y + d.colY + d.colH > thisColY1) {
						Game.instance.addVFX(new VFX.EnemyExplosionVFX(1, d.sprite));
						Resource.SFX_BLOCK.play();
						d.alive = false;
					}
				}
			}
		}else {
			for(Danmaku d : Game.instance.danmaku) {
				if(!d.isFriendly) continue;
				if(d.x + d.colX < thisColX2 && d.x + d.colX + d.colW > thisColX1) {
					if(d.y + d.colY < thisColY2 && d.y + d.colY + d.colH > thisColY1) {
						defeated();
					}
				}
			}
		}
			
	}
	
	public void defeated() {
		Resource.SFX_EXPLOSION.play();
		this.alive = false;
		Game.instance.addVFX(new VFX.EnemyExplosionVFX(2, this.sprite));
	}
	
	public void animate() {
		time ++;
		if(time == animateInterval) {
			time = 0;
			sprite.frame++;
			if(sprite.frame >= sprite.getFrameTotal()) {
				sprite.frame = 0;
			}
		}
		sprite.x = x;
		sprite.y = y;
	}
	
	public static class Bat extends Enemy {
		float startY, acc;
		int shootTimer;
		private static final float ACC = 0.05f, Y_VEL = 1.5f, OFF = 8.0f, X_VEL = -0.2f, DAN_VEL = 3.0f;
		private static final int SHOOT = 120;

		public Bat() {
			super(Resource.BAT_IMGS, Game.maxX+36, (float)(Math.random()*Game.maxY)*0.6f+Game.maxY*0.2f, 1, -8, -4, 14, 10, 4);
			this.startY = this.y;
			this.acc = ACC;
			this.vy = 0;
			this.vx = X_VEL;
			this.shootTimer = SHOOT;
		}

		@Override
		public void update() {
			vy += acc;
			vy = Math.max(vy, -Y_VEL);
			vy = Math.min(vy, Y_VEL);
			x += vx;
			y += vy;
			if(y < startY-OFF) {
				acc = ACC;
			}else if(y > startY+OFF) {
				acc = -ACC;
			}
			checkCollision();
			if(shootTimer == 0) {
				shoot();
			}else if(shootTimer > 0) {
				shootTimer --;
			}
			super.animate();
			if(this.x < -sprite.w) {
				alive = false;
			}
		}
		
		public void shoot() {
			shootTimer = SHOOT;
			Resource.SFX_ENEMY_SHOOT.play();
			float angle = (float) (Math.random() * Math.PI + Math.PI/2);
			for(int i=0; i<18; i++) {
				Game.instance.shootDanmaku(Resource.FIREROCK_IMGS, x, y, (float)Math.cos(angle)*DAN_VEL, (float)Math.sin(angle)*DAN_VEL, false, 4, -5, -5, 10, 10);
				angle += 0.34906f;
			}
		}
		
	}
	
	public static class Bullet extends Enemy {
		private static final float VEL = -1.0f;

		public Bullet(float y) {
			super(Resource.BULLET_IMGS, Game.maxX+40, y, 1, -4, -4, 24, 14, 4);
			this.vy = 0;
			this.vx = VEL;
			this.invincible = true;
		}

		@Override
		public void update() {
			x += vx;
			y += vy;
			checkCollision();
			super.animate();
			if(this.x < -sprite.w) {
				alive = false;
			}
		}
		
	}

}
