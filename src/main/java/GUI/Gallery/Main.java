package GUI.Gallery;

import GUI.Gallery.storage.LinkTransfer;
import GUI.Gallery.storage.StageContainer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
    public static boolean start = true;

    @Override
    public void start(Stage stage) throws Exception {
        LinkTransfer.setFlag(false);
        StageContainer.setStage(stage);
        stage.requestFocus();
        Parent root = FXMLLoader.load(getClass().getResource("SetupWindow.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) throws Exception {
        launch();
    }

}
