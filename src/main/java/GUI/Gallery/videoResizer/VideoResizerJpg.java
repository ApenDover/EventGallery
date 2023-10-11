package GUI.Gallery.videoResizer;

import lombok.NonNull;
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

public class VideoResizerJpg {

    public static void getImageFromVideo(Set<File> filesToResize, int newWidth, boolean videoSign) {

        for (File file : filesToResize) {
            final var filePath = file.getAbsolutePath();
            String targetFilePath;
            try (final var fFmpegFrameGrabber = new FFmpegFrameGrabber(filePath)) {
                fFmpegFrameGrabber.start();
                Frame frame = fFmpegFrameGrabber.grabImage();

                targetFilePath = getImagePath(filePath);
                doExecuteFrame(frame, targetFilePath, newWidth, videoSign);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private static String getImagePath(String filePath) {
        if (filePath.contains(".") && filePath.lastIndexOf(".") < filePath.length() - 1) {
            String[] fileArray = filePath.split("/");
            String fileNameFull = fileArray[fileArray.length - 1];
            String fileName = fileNameFull.substring(0, fileNameFull.length() - 4);
            File dir = new File(filePath.substring(0, filePath.lastIndexOf("/")).concat("/300"));
            dir.mkdir();
            filePath = filePath.substring(0, filePath.lastIndexOf("/")).concat("/300/").concat(fileName).concat(".").concat("png");
        }
        return filePath;
    }

    private static void doExecuteFrame(@NonNull Frame frame, String targetFilePath, int newWidth, boolean videoSign) throws IOException {
        if (Objects.nonNull(frame.image)) {
            Java2DFrameConverter converter = new Java2DFrameConverter();
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
        }
    }


}
