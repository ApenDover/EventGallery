package GUI.Gallery.Storage;

import GUI.Gallery.SetUp.SettingsLoader;
import javafx.scene.image.ImageView;

import java.util.Comparator;

public class ImageViewComparator implements Comparator<ImageView>
{
    @Override
    public int compare(ImageView o1, ImageView o2) {
        if (SettingsLoader.byName & SettingsLoader.newDown) {
            return (o1.getId().compareTo(o2.getId()));
        }
        if (SettingsLoader.byName & SettingsLoader.newUp) {
            return ((o1.getId().compareTo(o2.getId())) * -1);
        }
        return 0;
    }
}
