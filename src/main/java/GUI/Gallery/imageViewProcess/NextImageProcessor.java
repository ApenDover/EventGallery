package GUI.Gallery.imageViewProcess;

import GUI.Gallery.model.Resizeable;
import GUI.Gallery.singleton.ContainerLibrary;
import GUI.Gallery.singleton.LinkTransfer;
import javafx.scene.Node;
import javafx.scene.media.MediaView;

import java.util.ArrayList;

public class NextImageProcessor {

    public Node secondImage(boolean target) {
        ArrayList<Resizeable> allGalleryImageView = new ArrayList<>(ContainerLibrary.getInstance().getResizeableLinkedHashSet());
        allGalleryImageView.indexOf(LinkTransfer.getInstance().getResizeable());
        int num = allGalleryImageView.indexOf(LinkTransfer.getInstance().getResizeable());
        final int next;
        final int lastNumber;
        final int find;

        if (target) {
            next = num + 1;
            lastNumber = 0;
            find = allGalleryImageView.size() - 1;
        } else {
            next = num - 1;
            lastNumber = allGalleryImageView.size() - 1;
            find = 0;
        }

        final Resizeable file;
        if (num != find) {
            file = allGalleryImageView.get(next);
        } else {
            file = allGalleryImageView.get(lastNumber);
        }
        LinkTransfer.getInstance().setResizeable(file.getResizedImageContainer().getOriginalContainer());
        final var node = file.getResizedImageContainer().getOriginalContainer().getNode();
        if (node instanceof MediaView mediaView) {
            mediaView.getMediaPlayer().play();
        }
        return node;
    }

}
