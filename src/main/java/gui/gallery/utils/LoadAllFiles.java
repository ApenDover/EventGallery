package gui.gallery.utils;

import gui.gallery.model.ContainerFactory;
import gui.gallery.model.Resizeable;
import gui.gallery.singleton.ContainerLibrary;
import gui.gallery.singleton.SettingsConst;
import gui.gallery.singleton.SettingsLoader;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@UtilityClass
public class LoadAllFiles {

    private final List<String> notLoadFileList = List.of("config.json",
            String.valueOf(SettingsConst.SCALE_RESIZE_LONG_SIDE.getValue()), ".DS_Store");

    private final ContainerFactory containerFactory = new ContainerFactory();

    private final File dirResize = new File(SettingsLoader.getInstance().getQualityResizeFolder());

    public void load() {
        dirResize.mkdir();
        final var sourceFolder = new File(SettingsLoader.getInstance().getSourceFolder());
        if (!sourceFolder.exists()) {
            throw new IllegalArgumentException("Source folder doesn't exist");
        }
        final var fileList = new ArrayList<>(Arrays.asList(Objects.requireNonNull(sourceFolder.listFiles())));

        ContainerLibrary.getInstance().removeResizedWithoutOriginal();

        ContainerLibrary.getInstance().getResizeableStorage()
                .parallelStream().filter(resizeable -> !resizeable.getResizedImageContainer().isAlive())
                .forEach(Resizeable::createResizePreview);

        fileList.removeAll(ContainerLibrary.getInstance()
                .getResizeableStorage().stream()
                .map(resizeable -> resizeable.getResizedImageContainer().getOriginalContainer().getFile()).toList());
        log.debug("RESIZE START: " + Instant.now());
        try {
            fileList.parallelStream().filter(file -> !notLoadFileList.contains(file.getName()))
                    .forEach(file -> ContainerLibrary.getInstance().addContainerToLibrary(containerFactory.createContainer(file)));
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        log.debug("RESIZE END: " + Instant.now());
    }

}
