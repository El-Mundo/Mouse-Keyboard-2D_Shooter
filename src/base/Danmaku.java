package base;

import java.awt.image.BufferedImage;

public class Danmaku {
	Sprite sprite;
	float vx, vy, x, y;
	int time;
	boolean isFriendly;
	int animateInterval;
	boolean alive;
	int colX, colY, colW, colH;

	public Danmaku(BufferedImage[] imgs, float x, float y, float vx, float vy, boolean friendly, int animateInterval, int colX, int colY, int colW, int colH) {
		this.time = 0;
		this.vx = vx;
		this.vy = vy;
		this.x = x;
		this.y = y;
		this.isFriendly = friendly;
		this.sprite = Display.instance.newSprite(imgs, x, y, 1);
		this.animateInterval = animateInterval;
		this.alive = true;
		this.colX = colX;
		this.colY = colY;
		this.colH = colH;
		this.colW = colW;
	}
	
	public void update() {
		this.x += this.vx;
		this.y += this.vy;
		this.sprite.x = this.x;
		this.sprite.y = this.y;
		if(this.x > Game.maxX + this.sprite.w || this.x < Game.minX - this.sprite.w || this.y < Game.minY - this.sprite.h || this.y > Game.maxY + this.sprite.h) {
			this.alive = false;
		}
		animate();
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
	}

}
