package gui.gallery.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class FileStringConverterTest {

    private static final String PATH = "src/test/java/gui/gallery/utils/FileStringConverterTest.java";

    private static final String NAME = "FileStringConverterTest";

    private static final String FULL_NAME = "FileStringConverterTest.java";

    private static final String EXTENSION = "java";

    @Test
    void getName() {
        assertEquals(NAME, FileStringConverter.getName(PATH));
    }

    @Test
    void getFullNameFromPath() {
        assertEquals(FULL_NAME, FileStringConverter.getFullNameFromPath(PATH));
    }


    @Test
    void getExtension() {
        assertEquals(EXTENSION, FileStringConverter.getExtension(PATH));
    }

    @Test
    void convertToName() {
        // GIVEN
        File file = new File(PATH);

        //WHEN
        final var actual = FileStringConverter.getName(file);

        //THEN
        Assertions.assertEquals(NAME, actual);
    }

    @Test
    void convertToEx() {
        // GIVEN
        File file = new File(PATH);

        //WHEN
        final var actual = FileStringConverter.getExtension(file);

        //THEN
        Assertions.assertEquals(EXTENSION, actual);
    }

    @Test
    void getFilePath() {
        // GIVEN
        String expected = "source/name.jpg";

        //WHEN
        final var actual = FileStringConverter.getFilePath("source", "name", "jpg");

        //THEN
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getNameWithEx() {
        // GIVEN
        String expected = "source.name";

        //WHEN
        final var actual = FileStringConverter.getNameWithEx("source", "name");

        //THEN
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getFile() {
        // GIVEN
        File expected = new File(PATH);

        //WHEN
        final var actual = FileStringConverter.getFile("src/test/java/gui/gallery/utils", "FileStringConverterTest", "java");

        //THEN
        Assertions.assertEquals(expected, actual);
    }


}