package GUI.Gallery;

import GUI.Gallery.imageResizer.ImageDarkProcessor;
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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Screen;
import lombok.Getter;
import lombok.Setter;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
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

    private static double difSize = 0;

    private final ArrayList<String> allImageViewReversed = new ArrayList<>();


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
        secondImage(false);
    }

    public void rightPClick() {
        secondImage(true);
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

        if (SetupWindowController.isResultBgImageCheck2()) {
            mainPane.setBackground(new Background(new BackgroundImage(SetupWindowController.getImageForBackGround2(),
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
        }
        if ((colorNumber.length() == 6) || (colorNumber.length() == 7)) {
            mainPane.setStyle("-fx-background: rgb(" + SetupWindowController.getRED() + "," + SetupWindowController.getGREEN() + "," + SetupWindowController.getBLUE() + ");");
        } else {
            mainPane.setStyle("-fx-background: rgb(20,20,30);");
        }

        if (SettingsLoader.isByAddTime() && SettingsLoader.isNewUp()) {
            ArrayList<ImageView> allImageView = new ArrayList<>(NodeBase.getImageViewLinkedHashContainer());
            Collections.reverse(allImageView);
            allImageView.forEach(i -> allImageViewReversed.add(i.getId()));
        }
        if (SettingsLoader.isByAddTime() && SettingsLoader.isNewDown()) {
            NodeBase.getImageViewLinkedHashContainer().forEach(i -> allImageViewReversed.add(i.getId()));
        }
        if (SettingsLoader.isByName()) {
            ArrayList<ImageView> allImageView = new ArrayList<>(NodeBase.getImageViewTreeContainer());
            allImageView.forEach(iv -> allImageViewReversed.add(iv.getId()));
        }
        if (FileViewBase.getImgExtension().contains(FileStringConverter.getExtension(LinkTransfer.getLink()))) {
            createImageView(LinkTransfer.getLink());
        }
        BorderPane.setAlignment(imageView, Pos.CENTER);
        if (FileViewBase.getMovieExtension().contains(FileStringConverter.getExtension(LinkTransfer.getLink()))) {
            createMovie(LinkTransfer.getLink());
        }
    }

    //*** true = right, false - left  **//
    private void secondImage(boolean target) {

        int num = allImageViewReversed.indexOf(LinkTransfer.getLink());
        stopMediaPlayer();
        borderPane.setCenter(null);
        String exNext = "";

        final int next;
        final int lastNumber;
        final int find;
        if (target) {
            next = num + 1;
            lastNumber = 0;
            find = allImageViewReversed.size() - 1;
        } else {
            next = num - 1;
            lastNumber = allImageViewReversed.size() - 1;
            find = 0;
        }
        if (num != find) {
            exNext = FileStringConverter.getExtension(allImageViewReversed.get(next));
            if (FileViewBase.getImgExtension().contains(exNext)) {
                buildImageView(next);
            }
            if (FileViewBase.getMovieExtension().contains(exNext)) {
                buildMovie(next);
            }
        } else {
            exNext = FileStringConverter.getExtension(allImageViewReversed.get(lastNumber));
            if (FileViewBase.getImgExtension().contains(exNext)) {
                buildImageView(lastNumber);
            }
            if (FileViewBase.getMovieExtension().contains(exNext)) {
                buildMovie(lastNumber);
            }
        }
    }


    private void buildImageView(int id) {
        createImageView(allImageViewReversed.get(id));
        LinkTransfer.setLink(allImageViewReversed.get(id));
        borderPane.requestLayout();
    }

    private void buildMovie(int id) {
        createMovie(allImageViewReversed.get(id));
        mediaPlayer.play();
        borderPane.requestLayout();
    }

    public void createImageView(String file) {
        Image image = new Image(FileStringConverter.getFileInputString(SettingsLoader.getSourceFolder(), file));
        imageView.setImage(image);
        difSize = image.getWidth() / image.getHeight();
        if (difSize > 1) {
            imageView.setFitWidth(1200);
            imageView.setFitHeight(1200 / difSize);
        } else {
            imageView.setFitWidth(800 * difSize);
            imageView.setFitHeight(800);
        }
        imageView.setId(file);
        borderPane.setCenter(imageView);
    }

    public void createMovie(String file) {
        File mediaFile = FileStringConverter.getFile(SettingsLoader.getSourceFolder(), file);
        Media media;
        try {
            media = new Media(mediaFile.toURI().toURL().toString());
            difSize = (double) media.getWidth() / media.getHeight();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaView = new MediaView();
        if (difSize > 1) {
            imageView.setFitWidth(1200);
            imageView.setFitHeight(1200 / difSize);
        } else {
            imageView.setFitWidth(800 * difSize);
            imageView.setFitHeight(800);
        }
        mediaView.setMediaPlayer(mediaPlayer);
        mediaView.setId(file);
    }

    private void stopMediaPlayer() {
        if (Objects.nonNull(mediaPlayer)) {
            mediaPlayer.stop();
        }
    }

}
