package gui.gallery.model;

import gui.gallery.singleton.ContainerLibrary;
import gui.gallery.singleton.SettingsLoader;
import gui.gallery.utils.LoadAllFiles;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Objects;

class ImageContainerTest {

    private static final String RESOURCES_TEST_RESIZE_PATH = "src/test/resources/testSource/300";

    private static final String SOURCE_TEST_PATH = "src/test/resources/testSource";

    @BeforeEach
    void setup() {
        SettingsLoader.getInstance().setQualityResizeFolder(RESOURCES_TEST_RESIZE_PATH);
        SettingsLoader.getInstance().setSourceFolder(SOURCE_TEST_PATH);
        LoadAllFiles.load();
    }

    @AfterEach
    void delete() {
        try {
            Files.walk(Path.of(RESOURCES_TEST_RESIZE_PATH))
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void isImageContainerWork() {
        File file = new File(SOURCE_TEST_PATH + "/IMG_6133.jpg");
        File fileResized = new File(RESOURCES_TEST_RESIZE_PATH + "/IMG_6133.jpg");
        ImageContainer imageContainer = new ImageContainer(file);
        Assertions.assertTrue(ContainerLibrary.getInstance().getResizeableStorage().contains(imageContainer));
        Assertions.assertFalse(ContainerLibrary.getInstance().getResizeableImageViewList().isEmpty());
        Assertions.assertTrue(fileResized.exists());
        Assertions.assertEquals(imageContainer.isResizedAlive(), fileResized.exists());
        Assertions.assertTrue(imageContainer.isOriginalAlive());
        Assertions.assertTrue(Objects.nonNull(imageContainer.getImage()));
        Assertions.assertTrue(Objects.nonNull(imageContainer.getImageView()));
        Assertions.assertEquals(imageContainer.getNode(), imageContainer.getImageView());
        Assertions.assertEquals(imageContainer.getResizedImageContainer().getOriginalContainer(), imageContainer);
        Assertions.assertEquals(1200.00, imageContainer.getImageView().getFitWidth());
        Assertions.assertEquals(800.16, imageContainer.getImageView().getFitHeight());
        Assertions.assertEquals(300.00, imageContainer.getResizedImageContainer().getImageView().getFitWidth());
        Assertions.assertEquals(200.04, imageContainer.getResizedImageContainer().getImageView().getFitHeight());
    }


}