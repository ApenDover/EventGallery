package GUI.Gallery.videoResizer;

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

    public static void getImageFromVideo(Set<File> filesToResize, int newWidth, boolean videoSign) {
        filesToResize.parallelStream().forEach(file -> getImageFromVideo(file, newWidth, videoSign));
    }

    public static File getImageFromVideo(File file, int newWidth, boolean videoSign) {
        final var filePath = file.getAbsolutePath();
        String targetFilePath = FileStringConverter.getResizeFileFromOriginal(filePath);
        File fileResized = new File(targetFilePath);
        if (fileResized.exists()) {
            return fileResized;
        }
        System.out.println("RESIZE " + file.getAbsolutePath());
        try (final var fFmpegFrameGrabber = new FFmpegFrameGrabber(filePath)) {
            fFmpegFrameGrabber.start();
            Frame frame = fFmpegFrameGrabber.grabImage();
            return doExecuteFrame(frame, targetFilePath, newWidth, videoSign);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static File doExecuteFrame(@NonNull Frame frame, String targetFilePath, int newWidth, boolean videoSign) throws IOException {
        if (Objects.nonNull(frame.image)) {
            try (Java2DFrameConverter converter = new Java2DFrameConverter()) {
                BufferedImage biFromMovie = converter.getBufferedImage(frame);
                biFromMovie = Scalr.resize(biFromMovie, newWidth);
                String filePath = targetFilePath.substring(0, targetFilePath.lastIndexOf(".")) + ".jpg";
                File output = new File(filePath);

                if (videoSign) {
                    BufferedImage overlay = ImageIO.read(new File("Images/WatermarkPlay.png"));
                    int w = Math.max(biFromMovie.getWidth(), overlay.getWidth());
                    int h = Math.max(biFromMovie.getHeight(), overlay.getHeight());
                    int x = (int) (biFromMovie.getWidth() - biFromMovie.getWidth() * 0.125);
                    int y = (int) (biFromMovie.getHeight() * 0.125);
                    BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                    Graphics2D g = combined.createGraphics();
                    g.drawImage(biFromMovie, 0, 0, null);
                    g.drawImage(overlay, x - 14, y - 14, null);
                    g.dispose();
                    ImageIO.write(combined, "jpg", output);
                } else {
                    ImageIO.write(biFromMovie, "jpg", output);
                }
                return output;
            }
        }
        return null;
    }

}
