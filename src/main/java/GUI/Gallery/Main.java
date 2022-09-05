package GUI.Gallery;

import GUI.Gallery.MySQL.Connections.BaseConnection;
import GUI.Gallery.Storage.LinkTransfer;
import GUI.Gallery.Storage.MailBase;
import GUI.Gallery.Storage.StageConteiner;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.Iterator;


public class Main extends Application {
    public static boolean start = true;
    public static BaseConnection baseConnection;
    public static Color color;
    @Override
    public void start(Stage stage) throws Exception {
        new MailBase();
        LinkTransfer.flag = false;
        StageConteiner.stage = stage;
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
    Object object = new Object();
}

//--module-path
//        "/Users/andrey/Downloads/javafx-sdk-18.0.1/lib"
//        --add-modules
//        javafx.controls,javafx.fxml
//        --add-exports
//        javafx.base/com.sun.javafx.event=org.controlsfx.controls
//        -Dcom.sun.javafx.isEmbedded=true
//        -Dcom.sun.javafx.touch=true
//        -Dcom.sun.javafx.virtualKeyboard=javafx