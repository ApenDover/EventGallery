package gui.gallery.runnable;

import gui.gallery.utils.imageResizer.ImgScaleProcessor;
import javafx.scene.control.TextField;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public class ScalePreviewImagesProcess implements Runnable {

    private final TextField pathField;

    public ScalePreviewImagesProcess(TextField pathField) {
        this.pathField = pathField;
    }

    public void run() {
        File[] allFiles = new File(pathField.getText()).listFiles();
        if (Objects.nonNull(allFiles)) {
            final var fileList = List.of(allFiles);
            final Set<File> fileInFolder = new TreeSet<>();
            fileList.parallelStream().forEach(file -> {
                if (!file.isDirectory() && file.getName().charAt(0) != '.' && !Objects.equals(file.getName(), "config.json")) {
                    fileInFolder.add(file);
                }
            });
            if (!fileInFolder.isEmpty()) {
                ImgScaleProcessor.scale(fileInFolder);
            }
        }
    }

}
