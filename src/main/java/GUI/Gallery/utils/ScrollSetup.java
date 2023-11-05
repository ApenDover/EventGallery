package gui.gallery.utils;

import gui.gallery.SetupWindowController;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.ScrollPane;
import javafx.stage.Screen;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ScrollSetup {

    private static final int COLOR_LENGTH_SHARP = 7;

    private static final int COLOR_LENGTH = COLOR_LENGTH_SHARP - 1;

    public void setup(ScrollPane scrollPane, String colorNumber) {
        Rectangle2D r = Screen.getPrimary().getBounds();
        scrollPane.setPrefHeight(r.getHeight());
        scrollPane.setPrefWidth(r.getWidth());
        scrollPane.setStyle("-fx-background: transparent;");
        if (colorNumber.length() == COLOR_LENGTH || colorNumber.length() == COLOR_LENGTH_SHARP) {
            scrollPane.setStyle("-fx-background: rgb(" + SetupWindowController.getRed()
                    + "," + SetupWindowController.getGreen() + "," + SetupWindowController.getBlue() + ");");
        } else {
            if (!SetupWindowController.isResultBgImageCheck()) {
                scrollPane.setStyle("-fx-background: rgb(20,20,30);");
            }
        }
    }

}
