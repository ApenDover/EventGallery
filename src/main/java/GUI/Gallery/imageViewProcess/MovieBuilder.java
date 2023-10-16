package GUI.Gallery.imageViewProcess;

import GUI.Gallery.setUp.SettingsLoader;
import GUI.Gallery.storage.LinkTransfer;
import GUI.Gallery.utils.FileStringConverter;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

public class MovieBuilder {


    private final ImageView imageView;

    private MediaView mediaView;

    public MovieBuilder(MediaView mediaView, ImageView imageView) {
        this.imageView = imageView;
        this.mediaView = mediaView;
    }

    private void buildMovie(String file) {
        createMovie(file);
        LinkTransfer.setLink(file);
    }

    public void createMovie(String file) {
        File mediaFile = FileStringConverter.getFile(SettingsLoader.getSourceFolder(), file);
        Media media;
        try {
            media = new Media(mediaFile.toURI().toURL().toString());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        final var difSize = (double) media.getWidth() / media.getHeight();
        final var mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaView = new MediaView();
        if (difSize > 1) {
            imageView.setFitWidth(1200);
            imageView.setFitHeight(1200 / difSize);
        } else {
            imageView.setFitWidth(800 * difSize);
            imageView.setFitHeight(800);
        }
        mediaView.setMediaPlayer(mediaPlayer);
        mediaView.setId(file);
    }

}
