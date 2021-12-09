package base;

import java.awt.image.BufferedImage;

public abstract class Character {
	Sprite sprite;
	float vx, vy, x, y;
	int time;
	int freeze, invincible;
	public static final float MAX_VEL = 3.0f, ACC = 0.06f, SFT = 0.1f;

	public Character(BufferedImage[] img, float x, float y, float scale) {
		this.vx = this.vy = 0;
		this.x = x;
		this.y = y;
		this.sprite = Display.instance.newSprite(img, x, y, scale);
		this.time = 0;
		this.freeze = 0;
		this.invincible = 0;
	}
	
	public abstract void input();
	public abstract void attack();
	
	public void animate() {
		time ++;
		if(freeze > 0) freeze--;
		if(invincible > 0) invincible--;
		sprite.visible = true;
		if(invincible > 0) {
			if(time<=8) {sprite.frame = 12; sprite.visible = true;}
			else if(time<16) {sprite.visible = false;}
			else if(time<25) {sprite.frame = 13;  sprite.visible = true;}
			else if(time<32) {sprite.visible = false;}
			else {time = 0; sprite.frame = 13;}
		}else if(vy > ACC * 5) {
			if((freeze > 0)) {
				if(time<=8) {sprite.frame = 10;}
				else if(time<16) {sprite.frame = 11;}
				else {time = 0; sprite.frame = 11;}
			}else {
				if(time<=8) {sprite.frame = 8;}
				else if(time<16) {sprite.frame = 9;}
				else {time = 0; sprite.frame = 9;}
			}
		}else if(vy < -ACC * 5) {
			if((freeze>0)) {
				if(time<=8) {sprite.frame = 6;}
				else if(time<16) {sprite.frame = 7;}
				else {time = 0;sprite.frame = 7;}
			}else {
				if(time<=8) {sprite.frame = 4;}
				else if(time<16) {sprite.frame = 5;}
				else {time = 0;sprite.frame = 5;}
			}
		}else {
			if((freeze>0)) {
				if(time<=8) {sprite.frame = 2;}
				else if(time<16) {sprite.frame = 3;}
				else {time = 0;sprite.frame = 3;}
			}else {
				if(time<=8) {sprite.frame = 0;}
				else if(time<16) {sprite.frame = 1;}
				else {time = 0;sprite.frame = 1;}
			}
		}
		sprite.x = this.x;
		sprite.y = this.y;
	}
	
	public void update() {
		input();
		this.x += vx;
		this.y += vy;
		
		if(x > Game.maxX) {
			x = Game.maxX;
			vx = 0;
		}
		if(x < Game.minX) {
			x = Game.minX;
			vx = 0;
		}
		if(y > Game.maxY) {
			y = Game.maxY;
			vy = 0;
		}
		if(y < Game.minY) {
			y = Game.minY;
			vy = 0;
		}
		
		checkCollision();
	}
	
	public void checkCollision() {
		int thisColX1 = (int)x-2, thisColX2 = (int)x+8, thisColY1 = (int)y-8, thisColY2 = (int)y+12;
		for(Danmaku d : Game.instance.danmaku) {
			if(d.isFriendly) continue;
			if(d.x + d.colX < thisColX2 && d.x + d.colX + d.colW > thisColX1) {
				if(d.y + d.colY < thisColY2 && d.y + d.colY + d.colH > thisColY1) {
					hurt();
				}
			}
		}
		for(Enemy e : Game.instance.enemies) {
			if(e.x + e.colX < thisColX2 && e.x + e.colX + e.colW > thisColX1) {
				if(e.y + e.colY < thisColY2 && e.y + e.colY + e.colH > thisColY1) {
					hurt();
				}
			}
		}
	}
	
	public void hurt() {
		if(invincible <= 0) {
			invincible = 150;
			Resource.SFX_PLAYER_HURT.play();
			vy = -1.0f;
			vx = -2.0f;
		}
	}
	
	public static class Korriel extends Character {

		public Korriel() {
			super(Resource.KORRIEL_IMGS, Game.maxX/4, Game.maxY/4, 1);
		}

		@Override
		public void update() {
			super.update();
		}

		@Override
		public void input() {
			if(Display.down) {
				if(vy < MAX_VEL) vy += ACC;
			}else if(Display.up) {
				if(vy > -MAX_VEL) vy -= ACC;
			}else {
				vy = shift(vy);
			}
			if(Display.right) {
				if(vx < MAX_VEL) vx += ACC;
			}else if(Display.left) {
				if(vx > -MAX_VEL) vx -= ACC;
			}else {
				vx = shift(vx);
			}
			if(Display.A || Display.B) {
				if(freeze <= 0 && invincible <= 0)
					attack();
			}
		}

		@Override
		public void attack() {
			freeze = 20;
			Game.instance.shootDanmaku(Resource.KORRIEL_DANMAKU, x+6, y, 4.0f, vy*0.002f, true, 8, -6, -6, 12, 12);
			Resource.SFX_KORRIEL_SHOOT.play();
		}
		
	}
	
	public static class Mariutza extends Character {
		public final static float EASING = 0.05f;

		public Mariutza() {
			super(Resource.MARIUTZA_IMGS, Display.mouseX / Display.RENDER_RATE, Display.mouseY / Display.RENDER_RATE, 1);
		}

		@Override
		public void update() {
			super.update();
		}

		@Override
		public void input() {
			int targetX = Display.mouseX / Display.RENDER_RATE;
			int targetY = Display.mouseY / Display.RENDER_RATE;
			vx = (targetX-x) * EASING;
			vy = (targetY-y) * EASING;
			vx = Math.max(vx, -MAX_VEL);
			vx = Math.min(vx, MAX_VEL);
			vy = Math.max(vy, -MAX_VEL);
			vy = Math.min(vy, MAX_VEL);
			
			if(Display.mouseClicked || Display.mouseRightClicked) {
				if(freeze <= 0 && invincible <= 0)
					attack();
			}
		}

		@Override
		public void attack() {
			freeze = 15;
			Game.instance.shootDanmaku(Resource.MARIUTZA_DANMAKU, x, y, 4.0f, vy*0.002f, true, 8, -6, -6, 12, 12);
			Resource.SFX_MARIUTZA_SHOOT.play();
		}
		
	}
	
	private static float shift(float f) {
		if(f > SFT) {
			f -= SFT;
		}else if(f < -SFT) {
			f += SFT;
		}else {
			f = 0;
		}
		return f;
	}

}
