package gui.gallery;

import gui.gallery.data.dao.BaseDAO;
import gui.gallery.singleton.ExecutorServiceSingleton;
import gui.gallery.singleton.LinkTransfer;
import gui.gallery.singleton.StageContainer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main extends Application {

    @Override
    public void stop() throws Exception {
        if (BaseDAO.getInstance().getSession().isOpen()) {
            BaseDAO.getInstance().closeConnection();
        }
        ExecutorServiceSingleton.getInstance().stop();
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

    public static void main(String[] args) {
        launch();
    }

}
