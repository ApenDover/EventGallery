//package gui.gallery.utils;
//
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//
//import java.io.File;
//
//class FileStringConverterTest {
//
//    @Test
//    void convertToName() {
//        // GIVEN
//        File file = new File("/Users/andrey/IdeaProjects/JavaJFXGalleryMailSender
//         + /src/test/java/gui/gallery/utils/FileStringConverterTest.java");
//        String expected = "FileStringConverterTest";
//
//        //WHEN
//        final var actual = FileStringConverter.getName(file);
//
//        //THEN
//        Assertions.assertEquals(actual, expected);
//    }
//
//    @Test
//    void convertToEx() {
//        // GIVEN
//        File file = new File("/Users/andrey/IdeaProjects"
//                + "/JavaJFXGalleryMailSender/src/test/java/gui/gallery/utils/FileStringConverterTest.java");
//        String expected = "java";
//
//        //WHEN
//        final var actual = FileStringConverter.getExtension(file);
//
//        //THEN
//        Assertions.assertEquals(actual, expected);
//    }
//
//    @Test
//    void getFilePath() {
//        // GIVEN
//        String expected = "source/name.jpg";
//
//        //WHEN
//        final var actual = FileStringConverter.getFilePath("source", "name", "jpg");
//
//        //THEN
//        Assertions.assertEquals(actual, expected);
//    }
//
//    @Test
//    void getNameWithEx() {
//        // GIVEN
//        String expected = "source.name";
//
//        //WHEN
//        final var actual = FileStringConverter.getNameWithEx("source", "name");
//
//        //THEN
//        Assertions.assertEquals(actual, expected);
//    }
//
//    @Test
//    void getFile() {
//        // GIVEN
//        File expected = new File("/Users/andrey/IdeaProjects/JavaJFXGalleryMailSender"
//                + "/src/test/java/gui/gallery/utils/FileStringConverterTest.java");
//
//        //WHEN
//        final var actual = FileStringConverter.getFile("/Users/andrey/IdeaProjects"
//                + "/JavaJFXGalleryMailSender/src/test/java/gui/gallery/utils", "FileStringConverterTest", "java");
//
//        //THEN
//        Assertions.assertEquals(actual, expected);
//    }
//
//}
