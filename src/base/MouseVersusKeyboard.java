package base;

public class MouseVersusKeyboard {

	public static void main(String args[]) throws InterruptedException {
		/*ǿ�Ƴ�ʼ��JavaFX����*/
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
