package GUI.Gallery.storage;

import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

public class StageContainer {

    @Getter
    @Setter
    private static Stage stage;

    @Getter
    @Setter
    private static Scene scene;

    private StageContainer() {
    }

}
