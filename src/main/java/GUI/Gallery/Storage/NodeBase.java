package GUI.Gallery.Storage;

import javafx.scene.image.ImageView;

import java.util.LinkedHashSet;
import java.util.TreeSet;

public class NodeBase{
    public static TreeSet<ImageView> imageViewTreeConteiner = new TreeSet<>(new ImageViewComparator());
    public static LinkedHashSet<ImageView> imageViewLinkedHashConteiner = new LinkedHashSet<>();
}

