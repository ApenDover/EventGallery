package GUI.Gallery.storage;

import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.TreeSet;

public class NodeBase{

    @Getter
    @Setter
    private static TreeSet<ImageView> imageViewTreeConteiner = new TreeSet<>(new ImageViewComparator());

    @Getter
    @Setter
    private static LinkedHashSet<ImageView> imageViewLinkedHashConteiner = new LinkedHashSet<>();
}

