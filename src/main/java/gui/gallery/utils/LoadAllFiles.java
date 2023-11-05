package gui.gallery.utils;

import gui.gallery.model.Comparator.FileComparatorByName;
import gui.gallery.model.ContainerFactory;
import gui.gallery.model.Resizeable;
import gui.gallery.singleton.ContainerLibrary;
import gui.gallery.singleton.SettingsLoader;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@UtilityClass
public class LoadAllFiles {

    private static final ContainerFactory CONTAINER_FACTORY = new ContainerFactory();

    private static final FileComparatorByName COMPARATOR = new FileComparatorByName();

    private final File dirResize = new File(SettingsLoader.getInstance().getQualityResizeFolder());

    public void load() {
        dirResize.mkdir();
        final var sourceFolder = new File(SettingsLoader.getInstance().getSourceFolder());
        if (!sourceFolder.exists()) {
            throw new IllegalArgumentException("Source folder doesn't exist");
        }
        List<File> fileList = new ArrayList<>(Arrays.asList(Objects.requireNonNull(sourceFolder.listFiles())));

        ContainerLibrary.getInstance().removeResizedWithoutOriginal();

        ContainerLibrary.getInstance().getResizeableLinkedHashSet()
                .parallelStream().filter(resizeable -> !resizeable.getResizedImageContainer().isAlive())
                .forEach(Resizeable::createResizePreview);

        fileList.sort(COMPARATOR);
        fileList.removeAll(ContainerLibrary.getInstance()
                .getResizeableLinkedHashSet().stream()
                .map(resizeable -> resizeable.getResizedImageContainer().getOriginalContainer().getFile()).toList());
        fileList.forEach(file -> ContainerLibrary.getInstance().addContainerToLibrary(CONTAINER_FACTORY.createContainer(file)));
    }

}
