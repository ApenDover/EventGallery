package GUI.Gallery.imageResizer;

import GUI.Gallery.singleton.SettingsLoader;
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
        int newWidth = Integer.parseInt(SettingsLoader.getInstance().getQualityResizer());
        if (Objects.isNull(imgFiles)) {
            return;
        }

        File fileDst = new File(SettingsLoader.getInstance().getQualityResizeFolder());
        fileDst.mkdir();

        imgFiles.parallelStream().forEach(file -> {
            try {
                if (file.getAbsolutePath().endsWith(".jpg")) {
                    BufferedImage image = ImageIO.read(file);
                    if (Objects.nonNull(image)) {
                        BufferedImage newImage = Scalr.resize(image, newWidth);
                        File newFile = new File(SettingsLoader.getInstance().getQualityResizeFolder(), file.getName());
                        ImageIO.write(newImage, "jpg", newFile);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

