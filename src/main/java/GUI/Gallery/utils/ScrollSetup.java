package gui.gallery.utils;

import gui.gallery.SetupWindowController;
import gui.gallery.singleton.SettingsConst;
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
        if (colorNumber.length() == SettingsConst.COLOR_LENGTH.getValue()
                || colorNumber.length() == SettingsConst.COLOR_LENGTH_SHARP.getValue()) {
            scrollPane.setStyle("-fx-background: rgb(" + SetupWindowController.getRed()
                    + "," + SetupWindowController.getGreen() + "," + SetupWindowController.getBlue() + ");");
        } else {
            if (!SetupWindowController.isResultBgImageCheck()) {
                scrollPane.setStyle("-fx-background: rgb(20,20,30);");
            }
        }
    }

}
