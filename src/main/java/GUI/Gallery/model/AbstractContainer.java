package GUI.Gallery.model;

import javafx.scene.Node;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.File;
import java.util.Objects;

@RequiredArgsConstructor
@Getter
public class AbstractContainer {

    private final File file;

    private final String path;

    private final String fileName;

    private final String extension;

    @Setter
    private double width;

    @Setter
    private double height;

    public Node getNode() {
        return null;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractContainer that)) return false;
        return Objects.equals(getFile(), that.getFile()) && Objects.equals(getPath(), that.getPath()) && Objects.equals(getFileName(), that.getFileName()) && Objects.equals(getExtension(), that.getExtension());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFile(), getPath(), getFileName(), getExtension());
    }
}
