package base;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import sound.Music;
import sound.Sound;

public class Resource {
	/*蓝发女孩(键盘K)资源文件*/
	public final static BufferedImage[] KORRIEL_IMGS = getImage("res/Korriel-sheet.png", 1, 14), KORRIEL_DANMAKU = getImage("res/Star-sheet.png", 1, 4);
	public final static Sound SFX_KORRIEL_SHOOT = new Sound("res/sound/spit.wav");
	/*棕发女孩(鼠标M)资源文件*/
	public final static BufferedImage[] MARIUTZA_IMGS = getImage("res/Mariutza-sheet.png", 1, 14), MARIUTZA_DANMAKU = getImage("res/Fireball-sheet.png", 1, 4);
	public final static Sound SFX_MARIUTZA_SHOOT = new Sound("res/sound/fireball.wav");
	
	/*通用文件*/
	public final static Sound SFX_PLAYER_HURT = new Sound("res/sound/boom.wav"), SFX_EXPLOSION = new Sound("res/sound/super-stomp.wav");
	public final static Sound SFX_BLOCK = new Sound("res/sound/bump.wav");
	public final static BufferedImage[] EXPLOSION_IMGS = getImage("res/Explode-sheet.png", 1, 6), SKY_IMGS = getImage("res/SkyThunder-sheet.png", 1, 6);
	public final static Music MUSIC = new Music("res/Hirada_Kenichi-Cotton_Arcade-Stage_4.mp3");
	
	/*敌人资源文件*/
	public final static BufferedImage[] BAT_IMGS = getImage("res/Bat-sheet.png", 1, 4), FIREROCK_IMGS = getImage("res/FireRock-sheet.png", 1, 4);
	public final static Sound SFX_ENEMY_SHOOT = new Sound("res/sound/burb.wav"), SFX_BULLET = new Sound("res/sound/bullet.wav"), SFX_THUNDER = new Sound("res/sound/thunder.wav");
	public final static BufferedImage[] BULLET_IMGS = getImage("res/Torpedo-sheet.png", 1, 2);
	
	/*读取图片基本方法(路径,分割行数,分割列数)*/
	public static BufferedImage[] getImage(String PATH, int line, int column) {
		BufferedImage[] output = new BufferedImage[line*column];
		BufferedImage source = null;
		try {
			source = ImageIO.read(new File(PATH));
		} catch (IOException e) {
			e.printStackTrace();
		}
		int w = source.getWidth()/column;
		int h = source.getHeight()/line;
		for(int l=0;l<line;l++) {
			int cl = l*column;
			for(int c=0;c<column;c++) {
				output[c+cl] = source.getSubimage(c*w, l*h, w, h);
			}
		}
		return output;
	}

}
