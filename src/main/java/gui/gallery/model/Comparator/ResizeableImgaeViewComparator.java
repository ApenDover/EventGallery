package gui.gallery.model.Comparator;

import gui.gallery.model.Resizeable;

import java.util.Comparator;

public class ResizeableImgaeViewComparator implements Comparator<Resizeable> {

    @Override
    public int compare(Resizeable n1, Resizeable n2) {
        return n1.getResizedImageContainer().getImageView().getId().compareTo(n2.getResizedImageContainer().getImageView().getId());
    }

}
