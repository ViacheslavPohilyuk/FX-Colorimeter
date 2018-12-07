package viacheslav.pokhyliuk.projects.fxcolorimeter;

import org.apache.commons.io.FileUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

public class SnapshotMakerImpl implements SnapshotMaker {
    private String format;
    private String imagesLocation;
    private ImageConverter imageConverter;

    SnapshotMakerImpl(String format, String imagesLocation) {
        this.format = format;
        this.imagesLocation = imagesLocation;
        this.imageConverter = new ImageConverter();
    }

    public void make(String[][] pixels, int size) {
        Integer[][] rgb = computeRGB(pixels, size);
        BufferedImage image = createImage(rgb, size);
        save(image, size);
    }

    private Integer[][] computeRGB(String[][] pixels, int size) {
        Integer[][] rgb = new Integer[size][size];
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                String pixel = pixels[x][y];
                if (Objects.nonNull(pixel)) {
                    String clr = pixels[x][y].substring(1);
                    int red = Integer.parseInt(clr.substring(0, 2), 16);
                    int green = Integer.parseInt(clr.substring(2, 4), 16);
                    int blue = Integer.parseInt(clr.substring(4, 6), 16);
                    rgb[x][y] = new Color(red, green, blue).getRGB();
                }
            }
        }
        return rgb;
    }

    private BufferedImage createImage(Integer[][] pixels, int size) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Integer pixel = pixels[x][y];
                if (Objects.nonNull(pixel)) {
                    image.setRGB(x, y, pixel);
                }
            }
        }
        return image;
    }

    private void save(BufferedImage image, int size) {
        try {
            String current = LocalDateTime.now().toString();
            String fileName = String.format("%s/%s-%s.%s",
                    imagesLocation, current, size, format
            );
            byte[] imageData = imageConverter.toPNG(image);
            FileUtils.writeByteArrayToFile(new File(fileName), imageData);
        } catch (IOException e) {
            String errMsg = String.format(
                    "Error occurred when trying to save an image, " +
                            "location - %s; format - %s; size - %s",
                    imagesLocation,
                    format,
                    size
            );
            System.err.println(errMsg);
        }
    }
}
