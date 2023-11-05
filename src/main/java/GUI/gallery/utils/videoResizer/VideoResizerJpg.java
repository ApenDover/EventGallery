package gui.gallery.utils.videoResizer;

import gui.gallery.utils.FileStringConverter;
import lombok.experimental.UtilityClass;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;

@UtilityClass
public class VideoResizerJpg {

    public static final double SCALE = 0.125;
    public static final int SDVIG = 14;

    public void getImageFromVideo(Set<File> filesToResize, int newWidth, boolean videoSign) {
        filesToResize.parallelStream().forEach(file -> getImageFromVideo(file, newWidth, videoSign));
    }

    public File getImageFromVideo(File file, int newWidth, boolean videoSign) {
        final var filePath = file.getAbsolutePath();
        final var fileResized = new File(FileStringConverter.getResizeFileFromOriginal(filePath));
        if (fileResized.exists()) {
            return fileResized;
        }
        System.out.println("RESIZE " + file.getAbsolutePath());
        try (var fFmpegFrameGrabber = new FFmpegFrameGrabber(filePath)) {
            fFmpegFrameGrabber.start();
            final var frame = fFmpegFrameGrabber.grabImage();
            return doExecuteFrame(frame, fileResized, newWidth, videoSign);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private File doExecuteFrame(Frame frame, File outputFile, int newWidth, boolean videoSign) throws IOException {
        if (Objects.nonNull(frame.image)) {
            try (var converter = new Java2DFrameConverter()) {
                final var biFromMovie = Scalr.resize(converter.getBufferedImage(frame), newWidth);
                if (videoSign) {
                    final var overlay = ImageIO.read(new File("Images/WatermarkPlay.png"));
                    int w = Math.max(biFromMovie.getWidth(), overlay.getWidth());
                    int h = Math.max(biFromMovie.getHeight(), overlay.getHeight());
                    int x = (int) (biFromMovie.getWidth() - biFromMovie.getWidth() * SCALE);
                    int y = (int) (biFromMovie.getHeight() * SCALE);
                    final var combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                    final var g = combined.createGraphics();
                    g.drawImage(biFromMovie, 0, 0, null);
                    g.drawImage(overlay, x - SDVIG, y - SDVIG, null);
                    g.dispose();
                    ImageIO.write(combined, "jpg", outputFile);
                } else {
                    ImageIO.write(biFromMovie, "jpg", outputFile);
                }
                return outputFile;
            }
        }
        return null;
    }

}
