package GUI.Gallery;

import GUI.Gallery.imageResizer.ImgScaleProcessor;
import GUI.Gallery.setUp.SettingsLoader;
import GUI.Gallery.storage.FileViewBase;
import GUI.Gallery.storage.LinkTransfer;
import GUI.Gallery.storage.NodeBase;
import GUI.Gallery.storage.StageContainer;
import GUI.Gallery.utils.FileStringConverter;
import GUI.Gallery.videoResizer.ScrollSetup;
import GUI.Gallery.videoResizer.VideoResizerJpg;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.util.Duration;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.rgielen.fxweaver.core.FxmlView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.ResourceBundle;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

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
    private VBox vBox;

    @FXML
    private TilePane galleryPane;

    private Parent root;

    static Timeline fiveSecondsWonder;

    /**
     * Создаем новые ImageView - плашки
     */
    private void createImageView(String fileName) {
        String cubeImagePath = FileStringConverter.getFilePath(SettingsLoader.getInstance().getQualityResizeFolder(), fileName, "jpg");
        String exFile = FileViewBase.getInstance().getFileNamesMap().get(fileName);
        String filePath = FileStringConverter.getFilePath(SettingsLoader.getInstance().getSourceFolder(), fileName, exFile);

        double fileW = 0;
        double fileH = 0;

//      нужны размеры оригинального изображения чтобы создать плитку нужного размера
        if (FileViewBase.getInstance().getImgExtension().contains(exFile)) {
            try {
                Image imageOriginal = new Image(new FileInputStream(filePath));
                fileW = imageOriginal.getWidth();
                fileH = imageOriginal.getHeight();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        if (FileViewBase.getInstance().getMovieExtension().contains(exFile)) {
            File mediaFile = new File(filePath);
            try {
                Media media = new Media(mediaFile.toURI().toURL().toString());
                fileW = media.getWidth();
                fileH = media.getHeight();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }

        ImageView imageView = new ImageView();
        Image imageCube;
        try {
            imageCube = new Image(new FileInputStream(cubeImagePath));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        imageView.setImage(imageCube);
        imageView.setId(fileName + "." + exFile);
        double width = Integer.parseInt(SettingsLoader.getInstance().getQualityResizer());
        double height = (fileH * width) / fileW;
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            LinkTransfer.getInstance().setLink(imageView.getId());
            root = OpenWindow.open("ImageMedia-view.fxml");
            StageContainer.getInstance().getStage().centerOnScreen();
            StageContainer.getInstance().getStage().getScene().setRoot(root);
            fiveSecondsWonder.stop();
        });
        if (SettingsLoader.getInstance().isByAddTime()) {
            AtomicBoolean k = new AtomicBoolean(true);
            NodeBase.getInstance().getImageViewLinkedHashContainer().forEach(i -> {
                if (i.getId().equals(imageView.getId())) {
                    k.set(false);
                }
            });
            if (k.get()) {
                NodeBase.getInstance().getImageViewLinkedHashContainer().add(imageView);
            }
        } else {
            NodeBase.getInstance().getImageViewTreeContainer().add(imageView);
        }
    }

    /**
     * мониторинг папок
     */
    private void repeated() {

        File dirResize = new File(SettingsLoader.getInstance().getQualityResizeFolder());
        dirResize.mkdir();
        FileViewBase.getInstance().init();

        /**
         *  Если добавились файлы, то определяем конкретные и их тип,
         *  а далее отправляем на ресайз + сразу сделаем им imageView
         * */
        if ((FileViewBase.getInstance().getAllMovieOriginalFilePath().size() + FileViewBase.getInstance().getAllImgOriginalFilePath().size()) > FileViewBase.getInstance().getAllNamesPreviewResized().size()) {

            TreeSet<String> namesWithoutResize = new TreeSet<>(FileViewBase.getInstance().getAllOriginalFileNames());
            namesWithoutResize.removeAll(FileViewBase.getInstance().getAllNamesPreviewResized());
            TreeSet<File> filesImageWithoutResize = new TreeSet<>();
            TreeSet<File> filesMovieWithoutResize = new TreeSet<>();
            namesWithoutResize.forEach(s -> {
                String ex = FileViewBase.getInstance().getFileNamesMap().get(s);
                if (FileViewBase.getInstance().getMovieExtension().contains(ex)) {
                    filesMovieWithoutResize.add(FileStringConverter.getFile(SettingsLoader.getInstance().getSourceFolder(), s, ex));
                }
                if (FileViewBase.getInstance().getImgExtension().contains(ex)) {
                    filesImageWithoutResize.add(FileStringConverter.getFile(SettingsLoader.getInstance().getSourceFolder(), s, ex));
                }
            });
            if (!filesImageWithoutResize.isEmpty()) {
                ImgScaleProcessor.scale(filesImageWithoutResize);
            }
            if (!filesMovieWithoutResize.isEmpty()) {
                VideoResizerJpg.getImageFromVideo(filesMovieWithoutResize, Integer.parseInt(SettingsLoader.getInstance().getQualityResizer()), true);
            }
            namesWithoutResize.forEach(this::createImageView);

            /**
             * берем эти новые , находим новые ноды и создаем плашки
             **/
            if (SettingsLoader.getInstance().isByAddTime() && SettingsLoader.getInstance().isNewDown()) {
                NodeBase.getInstance().getImageViewLinkedHashContainer().forEach(imageView -> {  //для каждой ноды
                    namesWithoutResize.forEach(s -> {  // перебираем список новых имен
                        String ex = FileViewBase.getInstance().getFileNamesMap().get(s); // у имени находим расширение
                        String fileName = FileStringConverter.getNameWithEx(s, ex); //и сам файл
                        if (fileName.equals(imageView.getId())) // если имя совпало с ID ноды
                        {
                            galleryPane.getChildren().add(imageView); //добавляем ноду в галлерею
                        }
                    });
                });
                galleryPane.requestLayout();
            }
            if (SettingsLoader.getInstance().isByAddTime() && SettingsLoader.getInstance().isNewUp()) {
                ArrayList<Node> listFromLinkedHashForReverse = new ArrayList<>(NodeBase.getInstance().getImageViewLinkedHashContainer());
                Collections.reverse(listFromLinkedHashForReverse);
                LinkedHashSet<Node> linkedHashImageViewReverse = new LinkedHashSet<>(listFromLinkedHashForReverse);
                linkedHashImageViewReverse.forEach(imageView -> namesWithoutResize.forEach(s -> {
                    String ex = FileViewBase.getInstance().getFileNamesMap().get(s);
                    if (FileStringConverter.getNameWithEx(s, ex).equals(imageView.getId())) {
                        if (Main.start) {
                            galleryPane.getChildren().add(imageView);
                        } else {
                            galleryPane.getChildren().add(0, imageView);  // решить косяк с первой загрузкой
                        }
                    }
                }));
                Main.start = false;
                galleryPane.requestLayout();
            }
            if (SettingsLoader.getInstance().isByName() && (SettingsLoader.getInstance().isNewUp() || SettingsLoader.getInstance().isNewDown())) {
                AtomicInteger i = new AtomicInteger();
                i.set(0);
                NodeBase.getInstance().getImageViewTreeContainer().forEach(imageView -> {
                    i.incrementAndGet();
                    namesWithoutResize.forEach(s -> {
                        String ex = FileViewBase.getInstance().getFileNamesMap().get(s);
                        String fileName = FileStringConverter.getNameWithEx(s, ex);
                        if (fileName.equals(imageView.getId())) {
                            galleryPane.getChildren().add(i.get() - 1, imageView);
                        }
                    });
                });
                galleryPane.requestLayout();
            }
        }

        /**
         *  Если удалили из источника какой-то файл находим конкретный,
         *  удаляем ресайз и плашку
         * */
        if (FileViewBase.getInstance().getAllOriginalFileNames().size() < FileViewBase.getInstance().getAllNamesPreviewResized().size()) {
            TreeSet<String> deletedFilesName = new TreeSet<>(FileViewBase.getInstance().getAllNamesPreviewResized());
            ArrayList<ImageView> nodeForDelete = new ArrayList<>();
            deletedFilesName.removeAll(FileViewBase.getInstance().getAllOriginalFileNames());
            deletedFilesName.forEach(s -> {
                FileStringConverter.getFile(SettingsLoader.getInstance().getQualityResizeFolder(), s, "jpg").delete();
                galleryPane.getChildren().forEach(node -> {
                    if (node.getId().startsWith(s)) {
                        nodeForDelete.add((ImageView) node);
                    }
                });
            });
            galleryPane.getChildren().removeAll(nodeForDelete);
            nodeForDelete.forEach(NodeBase.getInstance().getImageViewTreeContainer()::remove);
            nodeForDelete.forEach(NodeBase.getInstance().getImageViewLinkedHashContainer()::remove);
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ScrollSetup.setup(scroll, colorNumber);

        if (SetupWindowController.isResultBgImageCheck()) {
            mainPane.setBackground(new Background(new BackgroundImage(SetupWindowController.getImageForBackGround(),
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
        }

        File dirResize = new File(SettingsLoader.getInstance().getQualityResizeFolder());
        dirResize.mkdir();
        FileViewBase.getInstance().init();

        /**
         * добавляем плашки при первой загрузке и перезагрузке
         * */
        if (SettingsLoader.getInstance().isByAddTime() && SettingsLoader.getInstance().isNewDown()) {
            if (galleryPane.getChildren().size() < FileViewBase.getInstance().getAllNamesPreviewResized().size()) {
                if (NodeBase.getInstance().getImageViewLinkedHashContainer().size() == FileViewBase.getInstance().getAllNamesPreviewResized().size()) {
                    NodeBase.getInstance().getImageViewLinkedHashContainer().forEach(imageView -> galleryPane.getChildren().add(imageView));
                    galleryPane.requestLayout();
                } else {
                    FileViewBase.getInstance().getAllNamesPreviewResized().forEach(this::createImageView);
                    NodeBase.getInstance().getImageViewLinkedHashContainer().forEach(imageView -> galleryPane.getChildren().add(imageView));
                }
            }

        }
        if (SettingsLoader.getInstance().isByAddTime() && SettingsLoader.getInstance().isNewUp()) {
            ArrayList<Node> setReversed = new ArrayList<>(NodeBase.getInstance().getImageViewLinkedHashContainer());
            Collections.reverse(setReversed);
            if (setReversed.size() == FileViewBase.getInstance().getAllNamesPreviewResized().size()) {
                setReversed.forEach(imageView -> galleryPane.getChildren().add(imageView));
                galleryPane.requestLayout();
            } else {
                FileViewBase.getInstance().getAllNamesPreviewResized().forEach(this::createImageView);
                setReversed.clear();
                setReversed.addAll(NodeBase.getInstance().getImageViewLinkedHashContainer());
                Collections.reverse(setReversed);
                setReversed.forEach(imageView -> galleryPane.getChildren().add(imageView));
                Main.start = false;
            }
            galleryPane.requestLayout();
        }
        if (SettingsLoader.getInstance().isByName() && (galleryPane.getChildren().size() < FileViewBase.getInstance().getAllNamesPreviewResized().size())) {
            if (NodeBase.getInstance().getImageViewTreeContainer().size() == FileViewBase.getInstance().getAllNamesPreviewResized().size()) {
                NodeBase.getInstance().getImageViewTreeContainer().forEach(imageView -> galleryPane.getChildren().add(imageView));
                galleryPane.requestLayout();
            } else {
                FileViewBase.getInstance().getAllNamesPreviewResized().forEach(this::createImageView);
                NodeBase.getInstance().getImageViewTreeContainer().forEach(imageView -> galleryPane.getChildren().add(imageView));
            }
        }

        /**
         *  запускаем поток мониторинга папки каждые N секунд
         *  */
        fiveSecondsWonder = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        event -> {
                            try {
                                repeated();
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }));
        fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
        fiveSecondsWonder.play();
    }

}