package GUI.Gallery.model;

import GUI.Gallery.OpenWindow;
import GUI.Gallery.singleton.ContainerLibrary;
import GUI.Gallery.singleton.LinkTransfer;
import GUI.Gallery.singleton.SettingsLoader;
import GUI.Gallery.singleton.StageContainer;
import GUI.Gallery.utils.FileStringConverter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Objects;


@Getter
@Setter
public class ResizedImageContainer extends AbstractContainer {

    // resizable
    private AbstractContainer originalContainer;

    private Image image;

    private ImageView imageView;

    public ResizedImageContainer(File file, AbstractContainer originalContainer) {
        super(file, file.getAbsolutePath(), FileStringConverter.getName(file), FileStringConverter.getExtension(file));
        this.originalContainer = originalContainer;
        try {
            image = new Image(new FileInputStream(getFile()));
            imageView = createImageView();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isOriginalAlive() {
        if (Objects.isNull(originalContainer)) {
            return false;
        }
        return originalContainer.getFile().exists();
    }

    public boolean isAlive() {
        return getFile().exists();
    }

    private ImageView createImageView() {
        final var iv = new ImageView();
        iv.setImage(image);
        iv.setId(FileStringConverter.getFullNameFromPath(originalContainer.getPath()));
        double width = Integer.parseInt(SettingsLoader.getInstance().getResizeQuality());
        double height = (originalContainer.getHeight() * width) / originalContainer.getWidth();
        iv.setFitWidth(width);
        iv.setFitHeight(height);
        iv.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            LinkTransfer.getInstance().setResizeable((Resizeable) originalContainer);
            final var root = OpenWindow.open("ImageMedia-view.fxml");
            StageContainer.getInstance().getStage().centerOnScreen();
            StageContainer.getInstance().getStage().getScene().setRoot(root);
            ContainerLibrary.getInstance().getFiveSecondsWonder().stop();
        });
        return iv;
    }


}
