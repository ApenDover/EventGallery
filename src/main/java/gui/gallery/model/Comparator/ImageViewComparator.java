package gui.gallery.model.Comparator;

import javafx.scene.Node;

import java.util.Comparator;

public class ImageViewComparator implements Comparator<Node> {

    @Override
    public int compare(Node n1, Node n2) {
            return (n1.getId().compareTo(n2.getId()));
    }

}
