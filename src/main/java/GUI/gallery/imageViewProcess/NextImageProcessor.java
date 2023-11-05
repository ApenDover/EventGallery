package gui.gallery.imageViewProcess;

import gui.gallery.singleton.LinkTransfer;
import gui.gallery.model.Resizeable;
import gui.gallery.singleton.ContainerLibrary;
import javafx.scene.Node;
import javafx.scene.media.MediaView;

import java.util.ArrayList;

public class NextImageProcessor {

    public Node secondImage(boolean target) {
        ArrayList<Resizeable> allResizeable = new ArrayList<>(ContainerLibrary.getInstance().getResizeableLinkedHashSet());
        allResizeable.indexOf(LinkTransfer.getInstance().getResizeable());
        int num = allResizeable.indexOf(LinkTransfer.getInstance().getResizeable());
        final int next;
        final int lastNumber;
        final int find;

        if (target) {
            next = num + 1;
            lastNumber = 0;
            find = allResizeable.size() - 1;
        } else {
            next = num - 1;
            lastNumber = allResizeable.size() - 1;
            find = 0;
        }

        final Resizeable file;
        if (num != find) {
            file = allResizeable.get(next);
        } else {
            file = allResizeable.get(lastNumber);
        }
        LinkTransfer.getInstance().setResizeable((Resizeable) file.getResizedImageContainer().getOriginalContainer());
        final var node = file.getResizedImageContainer().getOriginalContainer().getNode();
        if (node instanceof MediaView) {
            final var mediaView = (MediaView) node;
            mediaView.getMediaPlayer().play();
        }
        return node;
    }

}
