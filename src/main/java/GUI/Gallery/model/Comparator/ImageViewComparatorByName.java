package GUI.Gallery.model.Comparator;

import GUI.Gallery.singleton.SettingsLoader;
import javafx.scene.Node;

import java.util.Comparator;

public class ImageViewComparatorByName implements Comparator<Node> {

    @Override
    public int compare(Node o1, Node o2) {
        if (SettingsLoader.getInstance().isByName() && SettingsLoader.getInstance().isNewDown()) {
            return (o1.getId().compareTo(o2.getId()));
        }
        if (SettingsLoader.getInstance().isByName() && SettingsLoader.getInstance().isNewUp()) {
            return ((o1.getId().compareTo(o2.getId())) * -1);
        }
        return 0;
    }

}
