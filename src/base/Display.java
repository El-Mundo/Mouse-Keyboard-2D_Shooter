package base;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import sound.Music;
import sound.Sound;

public class Display extends JPanel {
	private static JFrame window;
	private static final long serialVersionUID = 1L;
	private static final Color COLOR_TRANSPARENT = new Color(255, 255, 255, 1);
	public static int windowWidth = 1920, windowHeight = 1080;
	public final static int RENDER_RATE = 4;
	public static Display instance;
	
	public ArrayList<Sprite> sprites, vfx;
	public int bkgOpac = 0, bkgOpSpd = -5;
	boolean pWheelClicked = false, pStartPressed = false;

	public Display() {
		//Set display area as full screen
		Toolkit tok = Toolkit.getDefaultToolkit();
		Dimension screenSize = tok.getScreenSize();
		windowWidth = screenSize.width;
		windowHeight = screenSize.height;
		this.setSize(windowWidth, windowHeight);
		
		window = new JFrame();
		window.setUndecorated(true);
		window.setSize(windowWidth, windowHeight);
		window.setAlwaysOnTop(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLocationRelativeTo(null);
		window.setBackground(COLOR_TRANSPARENT);
		this.setBackground(COLOR_TRANSPARENT);
		this.setOpaque(false);
		//window.getGraphicsConfiguration().getDevice().setFullScreenWindow(window);
		window.add(this);
		window.addKeyListener(keyboard);
		window.addMouseListener(mouseClick);
		window.addMouseMotionListener(mouseLocation);
		window.addMouseWheelListener(mouseWheel);
		window.addFocusListener(focused);
		
		sprites = new ArrayList<Sprite>();
		vfx = new ArrayList<Sprite>();
		window.setVisible(true);
		
		Music.BGM(Resource.MUSIC);
		Music.setVolume(0.3f);
		
		instance = this;
	}
	
	public Sprite newSprite(BufferedImage[] imgArray, float x, float y, float scale) {
		Sprite sprite = new Sprite(imgArray, x, y, scale);
		sprites.add(sprite);
		return sprite;
	}
	
	public Sprite newEffect(BufferedImage[] imgArray, float x, float y, float scale) {
		Sprite sprite = new Sprite(imgArray, x, y, scale);
		vfx.add(sprite);
		return sprite;
	}
	
	@Override
	public void paint(Graphics og) {
		super.paint(og);
		BufferedImage buffer = new BufferedImage(windowWidth/RENDER_RATE, windowHeight/RENDER_RATE, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = buffer.createGraphics();
		
		AffineTransform dTransform = g.getTransform();
		Composite dComposite = g.getComposite();
		
		if(mouseWheelClicked && !pWheelClicked) bkgOpSpd *= -1;
		if(start && !pStartPressed) bkgOpSpd *= -1;
		pWheelClicked = mouseWheelClicked;
		pStartPressed = start;
		bkgOpac += bkgOpSpd;
		bkgOpac = Integer.max(bkgOpac, 0);
		bkgOpac = Integer.min(bkgOpac, 255);
		if(Game.instance != null && bkgOpac > 0) {
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)bkgOpac/255));
			Sprite[] ss = Game.instance.getBackground();
			int i = 0;
			for(Sprite s : ss) {
				s.frame = Game.instance.backgroundFrame;
				s.x -= Game.BKG_VEL;
				if(i != ss.length - 1) {
					g.translate(s.x, s.y);
					g.scale(s.scale, s.scale);
					g.drawImage(s.show(), -s.w/2, -s.h/2, this);
					g.setTransform(dTransform);
				}
				if(s.x < -s.w) {
					s.x += (s.w * s.scale) * (ss.length - 1);
				}
				System.out.print(i+":"+s.x+" ");
				i++;
			}
			System.out.println();
			g.setComposite(dComposite);
		}
		
		for(int i=sprites.size()-1; i>=0; i--) {
			Sprite s = sprites.get(i);
			if(s.isAlive()) {
				if(!s.visible) continue;
				g.translate(s.x, s.y);
				g.scale(s.scale*(s.hFlip?-1:1), s.scale*(s.vFlip?-1:1));
				g.drawImage(s.show(), -s.w/2, -s.h/2, this);
				g.setTransform(dTransform);
			}else {
				sprites.remove(i);
			}
		}
		
		for(int i=vfx.size()-1; i>=0; i--) {
			Sprite s = vfx.get(i);
			if(s.isAlive()) {
				if(!s.visible) continue;
				g.translate(s.x, s.y);
				g.scale(s.scale*(s.hFlip?-1:1), s.scale*(s.vFlip?-1:1));
				g.drawImage(s.show(), -s.w/2, -s.h/2, this);
				g.setTransform(dTransform);
			}else {
				vfx.remove(i);
			}
		}
		
		og.drawImage(buffer, 0, 0, windowWidth, windowHeight, this);
		g.dispose();
		buffer.flush();
	}
	
	/*按键判定用开关*/
	public static boolean up = false;
	public static boolean down = false;
	public static boolean left = false;
	public static boolean right = false;
	public static boolean select = false;
	public static boolean start = false;
	public static boolean A = false;
	public static boolean B = false;
	/*对应按键设置*/
	public static int upButton = KeyEvent.VK_W;
	public static int downButton = KeyEvent.VK_S;
	public static int leftButton = KeyEvent.VK_A;
	public static int rightButton = KeyEvent.VK_D;
	public static int selectButton = KeyEvent.VK_SPACE;
	public static int startButton = KeyEvent.VK_ENTER;
	public static int AButton = KeyEvent.VK_J;
	public static int BButton = KeyEvent.VK_SPACE;
	
	/*键盘监听*/
	public static KeyListener keyboard = new KeyListener() {
		@Override
		public void keyTyped(KeyEvent e) {
			
		}
		@Override
		public void keyPressed(KeyEvent e) {
			int k = e.getKeyCode();
			if(k==upButton) {
				up = true;
			}
			if(k==downButton) {
				down = true;
			}
			if(k==leftButton) {
				left = true;
			}
			if(k==rightButton) {
				right = true;
			}
			if(k==AButton) {
				A = true;
			}
			if(k==BButton) {
				B = true;
			}
			if(k==startButton) {
				start = true;
			}
			if(k==selectButton) {
				select = true;
			}
		}
		@Override
		public void keyReleased(KeyEvent e) {
			int k = e.getKeyCode();
			if(k==upButton) {
				up = false;
			}
			if(k==downButton) {
				down = false;
			}
			if(k==leftButton) {
				left = false;
			}
			if(k==rightButton) {
				right = false;
			}
			if(k==AButton) {
				A = false;
			}
			if(k==BButton) {
				B = false;
			}
			if(k==startButton) {
				start = false;
			}
			if(k==selectButton) {
				select = false;
			}
			if(k==KeyEvent.VK_ESCAPE) {
				System.exit(1);
			}
		}
	};
	/*鼠标点击*/
	public static boolean mouseClicked = false;
	public static boolean mouseRightClicked = false;
	public static boolean mouseWheelClicked = false;
	/*鼠标监听*/
	public static MouseListener mouseClick = new MouseListener() {
		@Override
		public void mouseClicked(MouseEvent e) {}

		@Override
		public void mousePressed(MouseEvent e) {
			int m = e.getButton();
			if(m==MouseEvent.BUTTON1) {
				mouseClicked = true;
			}
			if(m==MouseEvent.BUTTON2) {
				mouseWheelClicked = true;
			}
			if(m==MouseEvent.BUTTON3) {
				mouseRightClicked = true;
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			int m = e.getButton();
			if(m==MouseEvent.BUTTON1) {
				mouseClicked = false;
			}
			if(m==MouseEvent.BUTTON2) {
				mouseWheelClicked = false;
			}
			if(m==MouseEvent.BUTTON3) {
				mouseRightClicked = false;
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {}
		@Override
		public void mouseExited(MouseEvent e) {}
	};
	/*鼠标坐标*/
	public static int mouseX = -20;
	public static int mouseY = -20;
	/*鼠标位置监听*/
	public static MouseMotionListener mouseLocation = new MouseMotionListener() {
		@Override
		public void mouseDragged(MouseEvent e) {
			mouseX = (int) e.getX();
			mouseY = (int) e.getY();
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			mouseX = (int) e.getX();
			mouseY = (int) e.getY();
		}
	};
	public static MouseWheelListener mouseWheel = new MouseWheelListener() {
		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			System.out.println(e.getWheelRotation());
		}
	};
	private static FocusListener focused = new FocusListener() {
		@Override
		public void focusGained(FocusEvent e) {
			Music.recover();
			Sound.recoverCurrentSounds();
		}
		
		@Override
		public void focusLost(FocusEvent e) {
			up = false;
			down = false;
			left = false;
			right = false;
			A = false;
			B = false;
			start = false;
			select = false;
			mouseClicked = false;
			mouseWheelClicked = false;
			mouseRightClicked = false;
			Music.pauseBGM();
			Sound.pauseCurrentSounds();
		}
	};
	
	public void getFocus() {
		window.requestFocus();
		this.requestFocus();
	}

}
