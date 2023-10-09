package GUI.Gallery.videoResizer;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.TreeSet;

public class VideoResizerJpg {

    public static void getImageFromVideo(TreeSet<File> filesToResize, int newWidth, boolean videoSign) throws Exception {
        Iterator<File> iterator = filesToResize.iterator();

        while (iterator.hasNext()) {
            final var file = iterator.next();
            final var filePath = file.getAbsolutePath();
            String targetFilePath = "";
            final var ff = new FFmpegFrameGrabber(filePath);
            ff.start();
            Frame f = ff.grabImage();

            targetFilePath = getImagePath(filePath);
            doExecuteFrame(f, targetFilePath, newWidth, videoSign);
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

    private static void doExecuteFrame(Frame f, String targetFilePath, int newWidth, boolean videoSign) throws IOException {
        if (null != f && null != f.image) {
            Java2DFrameConverter converter = new Java2DFrameConverter();
            BufferedImage biFromMovie = converter.getBufferedImage(f);
            biFromMovie = Scalr.resize(biFromMovie, newWidth, new BufferedImageOp[0]);
            String var10000;
            if (videoSign) {
                BufferedImage overlay = ImageIO.read(new File("Images/WatermarkPlay.png"));
                int w = Math.max(biFromMovie.getWidth(), overlay.getWidth());
                int h = Math.max(biFromMovie.getHeight(), overlay.getHeight());
                int x = (int) (biFromMovie.getWidth() - biFromMovie.getWidth() * 0.125);
                int y = (int) (biFromMovie.getHeight() * 0.125);
                BufferedImage combined = new BufferedImage(w, h, 1);
                Graphics g = combined.getGraphics();
                g.drawImage(biFromMovie, 0, 0, (ImageObserver) null);
                g.drawImage(overlay, x - 14, y - 14, (ImageObserver) null);
                int pointNum = targetFilePath.lastIndexOf(46);
                var10000 = targetFilePath.substring(0, pointNum);
                String pathToJpg = var10000 + ".jpg";
                File output = new File(pathToJpg);

                try {
                    ImageIO.write(combined, "jpg", output);
                } catch (IOException var18) {
                    var18.printStackTrace();
                }
            } else {
                int pointNum = targetFilePath.lastIndexOf(46);
                var10000 = targetFilePath.substring(0, pointNum);
                String pathToJpg = var10000 + ".jpg";
                File output = new File(pathToJpg);

                try {
                    ImageIO.write(biFromMovie, "jpg", output);
                } catch (IOException var17) {
                    var17.printStackTrace();
                }
            }

        }
    }
}
