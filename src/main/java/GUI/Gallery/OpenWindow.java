package gui.gallery;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.experimental.UtilityClass;

import java.io.IOException;

@UtilityClass
public class OpenWindow {

    private Parent root;

    public Parent open(String windowName) {
        try {
            root = FXMLLoader.load(OpenWindow.class.getResource(windowName));
            return root;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
