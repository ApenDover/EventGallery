package gui.gallery.singleton;

import gui.gallery.model.Resizeable;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public final class LinkTransfer {

    private static LinkTransfer instance;

    private Resizeable resizeable;

    private boolean flag;

    private LinkTransfer() {
    }

    public static LinkTransfer getInstance() {
        if (Objects.isNull(instance)) {
            instance = new LinkTransfer();
        }
        return instance;
    }


}
