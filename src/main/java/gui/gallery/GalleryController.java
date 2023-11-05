package gui.gallery;

import gui.gallery.utils.LoadAllFiles;
import gui.gallery.utils.ScrollSetup;
import gui.gallery.model.Comparator.ImageViewComparatorIfNeed;
import gui.gallery.singleton.ContainerLibrary;
import gui.gallery.singleton.SettingsLoader;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

/**
 * Если открыто следующее окно процесс мониторинга папок завершается.
 * Как вариант, прописать отдельный repeated(), который запускать по нажатию кнопки в GoToImage
 * В нем происходит ресайз новых, удаление старых, актуализация NodeBase, чтобы при загрузке просто
 * подгрузить недостающие, а не начинать делать.
 */

@RequiredArgsConstructor
public class GalleryController implements Initializable {

    @Setter
    private static String colorNumber = StringUtils.EMPTY;

    @FXML
    private Pane mainPane;

    @FXML
    private ScrollPane scroll;

    @FXML
    private TilePane galleryPane;

    @FXML
    private VBox vBox;

    private static final ImageViewComparatorIfNeed IMAGE_VIEW_COMPARATOR_IF_NEED = new ImageViewComparatorIfNeed();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        System.out.println(LocalTime.now() + " : " + galleryPane.getChildren().size());

        ScrollSetup.setup(scroll, colorNumber);
        if (SetupWindowController.isResultBgImageCheck()) {
            mainPane.setBackground(new Background(new BackgroundImage(SetupWindowController.getImageForBackGround(),
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
        }

        LoadAllFiles.load();

        final var actual = new ArrayList<>(ContainerLibrary.getInstance().getResizeableImageViewList());
        addImageViewToGallery(actual);

        /**
         *  запускаем поток мониторинга папки каждые N секунд
         *  */
        final var fiveSecondsWonder = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        event -> repeated()));
        fiveSecondsWonder.setCycleCount(Animation.INDEFINITE);
        fiveSecondsWonder.play();
        ContainerLibrary.getInstance().setFiveSecondsWonder(fiveSecondsWonder);
    }


    /**
     * мониторинг папок
     */
    private void repeated() {

        System.out.println(LocalTime.now() + " : " + galleryPane.getChildren().size());

        final var beforeReload = new ArrayList<>(ContainerLibrary.getInstance().getResizeableImageViewList());

        LoadAllFiles.load();

        final var actual = new ArrayList<>(ContainerLibrary.getInstance().getResizeableImageViewList());

        final var added = new ArrayList<>(actual);
        final var removed = new ArrayList<>(beforeReload);

        added.removeAll(beforeReload);
        removed.removeAll(actual);

        if (!added.isEmpty()) {
            System.out.println(LocalTime.now() + " added " + added.size());
            addImageViewToGallery(added);
        }
        if (!removed.isEmpty()) {
            System.out.println(LocalTime.now() + " removed " + removed.size());
            galleryPane.getChildren().removeAll(removed);
            galleryPane.requestLayout();
        }

        /**
         * Слушаем появление новых превью и правильно их распологаем
         * */
        vBox.widthProperty().addListener((o, oldValue, newValue) -> {
            double maxWidth = galleryPane.getChildren()
                    .stream()
                    .filter(ImageView.class::isInstance)
                    .map(n -> ((ImageView) n).getFitWidth())
                    .max(Double::compareTo)
                    .orElse(0d);
            galleryPane.setPrefColumns(Math.min(galleryPane.getChildren().size(),
                    Math.max(1, (int) (newValue.doubleValue() / (maxWidth + galleryPane.getHgap())))));
        });

    }

    private void addImageViewToGallery(ArrayList<ImageView> actual) {
        actual.sort(IMAGE_VIEW_COMPARATOR_IF_NEED);
        if (!SettingsLoader.getInstance().isByName() && SettingsLoader.getInstance().isNewUp()) {
            Collections.reverse(actual);
            galleryPane.getChildren().addAll(0, actual);
        } else {
            galleryPane.getChildren().addAll(actual);
        }
        galleryPane.requestLayout();
    }

}
