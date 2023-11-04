package GUI.Gallery.model;

import GUI.Gallery.utils.FileStringConverter;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ContainerFactory {

    private static final Set<String> IMG_EXTENSION = new HashSet<>(List.of("jpg", "jpeg", "png", "JPG", "JPEG", "PNG"));

    private static final Set<String> MOVIE_EXTENSION = new HashSet<>(List.of("mov", "mp4", "gif", "MOV", "MP4", "GIF"));

    public AbstractContainer createContainer(File file) {
        final var extension = FileStringConverter.getExtension(file);
        if (IMG_EXTENSION.contains(extension)) {
            return new ImageContainer(file);
        }
        if (MOVIE_EXTENSION.contains(extension)) {
            return new VideoContainer(file);
        }
        return null;
    }

}
