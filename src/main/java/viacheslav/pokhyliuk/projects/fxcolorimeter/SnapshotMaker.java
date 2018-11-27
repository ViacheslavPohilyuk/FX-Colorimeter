package viacheslav.pokhyliuk.projects.fxcolorimeter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

public class SnapshotMaker {
    private String format;
    private String imagesLocation;

    public SnapshotMaker(String format, String imagesLocation) {
        this.format = format;
        this.imagesLocation = imagesLocation;
    }

    public void make(PointInfo info) throws IOException {
        String[][] pixels = info.getPixels();
        int size = pixels[0].length;
        Integer[][] rgb = computeRgb(pixels, size);
        BufferedImage image = createImage(rgb, size);
        save(image, size);
    }

    private Integer[][] computeRgb(String[][] pixels, int size) {
        Integer[][] rgb = new Integer[size][size];
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                rgb[x][y] = fetchRgb(pixels[x][y]);
            }
        }
        return rgb;
    }

    private int fetchRgb(String clr) {
        clr = clr.substring(1);
        int red = Integer.parseInt(clr.substring(0, 2), 16);
        int green = Integer.parseInt(clr.substring(2, 4), 16);
        int blue = Integer.parseInt(clr.substring(4, 6), 16);
        return new Color(red, green, blue).getRGB();
    }

    private BufferedImage createImage(Integer[][] pixels, int size) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                image.setRGB(x, y, pixels[x][y]);
            }
        }
        return image;
    }

    private void save(BufferedImage image, int size) throws IOException {
        String now = LocalDateTime.now().toString();
        File output = new File(String.format("%s/%s-%s.%s", imagesLocation, size, now, format));
        ImageIO.write(image, format, output);
    }
}
