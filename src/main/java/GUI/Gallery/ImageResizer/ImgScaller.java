package GUI.Gallery.ImageResizer;

import GUI.Gallery.SetUp.SettingsLoader;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.TreeSet;

public class ImgScaller implements Runnable {

    private TreeSet<File> imgFiles;
    private final int newWidth;
    private final String dstFolder;
    private final String srcFolder;

    public ImgScaller(TreeSet<File> imgFiles) {
        this.srcFolder = SettingsLoader.getSourseFolder();
        this.dstFolder = srcFolder + "/" + SettingsLoader.getQualityResizer();
        this.newWidth = Integer.parseInt(SettingsLoader.getQualityResizer());
        if (imgFiles != null) {
            this.imgFiles = imgFiles;
            run();

        }
    }

    @Override
    public void run() {
        try {
            for (File file : imgFiles) {
                if (!file.getAbsolutePath().endsWith(".jpg")) {
// проверить будет ли работать метод с другими форматами файлов!
                } else {
                    BufferedImage image = ImageIO.read(file);
                    if (image == null) {
                        continue;
                    }
                    int newHeight = (int) Math.round(image.getHeight() / (image.getWidth() / (double) newWidth));
                    BufferedImage newImage; // = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);

                    newImage = Scalr.resize(image, newWidth);
                    File newFile = new File(dstFolder + "/" + file.getName());
                    ImageIO.write(newImage, "jpg", newFile);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

