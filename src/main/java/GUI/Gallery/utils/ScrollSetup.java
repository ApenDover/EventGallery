package GUI.Gallery.utils;

import GUI.Gallery.SetupWindowController;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.ScrollPane;
import javafx.stage.Screen;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ScrollSetup {

    public void setup(ScrollPane scrollPane, String colorNumber) {
        Rectangle2D r = Screen.getPrimary().getBounds();
        scrollPane.setPrefHeight(r.getHeight());
        scrollPane.setPrefWidth(r.getWidth());
        scrollPane.setStyle("-fx-background: transparent;");
        if (colorNumber.length() == 6 || colorNumber.length() == 7) {
            scrollPane.setStyle("-fx-background: rgb(" + SetupWindowController.getRed() + "," + SetupWindowController.getGreen() + "," + SetupWindowController.getBlue() + ");");
        } else {
            if (!SetupWindowController.isResultBgImageCheck()) {
                scrollPane.setStyle("-fx-background: rgb(20,20,30);");
            }
        }
    }

}
