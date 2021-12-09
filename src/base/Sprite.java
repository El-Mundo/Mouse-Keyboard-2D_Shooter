package base;

import java.awt.image.BufferedImage;

public class Sprite {
	private BufferedImage[] sprite;
	//Position
	float x, y;
	float scale;
	int w, h;
	int frame;
	private boolean alive;
	boolean hFlip, vFlip, visible;

	public Sprite(BufferedImage[] imgArray, float x, float y, float scale) {
		this.scale = scale;
		this.x = x;
		this.y = y;
		this.sprite = imgArray;
		this.frame = 0;
		this.alive = true;
		hFlip = vFlip = false;
		visible = true;
		w = imgArray[0].getWidth();
		h = imgArray[0].getHeight();
	}
	
	public BufferedImage show() {
		frame = Integer.min(frame, sprite.length-1);
		frame = Integer.max(frame, 0);
		return sprite[frame];
	}
	
	public void remove() {
		alive = false;
	}
	
	public boolean isAlive() {
		return alive;
	}
	
	public int getFrameTotal() {
		return sprite.length;
	}

}
