package GUI.Gallery;

import GUI.Gallery.imageResizer.ImageDarker;
import GUI.Gallery.setUp.SettingsLoader;
import GUI.Gallery.storage.FileViewBase;
import GUI.Gallery.storage.LinkTransfer;
import GUI.Gallery.storage.StageConteiner;
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
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Screen;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

import static GUI.Gallery.setUp.SettingsLoader.*;
import static GUI.Gallery.SetupWindowController.rezultbgImageCheck2;
import static GUI.Gallery.storage.NodeBase.imageViewLinkedHashConteiner;
import static GUI.Gallery.storage.NodeBase.imageViewTreeConteiner;

public class ImageMediaController implements Initializable {
    public VBox centerVbox;
    public Pane mainPane;
    private ImageView imageView = new ImageView();
    private MediaView mediaView;
    public static ArrayList<MediaPlayer> allPlayers = new ArrayList<>();
    private MediaPlayer mediaPlayer;
    @FXML
    private BorderPane borderPane;
    public static String colorNumber = "";
    public static Image image;
    private static int num = 0;
    private static double difSize = 0;
    ArrayList<String> listAll = new ArrayList<>();

    public void goToGallery() throws IOException {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        Parent root = FXMLLoader.load(getClass().getResource("Gallery-view.fxml"));
        StageConteiner.stage.centerOnScreen();
        StageConteiner.stage.getScene().setRoot(root);
    }

    public void sentToDB() throws IOException, AWTException {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        Rectangle2D r = Screen.getPrimary().getBounds();
        Robot robot = new Robot();
        Rectangle screenRect = new Rectangle((int) r.getWidth(), (int) r.getHeight());
        BufferedImage screenFullImage = robot.createScreenCapture(screenRect);
        image = ImageDarker.darker(screenFullImage, 0.7);

        Parent root = FXMLLoader.load(getClass().getResource("KeyBoard.fxml"));
        StageConteiner.stage.centerOnScreen();
        StageConteiner.stage.getScene().setRoot(root);
    }

    public void LeftPClick() throws FileNotFoundException {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
//      listALl со всеми документами из галлереи и источника
//      смотрим какой открыт сейчас, что находится слева от него
//      если слева видос, то видос, а нет так нет
        String now = LinkTransfer.link;
        String exLeft = "";
//       num = какой сейчас открыт порядковый номер
        for (int i = 0; i < listAll.size(); i++) {
            if (listAll.get(i).equals(now)) {
                num = i;
            }
        }
//        удалим центр и далее создадим то, что нужно или под видос или под картинку!!!
        borderPane.setCenter(null);
        if (num != 0) { // мы не уперлись в конец очереди
            exLeft = listAll.get(num - 1).substring(listAll.get(num - 1).lastIndexOf('.') + 1);
            //слева ждет картинка
            if (FileViewBase.imgExtension.contains(exLeft)) {
                Image image = new Image(new FileInputStream(SettingsLoader.getSourseFolder() + "/" + listAll.get(num - 1)));
                imageView.setImage(image);
                difSize = image.getWidth() / image.getHeight();
                if (difSize > 1) {
                    imageView.setFitWidth(1200);
                    imageView.setFitHeight(1200 / difSize);
                } else {
                    imageView.setFitWidth(800 * difSize);
                    imageView.setFitHeight(800);
                }
                imageView.setId(listAll.get(num - 1));
                borderPane.setCenter(imageView);
                LinkTransfer.link = listAll.get(num - 1);
                borderPane.requestLayout();
            }
            //слева ждет видос
            if (FileViewBase.movieExtension.contains(exLeft)) {
                File mediaFile = new File(SettingsLoader.getSourseFolder() + "/" + listAll.get(num - 1));
                Media media = null;
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
                mediaView.setId(listAll.get(num - 1));
                borderPane.setCenter(mediaView);
                LinkTransfer.link = listAll.get(num - 1);
                borderPane.requestLayout();
                mediaPlayer.play();
            }
        } else { // и мы уперлись в конец очереди
            exLeft = listAll.get(listAll.size() - 1).substring(listAll.get(listAll.size() - 1).lastIndexOf('.') + 1);
            //слева ждет картинка
            if (FileViewBase.imgExtension.contains(exLeft)) {
                Image image = new Image(new FileInputStream(SettingsLoader.getSourseFolder() + "/" + listAll.get(listAll.size() - 1)));
                difSize = image.getWidth() / image.getHeight();
                imageView.setImage(image);
                imageView.setId(listAll.get(listAll.size() - 1));
                if (difSize > 1) {
                    imageView.setFitWidth(1200);
                    imageView.setFitHeight(1200 / difSize);
                } else {
                    imageView.setFitWidth(800 * difSize);
                    imageView.setFitHeight(800);
                }
                borderPane.setCenter(imageView);
                LinkTransfer.link = listAll.get(listAll.size() - 1);
                borderPane.requestLayout();
            }
            //слева ждет видос
            if (FileViewBase.movieExtension.contains(exLeft)) { //слева ждет видос
                File mediaFile = new File(SettingsLoader.getSourseFolder() + "/" + listAll.get(listAll.size() - 1));
                Media media = null;
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
                mediaView.setId(listAll.get(listAll.size() - 1));
                LinkTransfer.link = listAll.get(listAll.size() - 1);
                borderPane.setCenter(mediaView);

                mediaPlayer.play();
            }
        }
    }

    public void RightPClick() throws FileNotFoundException {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
//      listALl со всеми документами из галереи и источника
//      смотрим какой открыт сейчас, что находится слева от него
//      если слева видос, то видос, а нет так нет
        String now = LinkTransfer.link;
        String exRight = "";
//      num = какой сейчас открыт порядковый номер
        for (int i = 0; i < listAll.size(); i++) {
            if (listAll.get(i).equals(now)) {
                num = i;
            }
        }
//        удалим центр и далее создадим то, что нужно или под видос или под картинку!!!
        borderPane.setCenter(null);

        if (num != listAll.size() - 1) { // мы не уперлись в конец очереди
            exRight = listAll.get(num + 1).substring(listAll.get(num + 1).lastIndexOf('.') + 1);
            //справа ждет картинка
            if (FileViewBase.imgExtension.contains(exRight)) {
                Image image = new Image(new FileInputStream(SettingsLoader.getSourseFolder() + "/" + listAll.get(num + 1)));
                difSize = image.getWidth() / image.getHeight();
                imageView.setImage(image);
                if (difSize > 1) {
                    imageView.setFitWidth(1200);
                    imageView.setFitHeight(1200 / difSize);
                } else {
                    imageView.setFitWidth(800 * difSize);
                    imageView.setFitHeight(800);
                }
                imageView.setId(listAll.get(num + 1));
                borderPane.setCenter(imageView);
                LinkTransfer.link = listAll.get(num + 1);
                borderPane.requestLayout();
            }
            //справа ждет видос
            if (FileViewBase.movieExtension.contains(exRight)) {
                File mediaFile = new File(SettingsLoader.getSourseFolder() + "/" + listAll.get(num + 1));
                Media media = null;
                try {
                    media = new Media(mediaFile.toURI().toURL().toString());
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
                LinkTransfer.link = listAll.get(num + 1);
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
                mediaView.setId(listAll.get(num + 1));
                borderPane.setCenter(mediaView);
                borderPane.requestLayout();
                LinkTransfer.link = listAll.get(num + 1);
                mediaPlayer.play();
            }
        } else { // и мы уперлись в конец очереди
            exRight = listAll.get(0).substring(listAll.get(0).lastIndexOf('.') + 1);
            //справа ждет картинка
            if (FileViewBase.imgExtension.contains(exRight)) {
                Image image = new Image(new FileInputStream(SettingsLoader.getSourseFolder() + "/" + listAll.get(0)));
                difSize = image.getWidth() / image.getHeight();
                imageView.setImage(image);
                imageView.setId(listAll.get(0));
                if (difSize > 1) {
                    imageView.setFitWidth(1200);
                    imageView.setFitHeight(1200 / difSize);
                } else {
                    imageView.setFitWidth(800 * difSize);
                    imageView.setFitHeight(800);
                }
                borderPane.setCenter(imageView);
                LinkTransfer.link = listAll.get(0);
                borderPane.requestLayout();
            }
            //справа ждет видос
            if (FileViewBase.movieExtension.contains(exRight)) { //справа ждет видос
                File mediaFile = new File(SettingsLoader.getSourseFolder() + "/" + listAll.get(0));
                Media media = null;
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
                mediaView.setId(listAll.get(0));
                mediaView.setMediaPlayer(mediaPlayer);
                borderPane.setCenter(mediaView);
                LinkTransfer.link = listAll.get(0);
                mediaPlayer.play();
            }
        }
    }

    public void NextKeyPress(KeyEvent keyEvent) throws FileNotFoundException {
        if (keyEvent.getCode() == KeyCode.LEFT) {
            LeftPClick();
        }
        if (keyEvent.getCode() == KeyCode.RIGHT) {
            RightPClick();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if (rezultbgImageCheck2) {
            mainPane.setBackground(new Background(new BackgroundImage(SetupWindowController.imageForBackGround2,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
        }
        if ((colorNumber.length() == 6) || (colorNumber.length() == 7)) {
            mainPane.setStyle("-fx-background: rgb(" + SetupWindowController.RED + "," + SetupWindowController.GREEN + "," + SetupWindowController.BLUE + ");");
        } else {
            mainPane.setStyle("-fx-background: rgb(20,20,30);");
        }

        if ((byAddTime) && (newUp)) {
            ArrayList<ImageView> ivlhcR = new ArrayList<>(imageViewLinkedHashConteiner);
            Collections.reverse(ivlhcR);
            ivlhcR.forEach(i -> listAll.add(i.getId()));
        }
        if ((byAddTime) && (newDown)) {
            imageViewLinkedHashConteiner.forEach(i -> listAll.add(i.getId()));
        }
        if (byName) {
            ArrayList<ImageView> ivlhcR = new ArrayList<>(imageViewTreeConteiner);
            ivlhcR.forEach(i -> listAll.add(i.getId()));
        }
        for (int i = 0; i < listAll.size(); i++) {
            if (listAll.get(i).equals(imageView.getId())) {
                num = i;
            }
        }
        if (FileViewBase.imgExtension.contains(LinkTransfer.link.substring(LinkTransfer.link.lastIndexOf('.') + 1))) {
            ImageView imageView = new ImageView();
            borderPane.setCenter(imageView);
            try {
                imageView.setId(LinkTransfer.link);
                Image setedImage = new Image(new FileInputStream(SettingsLoader.getSourseFolder() + "/" + LinkTransfer.link));
                imageView.setImage(setedImage);
                difSize = setedImage.getWidth() / setedImage.getHeight();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            if (difSize > 1) {
                imageView.setFitWidth(1200);
                imageView.setFitHeight(1200 / difSize);
            } else {
                imageView.setFitWidth(800 * difSize);
                imageView.setFitHeight(800);
            }
        }
        BorderPane.setAlignment(imageView, Pos.CENTER);
        if (FileViewBase.movieExtension.contains(LinkTransfer.link.substring(LinkTransfer.link.lastIndexOf('.') + 1))) {
            File mediaFile = new File(SettingsLoader.getSourseFolder() + "/" + LinkTransfer.link);
            Media media = null;
            try {
                media = new Media(mediaFile.toURI().toURL().toString());
                difSize = (double) media.getWidth() / media.getHeight();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            allPlayers.add(mediaPlayer);
            mediaView = new MediaView();
            if (difSize > 1) {
                imageView.setFitWidth(1200);
                imageView.setFitHeight(1200 / difSize);
            } else {
                imageView.setFitWidth(800 * difSize);
                imageView.setFitHeight(800);
            }
            mediaView.setMediaPlayer(mediaPlayer);
            borderPane.setCenter(mediaView);
            mediaPlayer.play();
        }
    }
}
