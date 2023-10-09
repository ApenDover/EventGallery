package GUI.Gallery.imageResizer;

import GUI.Gallery.setUp.SettingsLoader;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalDateTime;
import java.util.Objects;
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
            File file = new File(dstFolder);
            file.mkdir();
            this.imgFiles = imgFiles;
            run();

        }
    }

    @Override
    public void run() {
        System.out.println("START RESIZE " + LocalDateTime.now());
        try {
            imgFiles.parallelStream().forEach(file -> {
                try {
                    if (file.getAbsolutePath().endsWith(".jpg")) {
                        BufferedImage image = ImageIO.read(file);
                        if (Objects.nonNull(image)) {
                            BufferedImage newImage;
                            newImage = Scalr.resize(image, newWidth);
                            File newFile = new File(dstFolder + "/" + file.getName());
                            ImageIO.write(newImage, "jpg", newFile);
                        }
                    }
                } catch (Exception ignored) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("END RESIZE " + LocalDateTime.now());
    }
}

