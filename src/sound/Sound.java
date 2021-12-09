package sound;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Sound {
	private Media media;
	protected MediaPlayer player;
	private static double volume = 1;
	public static ArrayList<Sound> sounds;
	public static JFXPanel jfx;
	
	public Sound(String path) {
		File file = new File(path);
		try {
			this.media = new Media(file.toURI().toURL().toExternalForm());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		this.player = new MediaPlayer(media);
        Sound s = this;
		this.player.setOnEndOfMedia(new Runnable() {
			@Override
	        public void run() {
	        	s.stop();
	        }
	    });
	}

	public final void play() {
		sounds.add(this);
		this.player.setVolume(Sound.volume);
		this.player.play();
	}
	
	public void stop() {
		sounds.remove(this);
		this.player.stop();
		this.player.setVolume(1);
	}
	
	public void pause() {
		this.player.pause();
	}
	public final static void pauseCurrentSounds() {
		for(Sound s : Sound.sounds) {
			s.pause();
		}
	}
	public final static void recoverCurrentSounds() {
		for(Sound s : Sound.sounds) {
			s.player.play();
		}
	}
	
	public static void setVolume(double volume) {
		Sound.volume = volume;
		for(int i=0;i<sounds.size();i++) {
			sounds.get(i).player.setVolume(volume);
		}
	}
	public static double getVolume() {
		return Sound.volume;
	}
	
	public static void initJavaFXSound() {
		sounds = new ArrayList<Sound>();
		jfx = new JFXPanel();
		jfx.setEnabled(false);
	}

}
