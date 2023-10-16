package GUI.Gallery.imageViewProcess;

import GUI.Gallery.storage.FileViewBase;
import GUI.Gallery.storage.LinkTransfer;
import GUI.Gallery.utils.FileStringConverter;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaView;

import java.util.List;

public class NextImageProcessor {

    private final PictureBuilder pictureBuilder;

    private final MovieBuilder movieBuilder;

    public NextImageProcessor() {
        pictureBuilder = new PictureBuilder();
        movieBuilder = new MovieBuilder();
    }

    public ImageView createImage(String file) {
        return pictureBuilder.createPictureImageView(file);
    }

    public MediaView createMovie(String file) {
        return movieBuilder.createMovie(file);
    }

    public Node secondImage(boolean target, List<String> allGalleryImageView) {
        int num = allGalleryImageView.indexOf(LinkTransfer.getLink());
        final String exNext;
        final int next;
        final int lastNumber;
        final int find;

        if (target) {
            next = num + 1;
            lastNumber = 0;
            find = allGalleryImageView.size() - 1;
        } else {
            next = num - 1;
            lastNumber = allGalleryImageView.size() - 1;
            find = 0;
        }

        if (num != find) {
            final var file = allGalleryImageView.get(next);
            LinkTransfer.setLink(file);
            exNext = FileStringConverter.getExtension(file);
            if (FileViewBase.getImgExtension().contains(exNext)) {
                LinkTransfer.setLink(file);
                return pictureBuilder.buildImageView(file);
            }
            if (FileViewBase.getMovieExtension().contains(exNext)) {
                return movieBuilder.createMovie(file);
            }
        } else {
            final var file = allGalleryImageView.get(lastNumber);
            LinkTransfer.setLink(file);
            exNext = FileStringConverter.getExtension(file);
            if (FileViewBase.getImgExtension().contains(exNext)) {
                return pictureBuilder.buildImageView(file);
            }
            if (FileViewBase.getMovieExtension().contains(exNext)) {
                return movieBuilder.createMovie(file);
            }
        }
        throw new RuntimeException();
    }

}