package GUI.Gallery.model.Comparator;

import GUI.Gallery.singleton.SettingsLoader;
import javafx.scene.Node;

import java.util.Comparator;

public class ImageViewComparatorifNeed implements Comparator<Node> {

    @Override
    public int compare(Node n1, Node n2) {
        if (SettingsLoader.getInstance().isByName() && SettingsLoader.getInstance().isNewDown()) {
            return (n1.getId().compareTo(n2.getId()));
        }
        if (SettingsLoader.getInstance().isByName() && SettingsLoader.getInstance().isNewUp()) {
            return ((n1.getId().compareTo(n2.getId())) * -1);
        }
        return 0;
    }

}
