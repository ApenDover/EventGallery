package GUI.Gallery.model.Comparator;

import GUI.Gallery.singleton.SettingsLoader;
import javafx.scene.Node;

import java.io.File;
import java.util.Comparator;

public class FileComparatorByName implements Comparator<File> {

    @Override
    public int compare(File o1, File o2) {
        if (SettingsLoader.getInstance().isByName() && SettingsLoader.getInstance().isNewDown()) {
            return (o1.getName().compareTo(o2.getName()));
        }
        if (SettingsLoader.getInstance().isByName() && SettingsLoader.getInstance().isNewUp()) {
            return (o1.getName().compareTo(o2.getName()) * -1);
        }
        return 0;
    }

}
