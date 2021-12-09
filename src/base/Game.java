package base;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import base.Enemy.EnemyTypes;

public class Game {
	public static Game instance;
	
	Character kPlayer, mPlayer;
	ArrayList<Danmaku> danmaku;
	ArrayList<Enemy> enemies;
	ArrayList<VFX> effects;
	Sprite[] background;
	int backgroundTime, backgroundFrame;
	
	public final static int GROUND_Y = 1030, BKG_DIV = 5;
	public static int maxX = 1920, maxY = 1080, minX = 0, minY = 0;
	public final static float BKG_VEL = 3.0f;
	
	private int spawnTimer[];

	public Game() {
		maxX = Display.windowWidth / Display.RENDER_RATE;
		maxY = Display.windowHeight / Display.RENDER_RATE;
		kPlayer = new Character.Korriel();
		mPlayer = new Character.Mariutza();
		danmaku = new ArrayList<Danmaku>();
		enemies = new ArrayList<Enemy>();
		effects = new ArrayList<VFX>();
		instance = this;
		spawnTimer = new int[2];
		spawnTimer[0] = 120;
		spawnTimer[1] = 360;
		float bs = (float)(maxY+8)/(float)Resource.SKY_IMGS[0].getHeight();
		float bw = Resource.SKY_IMGS[0].getWidth() * bs;
		background = new Sprite[BKG_DIV];
		backgroundFrame = backgroundTime = 0;
		for(int i=0;i<BKG_DIV;i++) {
			background[i] = new Sprite(Resource.SKY_IMGS, maxX/2+i*bw, maxY/2, bs);
		}
	}
	
	public void update() {
		kPlayer.update();
		kPlayer.animate();
		mPlayer.update();
		mPlayer.animate();
		for(int i=0;i<danmaku.size();i++) {
			Danmaku d = danmaku.get(i);
			if(d.alive) {
				d.update();
			}else {
				d.sprite.remove();
				danmaku.remove(i);
				i--;
			}
		}
		for(int i=0;i<enemies.size();i++) {
			Enemy e = enemies.get(i);
			if(e.alive) {
				e.update();
			}else {
				e.sprite.remove();
				enemies.remove(i);
				i--;
			}
		}
		for(int i=0;i<effects.size();i++) {
			VFX v = effects.get(i);
			if(v.alive) {
				v.animate();
			}else {
				v.sprite.remove();
				effects.remove(i);
				i--;
			}
		}
		
		for(int i=0; i<spawnTimer.length; i++) {
			if(spawnTimer[i] > 0) {
				spawnTimer[i] --;
			}else if(spawnTimer[i] == 0) {
				switch(i) {
				case 0:
					spawnEnemy(EnemyTypes.Bat);
					spawnTimer[0] = (int)(Math.random() * 120 + 240);
					break;
				case 1:
					spawnBulletRain();
					spawnTimer[1] = (int)(Math.random() * 360 + 360);
					break;
				}
			}
		}
	}
	
	public void shootDanmaku(BufferedImage[] imgs, float x, float y, float vx, float vy, boolean friendly, int animateInterval, int colX, int colY, int colW, int colH) {
		danmaku.add(new Danmaku(imgs, x, y, vx, vy, friendly, animateInterval, colX, colY, colW, colH));
	}
	
	public void spawnEnemy(Enemy.EnemyTypes e) {
		switch (e) {
		case Bat:
			enemies.add(new Enemy.Bat());
			break;
		default:
			break;
		}
	}
	
	public void spawnEnemy(Enemy.EnemyTypes e, float param) {
		switch (e) {
		case Bullet:
			enemies.add(new Enemy.Bullet(param));
			break;
		default:
			break;
		}
	}
	
	public void addVFX(VFX effect) {
		effects.add(effect);
	}
	
	private void spawnBulletRain() {
		Resource.SFX_BULLET.play();
		int count = (int)(Math.random() * 3) + 3;
		float interval = Game.maxY / (count+1);
		for(int i=0; i<count; i++) {
			spawnEnemy(EnemyTypes.Bullet, interval*(i+1));
		}
	}
	
	public Sprite[] getBackground() {
		backgroundTime ++;
		switch(backgroundTime) {
		case 1:
			backgroundFrame = 0;
			break;
		case 300:
			backgroundFrame = 1;
			Resource.SFX_THUNDER.play();
			break;
		case 305:
			backgroundFrame = 2;
			break;
		case 310:
			backgroundFrame = 3;
			break;
		case 315:
			backgroundFrame = 4;
			break;
		case 320:
			backgroundFrame = 5;
			break;
		case 325:
			backgroundFrame = 2;
			break;
		case 330:
			backgroundFrame = 3;
			break;
		case 335:
			backgroundFrame = 4;
			break;
		case 340:
			backgroundFrame = 5;
			break;
		case 345:
			backgroundFrame = 0;
			backgroundTime = 0;
			break;
		}
		return background;
	}

}
