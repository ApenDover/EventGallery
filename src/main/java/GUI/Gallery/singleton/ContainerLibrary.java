package GUI.Gallery.singleton;

import GUI.Gallery.model.AbstractContainer;
import GUI.Gallery.model.ImageContainer;
import GUI.Gallery.model.Resizeable;
import GUI.Gallery.model.ResizedImageContainer;
import GUI.Gallery.model.VideoContainer;
import javafx.scene.image.ImageView;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class ContainerLibrary {

    private static ContainerLibrary instance;

    private final LinkedHashSet<Resizeable> resizeableLinkedHashSet = new LinkedHashSet<>();

    private final LinkedHashSet<ImageView> saveImageView = new LinkedHashSet<>();

    private final Set<ResizedImageContainer> resizedImageContainerHashSet = new HashSet<>();

    public static ContainerLibrary getInstance() {
        if (Objects.isNull(instance)) {
            instance = new ContainerLibrary();
        }
        return instance;
    }

    public void addContainerToLibrary(AbstractContainer container) {
        if (container instanceof ImageContainer imageContainer) {
            resizedImageContainerHashSet.add(imageContainer.getResized());
            resizeableLinkedHashSet.add(imageContainer);
        }
        if (container instanceof VideoContainer videoContainer) {
            resizedImageContainerHashSet.add(videoContainer.getResized());
            resizeableLinkedHashSet.add(videoContainer);
        }
    }

    public void removeResizedWithoutOriginal() {
        final var forRemoveResized = resizedImageContainerHashSet.stream()
                .filter(resizedImageContainer -> !resizedImageContainer.isOriginalAlive()).toList();
        forRemoveResized.forEach(resizedImageContainer -> {
            resizeableLinkedHashSet.remove(resizedImageContainer.getOriginalContainer());
            resizedImageContainer.getFile().delete();
        });
        forRemoveResized.forEach(resizedImageContainerHashSet::remove);
    }

    public LinkedHashSet<ImageView> getSaveImageView() {
        return saveImageView;
    }

    public int getResizeableSize() {
        return resizeableLinkedHashSet.size();
    }

    public Set<Resizeable> getResizeableLinkedHashSet() {
        return new LinkedHashSet<>(resizeableLinkedHashSet);
    }

    public void resizeAll() {
        resizeableLinkedHashSet.stream().filter(resizeable -> !resizeable.isResized()).forEach(Resizeable::createResizePreview);
    }


}
