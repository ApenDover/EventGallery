package GUI.Gallery.imageViewProcess;

import GUI.Gallery.OpenWindow;
import GUI.Gallery.model.AbstractContainer;
import GUI.Gallery.model.ImageContainer;
import GUI.Gallery.model.Resizeable;
import GUI.Gallery.model.VideoContainer;
import GUI.Gallery.singleton.FileViewBase;
import GUI.Gallery.singleton.LinkTransfer;
import GUI.Gallery.singleton.NodeBase;
import GUI.Gallery.singleton.RepeatableTimeline;
import GUI.Gallery.singleton.SettingsLoader;
import GUI.Gallery.singleton.StageContainer;
import GUI.Gallery.utils.FileStringConverter;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.concurrent.atomic.AtomicBoolean;

public class CreateImageView {

    private Parent root;

//    public void create(AbstractContainer abstractContainer) {
////        String cubeImagePath = FileStringConverter.getFilePath(SettingsLoader.getInstance().getQualityResizeFolder(), fileName, "jpg");
////        String exFile = FileViewBase.getInstance().getFileNamesMap().get(fileName);
////        String filePath = FileStringConverter.getFilePath(SettingsLoader.getInstance().getSourceFolder(), fileName, exFile);
//
//        double fileW = 0;
//        double fileH = 0;
//
////      нужны размеры оригинального изображения чтобы создать плитку нужного размера
//        if (abstractContainer instanceof ImageContainer imageContainer) {
//            fileW = imageContainer.getImage().getWidth();
//            fileH = imageContainer.getImage().getHeight();
//        }
//        if (abstractContainer instanceof VideoContainer videoContainer) {
//            fileW = videoContainer.getMedia().getWidth();
//            fileH = videoContainer.getMedia().getHeight();
//        }
//
//        ImageView imageView = new ImageView();
//        Image imageCube;
//        try {
//            imageCube = new Image(new FileInputStream(cubeImagePath));
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//        imageView.setImage(imageCube);
//        imageView.setId(fileName + "." + exFile);
//        double width = Integer.parseInt(SettingsLoader.getInstance().getResizeQuality());
//        double height = (fileH * width) / fileW;
//        imageView.setFitWidth(width);
//        imageView.setFitHeight(height);
//        imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
//            LinkTransfer.getInstance().setLink(imageView.getId());
//            root = OpenWindow.open("ImageMedia-view.fxml");
//            StageContainer.getInstance().getStage().centerOnScreen();
//            StageContainer.getInstance().getStage().getScene().setRoot(root);
//            RepeatableTimeline.getInstance().stop();
//        });
//        if (SettingsLoader.getInstance().isByAddTime()) {
//            AtomicBoolean k = new AtomicBoolean(true);
//            NodeBase.getInstance().getImageViewLinkedHashContainer().forEach(i -> {
//                if (i.getId().equals(imageView.getId())) {
//                    k.set(false);
//                }
//            });
//            if (k.get()) {
//                NodeBase.getInstance().getImageViewLinkedHashContainer().add(imageView);
//            }
//        } else {
//            NodeBase.getInstance().getImageViewTreeContainer().add(imageView);
//        }
//    }

}
