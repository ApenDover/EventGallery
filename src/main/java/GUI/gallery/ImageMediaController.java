package gui.gallery;

import gui.gallery.imageViewProcess.NextImageProcessor;
import gui.gallery.model.ImageContainer;
import gui.gallery.model.VideoContainer;
import gui.gallery.singleton.LinkTransfer;
import gui.gallery.singleton.SettingsConst;
import gui.gallery.singleton.StageContainer;
import gui.gallery.utils.imageResizer.ImageDarkProcessor;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ImageMediaController implements Initializable {

    private static final double FACTOR = 0.9;

    @FXML
    private BorderPane borderPane;

    @FXML
    private Pane mainPane;

    @Setter
    private static String colorNumber = StringUtils.EMPTY;

    @Getter
    private static Image image;

    private NextImageProcessor nextImageProcessor;


    public void goToGallery() {
        borderPane.setCenter(null);
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("gallery-view.fxml"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        StageContainer.getInstance().getStage().centerOnScreen();
        StageContainer.getInstance().getStage().getScene().setRoot(root);
    }

    public void sentToDB() {
        borderPane.setCenter(null);
        final var r = Screen.getPrimary().getBounds();
        Robot robot;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
        final var screenRect = new Rectangle((int) r.getWidth(), (int) r.getHeight());
        final var screenFullImage = robot.createScreenCapture(screenRect);
        image = ImageDarkProcessor.darker(screenFullImage, FACTOR);
        StageContainer.getInstance().getStage().getScene().setRoot(OpenWindow.open("KeyBoard.fxml"));
        StageContainer.getInstance().getStage().centerOnScreen();
    }

    public void leftPClick() {
        buildNextNode(false);
    }

    public void rightPClick() {
        buildNextNode(true);
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
        try {
            nextImageProcessor = new NextImageProcessor();
            if (SetupWindowController.isResultBgImageCheck2()) {
                mainPane.setBackground(new Background(new BackgroundImage(SetupWindowController.getImageForBackGround2(),
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
            }
            if (colorNumber.length() == SettingsConst.COLOR_LENGTH.getValue()
                    || colorNumber.length() == SettingsConst.COLOR_LENGTH_SHARP.getValue()) {
                mainPane.setStyle("-fx-background: rgb(" + SetupWindowController.getRed()
                        + "," + SetupWindowController.getGreen() + "," + SetupWindowController.getBlue() + ");");
            } else {
                mainPane.setStyle("-fx-background: rgb(20,20,30);");
            }

            if (LinkTransfer.getInstance().getResizeable() instanceof ImageContainer) {
                final var imageContainer = (ImageContainer) LinkTransfer.getInstance().getResizeable();
                setCenterNode(imageContainer.getImageView());
            }
            if (LinkTransfer.getInstance().getResizeable() instanceof VideoContainer) {
                final var videoContainer = (VideoContainer) LinkTransfer.getInstance().getResizeable();
                final var mediaView = videoContainer.getMediaView();
                mediaView.getMediaPlayer().play();
                setCenterNode(mediaView);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private void buildNextNode(boolean target) {
        borderPane.setCenter(null);
        Node node = nextImageProcessor.secondImage(target);
        setCenterNode(node);
        node.getId();
    }

    private void setCenterNode(Node node) {
        borderPane.setCenter(node);
        borderPane.requestLayout();
    }

}
