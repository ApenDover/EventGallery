package GUI.Gallery.singleton;

import GUI.Gallery.model.AbstractContainer;
import GUI.Gallery.model.ImageContainer;
import GUI.Gallery.model.Resizeable;
import GUI.Gallery.model.ResizedImageContainer;
import GUI.Gallery.model.VideoContainer;
import javafx.animation.Timeline;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;

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
        if (container instanceof ImageContainer imageContainer) {
            resizeableLinkedHashSet.add(imageContainer);
        }
        if (container instanceof VideoContainer videoContainer) {
            resizeableLinkedHashSet.add(videoContainer);
        }
    }

    public void removeResizedWithoutOriginal() {
        final var forRemoveResized = resizeableLinkedHashSet.stream()
                .filter(resizeable -> !resizeable.isAlive()).toList();
        forRemoveResized.forEach(resizeableLinkedHashSet::remove);
    }

    public Set<Resizeable> getResizeableLinkedHashSet() {
        return new LinkedHashSet<>(resizeableLinkedHashSet);
    }

    public List<ImageView> getResizeableImageViewList() {
        return resizeableLinkedHashSet.stream().map(resizeable -> resizeable.getResizedImageContainer().getImageView()).toList();
    }


}
