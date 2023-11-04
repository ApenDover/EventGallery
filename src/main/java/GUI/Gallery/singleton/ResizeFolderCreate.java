package GUI.Gallery.singleton;

import java.io.File;
import java.util.Objects;

public class ResizeFolderCreate {

    private static ResizeFolderCreate instance;

    public static ResizeFolderCreate getInstance() {
        if (Objects.isNull(instance)) {
            File dirResize = new File(SettingsLoader.getInstance().getQualityResizeFolder());
            dirResize.mkdir();
            FileViewBase.getInstance().init();
            instance = new ResizeFolderCreate();
        }
        return instance;
    }

}
