package gui.gallery.model.Comparator;

import gui.gallery.singleton.SettingsLoader;

import java.io.File;
import java.util.Comparator;

public class FileComparatorByName implements Comparator<File> {

    @Override
    public int compare(File n1, File n2) {
        final int compareResult = n1.getName().compareTo(n2.getName());
        if (SettingsLoader.getInstance().isNewUp()) {
            return compareResult * -1;
        }
        return compareResult;
    }

}
