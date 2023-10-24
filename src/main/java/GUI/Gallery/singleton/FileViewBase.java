package GUI.Gallery.singleton;

import GUI.Gallery.utils.FileStringConverter;
import lombok.Getter;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

@Getter
public class FileViewBase {

    private static FileViewBase instance;

    private final Set<String> allOriginalFileNames = new TreeSet<>();

    private final Set<File> allImgOriginalFilePath = new TreeSet<>();

    private final Set<File> allMovieOriginalFilePath = new TreeSet<>();

    private final Set<String> allNamesPreviewResized = new TreeSet<>();

    private final Set<String> allFullPathPreviewImages = new TreeSet<>();

    private final Set<String> imgExtension = new HashSet<>(List.of("jpg", "jpeg", "png", "JPG", "JPEG", "PNG"));

    private final Set<String> movieExtension = new HashSet<>(List.of("mov", "mp4", "gif", "MOV", "MP4", "GIF"));

    private final Map<String, String> fileNamesMap = new TreeMap<>();

    private FileViewBase() {
    }

    public static FileViewBase getInstance() {
        if (Objects.isNull(instance)) {
            instance = new FileViewBase();
        }
        return instance;
    }

    public void init() {
        fileNamesMap.clear();
        allOriginalFileNames.clear();
        allNamesPreviewResized.clear();
        allImgOriginalFilePath.clear();
        allMovieOriginalFilePath.clear();
        allFullPathPreviewImages.clear();

        File[] files = new File(SettingsLoader.getInstance().getSourceFolder()).listFiles(); //перебираем все файлы в srcFolder
        if (Objects.nonNull(files) && files.length > 0) {
            findFolderContent(files);
            findPreviewImages();
        }
        allNamesPreviewResized.forEach(s -> {
            String ex = fileNamesMap.get(s);
            allFullPathPreviewImages.add(FileStringConverter.getNameWithEx(s, ex));
        });

    }

    private void findPreviewImages() {
        File folder = new File(SettingsLoader.getInstance().getSourceFolder(), SettingsLoader.getInstance().getQualityResizer()); //перебираем все файлы в dstFolder
        if (folder.exists()) {
            File[] previewFiles = new File(SettingsLoader.getInstance().getSourceFolder(), SettingsLoader.getInstance().getQualityResizer()).listFiles();
            if (Objects.nonNull(previewFiles)) {
                for (File file : previewFiles) {
                    if (imgExtension.contains(FileStringConverter.getExtension(file))) {
                        allNamesPreviewResized.add(FileStringConverter.getName(file));
                    }
                }
            }
        } else {
            folder.mkdir();
        }
    }

    private void findFolderContent(File[] files) {
        for (File file : files) {
            if (file.getName().charAt(0) != '.' && !file.isDirectory() && !file.getName().equals("config.json")) //берем только нужные
            {
                String fileName = FileStringConverter.getName(file);
                String fileFormat = FileStringConverter.getExtension(file);
                fileNamesMap.put(fileName, fileFormat);
                allOriginalFileNames.add(FileStringConverter.getName(file));
                if (imgExtension.contains(FileStringConverter.getExtension(file))) {
                    allImgOriginalFilePath.add(file);
                }
                if (movieExtension.contains(FileStringConverter.getExtension(file))) {
                    allMovieOriginalFilePath.add(file);
                }
            }
        }
    }

}

