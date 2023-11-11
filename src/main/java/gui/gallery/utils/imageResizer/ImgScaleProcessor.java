package gui.gallery.utils.imageResizer;

import gui.gallery.singleton.SettingsConst;
import gui.gallery.singleton.SettingsLoader;
import lombok.experimental.UtilityClass;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.Objects;
import java.util.Set;

@UtilityClass
public class ImgScaleProcessor {

    public void scale(Set<File> imgFiles) {
        if (Objects.isNull(imgFiles)) {
            return;
        }
        imgFiles.parallelStream().forEach(ImgScaleProcessor::scale);
    }

    public File scale(File imgFile) {
        if (Objects.isNull(imgFile)) {
            return null;
        }
        try {
            if (imgFile.getAbsolutePath().toLowerCase().endsWith(".jpg")) {
                final var image = ImageIO.read(imgFile);
                if (Objects.nonNull(image)) {
                    final var newFile = new File(SettingsLoader.getInstance().getQualityResizeFolder(), imgFile.getName());
                    if (!newFile.exists()) {
                        final var newImage = Scalr.resize(image, SettingsConst.SCALE_RESIZE_LONG_SIDE.getValue());
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

