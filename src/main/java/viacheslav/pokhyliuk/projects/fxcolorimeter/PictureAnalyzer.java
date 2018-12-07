package viacheslav.pokhyliuk.projects.fxcolorimeter;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;

public class PictureAnalyzer {
    public static void main(String[] args) throws IOException {
        URL resource = PictureAnalyzer.class.getResource("/2018-12-02T18:56:37.009-5.png");
        BufferedImage image = ImageIO.read(resource);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                System.out.println(String.format("x:%s;y: %s - rgb: %s",
                        x, y, Math.abs(image.getRGB(x, y))
                ));
            }
        }
    }

    private static boolean findClr(int color) {
        final int red = (color & 0x00ff0000) >> 16;
        final int green = (color & 0x0000ff00) >> 8;
        final int blue = color & 0x000000ff;
        return red == 255 && green == 243 && blue == 81;
    }
}
