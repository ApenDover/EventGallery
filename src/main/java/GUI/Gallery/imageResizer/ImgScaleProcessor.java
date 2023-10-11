package GUI.Gallery.imageResizer;

import GUI.Gallery.setUp.SettingsLoader;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Objects;
import java.util.Set;

public class ImgScaleProcessor {

    private ImgScaleProcessor() {
    }

    public static void scale(Set<File> imgFiles) {
        String srcFolder = SettingsLoader.getSourceFolder();
        String dstFolder = srcFolder + "/" + SettingsLoader.getQualityResizer();
        int newWidth = Integer.parseInt(SettingsLoader.getQualityResizer());
        if (Objects.isNull(imgFiles)) {
            return;
        }

        File fileDst = new File(dstFolder);
        fileDst.mkdir();

        imgFiles.parallelStream().forEach(file -> {
            try {
                if (file.getAbsolutePath().endsWith(".jpg")) {
                    BufferedImage image = ImageIO.read(file);
                    if (Objects.nonNull(image)) {
                        BufferedImage newImage;
                        newImage = Scalr.resize(image, newWidth);
                        File newFile = new File(dstFolder, file.getName());
                        ImageIO.write(newImage, "jpg", newFile);
                    }
                }
            } catch (Exception ignored) {
            }
        });
    }
}

