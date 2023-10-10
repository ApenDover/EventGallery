package GUI.Gallery.imageResizer;

import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class ImageDarkProcessor {

    public static Image darker(BufferedImage imageToDark, double factor) {
        BufferedImage darkImage = new BufferedImage(imageToDark.getWidth(), imageToDark.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < imageToDark.getHeight(); y++) {
            for (int x = 0; x < imageToDark.getWidth(); x++) {
                Color color = new Color(imageToDark.getRGB(x, y));
                int r = Math.max(0, Math.min(255, color.getRed() - (int) (color.getRed() * factor)));
                int b = Math.max(0, Math.min(255, color.getBlue() - (int) (color.getBlue() * factor)));
                int g = Math.max(0, Math.min(255, color.getGreen() - (int) (color.getGreen() * factor)));
                darkImage.setRGB(x, y, new Color(r, g, b).getRGB());
            }
        }
        return convertToFxImage(darkImage);
    }

    private static Image convertToFxImage(BufferedImage image) {
        if (Objects.isNull(image)) {
            return null;
        }
        WritableImage writableImage = new WritableImage(image.getWidth(), image.getHeight());
        PixelWriter pw = writableImage.getPixelWriter();
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                pw.setArgb(x, y, image.getRGB(x, y));
            }
        }
        return writableImage;
    }

}