package sound;

import javafx.util.Duration;

public class Music extends Sound {
	private static Music BGM;
	private static double volume = 1;
	
	public Music(String path) {
		super(path);
		this.player.setOnEndOfMedia(new Runnable() {
	        @Override
	        public void run() {
	        	player.seek(Duration.ZERO);
	        	player.play();
	        }
	    });
	}
	public static void BGM(Music music) {
		if(BGM!=null) {
			BGM.stop();
		}
		BGM = music;
		BGM.player.setVolume(Music.volume);
		BGM.loop();
	}
	public static void setVolume(double d) {
		Music.volume = d;
		if(BGM!=null) {
			BGM.player.setVolume(d);
		}
	}
	public static double getVolume() {
		if(BGM!=null) {
			return BGM.player.getVolume();
		}else {
			return 0;
		}
	}
	
	private void loop() {
		if(BGM!=null) {
			BGM.player.play();
		}
	}
	
	public void stop() {
		this.player.stop();
		this.player.setVolume(1);
	}
	public static double getCurrentVolume() {
		return Music.volume;
	}
	public static void pauseBGM() {
		if(BGM!=null) {
			BGM.pause();
		}
	}
	public static void recover() {
		if(BGM!=null) {
			BGM.player.play();
		}
	}
	public static void mute() {
		if(BGM!=null) {
			BGM.stop();
			BGM = null;
		}
	}

}
