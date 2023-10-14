package GUI.Gallery.runnable;

import GUI.Gallery.imageResizer.ImgScaleProcessor;
import javafx.scene.control.TextField;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;

public class ScalePreviewImagesProcess implements Runnable {

    private final TextField pathField;

    public ScalePreviewImagesProcess(TextField pathField) {
        this.pathField = pathField;
    }

    public void run() {
        File[] allFiles = new File(pathField.getText()).listFiles();
        if (Objects.nonNull(allFiles)) {
            List<File> fileList = List.of(allFiles);
            TreeSet<File> fileInFolder = new TreeSet<>();
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
