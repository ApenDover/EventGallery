package GUI.Gallery.storage;

import GUI.Gallery.setUp.SettingsLoader;
import lombok.Getter;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class FileViewBase {

    @Getter
    private static final Set<String> stringTreeSet = new TreeSet<>();

    @Getter
    private static final Set<File> fileTreeSet = new TreeSet<>();

    @Getter
    private static final Set<File> filesMovieSrc = new TreeSet<>();

    @Getter
    private static final Set<String> namesFilesDst = new TreeSet<>();

    @Getter
    private static final Set<String> allFullNamesFilesDst = new TreeSet<>();

    @Getter
    private static final Set<String> imgExtension = new HashSet<>(List.of("jpg", "jpeg", "png", "JPG", "JPEG", "PNG"));

    @Getter
    private static final Set<String> movieExtension = new HashSet<>(List.of("mov", "mp4", "gif", "MOV", "MP4", "GIF"));

    @Getter
    private static final Map<String, String> fileNamesMap = new TreeMap<>();

    private FileViewBase() {
    }

    public static void init() {
        fileNamesMap.clear();
        stringTreeSet.clear();
        namesFilesDst.clear();
        fileTreeSet.clear();
        filesMovieSrc.clear();
        allFullNamesFilesDst.clear();

        File[] files = new File(SettingsLoader.getSourceFolder()).listFiles(); //перебираем все файлы в srcFolder
        if (Objects.nonNull(files) && files.length > 0) {
            findFolderContent(files);
            findPreviewImages();
        }
        getNamesFilesDst().forEach(s -> {
            String ex = getFileNamesMap().get(s);
            getAllFullNamesFilesDst().add(s + "." + ex);
        });

    }

    private static void findPreviewImages() {
        File folder = new File(SettingsLoader.getSourceFolder(), SettingsLoader.getQualityResizer()); //перебираем все файлы в dstFolder
        if (folder.exists()) {
            File[] dstFiles = new File(SettingsLoader.getSourceFolder(), SettingsLoader.getQualityResizer()).listFiles();
            if (Objects.nonNull(dstFiles)) {
                for (File file : dstFiles) {
                    if (getImgExtension().contains(file.getName().substring(file.getName().lastIndexOf('.') + 1))) {
                        getNamesFilesDst().add(file.getName().substring(0, file.getName().lastIndexOf('.')));
                    }
                }
            }
        } else {
            folder.mkdir();
        }
    }

    private static void findFolderContent(File[] files) {
        for (File file : files) {
            if (file.getName().charAt(0) != '.' && file.isDirectory() && !file.getName().equals("config.json")) //берем только нужные
            {
                String fileName = file.getName().substring(0, file.getName().lastIndexOf('.'));
                String fileFormat = file.getName().substring(file.getName().lastIndexOf('.') + 1);
                getFileNamesMap().put(fileName, fileFormat);
                getStringTreeSet().add(file.getName().substring(0, file.getName().lastIndexOf('.')));

                if (FileViewBase.getImgExtension().contains(file.getName().substring(file.getName().lastIndexOf('.') + 1))) {
                    fileTreeSet.add(file);
                }
                if (FileViewBase.getMovieExtension().contains(file.getName().substring(file.getName().lastIndexOf('.') + 1))) {
                    filesMovieSrc.add(file);
                }
            }
        }
    }

}

