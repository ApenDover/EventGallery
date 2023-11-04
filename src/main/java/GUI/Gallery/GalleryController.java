package GUI.Gallery;

import GUI.Gallery.model.Comparator.ImageViewComparatorByName;
import GUI.Gallery.singleton.ContainerLibrary;
import GUI.Gallery.singleton.RepeatableTimeline;
import GUI.Gallery.utils.LoadAllFiles;
import GUI.Gallery.videoResizer.ScrollSetup;
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
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.rgielen.fxweaver.core.FxmlView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Если открыто следующее окно процесс мониторинга папок завершается.
 * Как вариант, прописать отдельный repeated(), который запускать по нажатию кнопки в GoToImage
 * В нем происходит ресайз новых, удаление старых, актуализация NodeBase, чтобы при загрузке просто
 * подгрузить недостающие, а не начинать делать.
 */

@Component
@FxmlView("Gallery-view.fxml")
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

    ImageViewComparatorByName imageViewComparatorByName = new ImageViewComparatorByName();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ContainerLibrary.getInstance().getSaveImageView().clear();

        ScrollSetup.setup(scroll, colorNumber);
        if (SetupWindowController.isResultBgImageCheck()) {
            mainPane.setBackground(new Background(new BackgroundImage(SetupWindowController.getImageForBackGround(),
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
        }
        LoadAllFiles.load();
        final var actual = new ArrayList<>(ContainerLibrary.getInstance().getResizeableLinkedHashSet()
                .stream().map(resizeable -> resizeable.getResizedImageContainer().getImageView()).toList());
        actual.sort(imageViewComparatorByName);
        actual.forEach(imageView -> galleryPane.getChildren().add(imageView));

        mainPane.requestLayout();
        vBox.requestLayout();
        galleryPane.requestLayout();

        RepeatableTimeline.getInstance(actionEvent -> repeated()).start();

        galleryPane.getChildren().forEach(node -> ContainerLibrary.getInstance().getSaveImageView().add((ImageView) node));

    }


    /**
     * мониторинг папок
     */
    private void repeated() {

        mainPane.requestLayout();
        vBox.requestLayout();
        galleryPane.requestLayout();

        System.out.println(galleryPane.getChildren() + ":" + ContainerLibrary.getInstance().getSaveImageView().size());

        final var beforeReload = new ArrayList<>(ContainerLibrary.getInstance().getResizeableLinkedHashSet()
                .stream().map(resizeable -> resizeable.getResizedImageContainer().getImageView()).toList());

        final var removed = new ArrayList<>(beforeReload);

        LoadAllFiles.load();

        final var actual = new ArrayList<>(ContainerLibrary.getInstance().getResizeableLinkedHashSet()
                .stream().map(resizeable -> resizeable.getResizedImageContainer().getImageView()).toList());

        final var added = new ArrayList<>(actual);

        added.removeAll(beforeReload);
        removed.removeAll(actual);

        /**
         *  Если добавились файлы, то определяем конкретные и их тип,
         *  а далее отправляем на ресайз + сразу сделаем им imageView
         * */
        if (!added.isEmpty()) {
            System.out.println("added " + added.size());
            added.forEach(imageView -> galleryPane.getChildren().add(imageView));  //добавляем ноды в галлерею
            galleryPane.requestLayout();
        }
        if (!removed.isEmpty()) {
            System.out.println("removed " + removed.size());
            removed.forEach(imageView -> galleryPane.getChildren().remove(imageView));  //добавляем ноды в галлерею
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

        galleryPane.getChildren().forEach(node -> ContainerLibrary.getInstance().getSaveImageView().add((ImageView) node));

    }

}