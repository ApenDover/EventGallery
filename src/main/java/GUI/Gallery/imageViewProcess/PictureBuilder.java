package GUI.Gallery.imageViewProcess;

import GUI.Gallery.setUp.SettingsLoader;
import GUI.Gallery.storage.LinkTransfer;
import GUI.Gallery.utils.FileStringConverter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PictureBuilder {

    private final ImageView imageView;

    public PictureBuilder(ImageView imageView) {
        this.imageView = imageView;
    }

    public void buildImageView(String file) {
        createPictureImageView(file);
        LinkTransfer.setLink(file);
    }

    public void createPictureImageView(String file) {
        Image image = new Image(FileStringConverter.getFileInputString(SettingsLoader.getSourceFolder(), file));
        imageView.setImage(image);
        final var difSize = image.getWidth() / image.getHeight();
        if (difSize > 1) {
            imageView.setFitWidth(1200);
            imageView.setFitHeight(1200 / difSize);
        } else {
            imageView.setFitWidth(800 * difSize);
            imageView.setFitHeight(800);
        }
        imageView.setId(file);
    }

}
