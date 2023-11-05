package gui.gallery.model;

import gui.gallery.utils.FileStringConverter;
import gui.gallery.utils.imageResizer.ImgScaleProcessor;
import gui.gallery.singleton.SettingsConst;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Objects;

@Getter
@Setter
public class ImageContainer extends gui.gallery.model.AbstractContainer implements gui.gallery.model.Resizeable {

    private gui.gallery.model.ResizedImageContainer resized;

    private Image image;

    private ImageView imageView;

    public ImageContainer(String path) {
        super(new File(path), path, FileStringConverter.getName(path), FileStringConverter.getExtension(path));
        createResizePreview();
        try {
            image = new Image(new FileInputStream(getFile()));
            setWidth(image.getWidth());
            setHeight(image.getHeight());
            imageView = createImageView();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public ImageContainer(File file) {
        super(file, file.getAbsolutePath(), FileStringConverter.getName(file), FileStringConverter.getExtension(file));
        createResizePreview();
        try {
            image = new Image(new FileInputStream(getFile()));
            setWidth(image.getWidth());
            setHeight(image.getHeight());
            imageView = createImageView();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public boolean isResizedAlive() {
        if (Objects.isNull(resized)) {
            return false;
        }
        return resized.getFile().exists();
    }

    @Override
    public void createResizePreview() {
        if (!isResizedAlive()) {
            resized = new gui.gallery.model.ResizedImageContainer(ImgScaleProcessor.scale(this.getFile()), this);
        }
    }

    @Override
    public gui.gallery.model.ResizedImageContainer getResizedImageContainer() {
        return resized;
    }

    @Override
    public boolean isOriginalAlive() {
        return getFile().exists();
    }

    public gui.gallery.model.ResizedImageContainer getResized() {
        createResizePreview();
        return resized;
    }

    private ImageView createImageView() {
        ImageView iv = new ImageView();
        iv.setImage(image);
        final var difSize = image.getWidth() / image.getHeight();
        if (difSize > 1) {
            iv.setFitWidth(SettingsConst.LONG_SIDE.getValue());
            iv.setFitHeight(SettingsConst.LONG_SIDE.getValue() / difSize);
        } else {
            iv.setFitHeight(SettingsConst.SHOT_SIDE.getValue());
            iv.setFitWidth(SettingsConst.SHOT_SIDE.getValue() * difSize);
        }
        iv.setId(FileStringConverter.getFullNameFromPath(this.getPath()));
        return iv;
    }

    @Override
    public Node getNode() {
        return imageView;
    }

}
