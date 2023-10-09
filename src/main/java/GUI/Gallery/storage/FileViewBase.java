package GUI.Gallery.storage;

import GUI.Gallery.setUp.SettingsLoader;

import java.io.File;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class FileViewBase {
    public static TreeSet<String> namesFilesSRC = new TreeSet<>();
    public static TreeSet<File> filesImgSRC = new TreeSet<>();
    public static TreeSet<File> filesMovieSRC = new TreeSet<>();
    public static TreeSet<String> namesFilesDST = new TreeSet<>();
    public static TreeSet<String> allFullNamesFilesDST = new TreeSet<>();
    public static HashSet<String> imgExtension = new HashSet<>();
    public static HashSet<String> movieExtension = new HashSet<>();
    public static TreeMap<String, String> fileNamesMap = new TreeMap<>();
    public FileViewBase() {

        imgExtension.add("jpg");
        imgExtension.add("JPG");
        imgExtension.add("jpeg");
        imgExtension.add("JPEG");
        imgExtension.add("png");
        imgExtension.add("PNG");
        movieExtension.add("mov");
        movieExtension.add("MOV");
        movieExtension.add("mp4");
        movieExtension.add("MP4");
        movieExtension.add("gif");
        movieExtension.add("GIF");

        fileNamesMap.clear();
        namesFilesSRC.clear();
        namesFilesDST.clear();
        filesImgSRC.clear();
        filesMovieSRC.clear();
        allFullNamesFilesDST.clear();

        File[] files = new File(SettingsLoader.getSourseFolder()).listFiles(); //перебираем все файлы в srcFolder
        if (files.length != 0) {
            for (File file : files) {
                if ((!(file.getName().charAt(0) == '.')) & (!(file.isDirectory()) & (!(file.getName().equals("config.json"))))) //берем только нужные
                {
                    String fileName = file.getName().substring(0, file.getName().lastIndexOf('.'));
                    String fileFormat = file.getName().substring(file.getName().lastIndexOf('.') + 1);
                    fileNamesMap.put(fileName, fileFormat);
                    namesFilesSRC.add(file.getName().substring(0, file.getName().lastIndexOf('.')));

                    if (FileViewBase.imgExtension.contains(file.getName().substring(file.getName().lastIndexOf('.') + 1))) {
                        filesImgSRC.add(file);
                    }
                    if (FileViewBase.movieExtension.contains(file.getName().substring(file.getName().lastIndexOf('.') + 1))) {
                        filesMovieSRC.add(file);
                    }
                }
            }

            File folder = new File(SettingsLoader.getSourseFolder() + "/" + SettingsLoader.getQualityResizer()); //перебираем все файлы в dstFolder
            if (folder.exists()) {
                File[] dstFiles = new File(SettingsLoader.getSourseFolder() + "/" + SettingsLoader.getQualityResizer()).listFiles();
                for (File file : dstFiles) {
                    if (imgExtension.contains(file.getName().substring(file.getName().lastIndexOf('.')+1))) {
                        namesFilesDST.add(file.getName().substring(0, file.getName().lastIndexOf('.')));
                    }
                }
            } else {
                folder.mkdir();
            }
        }

        namesFilesDST.forEach(s -> {
            String ex = fileNamesMap.get(s);
            allFullNamesFilesDST.add(s + "." + ex);
        });

    }

}

