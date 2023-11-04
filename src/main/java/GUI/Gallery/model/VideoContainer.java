package GUI.Gallery.model;

import GUI.Gallery.singleton.SettingsLoader;
import GUI.Gallery.utils.FileStringConverter;
import GUI.Gallery.utils.videoResizer.VideoResizerJpg;
import javafx.scene.Node;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.Objects;

@Getter
@Setter
public class VideoContainer extends AbstractContainer implements Resizeable {

    private ResizedImageContainer resized;

    @Getter
    private MediaView mediaView;

    public VideoContainer(String path) {
        super(new File(path), path, FileStringConverter.getName(path), FileStringConverter.getExtension(path));
        createResizePreview();
        final var media = new Media(path);
        final var mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaView = new MediaView();
        mediaView.setFitWidth(1200);
        mediaView.setFitHeight(800);
        mediaView.setMediaPlayer(mediaPlayer);
        mediaView.setId(FileStringConverter.getFullNameFromPath(path));
        setWidth(media.getWidth());
        setHeight(media.getHeight());
    }

    public VideoContainer(File file) {
        super(file, file.getAbsolutePath(), FileStringConverter.getName(file), FileStringConverter.getExtension(file));
        createResizePreview();
        final var media = new Media(file.toURI().toString());
        final var mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaView = new MediaView();
        mediaView.setFitWidth(1200);
        mediaView.setFitHeight(800);
        mediaView.setMediaPlayer(mediaPlayer);
        mediaView.setId(FileStringConverter.getFullNameFromPath(file.getAbsolutePath()));
        setWidth(media.getWidth());
        setHeight(media.getHeight());
    }

    @Override
    public ResizedImageContainer getResizedImageContainer() {
        return resized;
    }

    public boolean isResized() {
        if (Objects.isNull(resized)) {
            return false;
        }
        return resized.getFile().exists();
    }

    @Override
    public void createResizePreview() {
        if (!isResized()) {
            resized = new ResizedImageContainer(VideoResizerJpg.getImageFromVideo(this.getFile(),
                    Integer.parseInt(SettingsLoader.getInstance().getResizeQuality()),
                    true), this);
        }
    }

    @Override
    public boolean isAlive() {
        return getFile().exists();
    }

    public ResizedImageContainer getResized() {
        createResizePreview();
        return resized;
    }


    @Override
    public Node getNode() {
        return mediaView;
    }

}
