package GUI.Gallery.utils;

import GUI.Gallery.model.Comparator.FileComparatorByName;
import GUI.Gallery.model.ContainerFactory;
import GUI.Gallery.singleton.ContainerLibrary;
import GUI.Gallery.singleton.SettingsLoader;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@UtilityClass
public class LoadAllFiles {

    private final ContainerFactory CONTAINER_FACTORY = new ContainerFactory();

    private final FileComparatorByName comparator = new FileComparatorByName();

    private final File dirResize = new File(SettingsLoader.getInstance().getQualityResizeFolder());

    public void load() {
        dirResize.mkdir();
        final var sourceFolder = new File(SettingsLoader.getInstance().getSourceFolder());
        if (!sourceFolder.exists()) {
            throw new IllegalArgumentException("Source folder doesn't exist");
        }

        List<File> fileList = new ArrayList<>(Arrays.asList(Objects.requireNonNull(sourceFolder.listFiles())));

        ContainerLibrary.getInstance().removeResizedWithoutOriginal();

        fileList.sort(comparator);
        try {
            fileList.removeAll(ContainerLibrary.getInstance()
                    .getResizeableLinkedHashSet().stream()
                    .map(resizeable -> resizeable.getResizedImageContainer().getOriginalContainer().getFile()).toList());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        for (File file : fileList) {
            ContainerLibrary.getInstance().addContainerToLibrary(CONTAINER_FACTORY.createContainer(file));
        }

    }

}
