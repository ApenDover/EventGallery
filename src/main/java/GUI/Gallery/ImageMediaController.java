package GUI.Gallery;

import GUI.Gallery.imageResizer.ImageDarkProcessor;
import GUI.Gallery.imageViewProcess.MovieBuilder;
import GUI.Gallery.imageViewProcess.NextImageProcessor;
import GUI.Gallery.imageViewProcess.PictureBuilder;
import GUI.Gallery.setUp.SettingsLoader;
import GUI.Gallery.storage.FileViewBase;
import GUI.Gallery.storage.LinkTransfer;
import GUI.Gallery.storage.NodeBase;
import GUI.Gallery.storage.StageContainer;
import GUI.Gallery.utils.FileStringConverter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Screen;
import lombok.Getter;
import lombok.Setter;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class ImageMediaController implements Initializable {

    @FXML
    private BorderPane borderPane;

    @FXML
    private Pane mainPane;

    @Setter
    private static String colorNumber = "";

    @Getter
    private static Image image;

    private final ImageView imageView = new ImageView();

    private MediaView mediaView;

    private MediaPlayer mediaPlayer;

    private final ArrayList<String> allGalleryImageView = new ArrayList<>();

    private NextImageProcessor nextImageProcessor;


    public void goToGallery() {
        stopMediaPlayer();
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("Gallery-view.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        StageContainer.getStage().centerOnScreen();
        StageContainer.getStage().getScene().setRoot(root);
    }

    public void sentToDB() {
        stopMediaPlayer();
        Rectangle2D r = Screen.getPrimary().getBounds();
        Robot robot;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
        Rectangle screenRect = new Rectangle((int) r.getWidth(), (int) r.getHeight());
        BufferedImage screenFullImage = robot.createScreenCapture(screenRect);
        image = ImageDarkProcessor.darker(screenFullImage, 0.7);

        try {
            StageContainer.getStage().getScene().setRoot(FXMLLoader.load(getClass().getResource("KeyBoard.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        StageContainer.getStage().centerOnScreen();
    }

    public void leftPClick() {
        stopMediaPlayer();
        nextImageProcessor.secondImage(false, allGalleryImageView);
        borderPane.setCenter(imageView);
        borderPane.requestLayout();
    }

    public void rightPClick() {
        stopMediaPlayer();
        nextImageProcessor.secondImage(true, allGalleryImageView);
        borderPane.setCenter(imageView);
        borderPane.requestLayout();
    }

    public void nextKeyPress(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.LEFT) {
            leftPClick();
        }
        if (keyEvent.getCode() == KeyCode.RIGHT) {
            rightPClick();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nextImageProcessor = new NextImageProcessor(imageView, mediaView);
        if (SetupWindowController.isResultBgImageCheck2()) {
            mainPane.setBackground(new Background(new BackgroundImage(SetupWindowController.getImageForBackGround2(),
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
        }
        if (List.of(6, 7).contains(colorNumber.length())) {
            mainPane.setStyle("-fx-background: rgb(" + SetupWindowController.getRED() + "," + SetupWindowController.getGREEN() + "," + SetupWindowController.getBLUE() + ");");
        } else {
            mainPane.setStyle("-fx-background: rgb(20,20,30);");
        }

        if (SettingsLoader.isByAddTime() && SettingsLoader.isNewUp()) {
            ArrayList<ImageView> allImageView = new ArrayList<>(NodeBase.getImageViewLinkedHashContainer());
            Collections.reverse(allImageView);
            allImageView.forEach(iv -> this.allGalleryImageView.add(iv.getId()));
        }
        if (SettingsLoader.isByAddTime() && SettingsLoader.isNewDown()) {
            NodeBase.getImageViewLinkedHashContainer().forEach(iv -> allGalleryImageView.add(iv.getId()));
        }
        if (SettingsLoader.isByName()) {
            NodeBase.getImageViewTreeContainer().forEach(iv -> allGalleryImageView.add(iv.getId()));
        }

        if (FileViewBase.getImgExtension().contains(FileStringConverter.getExtension(LinkTransfer.getLink()))) {
            nextImageProcessor.createImage(LinkTransfer.getLink());
            borderPane.setCenter(imageView);
            borderPane.requestLayout();
        }
        BorderPane.setAlignment(imageView, Pos.CENTER);
        if (FileViewBase.getMovieExtension().contains(FileStringConverter.getExtension(LinkTransfer.getLink()))) {
            nextImageProcessor.createMovie(LinkTransfer.getLink());
            borderPane.setCenter(imageView);
            borderPane.requestLayout();
        }
    }

    private void stopMediaPlayer() {
        if (Objects.nonNull(mediaPlayer)) {
            mediaPlayer.stop();
        }
    }

}
