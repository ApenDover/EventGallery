package GUI.Gallery.utils.videoResizer;

import GUI.Gallery.utils.FileStringConverter;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;

@UtilityClass
public class VideoResizerJpg {

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
        try (final var fFmpegFrameGrabber = new FFmpegFrameGrabber(filePath)) {
            fFmpegFrameGrabber.start();
            final var frame = fFmpegFrameGrabber.grabImage();
            return doExecuteFrame(frame, fileResized, newWidth, videoSign);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private File doExecuteFrame(Frame frame, File outputFile, int newWidth, boolean videoSign) throws IOException {
        if (Objects.nonNull(frame.image)) {
            try (final var converter = new Java2DFrameConverter()) {
                final var biFromMovie = Scalr.resize(converter.getBufferedImage(frame), newWidth);
                if (videoSign) {
                    final var overlay = ImageIO.read(new File("Images/WatermarkPlay.png"));
                    int w = Math.max(biFromMovie.getWidth(), overlay.getWidth());
                    int h = Math.max(biFromMovie.getHeight(), overlay.getHeight());
                    int x = (int) (biFromMovie.getWidth() - biFromMovie.getWidth() * 0.125);
                    int y = (int) (biFromMovie.getHeight() * 0.125);
                    final var combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                    final var g = combined.createGraphics();
                    g.drawImage(biFromMovie, 0, 0, null);
                    g.drawImage(overlay, x - 14, y - 14, null);
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
