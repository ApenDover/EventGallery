package GUI.Gallery.storage;

import GUI.Gallery.setUp.SettingsLoader;
import javafx.scene.image.ImageView;

import java.util.Comparator;

public class ImageViewComparator implements Comparator<ImageView> {

    @Override
    public int compare(ImageView o1, ImageView o2) {
        if (SettingsLoader.getInstance().isByName() && SettingsLoader.getInstance().isNewDown()) {
            return (o1.getId().compareTo(o2.getId()));
        }
        if (SettingsLoader.getInstance().isByName() && SettingsLoader.getInstance().isNewUp()) {
            return ((o1.getId().compareTo(o2.getId())) * -1);
        }
        return 0;
    }

}
