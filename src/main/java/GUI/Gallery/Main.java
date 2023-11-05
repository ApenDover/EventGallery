package gui.gallery;

import gui.gallery.data.dao.BaseDAO;
import gui.gallery.singleton.LinkTransfer;
import gui.gallery.singleton.StageContainer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void stop() throws Exception {
        BaseDAO.getInstance().getInstance().closeConnection();
        super.stop();
    }

    @Override
    public void start(Stage stage) throws Exception {
        LinkTransfer.getInstance().setFlag(false);
        StageContainer.getInstance().setStage(stage);
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
