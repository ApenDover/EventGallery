package GUI.Gallery;

import GUI.Gallery.imageResizer.ImgScaleProcessor;
import GUI.Gallery.setUp.SettingsLoader;
import GUI.Gallery.storage.FileViewBase;
import GUI.Gallery.storage.LinkTransfer;
import GUI.Gallery.storage.NodeBase;
import GUI.Gallery.storage.StageContainer;
import GUI.Gallery.utils.FileStringConverter;
import GUI.Gallery.videoResizer.VideoResizerJpg;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
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
import javafx.stage.Screen;
import javafx.util.Duration;
import lombok.Setter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
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
public class GalleryController implements Initializable {

    @Setter
    private static String colorNumber = "";

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
     * Переход в конкретную картинку
     */
    private void goToImage() {
        try {
            root = FXMLLoader.load(getClass().getResource("ImageMedia-view.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        StageContainer.getStage().centerOnScreen();
        StageContainer.getStage().getScene().setRoot(root);
        fiveSecondsWonder.stop();
    }

    /**
     * Создаем новые ImageView - плашки
     */
    private void createImageView(String fileName) {
        String plitkaPath = FileStringConverter.getFilePath(SettingsLoader.getQualityResizeFolder(), fileName, "jpg");
        String exFile = FileViewBase.getFileNamesMap().get(fileName);
        String filePath = FileStringConverter.getFilePath(SettingsLoader.getSourceFolder(), fileName, exFile);

        double fileW = 0;
        double fileH = 0;

//      нужны размеры оригинального изображения чтобы создать плитку нужного размера
        if (FileViewBase.getImgExtension().contains(exFile)) {
            Image imageOriginal = null;
            try {
                imageOriginal = new Image(new FileInputStream(filePath));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            fileW = imageOriginal.getWidth();
            fileH = imageOriginal.getHeight();
        }
        if (FileViewBase.getMovieExtension().contains(exFile)) {
            File mediaFile = new File(filePath);
            Media media;
            try {
                media = new Media(mediaFile.toURI().toURL().toString());
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            fileW = media.getWidth();
            fileH = media.getHeight();
        }

//      создаем плитку
        ImageView imageView = new ImageView();
        Image imagePlitka;
        try {
            imagePlitka = new Image(new FileInputStream(plitkaPath));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        imageView.setImage(imagePlitka);
        imageView.setId(fileName + "." + exFile);
        double width = Integer.parseInt(SettingsLoader.getQualityResizer());
        double height = (fileH * width) / fileW;
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            LinkTransfer.setLink(imageView.getId());
            goToImage();
        });
        if (SettingsLoader.isByAddTime()) {
            AtomicBoolean k = new AtomicBoolean(true);
            NodeBase.getImageViewLinkedHashContainer().forEach(i -> {
                if (i.getId().equals(imageView.getId())) {
                    k.set(false);
                }
            });
            if (k.get()) {
                NodeBase.getImageViewLinkedHashContainer().add(imageView);
            }
        } else {
            NodeBase.getImageViewTreeContainer().add(imageView);
        }
    }

    /**
     * мониторинг папок
     */
    private void repeated() {

        File dirResize = new File(SettingsLoader.getQualityResizeFolder());
        dirResize.mkdir();
        FileViewBase.init();

        /**
         *  Если добавились файлы, то определяем конкретные и их тип,
         *  а далее отправляем на ресайз + сразу сделаем им imageView
         * */
        if ((FileViewBase.getAllMovieOriginalFilePath().size() + FileViewBase.getAllImgOriginalFilePath().size()) > FileViewBase.getAllNamesPreviewResized().size()) {

            TreeSet<String> namesWithoutResize = new TreeSet<>(FileViewBase.getAllOriginalFileNames());
            namesWithoutResize.removeAll(FileViewBase.getAllNamesPreviewResized());
            TreeSet<File> filesImageWithoutResize = new TreeSet<>();
            TreeSet<File> filesMovieWithoutResize = new TreeSet<>();
            namesWithoutResize.forEach(s -> {
                String ex = FileViewBase.getFileNamesMap().get(s);
                if (FileViewBase.getMovieExtension().contains(ex)) {
                    filesMovieWithoutResize.add(FileStringConverter.getFile(SettingsLoader.getSourceFolder(), s ,ex));
                }
                if (FileViewBase.getImgExtension().contains(ex)) {
                    filesImageWithoutResize.add(FileStringConverter.getFile(SettingsLoader.getSourceFolder(), s ,ex));
                }
            });
            if (!filesImageWithoutResize.isEmpty()) {
                ImgScaleProcessor.scale(filesImageWithoutResize);
            }
            if (!filesMovieWithoutResize.isEmpty()) {
                VideoResizerJpg.getImageFromVideo(filesMovieWithoutResize, Integer.parseInt(SettingsLoader.getQualityResizer()), true);
            }
            namesWithoutResize.forEach(this::createImageView);

            /**
             * берем эти новые , находим новые ноды и создаем плашки
             * */
            if ((SettingsLoader.isByAddTime()) && (SettingsLoader.isNewDown())) {
                NodeBase.getImageViewLinkedHashContainer().forEach(imageView -> {  //для каждой ноды
                    namesWithoutResize.forEach(s -> {  // перебираем список новых имен
                        String ex = FileViewBase.getFileNamesMap().get(s); // у имени находим расширение
                        String fileName = FileStringConverter.getNameWithEx(s, ex); //и сам файл
                        if (fileName.equals(imageView.getId())) // если имя совпало с ID ноды
                        {
                            galleryPane.getChildren().add(imageView); //добавляем ноду в галлерею
                        }
                    });
                });
                galleryPane.requestLayout();
            }
            if ((SettingsLoader.isByAddTime()) && (SettingsLoader.isNewUp())) {
                ArrayList<Node> listFromLinkedHashForReverse = new ArrayList<>(NodeBase.getImageViewLinkedHashContainer());
                Collections.reverse(listFromLinkedHashForReverse);
                LinkedHashSet<Node> linkedHashImageViewReverse = new LinkedHashSet<>(listFromLinkedHashForReverse);
                linkedHashImageViewReverse.forEach(imageView -> namesWithoutResize.forEach(s -> {
                    String ex = FileViewBase.getFileNamesMap().get(s);
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
            if ((SettingsLoader.isByName()) && (SettingsLoader.isNewUp() || SettingsLoader.isNewDown())) {
                AtomicInteger i = new AtomicInteger();
                i.set(0);
                NodeBase.getImageViewTreeContainer().forEach(imageView -> {
                    i.incrementAndGet();
                    namesWithoutResize.forEach(s -> {
                        String ex = FileViewBase.getFileNamesMap().get(s);
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
        if ((FileViewBase.getAllOriginalFileNames().size()) < FileViewBase.getAllNamesPreviewResized().size()) {
            TreeSet<String> deletedFilesName = new TreeSet<>(FileViewBase.getAllNamesPreviewResized());
            ArrayList<ImageView> nodeForDelete = new ArrayList<>();
            deletedFilesName.removeAll(FileViewBase.getAllOriginalFileNames());
            deletedFilesName.forEach(s -> {
                FileStringConverter.getFile(SettingsLoader.getQualityResizeFolder(), s, "jpg").delete();
                galleryPane.getChildren().forEach(node -> {
                    if (node.getId().startsWith(s)) {
                        nodeForDelete.add((ImageView) node);
                    }
                });
            });
            galleryPane.getChildren().removeAll(nodeForDelete);
            nodeForDelete.forEach(NodeBase.getImageViewTreeContainer()::remove);
            nodeForDelete.forEach(NodeBase.getImageViewLinkedHashContainer()::remove);
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
        Rectangle2D r = Screen.getPrimary().getBounds();
        scroll.setPrefHeight(r.getHeight());
        scroll.setPrefWidth(r.getWidth());
        scroll.setStyle("-fx-background: transparent;");
        if (colorNumber.length() == 6 || colorNumber.length() == 7) {
            scroll.setStyle("-fx-background: rgb(" + SetupWindowController.getRED() + "," + SetupWindowController.getGREEN() + "," + SetupWindowController.getBLUE() + ");");
        } else {
            if (!SetupWindowController.isResultBgImageCheck()) {
                scroll.setStyle("-fx-background: rgb(20,20,30);");
            }
        }

        if (SetupWindowController.isResultBgImageCheck()) {
            mainPane.setBackground(new Background(new BackgroundImage(SetupWindowController.getImageForBackGround(),
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
        }
        FileViewBase.init();

        /**
         * добавляем плашки при первой загрузке и перезагрузке
         * */
        if ((SettingsLoader.isByAddTime()) && (SettingsLoader.isNewDown()) && (Objects.nonNull(galleryPane))) {
            if (galleryPane.getChildren().size() < FileViewBase.getAllNamesPreviewResized().size()) {
                if (NodeBase.getImageViewLinkedHashContainer().size() == FileViewBase.getAllNamesPreviewResized().size()) {
                    NodeBase.getImageViewLinkedHashContainer().forEach(imageView -> galleryPane.getChildren().add(imageView));
                    galleryPane.requestLayout();
                } else {
                    FileViewBase.getAllNamesPreviewResized().forEach(this::createImageView);
                    NodeBase.getImageViewLinkedHashContainer().forEach(imageView -> galleryPane.getChildren().add(imageView));
                }
            }

        }
        if (SettingsLoader.isByAddTime() && SettingsLoader.isNewUp()) {
            ArrayList<Node> setReversed = new ArrayList<>(NodeBase.getImageViewLinkedHashContainer());
            Collections.reverse(setReversed);

            if (Objects.nonNull(galleryPane)) {
                if (galleryPane.getChildren().size() < FileViewBase.getAllNamesPreviewResized().size()) {
                    if (setReversed.size() == FileViewBase.getAllNamesPreviewResized().size()) {
                        setReversed.forEach(imageView -> galleryPane.getChildren().add(imageView));
                        galleryPane.requestLayout();
                    } else {
                        FileViewBase.getAllNamesPreviewResized().forEach(this::createImageView);
                        setReversed.clear();
                        setReversed.addAll(NodeBase.getImageViewLinkedHashContainer());
                        Collections.reverse(setReversed);
                        setReversed.forEach(imageView -> galleryPane.getChildren().add(imageView));
                        Main.start = false;
                    }
                    galleryPane.requestLayout();
                }
            }
        }
        if (SettingsLoader.isByName() && (Objects.isNull(galleryPane))) {
            if (galleryPane.getChildren().size() < FileViewBase.getAllNamesPreviewResized().size()) {
                if (NodeBase.getImageViewTreeContainer().size() == FileViewBase.getAllNamesPreviewResized().size()) {
                    NodeBase.getImageViewTreeContainer().forEach(imageView -> galleryPane.getChildren().add(imageView));
                    galleryPane.requestLayout();
                } else {
                    FileViewBase.getAllNamesPreviewResized().forEach(this::createImageView);
                    NodeBase.getImageViewTreeContainer().forEach(imageView -> galleryPane.getChildren().add(imageView));
                }
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