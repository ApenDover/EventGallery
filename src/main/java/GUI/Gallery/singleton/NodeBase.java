package GUI.Gallery.singleton;

import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.TreeSet;

@Getter
@Setter
public class NodeBase{

    private static NodeBase instance;

    private TreeSet<ImageView> imageViewTreeContainer = new TreeSet<>(new ImageViewComparator());

    private LinkedHashSet<ImageView> imageViewLinkedHashContainer = new LinkedHashSet<>();

    private NodeBase() {
    }

    public static NodeBase getInstance() {
        if (Objects.isNull(instance)) {
            instance = new NodeBase();
        }
        return instance;
    }

}

