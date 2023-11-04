package GUI.Gallery;

import GUI.Gallery.model.Comparator.ImageViewComparatorifNeed;
import GUI.Gallery.singleton.ContainerLibrary;
import GUI.Gallery.utils.LoadAllFiles;
import GUI.Gallery.utils.ScrollSetup;
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
//import net.rgielen.fxweaver.core.FxmlView;
import org.apache.commons.lang3.StringUtils;
//import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ArrayList;
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

    ImageViewComparatorifNeed imageViewComparatorifNeed = new ImageViewComparatorifNeed();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ScrollSetup.setup(scroll, colorNumber);
        if (SetupWindowController.isResultBgImageCheck()) {
            mainPane.setBackground(new Background(new BackgroundImage(SetupWindowController.getImageForBackGround(),
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
        }

        LoadAllFiles.load();

        final var actual = new ArrayList<>(ContainerLibrary.getInstance().getResizeableImageViewList());
        actual.sort(imageViewComparatorifNeed);
        galleryPane.getChildren().addAll(actual);

        mainPane.requestLayout();
        vBox.requestLayout();
        galleryPane.requestLayout();

        /**
         *  запускаем поток мониторинга папки каждые N секунд
         *  */
        final var fiveSecondsWonder = new Timeline(
                new KeyFrame(Duration.seconds(3),
                        event -> repeated()));
        fiveSecondsWonder.setCycleCount(Animation.INDEFINITE);
        fiveSecondsWonder.play();
        ContainerLibrary.getInstance().setFiveSecondsWonder(fiveSecondsWonder);
    }


    /**
     * мониторинг папок
     */
    private void repeated() {

        final var beforeReload = new ArrayList<>(ContainerLibrary.getInstance().getResizeableImageViewList());

        LoadAllFiles.load();

        final var actual = new ArrayList<>(ContainerLibrary.getInstance().getResizeableImageViewList());

        final var added = new ArrayList<>(actual);
        final var removed = new ArrayList<>(beforeReload);

        added.removeAll(beforeReload);
        removed.removeAll(actual);

        if (!added.isEmpty()) {
            System.out.println("added " + added.size());
            galleryPane.getChildren().addAll(added);
            galleryPane.requestLayout();
        }
        if (!removed.isEmpty()) {
            System.out.println("removed " + removed.size());
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

}