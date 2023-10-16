package GUI.Gallery.imageViewProcess;

import GUI.Gallery.setUp.SettingsLoader;
import GUI.Gallery.utils.FileStringConverter;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.File;
import java.net.MalformedURLException;

public class MovieBuilder {

    public MediaView createMovie(String file) {
        File mediaFile = FileStringConverter.getFile(SettingsLoader.getSourceFolder(), file);
        Media media;
        try {
            media = new Media(mediaFile.toURI().toURL().toString());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        final var mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        MediaView mediaView = new MediaView();
        mediaView.setFitWidth(1200);
        mediaView.setFitHeight(800);
        mediaView.setMediaPlayer(mediaPlayer);
        mediaView.setId(file);
        mediaPlayer.play();
        return mediaView;
    }

}
