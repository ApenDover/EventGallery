package GUI.Gallery.imageResizer;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageDarker {

    public static Image darker(BufferedImage imageToDark, double factor) throws IOException {

        BufferedImage darkImage = new BufferedImage(imageToDark.getWidth(), imageToDark.getHeight(), BufferedImage.TYPE_INT_RGB);
        int width = imageToDark.getWidth();
        int height = imageToDark.getHeight();

        for (int y = 0; y < height; y++) {//loops for image matrix
            for (int x = 0; x < width; x++) {

                Color c = new Color(imageToDark.getRGB(x, y));

                //adding factor to rgb values
                int r = c.getRed() - (int) (c.getRed() * factor);
                int b = c.getBlue() - (int) (c.getBlue() * factor);
                int g = c.getGreen() - (int) (c.getGreen() * factor);
                if (r >= 256) {
                    r = 255;
                } else if (r < 0) {
                    r = 0;
                }

                if (g >= 256) {
                    g = 255;
                } else if (g < 0) {
                    g = 0;
                }

                if (b >= 256) {
                    b = 255;
                } else if (b < 0) {
                    b = 0;
                }
                darkImage.setRGB(x, y, new Color(r, g, b).getRGB());
            }
        }

        return convertToFxImage(darkImage);
    }

    private static Image convertToFxImage(BufferedImage image) {
        WritableImage wr = null;
        if (image != null) {
            wr = new WritableImage(image.getWidth(), image.getHeight());
            PixelWriter pw = wr.getPixelWriter();
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    pw.setArgb(x, y, image.getRGB(x, y));
                }
            }
        }
        return new ImageView(wr).getImage();
    }
}