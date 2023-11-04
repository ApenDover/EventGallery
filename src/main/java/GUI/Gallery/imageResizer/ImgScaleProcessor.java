package GUI.Gallery.imageResizer;

import GUI.Gallery.singleton.FileViewBase;
import GUI.Gallery.singleton.SettingsLoader;
import lombok.experimental.UtilityClass;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Objects;
import java.util.Set;

@UtilityClass
public class ImgScaleProcessor {

    private static final int NEW_WIDTH = Integer.parseInt(SettingsLoader.getInstance().getResizeQuality());

    public static void scale(Set<File> imgFiles) {
        if (Objects.isNull(imgFiles)) {
            return;
        }
        imgFiles.parallelStream().forEach(ImgScaleProcessor::scale);
    }

    public static File scale(File imgFile) {
        if (Objects.isNull(imgFile)) {
            return null;
        }
        FileViewBase.getInstance();
        try {
            if (imgFile.getAbsolutePath().endsWith(".jpg")) {
                BufferedImage image = ImageIO.read(imgFile);
                if (Objects.nonNull(image)) {
                    File newFile = new File(SettingsLoader.getInstance().getQualityResizeFolder(), imgFile.getName());
                    if (!newFile.exists()) {
                        BufferedImage newImage = Scalr.resize(image, NEW_WIDTH);
                        ImageIO.write(newImage, "jpg", newFile);
                    }
                    return newFile;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

