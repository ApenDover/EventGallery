package gui.gallery.singleton;

import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public final class StageContainer {

    private static StageContainer instance;

    private Stage stage;

    private Scene scene;

    private StageContainer() {
    }

    public static StageContainer getInstance() {
        if (Objects.isNull(instance)) {
            instance = new StageContainer();
        }
        return instance;
    }

}
