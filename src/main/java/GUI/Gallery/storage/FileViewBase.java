package GUI.Gallery.storage;

import GUI.Gallery.setUp.SettingsLoader;
import lombok.Getter;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class FileViewBase {

    @Getter
    private static Set<String> stringTreeSet = new TreeSet<>();

    @Getter
    private static Set<File> fileTreeSet = new TreeSet<>();

    @Getter
    private static Set<File> filesMovieSrc = new TreeSet<>();

    @Getter
    private static Set<String> namesFilesDst = new TreeSet<>();

    @Getter
    private static Set<String> allFullNamesFilesDst = new TreeSet<>();

    @Getter
    private static Set<String> imgExtension = new HashSet<>(List.of("jpg", "jpeg", "png"));

    @Getter
    private static Set<String> movieExtension = new HashSet<>(List.of("mov", "mp4", "gif"));

    @Getter
    private static Map<String, String> fileNamesMap = new TreeMap<>();

    public FileViewBase() {
        getFileNamesMap().clear();
        getStringTreeSet().clear();
        getNamesFilesDst().clear();
        fileTreeSet.clear();
        filesMovieSrc.clear();
        getAllFullNamesFilesDst().clear();

        File[] files = new File(SettingsLoader.getSourceFolder()).listFiles(); //перебираем все файлы в srcFolder
        if (files.length != 0) {
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

            File folder = new File(SettingsLoader.getSourceFolder(), SettingsLoader.getQualityResizer()); //перебираем все файлы в dstFolder
            if (folder.exists()) {
                File[] dstFiles = new File(SettingsLoader.getSourceFolder(), SettingsLoader.getQualityResizer()).listFiles();
                for (File file : dstFiles) {
                    if (getImgExtension().contains(file.getName().substring(file.getName().lastIndexOf('.') + 1))) {
                        getNamesFilesDst().add(file.getName().substring(0, file.getName().lastIndexOf('.')));
                    }
                }
            } else {
                folder.mkdir();
            }
        }

        getNamesFilesDst().forEach(s -> {
            String ex = getFileNamesMap().get(s);
            getAllFullNamesFilesDst().add(s + "." + ex);
        });

    }

}

