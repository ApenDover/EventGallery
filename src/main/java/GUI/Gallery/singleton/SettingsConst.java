package gui.gallery.singleton;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SettingsConst {

    COLOR_LENGTH_SHARP(7),

    COLOR_LENGTH(6),

    HEIGHT_RESIZE_NODE(1200),

    WIDTH_RESIZE_NODE(800),

    SCALE_RESIZE_LONG_SIDE(300);

    private final int value;

}
