package GUI.Gallery;

import javafx.scene.image.Image;
import javafx.scene.media.Media;
import lombok.Getter;

@Getter
public class ContentForGallery {

    private double width;

    private double height;

    private Image image;

    private Media movie;

    public ContentForGallery(String ImageFilePath) {

        this.width = image.getWidth();
        this.height = image.getHeight();
    }

}
