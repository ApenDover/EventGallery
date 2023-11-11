package gui.gallery.singleton;

import gui.gallery.model.Comparator.ResizeableImgaeViewComparator;
import gui.gallery.model.VideoContainer;
import gui.gallery.model.AbstractContainer;
import gui.gallery.model.ImageContainer;
import gui.gallery.model.Resizeable;
import javafx.animation.Timeline;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;

@Slf4j
public class ContainerLibrary {

    private static ContainerLibrary instance;

    @Getter
    @Setter
    private Timeline fiveSecondsWonder;

    @Getter
    private boolean firstLoad;

    private final LinkedHashSet<Resizeable> resizeableStorage = new LinkedHashSet<>();

    private final ResizeableImgaeViewComparator resizeableComparator = new ResizeableImgaeViewComparator();

    private final TreeSet<Resizeable> sortedAddedResizeableForStorage = new TreeSet<>(resizeableComparator);

    public static synchronized ContainerLibrary getInstance() {
        if (Objects.isNull(instance)) {
            instance = new ContainerLibrary();
        }
        return instance;
    }

    public void addContainerToLibrary(AbstractContainer container) {
        if (!Objects.isNull(container)) {
            log.debug("ADD " + container.getFile().getName() + ": " + Instant.now());
            if (container instanceof ImageContainer) {
                sortedAddedResizeableForStorage.add((ImageContainer) container);
            }
            if (container instanceof VideoContainer) {
                sortedAddedResizeableForStorage.add((VideoContainer) container);
            }
        }
    }

    private void actualizeResizableLinkedHashSet() {
        resizeableStorage.addAll(sortedAddedResizeableForStorage);
        sortedAddedResizeableForStorage.clear();
    }

    public void removeResizedWithoutOriginal() {
        final var forRemoveResized = resizeableStorage.stream()
                .filter(resizeable -> !resizeable.isOriginalAlive()).toList();
        forRemoveResized.forEach(resizeable -> {
            try {
                resizeableStorage.remove(resizeable);
                Files.delete(resizeable.getResizedImageContainer().getFile().toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public synchronized List<Resizeable> getResizeableStorage() {
        if (!sortedAddedResizeableForStorage.isEmpty()) {
            actualizeResizableLinkedHashSet();
        }
        final var getResizeable = new ArrayList<>(resizeableStorage);
        if (SettingsLoader.getInstance().isByName()) {
            getResizeable.sort(resizeableComparator);
        }
        if (SettingsLoader.getInstance().isNewUp()) {
            Collections.reverse(getResizeable);
            return getResizeable;
        }
        return getResizeable;
    }

    public List<ImageView> getResizeableImageViewList() {
        if (!firstLoad && !resizeableStorage.isEmpty()) {
            firstLoad = true;
        }
        return new ArrayList<>(getResizeableStorage().stream()
                .map(resizeable -> resizeable.getResizedImageContainer().getImageView()).toList());
    }

}
