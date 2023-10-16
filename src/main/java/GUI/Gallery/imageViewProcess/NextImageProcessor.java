package GUI.Gallery.imageViewProcess;

import GUI.Gallery.storage.FileViewBase;
import GUI.Gallery.storage.LinkTransfer;
import GUI.Gallery.utils.FileStringConverter;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaView;

import java.util.List;

public class NextImageProcessor {

    private final PictureBuilder pictureBuilder;

    private final MovieBuilder movieBuilder;

    public NextImageProcessor(ImageView imageView, MediaView mediaView) {
        pictureBuilder = new PictureBuilder(imageView);
        movieBuilder = new MovieBuilder(mediaView, imageView);
    }

    public void createImage(String file) {
        pictureBuilder.createPictureImageView(file);
    }

    public void secondImage(boolean target, List<String> allGalleryImageView) {
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
            exNext = FileStringConverter.getExtension(allGalleryImageView.get(next));
            if (FileViewBase.getImgExtension().contains(exNext)) {
                pictureBuilder.buildImageView(allGalleryImageView.get(next));

            }
            if (FileViewBase.getMovieExtension().contains(exNext)) {
                movieBuilder.createMovie(allGalleryImageView.get(next));
            }
        } else {
            exNext = FileStringConverter.getExtension(allGalleryImageView.get(lastNumber));
            if (FileViewBase.getImgExtension().contains(exNext)) {
                pictureBuilder.buildImageView(allGalleryImageView.get(lastNumber));
            }
            if (FileViewBase.getMovieExtension().contains(exNext)) {
                movieBuilder.createMovie(allGalleryImageView.get(lastNumber));
            }
        }
    }

    public void createMovie(String link) {
        movieBuilder.createMovie(link);
    }
}
