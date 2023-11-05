package gui.gallery.model;

public interface Resizeable {

    boolean isResizedAlive();

    void createResizePreview();

    ResizedImageContainer getResizedImageContainer();

    boolean isOriginalAlive();

}
