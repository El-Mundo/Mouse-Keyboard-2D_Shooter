package base;

public class MouseVersusKeyboard {

	public static void main(String args[]) throws InterruptedException {
		/*强制初始化JavaFX环境*/
		sound.Sound.initJavaFXSound();
		new Display();
		new Game();
		while(true) {
			Game.instance.update();
			Display.instance.repaint();
			Thread.sleep(16);
		}
	}

}
