package gui.gallery.singleton;

import gui.gallery.model.VideoContainer;
import gui.gallery.model.AbstractContainer;
import gui.gallery.model.ImageContainer;
import gui.gallery.model.Resizeable;
import javafx.animation.Timeline;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ContainerLibrary {

    private static ContainerLibrary instance;

    @Getter
    @Setter
    private Timeline fiveSecondsWonder;

    private final LinkedHashSet<Resizeable> resizeableLinkedHashSet = new LinkedHashSet<>();

    public static ContainerLibrary getInstance() {
        if (Objects.isNull(instance)) {
            instance = new ContainerLibrary();
        }
        return instance;
    }

    public void addContainerToLibrary(AbstractContainer container) {
        if (container instanceof ImageContainer) {
            resizeableLinkedHashSet.add((ImageContainer) container);
        }
        if (container instanceof VideoContainer) {
            resizeableLinkedHashSet.add((VideoContainer) container);
        }
    }

    public void removeResizedWithoutOriginal() {
        final var forRemoveResized = resizeableLinkedHashSet.stream()
                .filter(resizeable -> !resizeable.isOriginalAlive()).toList();
        forRemoveResized.forEach(resizeable -> {
            try {
                resizeableLinkedHashSet.remove(resizeable);
                Files.delete(resizeable.getResizedImageContainer().getFile().toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public Set<Resizeable> getResizeableLinkedHashSet() {
        return new LinkedHashSet<>(resizeableLinkedHashSet);
    }

    public List<ImageView> getResizeableImageViewList() {
        return resizeableLinkedHashSet.stream().map(resizeable -> resizeable.getResizedImageContainer().getImageView()).toList();
    }


}
