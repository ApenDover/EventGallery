package GUI.Gallery.model.Comparator;

import GUI.Gallery.singleton.SettingsLoader;
import javafx.scene.Node;

import java.util.Comparator;

public class ImageViewComparator implements Comparator<Node> {

    @Override
    public int compare(Node o1, Node o2) {
        return (o1.getId().compareTo(o2.getId()));
    }

}
