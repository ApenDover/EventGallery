package GUI.Gallery.singleton;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class LinkTransfer {

    private static LinkTransfer instance;

    private String link;

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
