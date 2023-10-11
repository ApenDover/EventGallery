package GUI.Gallery.storage;

import GUI.Gallery.setUp.SettingsLoader;
import javafx.scene.image.ImageView;

import java.util.Comparator;

public class ImageViewComparator implements Comparator<ImageView> {

    @Override
    public int compare(ImageView o1, ImageView o2) {
        if (SettingsLoader.isByName() && SettingsLoader.isNewDown()) {
            return (o1.getId().compareTo(o2.getId()));
        }
        if (SettingsLoader.isByName() && SettingsLoader.isNewUp()) {
            return ((o1.getId().compareTo(o2.getId())) * -1);
        }
        return 0;
    }

}
