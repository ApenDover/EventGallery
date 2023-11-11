package gui.gallery;

import gui.gallery.runnable.LoadProcessor;
import gui.gallery.singleton.ExecutorServiceSingleton;
import gui.gallery.utils.LoadAllFiles;
import gui.gallery.utils.ScrollSetup;
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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.Future;

@Slf4j
@RequiredArgsConstructor
public class GalleryController implements Initializable {

    @Setter
    private static String colorNumber = StringUtils.EMPTY;

    private static final LoadProcessor LOAD_PROCESSOR = new LoadProcessor();

    private Future<?> dataReadFuture;

    @FXML
    private Pane mainPane;

    @FXML
    private ScrollPane scroll;

    @FXML
    private TilePane galleryPane;

    @FXML
    private VBox vBox;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        log.debug(LocalTime.now() + " : " + galleryPane.getChildren().size());

        ScrollSetup.setup(scroll, colorNumber);
        if (SetupWindowController.isResultBgImageCheck()) {
            mainPane.setBackground(new Background(new BackgroundImage(SetupWindowController.getImageForBackGround(),
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
        }

        final var actual = new ArrayList<>(ContainerLibrary.getInstance().getResizeableImageViewList());
        addImageViewToGallery(actual);

        if (!ContainerLibrary.getInstance().isFirstLoad()) {
            log.debug("first load, start first load source");
            dataReadFuture = ExecutorServiceSingleton.getInstance().getExecutorService().submit(LOAD_PROCESSOR);
        }

        /**
         *  запускаем поток мониторинга папки каждые N секунд
         *  */
        final var fiveSecondsWonder = new Timeline(
                new KeyFrame(Duration.seconds(2),
                        event -> repeated()));
        fiveSecondsWonder.setCycleCount(Animation.INDEFINITE);
        fiveSecondsWonder.play();
        ContainerLibrary.getInstance().setFiveSecondsWonder(fiveSecondsWonder);

        /**
         * Слушаем появление новых превью и правильно их распологаем
         * */
        listen();
    }


    /**
     * мониторинг папок
     */
    private void repeated() {
        try {
            if (Objects.isNull(dataReadFuture) || dataReadFuture.isDone()) {
                log.debug("executor is off, start to search changes in source folder..");
                if (galleryPane.getChildren().isEmpty()) {
                    final var actual = new ArrayList<>(ContainerLibrary.getInstance().getResizeableImageViewList());
                    addImageViewToGallery(actual);
                }
                log.debug(LocalTime.now() + " : " + galleryPane.getChildren().size());
                final var beforeReload = new ArrayList<>(ContainerLibrary.getInstance().getResizeableImageViewList());
                LoadAllFiles.load();
                final var actual = new ArrayList<>(ContainerLibrary.getInstance().getResizeableImageViewList());
                final var added = new ArrayList<>(actual);
                final var removed = new ArrayList<>(beforeReload);
                added.removeAll(beforeReload);
                removed.removeAll(actual);
                if (!added.isEmpty()) {
                    log.info(LocalTime.now() + " added - " + added.size());
                    addImageViewToGallery(added);
                }
                if (!removed.isEmpty()) {
                    log.info(LocalTime.now() + " removed - " + removed.size());
                    galleryPane.getChildren().removeAll(removed);
                    galleryPane.requestLayout();
                }
                listen();
            } else {
                log.debug("executor in a progress..");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    private void addImageViewToGallery(ArrayList<ImageView> actual) {
        if (SettingsLoader.getInstance().isNewUp()) {
            galleryPane.getChildren().addAll(0, actual);
        } else {
            galleryPane.getChildren().addAll(actual);
        }
        galleryPane.requestLayout();
    }

    private void listen() {
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

}
