package GUI.Gallery.model;

public interface Resizeable {

    boolean isResized();

    void createResizePreview();

    ResizedImageContainer getResizedImageContainer();

    boolean isAlive();

}
