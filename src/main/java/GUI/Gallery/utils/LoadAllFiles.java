package GUI.Gallery.utils;

import GUI.Gallery.model.Comparator.FileComparatorByName;
import GUI.Gallery.model.Comparator.ImageViewComparatorByName;
import GUI.Gallery.model.ContainerFactory;
import GUI.Gallery.singleton.ContainerLibrary;
import GUI.Gallery.singleton.SettingsLoader;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@UtilityClass
public class LoadAllFiles {

    private static final ContainerFactory CONTAINER_FACTORY = new ContainerFactory();

    FileComparatorByName fileComparatorByName = new FileComparatorByName();

    public void load() {
        File dirResize = new File(SettingsLoader.getInstance().getQualityResizeFolder());
        dirResize.mkdir();
        final var originalArrayFiles = new File(SettingsLoader.getInstance().getSourceFolder()).listFiles();
        if (Objects.isNull(originalArrayFiles)) {
            throw new IllegalArgumentException("Source folder doesn't exist");
        }
        ContainerLibrary.getInstance().removeResizedWithoutOriginal();
        for (File file : originalArrayFiles) {
            ContainerLibrary.getInstance().addContainerToLibrary(CONTAINER_FACTORY.createContainer(file));
        }
    }

}
