package GUI.Gallery;

import GUI.Gallery.data.dao.BaseDAO;
import GUI.Gallery.singleton.LinkTransfer;
import GUI.Gallery.singleton.StageContainer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    public static boolean start = true;

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

//    private ConfigurableApplicationContext applicationContext;
//
//    @Override
//    public void init() {
//        String[] args = getParameters().getRaw().toArray(new String[0]);
//        this.applicationContext = new SpringApplicationBuilder()
//                .sources(Main.class)
//                .run(args);
//    }
//
//    @Override
//    public void start(Stage stage) {
//        LinkTransfer.getInstance().setFlag(false);
//        StageContainer.getInstance().setStage(stage);
//        stage.requestFocus();
//        FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
//        Parent root = fxWeaver.loadView(SetupWindowController.class);
//        Scene scene = new Scene(root);
//        stage.setScene(scene);
//        stage.centerOnScreen();
//        stage.setResizable(false);
//        stage.show();
//    }
//
//    @Override
//    public void stop() {
//        this.applicationContext.close();
//        Platform.exit();
//    }

}
