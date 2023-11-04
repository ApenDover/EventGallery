package GUI.Gallery;

import GUI.Gallery.imageResizer.ImageDarkProcessor;
import GUI.Gallery.imageViewProcess.NextImageProcessor;
import GUI.Gallery.model.ImageContainer;
import GUI.Gallery.model.VideoContainer;
import GUI.Gallery.singleton.LinkTransfer;
import GUI.Gallery.singleton.RepeatableTimeline;
import GUI.Gallery.singleton.StageContainer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
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
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;
import lombok.Getter;
import lombok.Setter;
import net.rgielen.fxweaver.core.FxmlView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

@Component
@FxmlView("ImageMedia-view.fxml")
public class ImageMediaController implements Initializable {

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
            root = FXMLLoader.load(getClass().getResource("Gallery-view.fxml"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        StageContainer.getInstance().getStage().centerOnScreen();
        StageContainer.getInstance().getStage().getScene().setRoot(root);
    }

    public void sentToDB() {
        borderPane.setCenter(null);
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
            if (List.of(6, 7).contains(colorNumber.length())) {
                mainPane.setStyle("-fx-background: rgb(" + SetupWindowController.getRed() + "," + SetupWindowController.getGreen() + "," + SetupWindowController.getBlue() + ");");
            } else {
                mainPane.setStyle("-fx-background: rgb(20,20,30);");
            }

            if (LinkTransfer.getInstance().getResizeable() instanceof ImageContainer imageContainer) {
                setCenterNode(imageContainer.getImageView());
            }
            if (LinkTransfer.getInstance().getResizeable() instanceof VideoContainer videoContainer) {
                final var mediaView = videoContainer.getMediaView();
                mediaView.getMediaPlayer().play();
                setCenterNode(mediaView);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private String buildNextNode(boolean target) {
        borderPane.setCenter(null);
        Node node = nextImageProcessor.secondImage(target);
        setCenterNode(node);
        return node.getId();
    }

    private void setCenterNode(Node node) {
        borderPane.setCenter(node);
        borderPane.requestLayout();
    }

}
